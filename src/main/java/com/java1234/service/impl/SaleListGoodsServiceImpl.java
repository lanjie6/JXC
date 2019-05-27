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

import com.java1234.dao.GoodsDao;
import com.java1234.dao.GoodsTypeDao;
import com.java1234.dao.SaleListDao;
import com.java1234.dao.SaleListGoodsDao;
import com.java1234.entity.Goods;
import com.java1234.entity.SaleList;
import com.java1234.entity.SaleListGoods;
import com.java1234.service.SaleListGoodsService;
import com.java1234.util.StringUtil;

/**
 * 商品销售实现类
 * @author 兰杰
 *
 */
@Service
public class SaleListGoodsServiceImpl implements SaleListGoodsService{
	
	@Autowired
	private SaleListGoodsDao saleListGoodsDao;
	
	@Autowired
	private SaleListDao saleListDao;
	
	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private GoodsTypeDao goodsTypeDao;

	@Override
	@Transactional // 添加事务
	public void save(SaleList saleList, List<SaleListGoods> saleListGoodsList) {
		
		// 保存销售单信息
		saleListDao.save(saleList);
		
		// 保存销售单商品信息
		for(SaleListGoods saleListGoods : saleListGoodsList){
			
			saleListGoods.setSaleList(saleList);
			
			saleListGoods.setType(goodsTypeDao.getOne(saleListGoods.getGoodsTypeId()));
			
			saleListGoodsDao.save(saleListGoods);
			
			// 修改商品库存，状态
			Goods goods = goodsDao.getOne(saleListGoods.getGoodsId());
			
			goods.setInventoryQuantity(goods.getInventoryQuantity()-saleListGoods.getNum());
			
			goods.setState(2);
			
			goodsDao.save(goods);
			
		}
	}

	@Override
	public List<SaleList> getSalelist(String saleNumber, String customerId, String state, String sTime,
			String eTime) {
		
		return saleListDao.findAll(new Specification<SaleList>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<SaleList> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(saleNumber)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("saleNumber"), "%"+saleNumber+"%"));
					
				}
				
				if(StringUtil.isNotEmpty(customerId)){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("customer").get("id"), Integer.parseInt(customerId)));
					
				}
				
				if(StringUtil.isNotEmpty(state)){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("state"), state));
					
				}
				
				if(StringUtil.isNotEmpty(sTime)){
					
					pd.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("saleDate"), sTime));
					
				}
				
				if(StringUtil.isNotEmpty(eTime)){
					
					pd.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("saleDate"), eTime));
					
				}
				
				return pd;
				
			}}, new Sort(Direction.DESC, "saleDate"));
	}

	@Override
	public List<SaleListGoods> getSaleListGoodsBySaleListId(Integer saleListId) {
		
		return saleListGoodsDao.getSaleListGoodsBySaleListId(saleListId);
	}

	@Override
	@Transactional
	public void delete(Integer saleListId) {
		
		saleListDao.deleteById(saleListId);
		
		saleListGoodsDao.deleteSaleListGoodsBySaleListId(saleListId);
		
	}

	@Override
	public SaleList getSaleList(Integer saleListId) {
		
		return saleListDao.getOne(saleListId);
	}

	@Override
	public Integer getSaleTotalByGoodsId(Integer goodsId) {
		
		Integer n = saleListGoodsDao.getSaleTotalByGoodsId(goodsId);
		
		return n==null ? 0 : n;
	}

	@Override
	@Transactional
	public void updateState(Integer state, Integer id) {
		
		saleListDao.updateState(state, id);
		
	}

	@Override
	public List<SaleListGoods> getSaleListGoodsBySaleListId(Integer saleListId, Integer goodsTypeId,
			String codeOrName) {
		
		return saleListGoodsDao.findAll(new Specification<SaleListGoods>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<SaleListGoods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(saleListId!=null){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("saleList").get("id"), saleListId));
					
				}
				
				if(goodsTypeId!=null){
					
					//查询类别ID为当前ID或父ID为当前类别ID的商品
					pd.getExpressions()
							.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("type").get("id"), goodsTypeId),
									criteriaBuilder.equal(root.get("type").get("pId"), goodsTypeId)));
					
				}
				
				if(StringUtil.isNotEmpty(codeOrName)){
					
					pd.getExpressions()
							.add(criteriaBuilder.or(criteriaBuilder.like(root.get("code"), "%" + codeOrName + "%"),
									criteriaBuilder.like(root.get("name"), "%" + codeOrName + "%")));
					
				}
				
				return pd;
				
			}});
	}

	@Override
	public List<Object> getSaleDataByDay(String sTime, String eTime) {
		
		return saleListDao.getSaleDataByDay(sTime, eTime);
	}

	@Override
	public List<Object> getSaleDataByMonth(String sTime, String eTime) {
		
		return saleListDao.getSaleDataByMonth(sTime, eTime);
	}

}
