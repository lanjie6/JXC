package com.java1234.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java1234.dao.GoodsTypeDao;
import com.java1234.entity.GoodsType;
import com.java1234.service.GoodsTypeService;

/**
 * 商品类别实现类
 * @author 兰杰
 *
 */
@Service
public class GoodsTypeServiceImpl implements GoodsTypeService{
	
	@Autowired
	private GoodsTypeDao goodsTypeDao;

	@Override
	public List<GoodsType> getAllGoodsTypeByParentId(Integer parentId) {
		
		return goodsTypeDao.getAllGoodsTypeByParentId(parentId);
	}

	@Override
	public void saveGoodsType(GoodsType goodsType) {
		
		goodsTypeDao.save(goodsType);
		
	}

	@Override
	public GoodsType getGoodsTypeById(Integer id) {
		
		return goodsTypeDao.getOne(id);
	}

	@Override
	public void delete(Integer id) {
		
		goodsTypeDao.deleteById(id);
		
	}

}
