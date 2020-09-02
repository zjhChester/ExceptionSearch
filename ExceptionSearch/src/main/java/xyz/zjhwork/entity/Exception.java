package xyz.zjhwork.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exception  implements Serializable {
    private Integer id;
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "描述不能为空")
    private String desc;
    @NotBlank(message = "内容不能为空")
    private String content;
    private String createTime;

    private String author;

    private String remark;
    @NotBlank(message = "类型不能为空")
    private String type;
    private Integer views;
}
