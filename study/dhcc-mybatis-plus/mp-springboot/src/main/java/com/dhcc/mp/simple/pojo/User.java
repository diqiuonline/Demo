package com.dhcc.mp.simple.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dhcc.mp.simple.emuns.SexEnum;
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
@KeySequence(value = "SEQ_USER",clazz = Long.class)
public class User extends Model<User> {
    @TableId(type = IdType.INPUT)  //指定主键为自增长  input  用户输入
    private Long id;
    private String username;
    @TableField(fill = FieldFill.INSERT)
    private String password;
    private String name;
    private Integer age;
    private String email;
    @Version //乐观锁版本
    private Integer version;
    @TableLogic  //逻辑删除 1删除 0 未删除
    private Integer deleted;

    private SexEnum sex;  //性别
}
