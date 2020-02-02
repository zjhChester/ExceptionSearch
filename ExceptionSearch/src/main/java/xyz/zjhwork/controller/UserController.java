package xyz.zjhwork.controller;


import com.sun.istack.internal.NotNull;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.zjhwork.entity.User;
import xyz.zjhwork.resmodel.ResponseModel;
import xyz.zjhwork.resmodel.userResModel.UserResModel;
import xyz.zjhwork.service.UserService;
import xyz.zjhwork.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 用户业务控制器
 * 1、登录注册
 * 2、用户信息维护
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/checkUser")
    public ResponseModel checkUser(@NotNull String username, @NotNull String password, HttpServletRequest request) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(Objects.nonNull(password)? DigestUtils.sha1Hex(password):"");
        int loginRes = userService.login(user);
        switch (loginRes){
                //密码错误
            case -1:
                return ResponseModel.failResModel(-1,"password error");
                //账号不存在
            case 0:
                return ResponseModel.failResModel(0,"user is not exist");
                //登陆成功
            case 1:
                //存放session  只存放username
                request.getSession().setAttribute("loginUser",username);
                //设置返回值
                List<UserResModel> userResModels = UserResModel.getUserResModels();
                UserResModel userResModel = UserResModel.getUserResModel();
                userResModel.setCurrTime(System.currentTimeMillis());
                userResModel.setUsername(username);
                userResModels.add(userResModel);
                return ResponseModel.successResModel(1,"SUCCESS",userResModels.toArray());

                default:
                    return ResponseModel.failResModel(0,"password error");
        }
    }
    @ResponseBody
    @PostMapping("/userExit")
    public ResponseModel userExit(HttpServletRequest request){
        request.getSession().removeAttribute("loginUser");
        return ResponseModel.successResModel(1,"logout success",null);
    }

    @ResponseBody
    @GetMapping("/userStatus")
    public ResponseModel userStatus(HttpServletRequest request){
            List<UserResModel> userResModels = UserResModel.getUserResModels();
            UserResModel userResModel = UserResModel.getUserResModel();
            userResModel.setUsername(request.getSession().getAttribute("loginUser")+"");
            userResModels.add(userResModel);
            return ResponseModel.successResModel(1,"success",userResModels.toArray());
    }
    @ResponseBody
    @GetMapping("/userInfo")
    public User userInfo(HttpServletRequest request){
        User user = new User();
        user.setUsername(request.getSession().getAttribute("loginUser")+"");
        List<User> users = userService.findUser(user);
        if(users.size()!=0){
            users.get(0).setPassword("*******");

            return users.get(0);
        }
        return null;
    }
    @ResponseBody
    @PostMapping("/userInfoUpdate")
    public int userInfoUpdate(String nickname,String password,String gender,String age,HttpServletRequest request){
        if(nickname!=null && password!= null && gender!=null && age!=null){
            User user = new User();
            user.setUsername(request.getSession().getAttribute("loginUser").toString());
            user.setPassword(DigestUtils.sha1Hex(password));
            user.setAge(age);
            user.setNickName(nickname);
            user.setGender(gender);
            int i = userService.updateUser(user);
            if(i!=0){
                return 1;
            }else{
                return 0;
            }
        }else {
            return 0;
        }
    }

    @ResponseBody
    @GetMapping("/isRegistered")
    public int isRegistered(String username){
      if(Objects.nonNull(username)){
          User user = new User();
          user.setUsername(username);
          List<User> user1 = userService.findUser(user);
          if(user1.size() == 0){
              return 1;
          }else{
              return 0;
          }
      }else{
          return 0;
      }
    }
    @ResponseBody
    @GetMapping("/signIn")
    public int register(String username,String password,String email){
        if(Objects.nonNull(username) && Objects.nonNull(password) && Objects.nonNull(email)){
            User user = new User();
            user.setUsername(username);
            user.setPassword(DigestUtils.sha1Hex(password));
            user.setEmail(email);
            user.setNickName(username);
            user.setGender("男");
            user.setAge("18");
            user.setCreateTime(DateUtils.getFormat(new Date()));
            int i = userService.insertUser(user);
            if(i!=0){
                return 1;
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }
}
