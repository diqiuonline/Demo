package com.dhcc.shanjupay.user.api;

import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.domain.PageVO;
import com.dhcc.shanjupay.user.api.dto.menu.MenuDTO;
import com.dhcc.shanjupay.user.api.dto.menu.MenuQueryDTO;

import java.util.List;

/**
 * 菜单服务
 * 菜单为手工建立维护
 */
public interface MenuService {

    /**
     * 根据ID查询菜单
     * @param id
     * @return
     */
    MenuDTO queryMenu(Long id) throws BusinessException;

    /**
     * 根据应用编码查询菜单列表
     * @param applicationCode
     * @return
     */
    List<MenuDTO> queryMenuByApplicationCode(String applicationCode) throws BusinessException;
    /**
     * 根据条件查询菜单列表
     * @param params
     * @param pageSize
     * @param pageNo
     */
    PageVO<MenuDTO>  queryMenu(MenuQueryDTO params, Integer pageNo, Integer pageSize) throws BusinessException;

    /**
     * 根据权限编码列表获取菜单
     * @param privileges 权限列表
     * @return
     */
    List<MenuDTO> queryMenuByPrivileges(String [] privileges) throws BusinessException;




}
