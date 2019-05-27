package com.java1234.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.java1234.entity.Log;
import com.java1234.service.LogService;
import com.java1234.util.DateUtil;

@RestController
@RequestMapping("/log")
public class LogController {
	
	@Autowired
	private LogService logService;
	
	/**
	 * 分页查询日志信息
	 * @param type 日志类型
	 * @param trueName 操作人员
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param page 当前页数
	 * @param rows 每页条数
	 * @return
	 */
	@RequestMapping("/list")
	public String list(String type,String trueName,String sTime,String eTime,Integer page,Integer rows){
		
		JsonObject result = new JsonObject();
		
		JsonArray array = new JsonArray();
		
		try {
			
			List<Log> logList = logService.getLogList(type, trueName, DateUtil.StringToDate(sTime, "yyyy-MM-dd HH:mm:ss"),
					DateUtil.StringToDate(eTime, "yyyy-MM-dd HH:mm:ss"), page, rows);
			
			for(Log log : logList){
				
				JsonObject obj = new JsonObject();
				
				obj.addProperty("id", log.getId());
				
				obj.addProperty("type", log.getType());
				
				obj.addProperty("trueName", log.getUser().getTrueName());
				
				obj.addProperty("date", DateUtil.DateToString(log.getDate(), "yyyy-MM-dd HH:mm:ss"));
				
				obj.addProperty("content", log.getContent());
				
				array.add(obj);
				
			}
			
			Long total = logService.getLogCount(type, trueName, DateUtil.StringToDate(sTime, "yyyy-MM-dd HH:mm:ss"),
					DateUtil.StringToDate(eTime, "yyyy-MM-dd HH:mm:ss"));
			
			result.add("rows", array);
			
			result.addProperty("total", total);
			
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		return result.toString();
	}

}
