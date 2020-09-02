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
    @Insert("insert into exception(title,exception.desc,content,createTime,author,remark,exception.type,views,delStatus) values(#{title},#{desc},#{content},#{createTime},#{author},#{remark},#{type},#{views},0)")
    int insertException(Exception e);
    int updateException(Exception e);
//    //管理员权限
//    @Delete("delete from exception where id = #{id}")
//    int deleteException(Exception e);

    /**
     * 核心业务
     */
    //检索
    List<Exception> searchExceptionByKeywords(@Param("title")List<String> title,@Param("content")List<String> content,@Param("desc")List<String> desc,@Param("type")List<String> type,@Param("currIndex") int currIndex);
    //检索数量
    int searchCount(@Param("title")List<String> title,@Param("content")List<String> content,@Param("desc")List<String> desc,@Param("type")List<String> type);
    //检索联想
    List<Exception> searchAssociation(@Param("keywords") List<String> keywords);

    //最新异常文章  40条新文章
    //修改createTime为id
    @Select("select id,title,createTime,author from exception where delStatus=0 order by id desc limit 0 , 40")
    List<Exception> newListException();

    //我的文章
    @Select("select id,title,createTime from exception where  author = #{username}and delStatus=0 order by id desc ")
    List<Exception> myListException(@Param("username") String username);

    //查询单个 通过id
    @Select("select id,title,exception.desc,author,exception.type,createTime from exception where id =#{id}and delStatus=0")
    Exception findExceptionById(@Param("id") int id);

    //增加浏览数量
    @Update("update exception set views = (views+1) where id = #{id}")
    int addViews(Integer id);

    //删除异常文章 逻辑删除delStatus字段改为1
    @Update("update exception set delStatus = 1 where id = #{id}")
    int delException(Integer id);

}
