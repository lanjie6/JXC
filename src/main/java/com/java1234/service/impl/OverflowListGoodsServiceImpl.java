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

import com.java1234.dao.OverflowListDao;
import com.java1234.dao.OverflowListGoodsDao;
import com.java1234.dao.GoodsDao;
import com.java1234.dao.GoodsTypeDao;
import com.java1234.entity.OverflowList;
import com.java1234.entity.OverflowListGoods;
import com.java1234.entity.Goods;
import com.java1234.service.OverflowListGoodsService;
import com.java1234.util.StringUtil;

/**
 * 商品报溢实现类
 * @author 兰杰
 *
 */
@Service
public class OverflowListGoodsServiceImpl implements OverflowListGoodsService{
	
	@Autowired
	private OverflowListGoodsDao overflowListGoodsDao;
	
	@Autowired
	private OverflowListDao overflowListDao;
	
	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private GoodsTypeDao goodsTypeDao;

	@Override
	@Transactional // 添加事务
	public void save(OverflowList overflowList, List<OverflowListGoods> overflowListGoodsList) {
		
		// 保存商品报溢单信息
		overflowListDao.save(overflowList);
		
		// 保存商品报溢单商品信息
		for(OverflowListGoods overflowListGoods : overflowListGoodsList){
			
			overflowListGoods.setOverflowList(overflowList);
			
			overflowListGoods.setType(goodsTypeDao.getOne(overflowListGoods.getGoodsTypeId()));
			
			overflowListGoodsDao.save(overflowListGoods);
			
			// 修改商品库存，状态
			Goods goods = goodsDao.getOne(overflowListGoods.getGoodsId());
			
			goods.setInventoryQuantity(goods.getInventoryQuantity()+overflowListGoods.getNum());
			
			goods.setState(2);
			
			goodsDao.save(goods);
			
		}
	}

	@Override
	public List<OverflowList> getOverflowlist(String sTime,String eTime) {
		
		return overflowListDao.findAll(new Specification<OverflowList>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<OverflowList> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(sTime)){
					
					pd.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("overflowDate"), sTime));
					
				}
				
				if(StringUtil.isNotEmpty(eTime)){
					
					pd.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("overflowDate"), eTime));
					
				}
				
				return pd;
				
			}}, new Sort(Direction.DESC, "overflowDate"));
	}

	@Override
	public List<OverflowListGoods> getOverflowListGoodsByOverflowListId(Integer overflowListId) {
		
		return overflowListGoodsDao.getOverflowListGoodsByOverflowListId(overflowListId);
	}

	@Override
	public OverflowList getOverflowList(Integer overflowListId) {
		
		return overflowListDao.getOne(overflowListId);
	}

}
