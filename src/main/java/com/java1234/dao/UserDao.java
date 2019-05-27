package com.java1234.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.User;

/**
 * 用户信息Dao接口
 * @author 兰杰 2018.10.18
 * @since 1.0
 *
 */
public interface UserDao extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User>{
	
	/**
	 * 根据用户名查询用户信息
	 * @param userName 用户名
	 * @return 用户信息实体
	 */
	@Query(value="select * from t_user where user_name=?1",nativeQuery=true)
	public User findUserByName(String userName);
	
}
