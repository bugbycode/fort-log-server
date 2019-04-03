package com.bugbycode.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bugbycode.module.log.LoginLog;
import com.bugbycode.service.login.LoginLogService;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/loginLog")
public class LoginLogController {

	@Autowired
	private LoginLogService LoginLogService;
	
	@RequestMapping("/query")
	@ResponseBody
	public String query(
			@RequestParam(name="keyword",defaultValue="")
			String keyWord,
			@RequestParam(name="startIndex",defaultValue="-1")
			int startIndex,
			@RequestParam(name="pageSize",defaultValue="10")
			int pageSize) {
		JSONObject json = new JSONObject();
		SearchResult<LoginLog> sr = LoginLogService.find(keyWord, startIndex, pageSize);
		json.put("data", sr.getList());
		json.put("page", sr.getPage());
		return json.toString();
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public String save(String jsonStr) {
		LoginLog log = JSONObject.toJavaObject(JSONObject.parseObject(jsonStr),LoginLog.class);
		JSONObject json = new JSONObject();
		int code = 0;
		String msg = "新建成功";
		try {
			LoginLogService.save(log);
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
