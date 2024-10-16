package com.dhcc.shanjupay.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 权限组
 * </p>
 *
 *
 * @since 2019-08-13
 */
@Data
@TableName("authorization_privilege_group")
public class AuthorizationPrivilegeGroup  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value="ID",type = IdType.AUTO)
    private Long id;

    /**
     * 父id
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 权限组名称
     */
    @TableField("NAME")
    private String name;


}
