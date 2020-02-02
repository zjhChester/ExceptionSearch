package xyz.zjhwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.zjhwork.entity.Exception;
import xyz.zjhwork.entity.History;
import xyz.zjhwork.service.ExceptionService;
import xyz.zjhwork.service.OtherService;
import xyz.zjhwork.utils.SortUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HisController {
    @Autowired
    private OtherService otherService;
    @Autowired
    private ExceptionService exceptionService;
    //查询历史记录
    @ResponseBody
    @GetMapping("/findHistoryByUsername")
    public List<Exception> findHistoryByUsername(HttpServletRequest request){
        List<History> histories = otherService.findHistoryByUsername(request.getSession().getAttribute("loginUser").toString());
        List<Exception> exceptions = new ArrayList<>();
        for (History h:
             histories) {
            Exception exception = new Exception();
            exception.setId(h.getExceptionId());
            List<Exception> exception1 = exceptionService.findException(exception);
            if(exception1.size()!=0){
                exceptions.add(exception1.get(0));
            }

        }
        for (Exception e:
                exceptions) {
            if(e.getTitle().length()>=15){
                e.getTitle().substring(0,15);
            }
        }
        return exceptions;
    }

}
