package com.dhcc.shanjupay.user.mapper;

import com.dhcc.shanjupay.user.api.dto.authorization.RoleDTO;
import com.dhcc.shanjupay.user.entity.AuthorizationRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色信息 Mapper 接口
 * </p>
 *
 *
 * @since 2019-08-13
 */
@Repository
public interface AuthorizationRoleMapper extends BaseMapper<AuthorizationRole> {

    //@Select("select * from authorization_role r where  r.TENANT_ID=#{tenantId} and  code  in (#{codes})  ")
    //List<AuthorizationRole> selectRoleInTenant(@Param("tenantId") Long tenantId,@Param("codes") String codes);

    /**
     * 某个角色的权限
     * @param id
     * @return
     */
    @Select("select p.`CODE` from authorization_privilege p\n" +
            "\tLEFT JOIN authorization_role_privilege rp on rp.PRIVILEGE_ID = p.ID\n" +
            "\tLEFT JOIN authorization_role r on rp.ROLE_ID = r.ID\n" +
            "\twhere r.ID=#{id}")
    List<String>  selectPrivilegeByRole(@Param("id") Long id);

    /**
     * 在租户下创建角色
     * @param tenantId
     * @param roles
     */
    @Insert("<script>" +
            "INSERT INTO authorization_role(NAME,CODE,TENANT_ID) VALUES " +
            "<foreach collection='roles' item='item'  separator=','>(#{item.name},#{item.code},#{tenantId})</foreach> " +
            "</script>")
    @Options(useGeneratedKeys = true,keyProperty = "roles.id")
    void createRoles(@Param("tenantId") Long tenantId,@Param("roles") List<AuthorizationRole> roles);

    /**
     * 判断某租户下角色code是否存在
     * @param tenantId
     * @param roleCode
     * @return
     */
    @Select("select count(*) from authorization_role r where r.TENANT_ID=#{tenantId} and r.`CODE` =#{roleCode}")
    int selectRoleCodeInTenant(@Param("tenantId") Long tenantId,@Param("roleCode") String roleCode);
}
