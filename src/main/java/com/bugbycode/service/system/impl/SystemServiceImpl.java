package com.bugbycode.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bugbycode.module.log.SystemLog;
import com.bugbycode.module.config.LogConfig;
import com.bugbycode.service.base.MongoSuportTemplate;
import com.bugbycode.service.system.SystemService;
import com.util.StringUtil;
import com.util.page.Page;
import com.util.page.SearchResult;

@Service("systemService")
public class SystemServiceImpl extends MongoSuportTemplate implements SystemService{

	private final String COLLECTION_NAME = "fortSystemLog";
	
	@Override
	public SearchResult<SystemLog> find(String keyword, int module, int type, 
			int level,int startIndex, int pageSize) {
		Query q = new Query();
		q.with(Sort.by("createTime").descending());
		if(module != LogConfig.NULL_MODULE) {
			q.addCriteria(Criteria.where("module").is(module));
		}
		if(type != LogConfig.NULL_TYPE) {
			q.addCriteria(Criteria.where("type").is(type));
		}
		if(level != LogConfig.NULL_LEVEL) {
			q.addCriteria(Criteria.where("level").is(level));
		}
		if(StringUtil.isNotBlank(keyword)) {
			Criteria c = new Criteria();
			c.orOperator(Criteria.where("userName").regex(keyword, "i"),
					Criteria.where("userLoginName").regex(keyword, "i"));
			q.addCriteria(c);
		}
		long total = mongoTemplate.count(q, COLLECTION_NAME);
		int totalCount = (int) (total & 0xFFFFFFFF);
		Page page = new Page(pageSize, startIndex);
		List<SystemLog> list = new ArrayList<SystemLog>();
		if(totalCount > 0) {
			page.setTotalCount(totalCount);
			q.skip(page.getStartIndex());
			q.limit(page.getPageSize());
			list = mongoTemplate.find(q, SystemLog.class, COLLECTION_NAME);
		}
		SearchResult<SystemLog> sr = new SearchResult<SystemLog>();
		sr.setList(list);
		sr.setPage(page);
		return sr;
	}

	@Override
	public SystemLog findByModuleId(int module, String moduleId) {
		Query q = new Query();
		q.addCriteria(Criteria.where("module").is(module));
		q.addCriteria(Criteria.where(" moduleId").is(moduleId));
		q.with(Sort.by("createTime").descending());
		q.limit(1);
		return mongoTemplate.findOne(q, SystemLog.class, COLLECTION_NAME);
	}

	@Override
	public void save(SystemLog log) {
		mongoTemplate.save(log, COLLECTION_NAME);
	}

}
