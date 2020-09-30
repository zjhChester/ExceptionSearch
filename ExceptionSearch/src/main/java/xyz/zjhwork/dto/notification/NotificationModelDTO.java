package xyz.zjhwork.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.zjhwork.dto.ExceptionDTO;

/**
 * @Describe:
 * @Author: zjhChester
 * @Date: 16:57 2020/9/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationModelDTO{
    private NotificationDTO notificationDTO;
    private ExceptionDTO exceptionDTO;
}
