package com.java1234.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.UserRole;

/**
 * 用户和角色关联Dao接口
 * @author 兰杰 2018.10.24
 *
 */
public interface UserRoleDao extends JpaRepository<UserRole, Integer>,JpaSpecificationExecutor<UserRole>{
	
	/**
	 * 根据用户ID删除关联关系
	 * @param userId 用户ID
	 */
	@Query(value="DELETE FROM t_user_role WHERE user_id=?1",nativeQuery=true)
	@Modifying//如果需要删除或修改数据，需要添加@Modifying注解来配合使用，否则Jpa会报错
	public void deleteUserRoleByUserId(Integer userId);
	
	/**
	 * 根据角色ID删除关联关系
	 * @param roleId 角色ID
	 */
	@Query(value="DELETE FROM t_user_role WHERE role_id=?1",nativeQuery=true)
	@Modifying
	public void deleteUserRoleByRoleId(Integer roleId);

}
