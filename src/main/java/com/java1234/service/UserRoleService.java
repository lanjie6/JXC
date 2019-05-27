package com.java1234.service;

import com.java1234.entity.UserRole;

/**
 * 用户和角色关联关系Service接口
 * @author 兰杰 2018.10.24
 *
 */
public interface UserRoleService {
	
	/**
	 * 根据用户ID删除关联关系
	 * @param userId 用户ID
	 */
	public void deleteUserRoleByUserId(Integer userId);
	
	/**
	 * 用户添加角色
	 * @param userRole 用户和角色关联实体
	 */
	public void addUserRole(UserRole userRole);
	
	/**
	 * 根据角色ID删除关联关系
	 * @param roleId 角色ID
	 */
	public void deleteUserRoleByRoleId(Integer roleId);
	
}
