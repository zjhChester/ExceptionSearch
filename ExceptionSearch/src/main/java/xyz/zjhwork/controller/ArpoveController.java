package xyz.zjhwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.zjhwork.entity.Approve;
import xyz.zjhwork.service.OtherService;
import xyz.zjhwork.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@Api(tags = "Approve Service Interfaces")
public class ArpoveController {
    @Autowired
    private OtherService otherService;
    //查询是否赞
    @ResponseBody
    @ApiOperation(value = "查询是否点赞接口", notes = "查询当前文章是否被当前用户赞过，权限控制")
    @GetMapping("/isAproByUsernameAndExceptionId")
    public int isAproByUsernameAndExceptionId(Integer id, HttpServletRequest request){
        if(id !=null){
            Approve approve = new Approve();
            approve.setExceptionId(id);
            approve.setUserId(request.getSession().getAttribute("loginUser").toString());
            int aproByUsernameAndExceptionId = otherService.isAproByUsernameAndExceptionId(approve);
            if(aproByUsernameAndExceptionId != 0){
                return 1;
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }

    //添加赞
    @ResponseBody
    @ApiOperation(value = "点赞接口", notes = "添加对该文章的点赞信息，权限控制")
    @PostMapping("/addAproByUsernameAndExceptionId")
    public int addAproByUsernameAndExceptionId(Integer id, HttpServletRequest request){
        if(id !=null){
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
                int aproByUsernameAndExceptionId = otherService.addAproByUsernameAndExceptionId(approve);
                if(aproByUsernameAndExceptionId != 0){
                    return 1;
                }else{
                    return 0;
                }
            }

        }else{
            return 0;
        }
    }
    //查询有多少赞  不需要走拦截器
    @ApiOperation(value = "查询当前文章有多少赞接口", notes = "查询当前文章有多少赞")
    @ResponseBody
    @GetMapping("/findApproCountByExceptionId")
    public int findApproCountByExceptionId(Integer id){
        if(id != null){
            int approCountByExceptionId = otherService.findApproCountByExceptionId(id);
            return approCountByExceptionId;
        }
        else{
            return 0;
        }
    }
}
