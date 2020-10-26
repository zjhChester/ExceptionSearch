package xyz.zjhwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zjhwork.entity.Exception;
import xyz.zjhwork.dto.ResponseModel;
import xyz.zjhwork.service.ExceptionService;
import xyz.zjhwork.service.FileService;
import xyz.zjhwork.utils.DateUtils;
import xyz.zjhwork.utils.ZipUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * Author:zjhChester
 * Date:
 */
@RestController
@Api(tags = "文件导出接口 FileOutPut Interfaces")
public class FileController {
    private final FileService fileService;
    private final ExceptionService exceptionService;
    @Autowired
    public FileController(FileService fileService, ExceptionService exceptionService) {
        this.fileService = fileService;
        this.exceptionService = exceptionService;
    }

    @ApiOperation(value = "文件导出接口",notes = "需要传入id集合，用于导出需要的文章，前端建议做下拉复选框/搜索复选框")
    @GetMapping("file/fileoutput")
    public ResponseModel fileOutPut(Integer []ids, HttpServletResponse response,HttpServletRequest request){
        ResponseModel responseModel = fileService.fileOutPut(ids);
        //zip打包
        List<String> nameList = new ArrayList<>();
        for (Object o : responseModel.getResult()) {
            File file =(File)o;
            nameList.add( file.getPath());
        }
        ZipUtils.downloadZipFiles(response, nameList, String.format("fileDownLoad-%s-%s-%s.zip",request.getSession().getAttribute("loginUser").toString(),responseModel.getResult().length, DateUtils.CURRENT_TIME));
        return ResponseModel.successResModel(1,responseModel.getDesc(),ids);
    }
    @GetMapping("file/myfileoutput")
    public ResponseModel fileOutPut(HttpServletResponse response,HttpServletRequest request){
        List<Exception> exceptions = exceptionService.findException(Exception.builder().author(request.getSession().getAttribute("loginUser").toString()).build());
        List<Integer> list = new ArrayList<>();
        exceptions.forEach(e->{list.add(e.getId());});
        Integer[] integers = list.toArray(new Integer[0]);
        ResponseModel responseModel = fileService.fileOutPut(integers);
        //zip打包
        List<String> nameList = new ArrayList<>();
        for (Object o : responseModel.getResult()) {
            File file =(File)o;
            nameList.add( file.getPath());
        }
        ZipUtils.downloadZipFiles(response, nameList, String.format("fileDownLoad-%s-%s-%s.zip",request.getSession().getAttribute("loginUser").toString(),responseModel.getResult().length, DateUtils.CURRENT_TIME));
        return ResponseModel.successResModel(1,responseModel.getDesc(),list.toArray());
    }
    /**
     * 格式化内容contentToMarkdown
     * @param request
     */
//    @GetMapping("test/update-content")
    public void updatingContent(HttpServletRequest request){
        List<Exception> list = exceptionService.findException(Exception.builder().author(request.getSession().getAttribute("loginUser").toString()).build());
//        List<Exception> list = exceptionService.findException(Exception.builder().id(98880).build());
        list.forEach(l->{
            l.setContent(l.getContent().replaceAll("```","\r\n```"));
        });
        for (Exception e : list) {

            char[] content = e.getContent().toCharArray();
            List<Integer> indexs=new ArrayList<>();
            for (int i = 0; i < content.length-1; i++) {
                if(content[i] == '#'&&content[i+1]!='#'){
                    indexs.add(i);
                }
            }
            for (Integer index : indexs) {
                e.setContent(e.getContent().replaceAll(content[index]+""+content[index+1]+"",content[index]+" "+content[index+1]));
            }
            int flag = exceptionService.updateException(e);
            System.out.println(flag);
        }

    }
}
