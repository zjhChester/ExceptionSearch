package xyz.zjhwork.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import xyz.zjhwork.entity.Notification;

import java.util.List;

/**
 * @Describe:
 * @Author: zjhChester
 * @Date: 9:20 2020/9/24
 */
@Repository
public interface NotificationDao {
    /**
     * 根据id查询推送
     * @param id notification id
     * @return Notification pojo
     */
    @Select("select  id, receiver,sender,createTime,exceptionId,type,delStatus from notification where id = #{id}")
    Notification get(Integer id);

    /**
     * 根据用户和分页信息分页查询推送信息
     * 根据修改时间排序
     * @param startIndex 开始下标
     * @param pageSize 每页数量
     * @param username 用户名
     * @return Notification集合
     */
    @Select("select id, receiver,sender,createTime,exceptionId,type,delStatus,modifyTime,message from notification where receiver = #{username} order by delStatus asc, modifyTime desc limit #{startIndex},#{pageSize}")
    List<Notification> getPage(@Param("username") String username,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);

    /**
     * 逻辑删除（已读逻辑）
     * @param id 推送id
     * @param modifyTime 修改时间
     * @return true/false
     */
    @Update("update notification set delStatus = '1', modifyTime = #{modifyTime} where id in (${id})")
    int del(@Param("id") String id,@Param("modifyTime")String modifyTime);

    /**
     * 推送创建
     * @param notification 推送pojo
     * @return true/false
     */
    @Insert("insert into notification(id,receiver,sender,createTime,modifyTime,exceptionId,type,delStatus,message) values (#{id},#{receiver},#{sender},#{createTime},#{modifyTime},#{exceptionId},#{type},#{delStatus},#{message})")
    int create(Notification notification);

    /**
     * 根据用户名查询推送条数
     * @param username 用户名
     * @return 条数
     */
    @Select("select count(id) from notification where receiver = #{username}")
    int getCountByReceiver(String username);

    /**
     * 根据用户名查询未读条数
     * @param username 用户名
     * @return 条数
     */
    @Select("select count(id) from notification where username=#{username} and delStatus='0'")
    int getUnreadCountByUsername(String username);
}
