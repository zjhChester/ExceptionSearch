package xyz.zjhwork.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import xyz.zjhwork.entity.History;

import java.util.List;

@Repository
public interface HisDao {
    //查询浏览历史  八条记录
    @Select("select * from exception.history where userId = #{username} order by time desc limit 0,8")
    List<History> findHistoryByUsername(String username);

    //新增浏览历史
    @Insert("insert into exception.history(exceptionId,userId,time,remark) values(#{exceptionId},#{userId},#{time},#{remark})")
    int addHistory(History history);

}
