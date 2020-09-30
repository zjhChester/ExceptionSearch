package xyz.zjhwork.service;

import xyz.zjhwork.dto.ResponseModel;



/**
 * Describe:
 * Author:
 * Date:
 */
public interface FileService {
    /**
     * 根据ids传入集合查询并导出压缩包
     */
    ResponseModel fileOutPut(Integer[]ids);
}
