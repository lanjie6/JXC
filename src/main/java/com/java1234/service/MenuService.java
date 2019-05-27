package com.java1234.service;

import java.util.List;

import com.java1234.entity.Menu;

/**
 * 菜单Service接口
 * @author 兰杰 2018.10.23
 *
 */
public interface MenuService {
	
	/**
	 * 根据角色ID查询菜单信息
	 * @param roleId 角色ID
	 * @return 菜单集合
	 */
	public List<Menu> getMenuByRoleId(Integer roleId);
	
	/**
	 * 根据菜单父ID获取当前角色下的菜单信息
	 * @param parentId 菜单父ID
	 * @param roleId 角色ID
	 * @return 菜单信息集合
	 */
	public List<Menu> getMenuByParentId(Integer parentId,Integer roleId);
	
	/**
	 * 根据父菜单ID查询所有子菜单
	 * @param parentId 菜单父ID
	 * @return
	 */
	public List<Menu> getAllMenuByParentId(Integer parentId);
	
	/**
	 * 根据菜单ID查询菜单信息
	 * @param menuId 菜单ID
	 * @return 菜单信息实体
	 */
	public Menu getMenuById(Integer menuId);

}
