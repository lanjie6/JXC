package com.java1234.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java1234.dao.UnitDao;
import com.java1234.entity.Unit;
import com.java1234.service.UnitService;

/**
 * 商品单位实现类
 * @author 兰杰
 *
 */
@Service
public class UnitServiceImpl implements UnitService{
	
	@Autowired
	private UnitDao unitDao;

	@Override
	public void saveUnit(Unit unit) {
		
		unitDao.save(unit);
	}

	@Override
	public void delete(Integer id) {
		
		unitDao.deleteById(id);
		
	}

	@Override
	public List<Unit> listAll() {
		
		return unitDao.findAll();
	}

	@Override
	public Unit getOne(Integer id) {
		
		return unitDao.getOne(id);
	}

}
