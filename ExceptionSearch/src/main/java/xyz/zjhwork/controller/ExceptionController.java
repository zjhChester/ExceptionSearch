package xyz.zjhwork.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.zjhwork.aop.interfaces.AddViewsCount;
import xyz.zjhwork.entity.Exception;
import xyz.zjhwork.entity.History;
import xyz.zjhwork.resmodel.ResponseModel;
import xyz.zjhwork.service.ExceptionService;
import xyz.zjhwork.service.OtherService;
import xyz.zjhwork.utils.DateUtils;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;


/**
 * 查询异常业务
 * 1、按关键词搜索
 * 2、分页
 * 3、按浏览量排名
 */
@Controller
@Api(tags = "Base Service interfaces ")
@Slf4j
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
    @ApiOperation(value = "文章搜索接口", notes = "分页搜索，返回值的desc为高亮关键词（也可以前端处理，后端做过一次算法处理，可能不全面）")
    @ResponseBody
    @GetMapping("/search")
    public ResponseModel search(String keywords,Integer currPage){
        //空请求拦截
        if("".equals(keywords) || currPage==null){
            return ResponseModel.failResModel(0,"please input args");
        }
        //处理高亮关键字的算法
        StringBuilder realKeywords = new StringBuilder();

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
        if(keywordsList.size()>1)
        keywordsList.add(keywords);
        List<Exception> resList = exceptionService.search(keywordsList, "title", currPage == null ? 1 : currPage);
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
//        List<Exception> resList = new ArrayList<>();
        //处理描述和标题字数过长
        for (Exception e:
                resList) {
            if(e.getDesc().trim().length()>=40){
                e.setDesc(e.getDesc().substring(0,40)+"...");
            }
            if(e.getTitle().trim().length()>=25){
                e.setTitle(e.getTitle().substring(0,25)+"...");
            }
        }

//        //如果搜索为空的再次迭代
//        if(searchResList.size()==0 && split[0].length()>=2){
//            searchResList =  exceptionService.search(keywords.substring(split[0].length()/2,split[0].length()), type, currPage==null?1:currPage);
//            realKeywords =new StringBuilder(keywords.substring(split[0].length()/2,split[0].length()));
//        }
        return ResponseModel.successResModel(1,realKeywords.toString().trim().substring(0,realKeywords.length()-1), resList.toArray());
    }

    @ApiOperation(value = "搜索条数接口", notes = "该接口配合搜索分页搜索接口进行分页，成功返回的desc为响应时间")
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
    @ApiOperation(value = "创建文章接口", notes = "新建文章接口（专用），传入标题，描述，内容，类型即可，其中内容支持富文本格式（markdown格式），最好前端选型即为markdown，其余参数后台生成，登录权限控制")
    @PostMapping("/newException")
    @ResponseBody
    public ResponseModel newException(@RequestBody @Valid Exception exception, HttpServletRequest request){
            exception.setAuthor(request.getSession().getAttribute("loginUser").toString());
            exception.setCreateTime(DateUtils.getFormat(new Date()));
            exception.setViews(0);
            exception.setRemark("");
            int i = exceptionService.insertException(exception);
            if(i==0){
                return ResponseModel.failResModel(0,"create Exception fail!");
            }else{
                return ResponseModel.successResModel(exceptionService.findException( Exception.builder().title(exception.getTitle()).build()).get(0).getId(),"SUCCESS",null);
            }


    }
    @ApiOperation(value = "当前用户文章的详情接口", notes = "用户文章详情接口，修改文章加载专用，权限控制，传入id即可，配合myListException接口进行使用，登录权限控制")
    @GetMapping("/exception/exceptionByUserAndId/{id}")
    @ResponseBody
    public ResponseModel exceptionByUserAndIdGet(@PathVariable("id")Integer id,HttpServletRequest request){
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser!=null){
            List<Exception> exceptions = exceptionService.findException(Exception.builder().id(id).author(loginUser.toString()).build());
            if(exceptions.size()!=0){
                return ResponseModel.successResModel(1,"success",exceptions.toArray());
            }else{
                return ResponseModel.failResModel(0,"只能修改自己的文章噢！");
            }
        }else{
            return ResponseModel.failResModel(0,"no session user");
        }
    }
    @ApiOperation(value = "修改文章接口", notes = "修改文章接口（专用），传入标题，描述，内容，类型即可，其余参数后台生成，登录权限控制")
    @PutMapping ("/exception/exceptionByUserAndId/{id}")
    @ResponseBody
    public ResponseModel exceptionByUserAndIdPut(@PathVariable ("id") Integer id,HttpServletRequest request, @RequestBody @Valid Exception exception){
        //防止接收失效
        exception.setId(id);
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser!=null&&exception.getTitle()!=null){
            exception.setCreateTime(DateUtils.getFormat(new Date()));
            int i = exceptionService.updateException(exception);
            return i==1?ResponseModel.successResModel(1,"SUCCESS",null):ResponseModel.failResModel(0,"修改失败");
        }else{
            return ResponseModel.failResModel(0,"no session user or gr");
        }
    }
    //查找
    @ApiOperation(value = "文章详情接口", notes = "文章详情接口（通用），传入id即可")
    @ResponseBody
    @AddViewsCount
    @GetMapping("/exception/{id}")
    public ResponseModel getException(@PathVariable("id")Integer id,HttpServletRequest request){
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
    @ApiOperation(value = "联想词组接口", notes = "联想词组接口，放在搜索框下面展示的结果列表")
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
        List<Exception> resList = exceptionService.searchAssociation(keywordsList);


        //处理过长
        List<String> FinResList = new ArrayList<>();

        for (Exception str:
        resList) {
            if(str.getTitle().trim().length()>=20)
                FinResList.add(str.getTitle().trim().substring(0,20));
            else
            FinResList.add(str.getTitle().trim());
        }
        if(resList.size()==0){
            List<String> strs = new ArrayList<>();
            strs.add("no Association words");
            return strs;
        }else{
            return FinResList;
        }
    }

    //最新文章
    @ApiOperation(value = "用户中心>最新文章接口", notes = "最新文章接口（专用），登录权限控制")
    @ResponseBody
    @GetMapping("/newListException")
    public List<Exception> newListException(){
        List<Exception> resList = exceptionService.newListException();
        return resList;
    }

    //我的文章
    @ApiOperation(value = "用户中心>我的文章接口", notes = "我的文章接口（专用），登录权限控制")
    @ResponseBody
    @GetMapping("/myListException")
    public List<Exception> myListException(HttpServletRequest request){
        List<Exception> resList = exceptionService.myListException(request.getSession().getAttribute("loginUser")+"");
        return resList;
    }
    @ApiOperation(value = "删除文章接口", notes = "删除文章接口（专用），登录权限控制")
    @DeleteMapping("/exception/{id}")
    @ResponseBody
    public ResponseModel delException(@PathVariable("id") Integer id,HttpServletRequest request){
        if(request.getSession().getAttribute("loginUser")==null){
            return ResponseModel.failResModel(0,"非法请求");
        }
        Exception exception = new Exception();
        exception.setId(id);
        List<Exception> list = exceptionService.findException(exception);

        if(list.size()==0) {
           return ResponseModel.failResModel(0,"非法参数");
        }else{
            if(request.getSession().getAttribute("loginUser").toString().equals(list.get(0).getAuthor())){
                int i = exceptionService.delException(id);
                return i==0?ResponseModel.failResModel(0,"删除失败，请联系管理员"):ResponseModel.successResModel(1,"删除成功",null);
            }else{
                return ResponseModel.failResModel(0,"不能删除不是自己的文章");
            }
        }
    }


}
