package xyz.zjhwork.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zjhwork.conf.exception.BlogSystemException;
import xyz.zjhwork.dao.ExceptionDao;
import xyz.zjhwork.dao.NotificationDao;
import xyz.zjhwork.dto.ExceptionDTO;
import xyz.zjhwork.dto.notification.NotificationDTO;
import xyz.zjhwork.dto.notification.NotificationModelDTO;
import xyz.zjhwork.dto.notification.NotificationOutputDTO;
import xyz.zjhwork.entity.Exception;
import xyz.zjhwork.entity.Notification;
import xyz.zjhwork.dto.ResponseModel;
import xyz.zjhwork.entity.User;
import xyz.zjhwork.service.NotificationService;
import xyz.zjhwork.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Describe: 消息推送接口实现类
 * @Author: zjhChester
 * @Date: 9:19 2020/9/24
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;
    private final ExceptionDao exceptionDao;
    private final UserServiceImpl userService;
    @Autowired
    public NotificationServiceImpl(NotificationDao notificationDao, ExceptionDao exceptionDao, UserServiceImpl userService) {
        this.notificationDao = notificationDao;
        this.exceptionDao = exceptionDao;
        this.userService = userService;
    }


    @Override
    public ResponseModel get(Integer id) {
        if(null==id){
           throw new BlogSystemException("id不能为空");
        }
        Notification notification = notificationDao.get(id);
        List<Notification> list = new ArrayList<>(1);
        list.add(notification);
        return ResponseModel.successResModel(1,"success",list.toArray());
    }

    @Override
    public ResponseModel create(Notification notification) {
        boolean flag = notificationDao.create(notification)!=0;
        if(!flag){
           throw new BlogSystemException("新增失败");
       }
        return ResponseModel.successResModel(1,"success",null);
    }

    @Override
    public ResponseModel del(Integer[] id) {
        List<String> collect = Arrays.stream(id).map(Object::toString).collect(Collectors.toList());
        String join = String.join(",",collect);
        boolean flag =  notificationDao.del(join, DateUtils.CURRENT_TIME)!=0;
        if(!flag){
            throw new BlogSystemException("删除失败");
        }
        return ResponseModel.successResModel(1,"success",null);
    }

    @Override
    public NotificationOutputDTO getPage(HttpServletRequest request, Integer currPage, Integer pageSize) {
        //参数校验
        if(currPage==null|| pageSize==null){
            throw new BlogSystemException("页参数错误");
        }
        //获取推送pojo集合
        List<Notification> notificationDaoPage = notificationDao.getPage(request.getSession().getAttribute("loginUser").toString(), (currPage - 1) * pageSize, pageSize);
        //根据推送列表查找exception集合
        List<String> ids = notificationDaoPage.stream().map(p->p.getExceptionId().toString()).collect(Collectors.toList());
        String idsStr = String.join(",", ids);
        //指定map的大小 因为pageSize>=exceptionList的大小
        Map<Integer,Exception> exceptionHashMap= new HashMap<>(pageSize);
        //避免没有找到数据而报sql异常
        if("".equals(idsStr)){
            idsStr="-1";
        }
        //分id装入hashMap
       exceptionDao.findListByIds(idsStr).forEach(e->exceptionHashMap.put(e.getId(),e));
        //构建返回DTO对象
        List<NotificationModelDTO> notificationModelsDTO = new ArrayList<>();
        //拼装返回数据
        for (Notification n : notificationDaoPage) {
            //NotificationDTO 构建
            NotificationDTO notificationDTO = NotificationDTO.builder().build();
            //pojo复制到DTO
            BeanUtils.copyProperties(n,notificationDTO);
            //将用户查询改为nickName
            notificationDTO.setSender(userService.findByUsername(n.getSender()).getNickName());
            //ExceptionDTO 构建
            ExceptionDTO exceptionDTO = ExceptionDTO.builder().build();
            //pojo复制到DTO
            BeanUtils.copyProperties(exceptionHashMap.get(n.getExceptionId()),exceptionDTO);
            //NotificationModelDTO 构建
            NotificationModelDTO notificationModelDTO = NotificationModelDTO.builder().exceptionDTO(exceptionDTO).notificationDTO(notificationDTO).build();
            //装填进返回DTO
            notificationModelsDTO.add(notificationModelDTO);
        }
        return   NotificationOutputDTO.builder().totalCount(notificationDao.getCountByReceiver(request.getSession().getAttribute("loginUser").toString())).resList(notificationModelsDTO).build();
    }

    @Override
    public Integer getUnreadCount(HttpServletRequest request) {

        return notificationDao.getUnreadCountByUsername(request.getSession().getAttribute("loginUser").toString());
    }
}
