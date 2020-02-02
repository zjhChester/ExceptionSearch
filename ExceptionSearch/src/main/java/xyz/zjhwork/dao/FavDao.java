package xyz.zjhwork.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import xyz.zjhwork.entity.Favorite;

import java.util.List;

@Repository
public interface FavDao {
    //查询自己的收藏
    @Select("select * from favorite where userId=#{username} order by time desc")
    List<Favorite> findFavByUsername(String username);
    //查询博文是否收藏
    @Select("select count(id) from favorite where userId = #{userId} and exceptionId = #{exceptionId}")
    int isFavByUsernameAndExceptionId(Favorite favorite);
    //取消收藏
    @Delete("delete from favorite where userId = #{userId} and exceptionId = #{exceptionId}")
    int deleteFavFromFavByUsernameAndExceptionId(Favorite favorite);
    //添加收藏
    @Insert("insert into favorite (exceptionId,userId,time,remark) values(#{exceptionId},#{userId},#{time},#{remark})")
    int addFavByUsernameAndExceptionId(Favorite favorite);
}
