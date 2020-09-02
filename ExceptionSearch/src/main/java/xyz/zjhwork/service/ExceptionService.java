package xyz.zjhwork.service;


import org.apache.ibatis.annotations.Param;
import xyz.zjhwork.entity.Exception;

import java.util.List;

public interface ExceptionService {
    //CRUD
    List<Exception> findException(Exception e);
    int insertException(Exception e);
    int updateException(Exception e);


    //业务
    //检索
    List<Exception> search(List<String> keywords,String type,int currPage);
    int searchCount(List<String> keywords,String type);
    List<Exception> searchAssociation( List<String> keywords);
    List<Exception> newListException();
    List<Exception> myListException(String username);

    Exception findExceptionById(int id);
    int delException(Integer id);
}
