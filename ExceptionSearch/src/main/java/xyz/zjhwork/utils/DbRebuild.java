package xyz.zjhwork.utils;

import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.zjhwork.dao.ExceptionDao;
import xyz.zjhwork.entity.Exception;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describe:用于md文档反向导入blog.zjhwork.xyz博客
 * Author:zjhChester
 * Date:2020-09-14
 * @author zjhChester
 */
@Api(tags = "dataBase rebuild ")
public class DbRebuild {
    @Autowired
    private ExceptionDao exceptionDao;
    @GetMapping("/rebuild")
    public int reBuild(){
        List<Exception> list = fileScan();
        list.forEach(l->exceptionDao.insertException(l));

        return list.size();
    }

    private  List<Exception> fileScan(){
        String path = "E:\\workplace\\my_notes\\personal_directory\\posts_";
        File directory = new File(path);
        File[] files = directory.listFiles();
        List<Exception> commentList = new ArrayList<>();
        String content;
        String prefix;
        String type;
        String title;
        String date;
        String author;
        String desc;
        for (File f : files != null ? files : new File[0]) {
            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8);
                StringBuilder sb = new StringBuilder();

                int count =0;
                while ((count=is.read())!=-1){
                    sb.append((char)count);
                }
                title = f.getName().substring(11,f.getName().length()-3);
                date = f.getName().substring(0,10)+" 00:00:00";
                prefix = sb.substring(0,sb.indexOf("#"));
                type = prefix.substring(prefix.indexOf("- ")+2,prefix.length()-3);
                content= sb.substring(sb.indexOf("#"),sb.length());
                author = prefix.substring(prefix.indexOf("author:")+"author:".length(),prefix.indexOf("header-img")).trim();
                desc = content.substring(0,50);
                commentList.add(Exception.builder().title(title).createTime(date).type(type).content(content).author(author).desc(desc).build());
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return commentList;
    }

}
