package xyz.zjhwork.service;

import xyz.zjhwork.conf.exception.BlogSystemException;
import xyz.zjhwork.dto.notification.NotificationOutputDTO;
import xyz.zjhwork.entity.Notification;
import xyz.zjhwork.dto.ResponseModel;

import javax.servlet.http.HttpServletRequest;

/**
 * @Describe: 通知业务接口
 * @Author: zjhChester
 * @Date: 17:59 2020/9/23
 */
public interface NotificationService {
    /**
     * 查询我的消息推送
     * @param id 推送id
     * @return 统一返回体ResponseModel
     * @throws BlogSystemException 系统异常
     */

    ResponseModel get(Integer id) ;

    /**
     * 新建推送
     * @param notification 创建推送
     * @return 统一返回体ResponseModel
     */
    ResponseModel create(Notification notification);


    /**
     * 关闭推送
     * @param id 推送id
     * @return 统一返回体ResponseModel
     */
    ResponseModel del(Integer[] id);

    /**
     * 分页获取未读推送
     * @param request session
     * @param currPage 当前页
     * @param pageSize 每页的大小
     * @return 分页返回体NotificationOutputDTO
     */
    NotificationOutputDTO getPage(HttpServletRequest request, Integer currPage, Integer pageSize);

}
