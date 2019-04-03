package com.bugbycode.service.login.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bugbycode.module.log.LoginLog;
import com.bugbycode.service.base.MongoSuportTemplate;
import com.bugbycode.service.login.LoginLogService;
import com.util.StringUtil;
import com.util.page.Page;
import com.util.page.SearchResult;

@Service("loginLogService")
public class LoginLogServiceImpl extends MongoSuportTemplate implements LoginLogService {

	private final String COLLECTION_NAME = "fortLoginLog";
	
	@Override
	public SearchResult<LoginLog> find(String keyword, int startIndex, int pageSize) {
		SearchResult<LoginLog> sr = new SearchResult<LoginLog>();
		Query q = new Query();
		q.with(Sort.by("createTime").descending());
		if(StringUtil.isNotBlank(keyword)) {
			Criteria c = new Criteria();
			c.orOperator(Criteria.where("userName").regex(keyword, "i"),
					Criteria.where("userLoginName").regex(keyword, "i"));
			q.addCriteria(c);
		}
		long total = mongoTemplate.count(q, COLLECTION_NAME);
		int totalCount = (int) (total & 0xFFFFFFFF);
		Page page = new Page(pageSize, startIndex);
		List<LoginLog> list = new ArrayList<LoginLog>(); 
		if(totalCount > 0) {
			page.setTotalCount(totalCount);
			q.skip(page.getStartIndex());
			q.limit(page.getPageSize());
			list = mongoTemplate.find(q, LoginLog.class, COLLECTION_NAME);
		}
		sr.setList(list);
		sr.setPage(page);
		return sr;
	}

	@Override
	public void save(LoginLog log) {
		mongoTemplate.save(log, COLLECTION_NAME);
	}

}
