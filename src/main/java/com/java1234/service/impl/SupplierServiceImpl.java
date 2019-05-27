package com.java1234.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java1234.dao.SupplierDao;
import com.java1234.entity.Supplier;
import com.java1234.service.SupplierService;
import com.java1234.util.StringUtil;

/**
 * 供应商业务层实现类
 * @author 兰杰 2018.11.6
 *
 */
@Service
public class SupplierServiceImpl implements SupplierService{
	
	@Autowired
	private SupplierDao supplierDao;

	@Override
	public Supplier getSupplierById(Integer supplierId) {
		
		return supplierDao.getOne(supplierId);
	}

	@Override
	public List<Supplier> getSupplierList(Integer page, Integer pageSize, String supplierName) {
		
		return supplierDao.findAll(new Specification<Supplier>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(supplierName)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("name"), "%"+supplierName+"%"));
					
				}
				
				return pd;
				
			}}, PageRequest.of(page-1, pageSize,Direction.ASC,"id")).getContent();
	}

	@Override
	public Long getSupplierCount(String supplierName) {
		
		return supplierDao.count(new Specification<Supplier>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(supplierName)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("name"), "%"+supplierName+"%"));
					
				}
				
				return pd;	
			}});
	}

	@Override
	public void saveSupplier(Supplier supplier) {
		
		supplierDao.save(supplier);
		
	}

	@Override
	public void deleteSupplier(Integer id) {
		
		supplierDao.deleteById(id);
	}

	@Override
	public List<Supplier> getComboboxList(String name) {
		
		return supplierDao.findAll(new Specification<Supplier>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(name)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
					
				}
				
				return pd;	

			}}, Sort.by(Direction.ASC,"id"));
	}

}
