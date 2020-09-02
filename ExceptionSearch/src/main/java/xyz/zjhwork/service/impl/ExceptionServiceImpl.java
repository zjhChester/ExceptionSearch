package xyz.zjhwork.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zjhwork.aop.interfaces.AddViewsCount;
import xyz.zjhwork.dao.ExceptionDao;
import xyz.zjhwork.entity.Exception;
import xyz.zjhwork.service.ExceptionService;

import java.util.ArrayList;
import java.util.List;
@Service
public class ExceptionServiceImpl implements ExceptionService {
    /**
     * CURD
     */
    @Autowired
    private ExceptionDao exceptionDao;

    @Override
    public List<Exception> findException(Exception e) {
        return exceptionDao.findException(e);
    }

    @Override
    public int insertException(Exception e) {
        return exceptionDao.insertException(e);
    }

    @Override
    public int updateException(Exception e) {
        return exceptionDao.updateException(e);
    }



    /**
     * 核心业务
     *搜索业务
     * @param
     * @return
     */
    @Override
    public List<Exception> search(List<String> keywords, String type, int currPage) {
        //起始下标
        int currIndex = (currPage - 1) * 20;
        switch (type) {
            //标题检索
            case "title":
                return exceptionDao.searchExceptionByKeywords(keywords, null, null, null, currIndex);
            //内容检索
            case "content":
                return exceptionDao.searchExceptionByKeywords(null, keywords, null, null, currIndex);
            //描述检索
            case "desc":
                return exceptionDao.searchExceptionByKeywords(null, null, keywords, null, currIndex);
            //类型检索
            case "type":
                return exceptionDao.searchExceptionByKeywords(null, null, null, keywords, currIndex);
//            //默认使用标题检索
            default:
                return exceptionDao.searchExceptionByKeywords(keywords, null, null, null, currIndex);
        }
    }


    /**
     * 检索结果数量  优化检索速度
     * @param keywords
     * @param type
     * @return
     */
    @Override
    public int searchCount(List<String> keywords, String type) {
        switch (type) {
            //标题检索
            case "title":
                return exceptionDao.searchCount(keywords, null, null, null);
            //内容检索
            case "content":
                return exceptionDao.searchCount(null, keywords, null, null);
            //描述检索
            case "desc":
                return exceptionDao.searchCount(null, null, keywords, null);
            //类型检索
            case "type":
                return exceptionDao.searchCount(null, null, null, keywords);
//            //默认使用标题检索
            default:
                return exceptionDao.searchCount(keywords, null, null, null);
        }
    }

    @Override
    public List<Exception> searchAssociation(List<String> keywords) {
        return exceptionDao.searchAssociation(keywords);

    }

    @Override
    public List<Exception> newListException() {
        List<Exception> exceptions = exceptionDao.newListException();

        return exceptions;
    }

    @Override
    public List<Exception> myListException(String username) {
        List<Exception> exceptions = exceptionDao.myListException(username);

        return exceptions;
    }

    @Override
    public Exception findExceptionById(int id) {
        return exceptionDao.findExceptionById(id);
    }

    @Override
    public int delException(Integer id) {
        return exceptionDao.delException(id);
    }


}
