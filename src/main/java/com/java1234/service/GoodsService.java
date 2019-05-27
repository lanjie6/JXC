package com.java1234.service;

import java.util.List;

import com.java1234.entity.Goods;

/**
 * 商品Service接口
 * @author 兰杰
 *
 */
public interface GoodsService {
	
	/**
	 * 根据商品类别ID查询商品
	 * @param type 商品类别ID
	 * @return
	 */
	public List<Goods> getGoodsByTypeId(Integer typeId);
	
	/**
	 * 分页查询商品信息
	 * @param page 当前页
	 * @param pageSize 每页显示条数
	 * @param name 商品名称
	 * @param typeId 商品类别ID
	 * @return
	 */
	public List<Goods> getGoodsList(Integer page,Integer pageSize,String name,Integer typeId);
	
	/**
	 * 查询商品记录数
	 * @param name 商品名称
	 * @param typeId 商品类别ID
	 * @return
	 */
	public Long getGoodsCount(String name,Integer typeId);
	
	/**
	 * 查询当前商品最大的编码号
	 * @return
	 */
	public String getMaxCode();
	
	/**
	 * 新增或修改商品信息
	 * @param goods 商品实体
	 */
	public void saveGoods(Goods goods);
	
	/**
	 * 删除商品信息
	 * @param id 商品ID
	 */
	public void deleteGoods(Integer id);
	
	/**
	 * 根据商品ID查询商品
	 * @param id 商品ID
	 * @return 商品实体
	 */
	public Goods findByGoodsId(Integer id);
	
	/**
	 * 分页查询没有库存的商品信息
	 * @param page 当前页
	 * @param pageSize 每页记录数
	 * @param nameOrCode 搜索条件，商品名称或商品编码
	 * @return 商品集合
	 */
	public List<Goods> getNoInventoryQuantity(Integer page,Integer pageSize,String nameOrCode);
	
	/**
	 * 查询没有库存的商品数量
	 * @param nameOrCode  搜索条件，商品名称或商品编码
	 * @return 商品数量
	 */
	public Long getNoInventoryQuantityCount(String nameOrCode);
	
	/**
	 * 分页查询有库存的商品信息
	 * @param page 当前页
	 * @param pageSize 每页记录数
	 * @param nameOrCode 搜索条件，商品名称或商品编码
	 * @return
	 */
	public List<Goods> getHasInventoryQuantity(Integer page,Integer pageSize,String nameOrCode);
	
	/**
	 * 查询有库存的商品数量
	 * @param nameOrCode  搜索条件，商品名称或商品编码
	 * @return 商品数量
	 */
	public Long getHasInventoryQuantityCount(String nameOrCode);
	
	/**
	 * 查询商品库存信息记录数
	 * @param codeOrName 商品编码或名称
	 * @param typeId 商品类别ID
	 * @return
	 */
	public Long getGoodsInventoryCount(String codeOrName, Integer typeId);
	
	/**
	 * 查询商品库存信息
	 * @param page 当前页数
	 * @param pageSize 每页显示条数
	 * @param codeOrName 商品编码或商品名称
	 * @param typeId 商品类别ID
	 * @return
	 */
	public List<Goods> getGoodsInventoryList(Integer page, Integer pageSize, String codeOrName, Integer typeId);
	
	/**
	 * 查询当前库存小于最小库存的商品
	 * @return
	 */
	public List<Goods> getGoodsAlarm();

}
