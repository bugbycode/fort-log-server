package com.bugbycode.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bugbycode.module.log.SystemLog;
import com.bugbycode.service.system.SystemService;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/systemLog")
public class SystemLogController {
	
	@Autowired
	private SystemService systemService;
	
	@RequestMapping("/query")
	@ResponseBody
	public String query(
			@RequestParam(name="keyword",defaultValue="")
			String keyWord,
			@RequestParam(name="module",defaultValue="-1")
			int module, 
			@RequestParam(name="type",defaultValue="-1")
			int type, 
			@RequestParam(name="level",defaultValue="-1")
			int level,
			@RequestParam(name="startIndex",defaultValue="-1")
			int startIndex,
			@RequestParam(name="pageSize",defaultValue="10")
			int pageSize) {
		JSONObject json = new JSONObject();
		SearchResult<SystemLog> sr = systemService.find(keyWord, module, type, level, 
				startIndex, pageSize);
		json.put("data", sr.getList());
		json.put("page", sr.getPage());
		return json.toString();
	}
	
	@RequestMapping("/findByModuleId")
	@ResponseBody
	public String findByModuleId(int module, String moduleId) {
		JSONObject json = new JSONObject();
		json.put("data", systemService.findByModuleId(module, moduleId));
		return json.toString();
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public String save(String jsonStr) {
		SystemLog log = JSONObject.toJavaObject(JSONObject.parseObject(jsonStr),SystemLog.class);
		JSONObject json = new JSONObject();
		int code = 0;
		String msg = "新建成功";
		try {
			systemService.save(log);
		}catch (Exception e) {
			code = 1;
			msg = e.getMessage();
		}
		json.put("msg", msg);
		json.put("id", log.get_id());
		json.put("code", code);
		return json.toString();
	}
}
