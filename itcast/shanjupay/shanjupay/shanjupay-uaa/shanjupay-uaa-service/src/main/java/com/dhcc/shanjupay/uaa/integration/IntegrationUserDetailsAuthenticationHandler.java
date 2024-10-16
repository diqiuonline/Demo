package com.dhcc.shanjupay.uaa.integration;

import com.alibaba.fastjson.JSON;
import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.util.StringUtil;
import com.dhcc.shanjupay.uaa.domain.AuthPrincipal;
import com.dhcc.shanjupay.uaa.domain.UnifiedUserDetails;
import com.dhcc.shanjupay.user.api.TenantService;
import com.dhcc.shanjupay.user.api.dto.authorization.AuthorizationInfoDTO;
import com.dhcc.shanjupay.user.api.dto.resource.ApplicationDTO;
import com.dhcc.shanjupay.user.api.dto.resource.ResourceDTO;
import com.dhcc.shanjupay.user.api.dto.tenant.LoginInfoDTO;
import com.dhcc.shanjupay.user.api.dto.tenant.LoginRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class IntegrationUserDetailsAuthenticationHandler {

    private TenantService tenantService;


    public void setTenantService(TenantService tenantService) {
        this.tenantService = tenantService;
    }


    /**
     * 认证处理 简易判断，后期优化结构
     * @param authPrincipal 认证用户的身份信息
     * @param credentials 认证用户的登录凭证
     * @return
     */
    public UnifiedUserDetails authentication(AuthPrincipal authPrincipal, String credentials) {
        /**
         * 1.调用TenantService.login(LoginRequestDTO loginRequest)获取应用、权限、资源，设置为UnifiedUserDetails.payload
         * 2.根据client_id获取当前接入应用所属租户ID(ResourceService.queryApplication(String applicationCode))，设置为UnifiedUserDetails.tenantId
         **/

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setPrincipal(authPrincipal.getUsername());
        loginRequestDTO.setCertificate(credentials);
        loginRequestDTO.setAuthenticationType(authPrincipal.getAuthenticationType());
        if("sms".equals(authPrincipal.getAuthenticationType())){
            loginRequestDTO.setSmsKey(authPrincipal.getPayload().get("smsKey").toString());
        }
        LoginInfoDTO loginInfoDTO = null;
        try {
            loginInfoDTO = tenantService.login(loginRequestDTO);
            log.info("loginInfoDTO:{}",JSON.toJSONString(loginInfoDTO));
        } catch (Exception ex) {
            ex.printStackTrace();
            if(ex instanceof BusinessException){
                BusinessException be = (BusinessException) ex;
                log.info(JSON.toJSONString(be));
                throw new BadCredentialsException("login error-" + be.getErrorCode().getDesc());
            } else {
                throw new BadCredentialsException("login error " + ex.getMessage());
            }
        }
        if(loginInfoDTO == null){
            throw new BadCredentialsException("User not found");
        }

        //%%%%%%%%%%%%%%   上面是login返回的结构 自行格式化查看 需要组装成下面结构   %%%%%%%%%%%%%%%%%%
            /*"payload": {    //描述的是某账号在不同租户下的信息
                "租户A": { //登录账号所属租户id，每个登录账号可能从属多个租户
                    "user_authorities": { //账号在某租户下权限
                        "ROLE_DOMAIN": [ //角色
                            "P1",//角色下权限
                            "P2"
                        ]
                    },
                    "resources": { //账号在某租户下拥有的资源
                        "应用A": { //应用id，资源按应用分别描述
                            "menu": {}, //某资源类型下资源，如菜单类型、按钮类型
                            "button": {}
                        },
                        "应用B": {}
                    }
                },
                "租户B": {}
            }
            */
        UnifiedUserDetails userDetails = new UnifiedUserDetails(authPrincipal.getUsername(), credentials);
        userDetails.setMobile(loginInfoDTO.getMobile());
        Map<Long, Object> payload = new HashMap<>();//拼最后的payload结构
        Map<Long, AuthorizationInfoDTO> tenantAuthorizationInfoMap = loginInfoDTO.getTenantAuthorizationInfoMap();//得到权限信息
        Map<Long, List<ResourceDTO>> resources = loginInfoDTO.getResources();// 得到资源信息

        for(Map.Entry<Long, AuthorizationInfoDTO> entry : tenantAuthorizationInfoMap.entrySet()){
            Long tenantId = entry.getKey();
            Map map = new HashMap();
            map.put("user_authorities",entry.getValue().getRolePrivilegeMap());
            map.put("resources",resources.get(tenantId));

            payload.put(tenantId,map);
        }
        userDetails.setPayload(payload);

        Map detailsMap = authPrincipal.getPayload();
        String client_id = (String)detailsMap.get("client_id");

        //授权码模式 简化模式没有client_id  密码模式会传client_id
        if(StringUtil.isNotBlank(client_id)){
            ApplicationDTO applicationDTO = tenantService.getApplicationDTOByClientId(client_id);
            Map tenantIdMap = new HashMap();
            tenantIdMap.put("tenantId",applicationDTO.getTenantId());
            userDetails.setTenant(tenantIdMap);
        }
        log.info("@@@@@@@@@@@:{}",JSON.toJSONString(userDetails));
        return  userDetails;
    }

}

