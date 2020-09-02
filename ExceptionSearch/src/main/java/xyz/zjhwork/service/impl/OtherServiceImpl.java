package xyz.zjhwork.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zjhwork.dao.AproveDao;
import xyz.zjhwork.dao.CommentDao;
import xyz.zjhwork.dao.FavDao;
import xyz.zjhwork.dao.HisDao;
import xyz.zjhwork.entity.*;
import xyz.zjhwork.entity.Exception;
import xyz.zjhwork.service.OtherService;
import xyz.zjhwork.utils.DateUtils;

import java.util.Date;
import java.util.List;

@Service
public class OtherServiceImpl implements OtherService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private AproveDao aproveDao;
    @Autowired
    private FavDao favDao;
    @Autowired
    private HisDao hisDao;
    @Override
    public int insertComment(Comment comment) {
        return commentDao.insertComment(comment);
    }

    @Override
    public List<Comment> findComment(Comment comment) {
        return commentDao.findComment(comment);
    }

    @Override
    public int isAproByUsernameAndExceptionId(Approve approve) {
        return aproveDao.isAproByUsernameAndExceptionId(approve);
    }

    @Override
    public int addAproByUsernameAndExceptionId(Approve approve) {
        return aproveDao.addAproByUsernameAndExceptionId(approve);
    }

    @Override
    public List<Exception> findFavByUsername(String username) {
        return favDao.findFavByUsername(username);
    }

    @Override
    public int isFavByUsernameAndExceptionId(Favorite favorite) {
        return favDao.isFavByUsernameAndExceptionId(favorite);
    }

    @Override
    public int deleteFavFromFavByUsernameAndExceptionId(Favorite favorite) {
        return favDao.deleteFavFromFavByUsernameAndExceptionId(favorite);
    }

    @Override
    public int addFavByUsernameAndExceptionId(Favorite favorite) {
        return favDao.addFavByUsernameAndExceptionId(favorite);
    }

    @Override
    public List<Exception> findHistoryByUsername(String username) {
        return hisDao.findHistoryByUsername(username);
    }

    @Override
    public int addHistory(History history) {
        //添加前先检查是否已存在
        Integer id = queryHistoryByUserIdAndExceptionId(history);
        if(id!=null){
        //
            history.setTime(DateUtils.getFormat(new Date()));
            return updateHistory(history);
        }else{
            return hisDao.addHistory(history);
        }
    }

    @Override
    public int findApproCountByExceptionId(Integer id) {
        return aproveDao.findApproCountByExceptionId(id);
    }

    @Override
    public Integer queryHistoryByUserIdAndExceptionId(History history) {
        return hisDao.queryHistoryByUserIdAndExceptionId(history);
    }

    @Override
    public int updateHistory(History history) {
        return hisDao.updateHistory(history);
    }
}
