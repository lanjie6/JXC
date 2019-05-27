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

import com.java1234.dao.CustomerDao;
import com.java1234.entity.Customer;
import com.java1234.service.CustomerService;
import com.java1234.util.StringUtil;

/**
 * 客户管理业务层实现类
 * @author 兰杰 2018.11.6
 *
 */
@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	private CustomerDao customerDao;

	@Override
	public Customer getCustomerById(Integer customerId) {
		
		return customerDao.getOne(customerId);
	}

	@Override
	public List<Customer> getCustomerList(Integer page, Integer pageSize, String customerName) {
		
		return customerDao.findAll(new Specification<Customer>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(customerName)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("name"), "%"+customerName+"%"));
					
				}
				
				return pd;
				
			}}, PageRequest.of(page-1, pageSize,Direction.ASC,"id")).getContent();
	}

	@Override
	public Long getCustomerCount(String customerName) {
		
		return customerDao.count(new Specification<Customer>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(customerName)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("name"), "%"+customerName+"%"));
					
				}
				
				return pd;	
			}});
	}

	@Override
	public void saveCustomer(Customer customer) {
		
		customerDao.save(customer);
		
	}

	@Override
	public void deleteCustomer(Integer id) {
		
		customerDao.deleteById(id);
	}

	@Override
	public List<Customer> getComboboxList(String name) {
		
		return customerDao.findAll(new Specification<Customer>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(name)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
					
				}
				
				return pd;	

			}}, Sort.by(Direction.ASC,"id"));
	}
	
	

}
