package xyz.zjhwork.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Describe:
 * @Author: zjhChester
 * @Date: 16:18 2020/9/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Integer id;
    private String receiver;
    private String sender;
    private String exceptionId;
    private String type;
    private String delStatus;
    private String createTime;
    private String modifyTime;
}
