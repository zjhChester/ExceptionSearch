package xyz.zjhwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.zjhwork.conf.exception.BlogSystemException;
import xyz.zjhwork.entity.Exception;
import xyz.zjhwork.entity.Favorite;
import xyz.zjhwork.entity.Notification;
import xyz.zjhwork.service.ExceptionService;
import xyz.zjhwork.service.NotificationService;
import xyz.zjhwork.service.OtherService;
import xyz.zjhwork.utils.DateUtils;
import xyz.zjhwork.utils.SortUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 收藏的业务
 * @author zjhChester
 */
@Api(tags = "Favorite Msg Service interfaces ")
@RestController
public class FavController {
    @Autowired
    private OtherService otherService;


    /**
     * 查询当前文章是否已经收藏
     * @param id 文章id
     * @param request session用户信息
     * @return int是or否
     */
    @ApiOperation(value = "查询是否收藏接口", notes = "查询当前用户是否已经收藏，权限控制")
    @ResponseBody
    @GetMapping("/isFavByUsernameAndExceptionId")
    public int isFavByUsernameAndExceptionId(Integer id, HttpServletRequest request){
        if(id == null){
            throw new BlogSystemException("文章id不能为空");
        }
            Favorite favorite = new Favorite();
            favorite.setExceptionId(id);
            favorite.setUserId(request.getSession().getAttribute("loginUser")+"");
            int favByUsernameAndExceptionId = otherService.isFavByUsernameAndExceptionId(favorite);
            if(favByUsernameAndExceptionId!=0){
                return 1;
            }else
            {
                return 0;
            }

    }

    /**
     * 查询收藏文章列表
     * @param request session
     * @return List<Exception>
     */
    @ApiOperation(value = "收藏文章列表接口", notes = "查询当前用户收藏文章列表接口，权限控制")
    @ResponseBody
    @GetMapping("/findFavByUsername")
    public List<Exception> findFavByUsername(HttpServletRequest request){
        return otherService.findFavByUsername(request.getSession().getAttribute("loginUser").toString());
    }

    /**
     * 取消收藏
     * @param id      文章id
     * @param request session
     * @return int 相应条数成功与否
     */
    @ApiOperation(value = "取消收藏接口", notes = "取消当前用户对该文章的收藏")
    @ResponseBody
    @PostMapping("/deleteFavFromFavByUsernameAndExceptionId")
    public int deleteFavFromFavByUsernameAndExceptionId(Integer id, HttpServletRequest request){
      if(id==null) {
          throw new BlogSystemException("文章id不能为空");
      }
          Favorite favorite = new Favorite();
          favorite.setExceptionId(id);
          favorite.setUserId(request.getSession().getAttribute("loginUser").toString());
          int i = otherService.deleteFavFromFavByUsernameAndExceptionId(favorite);
          if(i != 0){
              return 1;
          }else{
              return 0;
          }
    }

    /**
     * 添加收藏接口
     * @param id 文章id
     * @param request session
     * @return int 成功与否
     */
    @ApiOperation(value = "添加收藏接口", notes = "当前用户添加该文章为自己的收藏文章")
    @ResponseBody
    @PostMapping("/addFavByUsernameAndExceptionId")
    public int addFavByUsernameAndExceptionId(Integer id, HttpServletRequest request){
        if(id==null){
            throw new BlogSystemException("文章id 不能为空");
        }
            Favorite favorite = new Favorite();
            favorite.setExceptionId(id);
            favorite.setUserId(request.getSession().getAttribute("loginUser").toString());
            //避免事务延迟
            int favByUsernameAndExceptionId = otherService.isFavByUsernameAndExceptionId(favorite);

            if(favByUsernameAndExceptionId!=0){
                throw new BlogSystemException("该文章已收藏");
            }
            favorite.setRemark("");
            favorite.setTime(DateUtils.getFormat(new Date()));
            int i = otherService.addFavByUsernameAndExceptionId(favorite);
            if(i == 0){
                throw new BlogSystemException("收藏失败");
            }
            return 1;
    }
}
