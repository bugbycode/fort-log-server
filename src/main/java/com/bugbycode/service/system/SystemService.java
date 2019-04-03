package com.bugbycode.service.system;

import com.bugbycode.module.log.SystemLog;
import com.util.page.SearchResult;

public interface SystemService {

	public SearchResult<SystemLog> find(String keyword,int module,int type,
			int level,int startIndex,int pageSize);
	
	/**
	 * 根据模型类型和模型ID查询最后一次操作的记录
	 * @param module
	 * @param moduleId
	 * @return
	 */
	public SystemLog findByModuleId(int module,String moduleId);
	
	public void save(SystemLog log);
}
