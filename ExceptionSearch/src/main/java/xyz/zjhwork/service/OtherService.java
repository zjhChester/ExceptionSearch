package xyz.zjhwork.service;

import xyz.zjhwork.entity.*;
import xyz.zjhwork.entity.Exception;

import java.util.List;

/**
 * 包含所有对exception的附加操作
 */
public interface OtherService {
    int insertComment(Comment comment);
    List<Comment> findComment(Comment comment);
    int isAproByUsernameAndExceptionId(Approve approve);
    int addAproByUsernameAndExceptionId(Approve approve);
    List<Exception> findFavByUsername(String username);
    int isFavByUsernameAndExceptionId(Favorite favorite);
    int deleteFavFromFavByUsernameAndExceptionId(Favorite favorite);
    int addFavByUsernameAndExceptionId(Favorite favorite);
    List<Exception> findHistoryByUsername(String username);
    int addHistory(History history);
    int findApproCountByExceptionId(Integer id);
    Integer queryHistoryByUserIdAndExceptionId(History history);
    //更新time字段
    int updateHistory(History history);
}
