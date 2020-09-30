package xyz.zjhwork.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @Describe:
 * @Author: zjhChester
 * @Date: 16:15 2020/9/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationOutputDTO {
    private Integer totalCount;
    private List<NotificationModelDTO> resList;
}

