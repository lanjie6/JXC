package com.java1234.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java1234.dao.MenuDao;
import com.java1234.entity.Menu;
import com.java1234.service.MenuService;

/**
 * 菜单实现类
 * @author 兰杰 2018.10.23
 *
 */
@Service
public class MenuServiceImpl implements MenuService{
	
	@Autowired
	private MenuDao menuDao;

	@Override
	public List<Menu> getMenuByParentId(Integer parentId, Integer roleId) {
		
		return menuDao.getMenuByParentId(parentId, roleId);
	}

	@Override
	public List<Menu> getMenuByRoleId(Integer roleId) {
		
		return menuDao.getMenuByRoleId(roleId);
	}

	@Override
	public List<Menu> getAllMenuByParentId(Integer parentId) {
		
		return menuDao.getAllMenuByParentId(parentId);
	}

	@Override
	public Menu getMenuById(Integer menuId) {
		
		return menuDao.getOne(menuId);
	}
	
	

}
