package com.java1234.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.DamageListGoods;

/**
 * 商品报损商品Dao
 * @author 兰杰
 *
 */
public interface DamageListGoodsDao extends JpaRepository<DamageListGoods, Integer>,JpaSpecificationExecutor<DamageListGoods>{
	
	@Query(value="SELECT * FROM t_damage_list_goods WHERE damage_list_id=?1",nativeQuery=true)
	public List<DamageListGoods> getDamageListGoodsByDamageListId(Integer damageListId);
}
