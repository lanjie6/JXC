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
import com.java1234.dao.ReturnListDao;
import com.java1234.dao.ReturnListGoodsDao;
import com.java1234.entity.Goods;
import com.java1234.entity.ReturnList;
import com.java1234.entity.ReturnListGoods;
import com.java1234.service.ReturnListGoodsService;
import com.java1234.util.StringUtil;

/**
 * 商品退货出库实现类
 * @author 兰杰
 *
 */
@Service
public class ReturnListGoodsServiceImpl implements ReturnListGoodsService{
	
	@Autowired
	private ReturnListGoodsDao returnListGoodsDao;
	
	@Autowired
	private ReturnListDao returnListDao;
	
	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private GoodsTypeDao goodsTypeDao;

	@Override
	@Transactional // 添加事务
	public void save(ReturnList returnList, List<ReturnListGoods> returnListGoodsList) {
		
		// 保存退货单信息
		returnListDao.save(returnList);
		
		// 保存退货单商品信息
		for(ReturnListGoods returnListGoods : returnListGoodsList){
			
			returnListGoods.setReturnList(returnList);
			
			returnListGoods.setType(goodsTypeDao.getOne(returnListGoods.getGoodsTypeId()));
			
			returnListGoodsDao.save(returnListGoods);
			
			// 修改商品库存，状态
			Goods goods = goodsDao.getOne(returnListGoods.getGoodsId());
			
			goods.setInventoryQuantity(goods.getInventoryQuantity()-returnListGoods.getNum());
			
			goods.setState(2);
			
			goodsDao.save(goods);
			
		}
	}
	
	@Override
	public List<ReturnList> getReturnlist(String returnNumber, String supplierId, String state, String sTime,
			String eTime) {
		
		return returnListDao.findAll(new Specification<ReturnList>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ReturnList> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(returnNumber)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("returnNumber"), "%"+returnNumber+"%"));
					
				}
				
				if(StringUtil.isNotEmpty(supplierId)){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("supplier").get("id"), Integer.parseInt(supplierId)));
					
				}
				
				if(StringUtil.isNotEmpty(state)){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("state"), state));
					
				}
				
				if(StringUtil.isNotEmpty(sTime)){
					
					pd.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("returnDate"), sTime));
					
				}
				
				if(StringUtil.isNotEmpty(eTime)){
					
					pd.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("returnDate"), eTime));
					
				}
				
				return pd;
				
			}}, new Sort(Direction.DESC, "returnDate"));
	}

	@Override
	public List<ReturnListGoods> getReturnListGoodsByReturnListId(Integer returnListId) {
		
		return returnListGoodsDao.getReturnListGoodsByReturnListId(returnListId);
	}

	@Override
	@Transactional
	public void delete(Integer returnListId) {
		
		returnListDao.deleteById(returnListId);
		
		returnListGoodsDao.deleteReturnListGoodsByReturnListId(returnListId);
		
	}

	@Override
	public ReturnList getReturnList(Integer returnListId) {
		
		return returnListDao.getOne(returnListId);
	}

	@Override
	@Transactional
	public void updateState(Integer state, Integer id) {
		
		returnListDao.updateState(state, id);
		
	}

	@Override
	public List<ReturnListGoods> getReturnListGoodsByReturnListId(Integer returnListId, Integer goodsTypeId,
			String codeOrName) {
		
		return returnListGoodsDao.findAll(new Specification<ReturnListGoods>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ReturnListGoods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(returnListId!=null){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("returnList").get("id"), returnListId));
					
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

}
