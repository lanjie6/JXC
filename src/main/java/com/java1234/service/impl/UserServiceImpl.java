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
import org.springframework.transaction.annotation.Transactional;

import com.java1234.dao.UserDao;
import com.java1234.entity.User;
import com.java1234.service.UserService;
import com.java1234.util.StringUtil;

/**
 * 用户业务层实现类
 * @author 兰杰 2018.10.19
 *
 */
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;

	@Override
	public User findUserByName(String userName) {
		
		return userDao.findUserByName(userName);
	}

	@Override
	public List<User> getUserList(Integer page, Integer pageSize, String userName) {
		
		return userDao.findAll(new Specification<User>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(userName)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("userName"), "%"+userName+"%"));
					
				}
				
				//排除系统管理员
				pd.getExpressions().add(criteriaBuilder.notEqual(root.get("userName"), "admin"));
				
				return pd;
				
			}}, PageRequest.of(page-1, pageSize,Direction.ASC,"id")).getContent();
		
	}

	@Override
	public Long getUserCount(String userName) {
		
		return userDao.count(new Specification<User>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(userName)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("userName"), "%"+userName+"%"));
					
				}
				
				//排除系统管理员
				pd.getExpressions().add(criteriaBuilder.notEqual(root.get("userName"), "admin"));
				
				return pd;	
			}});
	}

	@Override
	public void saveUser(User user) {
		
		userDao.save(user);
		
	}

	@Override
	@Transactional
	public void deleteUser(Integer id) {
		
		userDao.deleteById(id);
		
	}

	@Override
	public User getUserById(Integer userId) {
		
		return userDao.getOne(userId);
	}

}
