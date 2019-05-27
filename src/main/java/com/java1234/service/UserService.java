package com.java1234.service;

import java.util.List;

import com.java1234.entity.User;

public interface UserService {
	
	/**
	 * 根据用户ID查询用户信息
	 * @param userId 用户ID
	 * @return 用户信息实体
	 */
	public User getUserById(Integer userId);
	
	/**
	 * 根据用户名查询用户信息
	 * @param userName 用户名
	 * @return 用户信息实体
	 */
	public User findUserByName(String userName);
	
	/**
	 * 分页查询用户信息
	 * @param page 当前页数
	 * @param pageSiez 每页显示条数
	 * @param userName 用户名模糊查询条件
	 * @return 用户信息集合
	 */
	public List<User> getUserList(Integer page,Integer pageSize,String userName);
	
	/**
	 * 查询用户数量
	 * @param userName 用户名模糊查询条件
	 * @return 数量
	 */
	public Long getUserCount(String userName);
	
	/**
	 * 保存或修改用户信息
	 * @param user 用户实体
	 */
	public void saveUser(User user);
	
	/**
	 * 删除用户信息
	 * @param userId
	 */
	public void deleteUser(Integer id);
}
