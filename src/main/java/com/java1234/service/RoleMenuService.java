package com.java1234.service;

import com.java1234.entity.RoleMenu;

/**
 * 角色和菜单关联关系Service接口
 * @author 兰杰 2018.10.29
 *
 */
public interface RoleMenuService {
	
	/**
	 * 根据根据角色ID删除关联关系
	 * @param roleId 角色ID
	 */
	public void deleteRoleMenuByRoleId(Integer roleId);
	
	/**
	 * 为角色添加菜单权限
	 * @param roleMenu 角色和菜单关联实体
	 */
	public void save(RoleMenu roleMenu);
	
}
