package xyz.zjhwork.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import xyz.zjhwork.entity.Exception;
import xyz.zjhwork.entity.History;

import java.util.List;

@Repository
public interface HisDao {
    //查询浏览历史  40条记录
    @Select("SELECT a.id,a.title,b.time createTime from exception a,history b where b.userId =#{username} and a.id=b.exceptionId and a.delStatus=0 order by b.time desc limit 0,40")
    List<Exception> findHistoryByUsername(@Param("username") String username);

    //新增浏览历史
    @Transactional
    @Insert("insert into exception.history(exceptionId,userId,time,remark) values(#{exceptionId},#{userId},#{time},#{remark})")
    int addHistory(History history);

    //查询历史记录是否为已经录入过的数据   exceptionId
    @Select("select id from exception.history where exceptionId =#{exceptionId} and userId =#{userId}")
    Integer queryHistoryByUserIdAndExceptionId(History history);

    //更新time字段
    @Transactional
    @Update("update exception.history set time = #{time} where exceptionId = #{exceptionId} and userId = #{userId}")
    int updateHistory(History history);
}
