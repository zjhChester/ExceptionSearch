package xyz.zjhwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.zjhwork.entity.Comment;
import xyz.zjhwork.service.OtherService;
import xyz.zjhwork.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private OtherService otherService;
    //添加评论
    @ResponseBody
    @PostMapping("/insertComment")
    public int insertComment(Integer id, String content, HttpServletRequest request){
        if(id!=null && content!=null){
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setExceptionId(id);
            comment.setRemark("");
            comment.setTime(DateUtils.getFormat(new Date()));
            comment.setUserId(request.getSession().getAttribute("loginUser").toString());
            int i = otherService.insertComment(comment);
            if(i!=0){
                return 1;
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }

    //查询博文的评论 //不需要走拦截器
    @ResponseBody
    @GetMapping("/findComment")
    public List<Comment> findComment(Integer id){
        if(id!=0){
            Comment comment = new Comment();
            comment.setExceptionId(id);
            List<Comment> comments = otherService.findComment(comment);
            return comments;
        }else{
            return null;
        }
    }
}
