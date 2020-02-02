package xyz.zjhwork.service;

import org.springframework.stereotype.Service;
import xyz.zjhwork.entity.User;

import java.util.List;

public interface UserService {
    //CRUD
    List<User> findUser(User user);
    int insertUser(User user);
    int updateUser(User user);
    //管理员权限
    int deleteUser(User user);


    //业务
    int login(User user);
}
