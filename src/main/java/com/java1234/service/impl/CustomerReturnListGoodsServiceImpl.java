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

import com.java1234.dao.CustomerReturnListDao;
import com.java1234.dao.CustomerReturnListGoodsDao;
import com.java1234.dao.GoodsDao;
import com.java1234.dao.GoodsTypeDao;
import com.java1234.entity.CustomerReturnList;
import com.java1234.entity.CustomerReturnListGoods;
import com.java1234.entity.Goods;
import com.java1234.service.CustomerReturnListGoodsService;
import com.java1234.util.StringUtil;

/**
 * 客户退货实现类
 * @author 兰杰
 *
 */
@Service
public class CustomerReturnListGoodsServiceImpl implements CustomerReturnListGoodsService{
	
	@Autowired
	private CustomerReturnListGoodsDao customerReturnListGoodsDao;
	
	@Autowired
	private CustomerReturnListDao customerReturnListDao;
	
	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private GoodsTypeDao goodsTypeDao;

	@Override
	@Transactional // 添加事务
	public void save(CustomerReturnList customerReturnList, List<CustomerReturnListGoods> customerReturnListGoodsList) {
		
		// 保存销售单信息
		customerReturnListDao.save(customerReturnList);
		
		// 保存销售单商品信息
		for(CustomerReturnListGoods customerReturnListGoods : customerReturnListGoodsList){
			
			customerReturnListGoods.setCustomerReturnList(customerReturnList);
			
			customerReturnListGoods.setType(goodsTypeDao.getOne(customerReturnListGoods.getGoodsTypeId()));
			
			customerReturnListGoodsDao.save(customerReturnListGoods);
			
			// 修改商品库存，状态
			Goods goods = goodsDao.getOne(customerReturnListGoods.getGoodsId());
			
			goods.setInventoryQuantity(goods.getInventoryQuantity()+customerReturnListGoods.getNum());
			
			goods.setState(2);
			
			goodsDao.save(goods);
			
		}
	}

	@Override
	public List<CustomerReturnList> getCustomerReturnlist(String customerReturnNumber, String customerId, String state, String sTime,
			String eTime) {
		
		return customerReturnListDao.findAll(new Specification<CustomerReturnList>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<CustomerReturnList> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(customerReturnNumber)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("customerReturnNumber"), "%"+customerReturnNumber+"%"));
					
				}
				
				if(StringUtil.isNotEmpty(customerId)){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("customer").get("id"), Integer.parseInt(customerId)));
					
				}
				
				if(StringUtil.isNotEmpty(state)){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("state"), state));
					
				}
				
				if(StringUtil.isNotEmpty(sTime)){
					
					pd.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("customerReturnDate"), sTime));
					
				}
				
				if(StringUtil.isNotEmpty(eTime)){
					
					pd.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("customerReturnDate"), eTime));
					
				}
				
				return pd;
				
			}}, new Sort(Direction.DESC, "customerReturnDate"));
	}

	@Override
	public List<CustomerReturnListGoods> getCustomerReturnListGoodsByCustomerReturnListId(Integer customerReturnListId) {
		
		return customerReturnListGoodsDao.getCustomerReturnListGoodsByCustomerReturnListId(customerReturnListId);
	}

	@Override
	@Transactional
	public void delete(Integer customerReturnListId) {
		
		customerReturnListDao.deleteById(customerReturnListId);
		
		customerReturnListGoodsDao.deleteCustomerReturnListGoodsByCustomerReturnListId(customerReturnListId);
		
	}

	@Override
	public CustomerReturnList getCustomerReturnList(Integer customerReturnListId) {
		
		return customerReturnListDao.getOne(customerReturnListId);
	}

	@Override
	public Integer getCustomerReturnTotalByGoodsId(Integer goodsId) {
		
		Integer n = customerReturnListGoodsDao.getCustomerReturnTotalByGoodsId(goodsId);
		
		return n==null ? 0 : n;
	}

	@Override
	@Transactional
	public void updateState(Integer state, Integer id) {
		
		customerReturnListDao.updateState(state, id);
		
	}

	@Override
	public List<CustomerReturnListGoods> getCustomerReturnListGoodsByCustomerReturnListId(Integer customerReturnListId,
			Integer goodsTypeId, String codeOrName) {
		
		return customerReturnListGoodsDao.findAll(new Specification<CustomerReturnListGoods>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<CustomerReturnListGoods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(customerReturnListId!=null){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("customerReturnList").get("id"), customerReturnListId));
					
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
