package com.java1234.service.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java1234.dao.LogDao;
import com.java1234.dao.UserDao;
import com.java1234.entity.Log;
import com.java1234.entity.User;
import com.java1234.service.LogService;
import com.java1234.util.StringUtil;

/**
 * 日志Service实现类
 * @author 兰杰 2018.11.1
 * @since 1.0
 *
 */
@Service
public class LogServiceImpl implements LogService{
	
	@Autowired
	private LogDao logDao;
	
	@Autowired
	private UserDao userDao;

	@Override
	public void save(Log log) {
		
		log.setDate(new Date());
		
		User user = userDao.findUserByName((String)SecurityUtils.getSubject().getPrincipal());
		
		log.setUser(user);
		
		logDao.save(log);
		
	}

	@Override
	public List<Log> getLogList(String type, String trueName, Date sTime, Date eTime, Integer page, Integer rows) {
		
		return logDao.findAll(new Specification<Log>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(type)){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("type"), type));
					
				}
				
				if(StringUtil.isNotEmpty(trueName)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("user").get("trueName"), "%"+trueName+"%"));
					
				}
				
				if(sTime!=null){
					
					pd.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), sTime));
					
				}
				
				if(eTime!=null){
					
					pd.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), eTime));
					
				}
				
				return pd;
				
			}}, PageRequest.of(page-1, rows, Direction.DESC, "date")).getContent();
	}

	@Override
	public Long getLogCount(String type, String trueName, Date sTime, Date eTime) {
		
		return logDao.count(new Specification<Log>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate pd = criteriaBuilder.conjunction();
				
				if(StringUtil.isNotEmpty(type)){
					
					pd.getExpressions().add(criteriaBuilder.equal(root.get("type"), type));
					
				}
				
				if(StringUtil.isNotEmpty(trueName)){
					
					pd.getExpressions().add(criteriaBuilder.like(root.get("user").get("trueName"), "%"+trueName+"%"));
					
				}
				
				if(sTime!=null){
					
					pd.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), sTime));
					
				}
				
				if(eTime!=null){
					
					pd.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), eTime));
					
				}
				
				return pd;
				
			}});
	}

}
