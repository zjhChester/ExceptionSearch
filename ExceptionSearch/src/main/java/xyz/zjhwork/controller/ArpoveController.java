package xyz.zjhwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.zjhwork.common.NotificationType;
import xyz.zjhwork.conf.exception.BlogSystemException;
import xyz.zjhwork.entity.Approve;
import xyz.zjhwork.entity.Notification;
import xyz.zjhwork.service.ExceptionService;
import xyz.zjhwork.service.NotificationService;
import xyz.zjhwork.service.OtherService;
import xyz.zjhwork.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author zjhChester
 */
@Controller
@Api(tags = "点赞接口 Approve Service Interfaces")
public class ArpoveController {
    private final OtherService otherService;

    private final ExceptionService exceptionService;

    private final NotificationService notificationService;
    @Autowired
    public ArpoveController(ExceptionService exceptionService, OtherService otherService, NotificationService notificationService) {
        this.exceptionService = exceptionService;
        this.otherService = otherService;
        this.notificationService = notificationService;
    }

    /**
     * 查询是否点赞接口
     * @param id 文章id
     * @param request 用户信息
     * @return int成功与否
     */
    @ResponseBody
    @ApiOperation(value = "查询是否点赞接口", notes = "查询当前文章是否被当前用户赞过，权限控制")
    @GetMapping("/isAproByUsernameAndExceptionId")
    public int isAproByUsernameAndExceptionId(Integer id, HttpServletRequest request){
        if(id ==null){
            throw new BlogSystemException("当前文章id不能为空");
        }
            Approve approve = new Approve();
            approve.setExceptionId(id);
            approve.setUserId(request.getSession().getAttribute("loginUser").toString());
            int aproByUsernameAndExceptionId = otherService.isAproByUsernameAndExceptionId(approve);
            if(aproByUsernameAndExceptionId != 0){
                return 1;
            }else{
                return 0;
            }

    }

    /**
     * 点赞接口
     * @param id 文章id
     * @param request 用户信息
     * @return int成功与否
     */
    @ResponseBody
    @ApiOperation(value = "点赞接口", notes = "添加对该文章的点赞信息，权限控制")
    @PostMapping("/addAproByUsernameAndExceptionId")
    public int addApproveByUsernameAndExceptionId(Integer id, HttpServletRequest request){
        if(id ==null){
            throw new BlogSystemException("文章id不能为空");
        }
            Approve approve = new Approve();
            approve.setExceptionId(id);
            approve.setUserId(request.getSession().getAttribute("loginUser").toString());
            //查询是否赞过   避免事务提交延迟
            int aproByUsernameAndExceptionId1 = otherService.isAproByUsernameAndExceptionId(approve);
            if(aproByUsernameAndExceptionId1 != 0){
                return -1;
            }else{
                approve.setRemark("");
                approve.setTime(DateUtils.getFormat(new Date()));
                int approveByUsernameAndExceptionId = otherService.addAproByUsernameAndExceptionId(approve);

                if(approveByUsernameAndExceptionId != 0){
                    //创建推送通知

                    Notification notification = Notification.builder().
                            delStatus("0").
                            id(0).
                            createTime(DateUtils.CURRENT_TIME).
                            exceptionId(id).
                            modifyTime(DateUtils.CURRENT_TIME).
                            receiver(exceptionService.findExceptionById(id).getAuthor()).
                            sender(request.getSession().getAttribute("loginUser").toString()).
                            type(NotificationType.APPRECIATE.toString()).
                            build();
                    notificationService.create(notification);
                    return 1;
                }else{
                    return 0;
                }
            }


    }

    /***
     * 查询有多少赞  不需要走拦截器
     * @param id 文章id
     * @return int成功与否
     */
    @ApiOperation(value = "查询当前文章有多少赞接口", notes = "查询当前文章有多少赞")
    @ResponseBody
    @GetMapping("/findApproCountByExceptionId")
    public int findApproCountByExceptionId(Integer id){
        if(id == null){
            throw new BlogSystemException("文章id不能为空");
        }
        return otherService.findApproCountByExceptionId(id);

    }
}
