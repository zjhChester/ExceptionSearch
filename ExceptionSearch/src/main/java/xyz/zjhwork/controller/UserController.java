package xyz.zjhwork.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
@Api(tags = "User Service Interfaces")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录接口", notes = "登录接口，进行登录请求前，需要对前端的密码进行MD5加密，否则会登录失败，当前版本使用的是session会话技术，如果是vue2.0+版本需要对返回的desc放在'Cookie:JSESSIONID='里面去，当成以后每一次权限控制接口的身份验证参数")
    @ResponseBody
    @PostMapping("/checkUser")
    public ResponseModel checkUser( String username,  String password, HttpServletRequest request) {
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
                return ResponseModel.successResModel(1,request.getSession().getId(),userResModels.toArray());

                default:
                    return ResponseModel.failResModel(0,"password error");
        }
    }
    @ApiOperation(value = "用户退出接口",notes = "用户退出时调用的接口，啥都不需要传，权限控制")
    @ResponseBody
    @PostMapping("/userExit")
    public ResponseModel userExit(HttpServletRequest request){
        request.getSession().removeAttribute("loginUser");
        return ResponseModel.successResModel(1,"logout success",null);
    }

    @ApiOperation(value = "用户登录状态接口",notes = "进行初始化页面调用的用户是否登录的状态接口")
    @ResponseBody
    @GetMapping("/userStatus")
    public ResponseModel userStatus(HttpServletRequest request){
            List<UserResModel> userResModels = UserResModel.getUserResModels();
            UserResModel userResModel = UserResModel.getUserResModel();
            userResModel.setUsername(request.getSession().getAttribute("loginUser")+"");
            userResModel.setCurrTime(System.currentTimeMillis());
            userResModels.add(userResModel);
            return ResponseModel.successResModel(1,"success",userResModels.toArray());
    }

    @ApiOperation(value = "用户信息接口",notes = "展示用户信息的接口，权限控制")
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
    @ApiOperation(value = "用户信息更新接口",notes = "用户进行信息更新的接口，权限控制")
    @ResponseBody
    @PutMapping("/userInfoUpdate")
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

    @ApiOperation(value = "判断用户是否已经注册接口",notes = "判断用户是否已经注册接口")
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
    @ApiOperation(value = "注册接口",notes = "注解用户的接口")
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
