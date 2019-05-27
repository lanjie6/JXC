package com.java1234.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java1234.dao.UserRoleDao;
import com.java1234.entity.UserRole;
import com.java1234.service.UserRoleService;

/**
 * 用户和角色关联关系Service实现类
 * @author 兰杰 2018.10.24
 *
 */
@Service
@Transactional//打开事务控制
public class UserRoleServiceImpl implements UserRoleService{
	
	@Autowired
	private UserRoleDao userRoleDao;

	@Override
	public void deleteUserRoleByUserId(Integer userId) {
		
		userRoleDao.deleteUserRoleByUserId(userId);
	}

	@Override
	public void addUserRole(UserRole userRole) {
		
		userRoleDao.save(userRole);
	}

	@Override
	public void deleteUserRoleByRoleId(Integer roleId) {
		
		userRoleDao.deleteUserRoleByRoleId(roleId);
	}

}
