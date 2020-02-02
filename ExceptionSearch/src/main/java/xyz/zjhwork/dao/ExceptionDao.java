package xyz.zjhwork.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import xyz.zjhwork.entity.Exception;

import java.util.List;

@Repository
public interface ExceptionDao {
    /**
     * CURD
     * @param e
     * @return
     */
    List<Exception> findException(Exception e);
    @Insert("insert into exception(title,exception.desc,content,createTime,author,remark,exception.type,views) values(#{title},#{desc},#{content},#{createTime},#{author},#{remark},#{type},#{views})")
    int insertException(Exception e);
    int updateException(Exception e);
    //管理员权限
    @Delete("delete from exception where id = #{id}")
    int deleteException(Exception e);

    /**
     * 核心业务
     */
    //检索
    List<Exception> searchExceptionByKeywords(@Param("title")List<String> title,@Param("content")List<String> content,@Param("desc")List<String> desc,@Param("type")List<String> type,@Param("currIndex") int currIndex);
    //检索数量
    int searchCount(@Param("title")List<String> title,@Param("content")List<String> content,@Param("desc")List<String> desc,@Param("type")List<String> type);
    //检索联想
    List<Integer> searchAssociation(@Param("keywords") List<String> keywords);

    //最新异常文章  40条新文章
    @Select("select id from exception order by createTime desc limit 0 , 40")
    List<Exception> newListException();

    //我的文章
    @Select("select id from exception where  author = #{username} order by createTime desc")
    List<Exception> myListException(@Param("username") String username);

    //查询单个 通过id
    @Select("select id,title,exception.desc,author,exception.type,createTime from exception where id =#{id}")
    Exception findExceptionById(@Param("id") int id);
}
