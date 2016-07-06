package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.Server;


public interface ServerService {
	
	void insert(Server server);
	
	void updatebyselective(Server server);
	
	List<Server> getList(Server server);
	
	Server selectByPrimaryKey(int serverId);

	Server selectByIp(Server server);
}
