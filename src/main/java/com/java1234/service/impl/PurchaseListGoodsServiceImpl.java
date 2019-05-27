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
import com.java1234.dao.PurchaseListDao;
import com.java1234.dao.PurchaseListGoodsDao;
import com.java1234.entity.Goods;
import com.java1234.entity.PurchaseList;
import com.java1234.entity.PurchaseListGoods;
import com.java1234.service.PurchaseListGoodsService;
import com.java1234.util.BigDecimalUtil;
import com.java1234.util.StringUtil;

/**
 * 商品进货入库实现类
 * @author 兰杰
 *
 */
@Service
public class PurchaseListGoodsServiceImpl implements PurchaseListGoodsService{
	
	@Autowired
	private PurchaseListGoodsDao purchaseListGoodsDao;
	
	@Autowired
	private PurchaseListDao purchaseListDao;
	
	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private GoodsTypeDao goodsTypeDao;

	@Override
	@Transactional // 添加事务
	public void save(PurchaseList purchaseList, List<PurchaseListGoods> purchaseListGoodsList) {
		
		// 保存进货单信息
		purchaseListDao.save(purchaseList);
		
		// 保存进货单商品信息
		for(PurchaseListGoods purchaseListGoods : purchaseListGoodsList){
			
			purchaseListGoods.setPurchaseList(purchaseList);
			
			purchaseListGoods.setType(goodsTypeDao.getOne(purchaseListGoods.getGoodsTypeId()));
			
			purchaseListGoodsDao.save(purchaseListGoods);
			
			// 修改商品上一次进货价，进货均价，库存，状态
			Goods goods = goodsDao.getOne(purchaseListGoods.getGoodsId());
			
			goods.setLastPurchasingPrice(purchaseListGoods.getPrice());
			
			goods.setInventoryQuantity(goods.getInventoryQuantity()+purchaseListGoods.getNum());
			
			goods.setPurchasingPrice(BigDecimalUtil.keepTwoDecimalPlaces((goods.getPurchasingPrice()+purchaseListGoods.getPrice())/2));
			
			goods.setState(2);
			
			goodsDao.save(goods);
			
		}
	}

	@Override
	public List<PurchaseList> getPurchaselist(String purchaseNumber, String supplierId, String state, String sTime,
			String eTime) {
		
		return purchaseListDao.findAll(new Specification<PurchaseList>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<PurchaseList> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(purchaseNumber)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("purchaseNumber"), "%"+purchaseNumber+"%"));
					
				}
				
				if(StringUtil.isNotEmpty(supplierId)){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("supplier").get("id"), Integer.parseInt(supplierId)));
					
				}
				
				if(StringUtil.isNotEmpty(state)){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("state"), state));
					
				}
				
				if(StringUtil.isNotEmpty(sTime)){
					
					pd.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("purchaseDate"), sTime));
					
				}
				
				if(StringUtil.isNotEmpty(eTime)){
					
					pd.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("purchaseDate"), eTime));
					
				}
				
				return pd;
				
			}}, new Sort(Direction.DESC, "purchaseDate"));
	}

	@Override
	public List<PurchaseListGoods> getPurchaseListGoodsByPurchaseListId(Integer purchaseListId) {
		
		return purchaseListGoodsDao.getPurchaseListGoodsByPurchaseListId(purchaseListId);
	}

	@Override
	@Transactional
	public void delete(Integer purchaseListId) {
		
		purchaseListDao.deleteById(purchaseListId);
		
		purchaseListGoodsDao.deletePurchaseListGoodsByPurchaseListId(purchaseListId);
		
	}

	@Override
	public PurchaseList getPurchaseList(Integer purchaseListId) {
		
		return purchaseListDao.getOne(purchaseListId);
	}

	@Override
	@Transactional
	public void updateState(Integer state, Integer id) {
		
		purchaseListDao.updateState(state, id);
		
	}

	@Override
	public List<PurchaseListGoods> getPurchaseListGoodsByPurchaseListId(Integer purchaseListId, Integer goodsTypeId,
			String codeOrName) {
		
		return purchaseListGoodsDao.findAll(new Specification<PurchaseListGoods>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<PurchaseListGoods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(purchaseListId!=null){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("purchaseList").get("id"), purchaseListId));
					
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
