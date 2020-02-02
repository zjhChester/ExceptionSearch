package xyz.zjhwork.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import xyz.zjhwork.entity.Comment;
import xyz.zjhwork.entity.Favorite;

import java.util.List;

@Repository
public interface CommentDao {
    //添加评论
    @Insert("insert into comment(exceptionId,userId,time,content,remark) values (#{exceptionId},#{userId},#{time},#{content},#{remark})")
    int insertComment(Comment comment);
    //查询博文的评论
    @Select("select * from comment where exceptionId = #{exceptionId}")
    List<Comment> findComment(Comment comment);

}
