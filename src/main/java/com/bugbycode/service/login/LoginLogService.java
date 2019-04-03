package com.bugbycode.service.login;

import com.bugbycode.module.log.LoginLog;
import com.util.page.SearchResult;

public interface LoginLogService {
	
	public SearchResult<LoginLog> find(String keyword,int startIndex, int pageSize);
	
	public void save(LoginLog log);
}
