package xyz.zjhwork.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理主页业务
 * 1、页面跳转
 */
@Controller
@Api(tags = "Abandoned Interfaces！")
public class IndexController {
    @RequestMapping("/favicon.ico")
    @ResponseBody
    public String favicon(){return "no favicon";}
    @RequestMapping("/")
    @ResponseBody
    public String index(){return "no index";}
}
