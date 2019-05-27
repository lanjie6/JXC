package com.java1234.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java1234.dao.DamageListDao;
import com.java1234.dao.DamageListGoodsDao;
import com.java1234.dao.GoodsDao;
import com.java1234.dao.GoodsTypeDao;
import com.java1234.entity.DamageList;
import com.java1234.entity.DamageListGoods;
import com.java1234.entity.Goods;
import com.java1234.service.DamageListGoodsService;
import com.java1234.util.StringUtil;

/**
 * 商品报损实现类
 * @author 兰杰
 *
 */
@Service
public class DamageListGoodsServiceImpl implements DamageListGoodsService{
	
	@Autowired
	private DamageListGoodsDao damageListGoodsDao;
	
	@Autowired
	private DamageListDao damageListDao;
	
	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private GoodsTypeDao goodsTypeDao;

	@Override
	@Transactional // 添加事务
	public void save(DamageList damageList, List<DamageListGoods> damageListGoodsList) {
		
		// 保存商品报损单信息
		damageListDao.save(damageList);
		
		// 保存商品报损单商品信息
		for(DamageListGoods damageListGoods : damageListGoodsList){
			
			damageListGoods.setDamageList(damageList);
			
			damageListGoods.setType(goodsTypeDao.getOne(damageListGoods.getGoodsTypeId()));
			
			damageListGoodsDao.save(damageListGoods);
			
			// 修改商品库存，状态
			Goods goods = goodsDao.getOne(damageListGoods.getGoodsId());
			
			goods.setInventoryQuantity(goods.getInventoryQuantity()-damageListGoods.getNum());
			
			goods.setState(2);
			
			goodsDao.save(goods);
			
		}
	}

	@Override
	public List<DamageList> getDamagelist(String sTime,String eTime) {
		
		return damageListDao.findAll(new Specification<DamageList>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<DamageList> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(sTime)){
					
					pd.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("damageDate"), sTime));
					
				}
				
				if(StringUtil.isNotEmpty(eTime)){
					
					pd.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("damageDate"), eTime));
					
				}
				
				return pd;
				
			}}, new Sort(Direction.DESC, "damageDate"));
	}

	@Override
	public List<DamageListGoods> getDamageListGoodsByDamageListId(Integer damageListId) {
		
		return damageListGoodsDao.getDamageListGoodsByDamageListId(damageListId);
	}

	@Override
	public DamageList getDamageList(Integer damageListId) {
		
		return damageListDao.getOne(damageListId);
	}

}
