package xyz.zjhwork.controller;

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
public class ArpoveController {
    @Autowired
    private OtherService otherService;


    //查询是否赞
    @ResponseBody
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
