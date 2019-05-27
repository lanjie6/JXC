package com.java1234.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.java1234.entity.Goods;
import com.java1234.entity.GoodsType;
import com.java1234.entity.Log;
import com.java1234.resultConfig.ResultCode;
import com.java1234.service.GoodsService;
import com.java1234.service.GoodsTypeService;
import com.java1234.service.LogService;

/**
 * 商品类别控制器
 * @author 兰杰
 *
 */
@RestController
@RequestMapping("/goodsType")
public class GoodsTypeController {
	
	@Autowired
	private GoodsTypeService goodsTypeService;
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private LogService logService;
	
	/**
	 * 删除商品类别
	 * @param id 商品类别ID
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value={"商品管理","进货入库","退货出库","销售出库","客户退货","商品报损","商品报溢"},logical=Logical.OR)
	public Map<String,Object> delete(Integer id){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		//根据商品类别ID来查询商品信息，如果该类别下有商品信息，则不给予删除
		List<Goods> goodsList = goodsService.getGoodsByTypeId(id);
		
		if(goodsList.size()!=0){
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "该商品类别下有商品，无法删除");
			
			return map;
			
		}
		
		//这里的逻辑是先根据商品类别ID查询出商品类别的信息，找到商品类别的父级商品类别
		//如果父商品类别的子商品类别信息等于1，那么再删除商品信息的时候，父级商品类别的状态也应该从根节点改为叶子节点
		GoodsType goodsType = goodsTypeService.getGoodsTypeById(id);
		
		List<GoodsType> goodsTypeList = goodsTypeService.getAllGoodsTypeByParentId(goodsType.getpId());
		
		if(goodsTypeList.size()==1){
			
			GoodsType parentGoodsType = goodsTypeService.getGoodsTypeById(goodsType.getpId());
			
			parentGoodsType.setState(0);
			
			goodsTypeService.saveGoodsType(parentGoodsType);
			
		}
		
		goodsTypeService.delete(id);
		
		logService.save(new Log(Log.DELETE_ACTION,"删除商品类别："+goodsType.getName()));
		
		map.put("resultCode", ResultCode.SUCCESS);
		
		map.put("resultContent", "删除商品类别成功");
		
		return map;
		
	}
	
	/**
	 * 添加商品类别
	 * @param name 类别名
	 * @param pId 父类ID
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value={"商品管理","进货入库","退货出库","销售出库","客户退货","商品报损","商品报溢"},logical=Logical.OR)
	public Map<String,Object> save(String name,Integer pId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		GoodsType goodsType = new GoodsType(name,0,pId);
		
		goodsTypeService.saveGoodsType(goodsType);
		
		//根据父类ID来查询父类实体
		GoodsType parentGoodsType = goodsTypeService.getGoodsTypeById(pId);
		
		//如果当前父商品类别是叶子节点，则需要修改为状态为根节点
		if(parentGoodsType.getState()==0){
			
			parentGoodsType.setState(1);
			
			goodsTypeService.saveGoodsType(parentGoodsType);
			
		}
		
		logService.save(new Log(Log.INSERT_ACTION, "新增商品类别:"+name));
		
		map.put("resultCode", ResultCode.SUCCESS);
		
		map.put("resultContent", "新增商品类别成功");
		
		return map;
	}
	
	/**
	 * 查询所有商品类别
	 * @return easyui要求的JSON格式字符串
	 */
	@RequestMapping("/loadGoodsType")
	@RequiresPermissions(value={"商品管理","进货入库","退货出库","销售出库","客户退货","当前库存查询","商品报损","商品报溢","商品采购统计"},logical=Logical.OR)
	public String loadCheckMenu(Integer roleId){
		
		logService.save(new Log(Log.SELECT_ACTION, "查询商品类别信息"));
		
		return this.getAllGoodsType(-1).toString(); //根节点默认从-1开始
	}
	
	/**
	 * 递归查询所有商品类别
	 * @return
	 */
	public JsonArray getAllGoodsType(Integer parentId){
		
		JsonArray array = this.getGoodSTypeByParentId(parentId);
		
		for(int i=0;i<array.size();i++){
			
			JsonObject obj = (JsonObject) array.get(i);
			
			if(obj.get("state").getAsString().equals("open")){//如果是叶子节点，不再递归
				
				continue;
				
			}else{//如果是根节点，继续递归查询
				
				obj.add("children", this.getAllGoodsType(obj.get("id").getAsInt()));
				
			}
			
		}
		
		return array;
	}
	
	/**
	 * 根据父ID获取所有子商品类别
	 * @return
	 */
	public JsonArray getGoodSTypeByParentId(Integer parentId){
		
		JsonArray array = new JsonArray();
		
		List<GoodsType> goodsTypeList = goodsTypeService.getAllGoodsTypeByParentId(parentId);
		
		//遍历商品类别
		for(GoodsType goodsType : goodsTypeList){
			
			JsonObject obj = new JsonObject();
			
			obj.addProperty("id", goodsType.getId());//ID
			
			obj.addProperty("text", goodsType.getName());//类别名称
			
			if(goodsType.getState()==1){
				
				obj.addProperty("state", "closed"); //根节点
					
			}else{
				
				obj.addProperty("state", "open");//叶子节点
				
			}
			
			obj.addProperty("iconCls", "goods-type");//图标默认为自定义的商品类别图标
			
			JsonObject attributes = new JsonObject(); //扩展属性
			
			attributes.addProperty("state", goodsType.getState());//就加入当前节点的类型
			
			obj.add("attributes", attributes);
			
			array.add(obj);
				
		}
		
		return array;
	}

}
