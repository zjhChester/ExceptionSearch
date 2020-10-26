package xyz.zjhwork.dto;

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
public class ExceptionDTO {
    private Integer id;
    private String title;
    private String desc;
    private String createTime;
    private String author;
    private String remark;
    private String type;
    private Integer views;
    private String content;
}
