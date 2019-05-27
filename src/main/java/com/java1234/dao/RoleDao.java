package com.java1234.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.Role;

/**
 * 角色Dao
 * @author 兰杰 2018.10.19
 *
 */
public interface RoleDao extends JpaRepository<Role, Integer>,JpaSpecificationExecutor<Role>{
	
	/**
	 * 根据用户ID查询用户角色
	 * @param Id 用户ID
	 * @return 用户角色集合
	 */
	@Query(value="SELECT r.* FROM t_role r,t_user u,t_user_role ur WHERE r.id=ur.role_id AND u.id=ur.user_id AND u.id=?1",nativeQuery=true)
	public List<Role> getRoleByUserId(Integer Id);
	
	/**
	 * 根据角色ID查询角色信息
	 * @param id 角色ID
	 * @return
	 */
	@Query(value="SELECT * FROM t_role WHERE id=?1",nativeQuery=true)
	public Role getRoleById(Integer id);
	
	/**
	 * 根据角色名查询角色信息
	 * @param roleName 角色名
	 * @return 角色信息实体
	 */
	@Query(value="select * from t_role where name=?1",nativeQuery=true)
	public Role findRoleByName(String roleName);
}
