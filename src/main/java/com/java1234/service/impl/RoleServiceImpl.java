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

import com.java1234.dao.RoleDao;
import com.java1234.entity.Role;
import com.java1234.service.RoleService;
import com.java1234.util.StringUtil;

/**
 * 角色业务层实现类
 * @author 兰杰 2018.10.19
 *
 */
@Service
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	private RoleDao roleDao;

	@Override
	public List<Role> getRoleByUserId(Integer Id) {
		
		return roleDao.getRoleByUserId(Id);
	}

	@Override
	public Role getRoleById(Integer id) {
		
		return roleDao.getRoleById(id);
	}

	@Override
	public List<Role> findAll() {
		
		return roleDao.findAll();
	}

	@Override
	public List<Role> getRoleList(Integer page, Integer pageSize, String roleName) {
		
		return roleDao.findAll(new Specification<Role>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(roleName)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("name"), "%"+roleName+"%"));
					
				}
				
				return pd;
				
			}}, PageRequest.of(page-1, pageSize,Direction.ASC,"id")).getContent();
	}

	@Override
	public Long getRoleCount(String roleName) {
		
			return roleDao.count(new Specification<Role>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(roleName)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("name"), "%"+roleName+"%"));
					
				}
				
				return pd;	
			}});
	}

	@Override
	public void saveRole(Role role) {
		
		roleDao.save(role);
		
	}

	@Override
	public void deleteRole(Integer id) {
		
		roleDao.deleteById(id);
		
	}

	@Override
	public Role findRoleByName(String roleName) {
		
		return roleDao.findRoleByName(roleName);
	}
	
}
