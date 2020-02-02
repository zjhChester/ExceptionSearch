package xyz.zjhwork.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zjhwork.dao.UserDao;
import xyz.zjhwork.entity.User;
import xyz.zjhwork.service.UserService;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> findUser(User user) {
        return userDao.findUser(user);
    }

    @Override
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public int updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public int deleteUser(User user) {
        return userDao.deleteUser(user);
    }

    @Override
    public int login(User user) {
        if(Objects.isNull(user.getUsername())){
            return 0;
        }else {
            User tempUser = new User();
            tempUser.setUsername(user.getUsername());
            List<User> users = userDao.findUser(tempUser);

            if(users.size()==0){
                //账号不存在
                return 0;
            }else {
                if(users.get(0).getPassword().equals(user.getPassword())){
                    //登陆成功
                    return 1;
                }else{
                    //密码错误
                    return -1;
                }
            }
        }
    }
}
