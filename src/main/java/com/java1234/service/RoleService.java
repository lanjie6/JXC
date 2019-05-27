package com.java1234.service;

import java.util.List;

import com.java1234.entity.Role;

/**
 * 角色Service接口
 * @author 兰杰 2018.10.19
 * @since 1.0
 *
 */
public interface RoleService {
	
	/**
	 * 根据用户ID查询用户角色
	 * @param Id 用户ID
	 * @return 用户角色集合
	 */
	public List<Role> getRoleByUserId(Integer Id);
	
	/**
	 * 根据角色ID查询角色信息
	 * @param id 角色ID
	 * @return 角色信息
	 */
	public Role getRoleById(Integer id);
	
	/**
	 * 查询所有角色信息
	 * @return
	 */
	public List<Role> findAll();
	
	/**
	 * 分页查询角色信息
	 * @param page 当前页数
	 * @param pageSiez 每页显示条数
	 * @param roleName 角色名模糊查询条件
	 * @return 用户信息集合
	 */
	public List<Role> getRoleList(Integer page,Integer pageSize,String roleName);
	
	/**
	 * 查询用户数量
	 * @param roleName 角色名模糊查询条件
	 * @return 数量
	 */
	public Long getRoleCount(String roleName);
	
	/**
	 * 保存或修改角色信息
	 * @param role 角色实体
	 */
	public void saveRole(Role role);
	
	/**
	 * 删除角色信息
	 * @param roleId 角色ID
	 */
	public void deleteRole(Integer id);
	
	/**
	 * 根据角色名查询角色信息
	 * @param roleName 角色名
	 * @return 角色实体
	 */
	public Role findRoleByName(String roleName);
}
