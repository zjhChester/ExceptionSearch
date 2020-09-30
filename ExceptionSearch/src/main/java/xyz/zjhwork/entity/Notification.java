package xyz.zjhwork.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Describe: 消息提醒
 * @Author: zjhChester
 * @Date: 2020-09-23
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements Serializable {
    private Integer id;
    private String receiver;
    private String sender;
    private Integer exceptionId;
    private String type;
    private String delStatus;
    private String createTime;
    private String modifyTime;

}
