package com.java1234.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java1234.dao.GoodsDao;
import com.java1234.entity.Goods;
import com.java1234.service.GoodsService;
import com.java1234.util.StringUtil;

/**
 * 商品实现类
 * @author 兰杰
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService{
	
	@Autowired
	private GoodsDao goodsDao;

	@Override
	public List<Goods> getGoodsByTypeId(Integer typeId) {
		
		return goodsDao.getGoodsByTypeId(typeId);
	}

	@Override
	public List<Goods> getGoodsList(Integer page, Integer pageSize, String name, Integer typeId) {
		
		return goodsDao.findAll(new Specification<Goods>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(name)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
					
				}
				
				if(typeId!=null && typeId!=1){
					
					//查询类别ID为当前ID或父ID为当前类别ID的商品
					pd.getExpressions()
							.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("type").get("id"), typeId),
									criteriaBuilder.equal(root.get("type").get("pId"), typeId)));
					
				}
				
				return pd;
				
			}}, PageRequest.of(page-1, pageSize,Direction.ASC,"id")).getContent();
	}

	@Override
	public Long getGoodsCount(String name, Integer typeId) {
		
		return goodsDao.count(new Specification<Goods>(){

			private static final long serialVersionUID = 1L;
	
			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(name)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
					
				}
				
				if(typeId!=null && typeId!=1){
					
					//查询类别ID为当前ID或父ID为当前类别ID的商品
					pd.getExpressions()
							.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("type").get("id"), typeId),
									criteriaBuilder.equal(root.get("type").get("pId"), typeId)));
					
				}
				
				return pd;	
			}});
	}

	@Override
	public String getMaxCode() {
		
		return goodsDao.getMaxCode();
	}

	@Override
	public void saveGoods(Goods goods) {
		
		goodsDao.save(goods);
		
	}

	@Override
	public void deleteGoods(Integer id) {
		
		goodsDao.deleteById(id);
		
	}

	@Override
	public Goods findByGoodsId(Integer id) {
		
		return goodsDao.getOne(id);
	}

	@Override
	public List<Goods> getNoInventoryQuantity(Integer page, Integer pageSize, String nameOrCode) {
		
		return goodsDao.findAll(new Specification<Goods>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(nameOrCode)){
					
					pd.getExpressions()
							.add(criteriaBuilder.or(criteriaBuilder.like(root.get("name"), "%"+nameOrCode+"%"),
									criteriaBuilder.like(root.get("code"), "%"+nameOrCode+"%")));
					
				}
				
				pd.getExpressions().add(criteriaBuilder.lessThan(root.get("inventoryQuantity"), 1));
				
				return pd;
				
			}}, PageRequest.of(page-1, pageSize,Direction.ASC,"id")).getContent();
	}

	@Override
	public Long getNoInventoryQuantityCount(String nameOrCode) {
		
		return goodsDao.count(new Specification<Goods>(){
			
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(nameOrCode)){
					
					pd.getExpressions()
							.add(criteriaBuilder.or(criteriaBuilder.like(root.get("name"), "%"+nameOrCode+"%"),
									criteriaBuilder.like(root.get("code"), "%"+nameOrCode+"%")));
					
				}
				
				pd.getExpressions().add(criteriaBuilder.lessThan(root.get("inventoryQuantity"), 1));
				
				return pd;
			}});
	}

	@Override
	public List<Goods> getHasInventoryQuantity(Integer page, Integer pageSize, String nameOrCode) {
		
		return goodsDao.findAll(new Specification<Goods>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(nameOrCode)){
					
					pd.getExpressions()
							.add(criteriaBuilder.or(criteriaBuilder.like(root.get("name"), "%"+nameOrCode+"%"),
									criteriaBuilder.like(root.get("code"), "%"+nameOrCode+"%")));
					
				}
				
				pd.getExpressions().add(criteriaBuilder.greaterThan(root.get("inventoryQuantity"), 0));
				
				return pd;
				
			}}, PageRequest.of(page-1, pageSize,Direction.ASC,"id")).getContent();
	}

	@Override
	public Long getHasInventoryQuantityCount(String nameOrCode) {
		
		return goodsDao.count(new Specification<Goods>(){
			
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(nameOrCode)){
					
					pd.getExpressions()
							.add(criteriaBuilder.or(criteriaBuilder.like(root.get("name"), "%"+nameOrCode+"%"),
									criteriaBuilder.like(root.get("code"), "%"+nameOrCode+"%")));
					
				}
				
				pd.getExpressions().add(criteriaBuilder.lessThan(root.get("inventoryQuantity"), 1));
				
				return pd;
			}});
	}

	@Override
	public List<Goods> getGoodsInventoryList(Integer page, Integer pageSize, String codeOrName, Integer typeId) {
		
		return goodsDao.findAll(new Specification<Goods>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(codeOrName)){
					
					pd.getExpressions()
							.add(criteriaBuilder.or(criteriaBuilder.like(root.get("code"), "%" + codeOrName + "%"),
									criteriaBuilder.like(root.get("name"), "%" + codeOrName + "%")));	
				}
				
				if(typeId!=null && typeId!=1){
					
					//查询类别ID为当前ID或父ID为当前类别ID的商品
					pd.getExpressions()
							.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("type").get("id"), typeId),
									criteriaBuilder.equal(root.get("type").get("pId"), typeId)));
					
				}
				
				return pd;
				
			}}, PageRequest.of(page-1, pageSize,Direction.ASC,"id")).getContent();
	}

	@Override
	public Long getGoodsInventoryCount(String codeOrName, Integer typeId) {
		
		return goodsDao.count(new Specification<Goods>(){

			private static final long serialVersionUID = 1L;
	
			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(codeOrName)){
					
					pd.getExpressions()
							.add(criteriaBuilder.or(criteriaBuilder.like(root.get("code"), "%" + codeOrName + "%"),
									criteriaBuilder.like(root.get("name"), "%" + codeOrName + "%")));	
				}
				
				if(typeId!=null && typeId!=1){
					
					if(typeId!=null && typeId!=1){
						
						//查询类别ID为当前ID或父ID为当前类别ID的商品
						pd.getExpressions()
								.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("type").get("id"), typeId),
										criteriaBuilder.equal(root.get("type").get("pId"), typeId)));
						
					}
					
				}
				
				return pd;	
			}});
	}

	@Override
	public List<Goods> getGoodsAlarm() {
		
		return goodsDao.getGoodsAlarm();
	}

}
