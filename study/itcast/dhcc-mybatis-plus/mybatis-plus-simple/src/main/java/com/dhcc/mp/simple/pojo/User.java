package com.dhcc.mp.simple.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/7 15:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class User {

    private Long id;
    private String username;
    private String password;
    private String name;
    private Integer age;
    private String email;
}
