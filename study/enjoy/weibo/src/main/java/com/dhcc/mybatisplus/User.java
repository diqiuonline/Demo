package com.dhcc.mybatisplus;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@TableName("facebook1")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class User {
    private Long id;

    private String html;



}
