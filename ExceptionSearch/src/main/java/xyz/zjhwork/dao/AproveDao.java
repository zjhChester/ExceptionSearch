package xyz.zjhwork.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import xyz.zjhwork.entity.Approve;

@Repository
public interface AproveDao {

    //查询是否赞
    @Select("select count(id) from approve where userId = #{userId} and exceptionId=#{exceptionId}")
    int isAproByUsernameAndExceptionId(Approve approve);

    //添加赞
    @Insert("insert into approve(exceptionId,userId,time,remark) values (#{exceptionId},#{userId},#{time},#{remark})")
    int addAproByUsernameAndExceptionId(Approve approve);

    //查询有多少赞
    @Select("select count(*) from approve where exceptionId = #{id}")
    int findApproCountByExceptionId(Integer id);

}
