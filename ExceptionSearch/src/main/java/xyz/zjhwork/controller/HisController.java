package xyz.zjhwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "History Steps Interfaces")
public class HisController {
    @Autowired
    private OtherService otherService;
    @Autowired
    private ExceptionService exceptionService;
    //查询历史记录
    @ApiOperation(value = "查询历史记录接口", notes = "查询当前用户的访问历史记录文章列表，权限控制")
    @ResponseBody
    @GetMapping("/findHistoryByUsername")
    public List<Exception> findHistoryByUsername(HttpServletRequest request){
        List<Exception> exceptions = otherService.findHistoryByUsername(request.getSession().getAttribute("loginUser").toString());


        return exceptions;
    }

}
