package xyz.zjhwork.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.zjhwork.dto.ResponseModel;
import xyz.zjhwork.dto.notification.NotificationOutputDTO;
import xyz.zjhwork.service.NotificationService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Describe: 消息推送接口
 * @Author: zjhChester
 * @Date: 17:43 2020/9/23
 */
@RestController
@RequestMapping("/notification")
@Api(tags = "消息推送接口")
public class NotificationController {

    private NotificationService notificationService;
    @Autowired
    public NotificationController (NotificationService notificationService) {
        this.notificationService=notificationService;
    }

    @GetMapping("/page")
    public NotificationOutputDTO getPage(HttpServletRequest request, Integer currPage, Integer pageSize){
        return  notificationService.getPage(request,currPage,pageSize);
    }

    @GetMapping("/{id}")
    public ResponseModel get(@PathVariable("id") Integer id){
        return notificationService.get(id);
    }

    /**
     * 标记已读
     * @param id
     * @return
     */
    @DeleteMapping("/batch")
    public ResponseModel del(Integer[] id){
        return notificationService.del(id);
    }
}
