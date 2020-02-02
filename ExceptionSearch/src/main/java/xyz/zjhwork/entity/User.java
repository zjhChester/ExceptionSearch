package xyz.zjhwork.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String nickName;
    private String age;
    private String gender;
    private String email;
    private String createTime;
    private String remark;

}
