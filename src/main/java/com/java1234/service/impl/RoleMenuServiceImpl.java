package com.java1234.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java1234.dao.RoleMenuDao;
import com.java1234.entity.RoleMenu;
import com.java1234.service.RoleMenuService;

@Service
@Transactional
public class RoleMenuServiceImpl implements RoleMenuService{
	
	@Autowired
	private RoleMenuDao roleMenuDao;

	@Override
	public void deleteRoleMenuByRoleId(Integer roleId) {
		
		roleMenuDao.deleteRoleMenuByRoleId(roleId);
		
	}

	@Override
	public void save(RoleMenu roleMenu) {
		
		roleMenuDao.save(roleMenu);
	}

}
