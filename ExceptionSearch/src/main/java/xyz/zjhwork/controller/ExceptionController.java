package xyz.zjhwork.controller;

import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.zjhwork.entity.Exception;
import xyz.zjhwork.entity.History;
import xyz.zjhwork.resmodel.ResponseModel;
import xyz.zjhwork.service.ExceptionService;
import xyz.zjhwork.service.OtherService;
import xyz.zjhwork.utils.DateUtils;
import xyz.zjhwork.utils.SortUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 查询异常业务
 * 1、按关键词搜索
 * 2、分页
 * 3、按浏览量排名
 */
@Controller
public class ExceptionController {
    @Autowired
    private ExceptionService exceptionService;
    @Autowired
    private OtherService otherService;
    /**
     * 查询业务---->核心业务
     * 1、关键字查询
     * 2、查询类别选择title content type
     * 3、查询分页处理，每页加载20条
     * 4、包含检索速度
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/search")
    public ResponseModel search(String keywords,String type,Integer currPage){
        //空请求拦截
        if("".equals(keywords) || currPage==null || "".equals(type)){
            return ResponseModel.failResModel(0,"please input args");
        }
        //处理高亮关键字的算法
        StringBuilder realKeywords = new StringBuilder();
        Date date1 = new Date();

        //处理多个关键词的算法   转成关键词组
        String[] split = keywords.split("\\s+|、|，|。|；|？|！|,|\\.|;|\\?|!|]|的|得|地|中|内|外");

        //默认先搜索
        List<String> keywordsList = new ArrayList<>();
        for (String s:
                split) {
            //处理介词空串
            if(!"".equals(s)){
                keywordsList.add(s);
            }
        }

        //如果只输入了介词直接返回
        if(keywordsList.size()==0){
            return ResponseModel.failResModel(0,"please input args");
        }
        //将原关键词串也加入其中
        keywordsList.add(keywords);

        List<Exception> searchResList = exceptionService.search(keywordsList, type, currPage == null ? 1 : currPage);
        //不做搜索关键词处理，做高亮算法处理  提高精准度
        for (String s:
                split) {
            if(s.length()>=4){
                keywordsList.add(s.substring(0,s.length()/2));
                keywordsList.add(s.substring(s.length()/2));

            }
        }
        //其他优先级关键词
        for (String s:
             keywordsList) {
            realKeywords.append(s+",");
        }

        //根据搜索的出来的Id查询表
        List<Exception> resList = new ArrayList<>();
        for (Exception e:
                searchResList) {
            resList.add(exceptionService.findExceptionById(e.getId()));
        }
        //处理描述和标题字数过长
        for (Exception e:
                resList) {
            if(e.getDesc().length()>=40){
                e.setDesc(e.getDesc().substring(0,40)+"...");
            }
            if(e.getTitle().length()>=25){
                e.setTitle(e.getTitle().substring(0,25)+"...");
            }
        }
        Date date2 = new Date();
        long searchTime = date2.getTime()-date1.getTime();
//        //如果搜索为空的再次迭代
//        if(searchResList.size()==0 && split[0].length()>=2){
//            searchResList =  exceptionService.search(keywords.substring(split[0].length()/2,split[0].length()), type, currPage==null?1:currPage);
//            realKeywords =new StringBuilder(keywords.substring(split[0].length()/2,split[0].length()));
//        }
        return ResponseModel.successResModel(1,realKeywords.toString().trim().substring(0,realKeywords.length()-1), resList.toArray());
    }

    @ResponseBody
    @GetMapping("/searchCount")
    public ResponseModel searchCount(String keywords,String type){
        Date date1 = new Date();
        if("".equals(keywords) || "".equals(type)){
            return ResponseModel.failResModel(0,"please input args");
        }
        //处理多个关键词的算法
        String[] split = keywords.split("\\s+|、|，|。|；|？|！|,|\\.|;|\\?|!|]|的|得|地|中|内|外");
        List<String> keywordsList = new ArrayList<>();
        for (String s:
             split) {
            if(!"".equals(s)){
                keywordsList.add(s);
            }
        }

        //如果只输入了介词直接返回
        if(keywordsList.size()==0){
            return ResponseModel.failResModel(0,"please input args");
        }
        //默认先搜索
        int count = exceptionService.searchCount(keywordsList,type);

        //如果搜索为空的再次迭代
        Date date2 = new Date();
        long searchTime = date2.getTime()-date1.getTime();
//        if(count == 0 && keywords.length()>=2){
//            Date date3 = new Date();
//            count = exceptionService.searchCount(keywords.substring(keywords.length()/2,keywords.length()), type);
//            searchTime = date3.getTime() - date2.getTime();
//        }
        return ResponseModel.successResModel(count,"用时 "+searchTime/1000.0000+"秒",null);
    }

    /**
     * CRUD
     */
    //新增
    @ResponseBody
    @PostMapping("/newException")
    public ResponseModel newException(String title, String desc, String content, String type, HttpServletRequest request){
        if(Objects.nonNull(title)&&Objects.nonNull(desc)&Objects.nonNull(content)&Objects.nonNull(type)){
            Exception exception = new Exception();
            exception.setTitle(title);
            exception.setDesc(desc);
            exception.setType(type);
            exception.setContent(content);
            exception.setAuthor(request.getSession().getAttribute("loginUser")+"");
            exception.setCreateTime(DateUtils.getFormat(new Date()));
            exception.setViews(0);
            exception.setRemark("");
            int i = exceptionService.insertException(exception);
            if(i==0){
                return ResponseModel.failResModel(0,"create Exception fail!");
            }else{
                return ResponseModel.successResModel(1,"SUCCESS",null);
            }
        }else{
            return ResponseModel.failResModel(0,"please input args!");
        }

    }
    //查找
    @ResponseBody
    @GetMapping("/getException")
    public ResponseModel getException(@NotNull Integer id,HttpServletRequest request){
        if(Objects.nonNull(id)){
            //添加浏览记录
          if(request.getSession().getAttribute("loginUser")!=null){
              History history = new History();
              history.setExceptionId(id);
              history.setUserId(request.getSession().getAttribute("loginUser").toString());
              history.setRemark("");
              history.setTime(DateUtils.getFormat(new Date()));
              otherService.addHistory(history);
          }

            //查询exception
            Exception exception = new Exception();
            exception.setId(id);
            List<Exception> exceptions = exceptionService.findException(exception);

            if(exceptions.size()==0){
                return ResponseModel.failResModel(0,"no ExceptionId is "+id);
            }else{
                return ResponseModel.successResModel(1,"SUCCESS",exceptions.toArray());
            }
        }
        else{
            return ResponseModel.failResModel(0,"please input ExceptionId");
        }
    }

    /**
     * 联想词组
     * @param keywords
     * @return
     */
    @ResponseBody
    @GetMapping("/searchAssociation")
    public List<String> searchAssociation(String keywords){
        String[] split = keywords.split("\\s+|、|，|。|；|？|！|,|\\.|;|\\?|!|]|的|得|地|中|内|外");
        List<String> keywordsList = new ArrayList<>();
        for (String s:
                split) {
            //处理介词空串
            if(!"".equals(s)){
                keywordsList.add(s);
            }
        }

        //如果只输入了介词直接返回
        if(keywordsList.size()==0){
            List<String> strs = new ArrayList<>();
            strs.add("please input keywords");
            return strs;
        }
        List<Integer> list = exceptionService.searchAssociation(keywordsList);
        List<String> resList = new ArrayList<>();
        for (Integer i:
             list) {
            resList.add(exceptionService.findExceptionById(i).getTitle());
        }
        //处理过长
        List<String> FinResList = new ArrayList<>();

        for (String str:
        resList) {
            if(str.length()>=15)
                {
                  str = str.substring(0,15);
            }
            FinResList.add(str);
        }
        //不做搜索关键词处理，做高亮算法处理  提高精准度
        for (String s:
                split) {
            if(s.length()>=4){
                keywordsList.add(s.substring(0,s.length()/2));
                keywordsList.add(s.substring(s.length()/2));

            }
        }
        if(list.size()==0){
            List<String> strs = new ArrayList<>();
            strs.add("no Association words");
            return strs;
        }else{
            return FinResList;
        }
    }

    //最新文章
    @ResponseBody
    @GetMapping("/newListException")
    public List<Exception> newListException(){
        List<Exception> exceptions = exceptionService.newListException();
        List<Exception> resList = new ArrayList<>();
        for (Exception e:
             exceptions) {
            resList.add(exceptionService.findExceptionById(e.getId()));
        }
        for (Exception e :
                resList) {
            if(e.getTitle().length()>=20)
            {
                e.setTitle(e.getTitle().substring(0,20));
            }
        }
        return SortUtils.sort(resList);
    }

    //我的文章
    @ResponseBody
    @GetMapping("/myListException")
    public List<Exception> myListException(HttpServletRequest request){
        List<Exception> exceptions = exceptionService.myListException(request.getSession().getAttribute("loginUser")+"");
        List<Exception> resList = new ArrayList<>();
        for (Exception e:
                exceptions) {
            resList.add(exceptionService.findExceptionById(e.getId()));
        }
        for (Exception e :
                resList) {
            if(e.getTitle().length()>=20)
            {
                e.setTitle(e.getTitle().substring(0,20));
            }
        }
        return SortUtils.sort(resList);
    }

}
