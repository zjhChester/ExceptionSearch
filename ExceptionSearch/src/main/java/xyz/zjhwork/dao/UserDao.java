package xyz.zjhwork.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;
import xyz.zjhwork.entity.User;

import java.util.List;

@Repository
public interface UserDao {
    List<User> findUser(User user);
    @Insert("insert into user(username,password,nickName,age,gender,createTime,email,remark) values(#{username},#{password},#{nickName},#{age},#{gender},#{createTime},#{email},#{remark})")
    int insertUser(User user);
    int updateUser(User user);
    //管理员权限
    @Delete("delete from user where username = #{username}")
    int deleteUser(User user);
}
