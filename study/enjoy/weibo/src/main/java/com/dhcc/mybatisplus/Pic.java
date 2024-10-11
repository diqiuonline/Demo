package com.dhcc.mybatisplus;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@TableName("pic")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class Pic {
    private Long id;

    private String html;
    private String status;
}
