package xyz.zjhwork.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.zjhwork.resmodel.ResponseModel;
import xyz.zjhwork.resmodel.userResModel.UserResModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRequestURI().equals("/")){
            //欢迎页面
            request.getRequestDispatcher("/index.html").forward(request,response);
            return false;
       }

        if(Objects.nonNull(request.getSession().getAttribute("loginUser"))){
            return true;
        }
        else{
            ResponseModel responseModel = ResponseModel.getResponseModel();
            List<UserResModel> userResModels = UserResModel.getUserResModels();
            responseModel.setCode(0);
            responseModel.setDesc("no session user");
            responseModel.setResult(userResModels.toArray());
            response.getWriter().print(JSONObject.toJSON(responseModel));
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
