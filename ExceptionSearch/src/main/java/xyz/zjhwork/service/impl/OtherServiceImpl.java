package xyz.zjhwork.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zjhwork.dao.AproveDao;
import xyz.zjhwork.dao.CommentDao;
import xyz.zjhwork.dao.FavDao;
import xyz.zjhwork.dao.HisDao;
import xyz.zjhwork.entity.*;
import xyz.zjhwork.service.OtherService;

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
    public List<Favorite> findFavByUsername(String username) {
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
    public List<History> findHistoryByUsername(String username) {
        return hisDao.findHistoryByUsername(username);
    }

    @Override
    public int addHistory(History history) {
        return hisDao.addHistory(history);
    }

    @Override
    public int findApproCountByExceptionId(Integer id) {
        return aproveDao.findApproCountByExceptionId(id);
    }
}
