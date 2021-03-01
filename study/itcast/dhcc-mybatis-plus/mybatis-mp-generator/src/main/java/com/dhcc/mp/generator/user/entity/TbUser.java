package com.dhcc.mp.generator.user.entity;

    import java.io.Serializable;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 
    * </p>
*
* @author dhcc
* @since 2020-05-24
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class TbUser implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 用户名
            */
    private String username;

            /**
            * 密码
            */
    private String password;

            /**
            * 姓名
            */
    private String name;

            /**
            * 年龄
            */
    private Integer age;

            /**
            * 邮箱
            */
    private String email;


}
