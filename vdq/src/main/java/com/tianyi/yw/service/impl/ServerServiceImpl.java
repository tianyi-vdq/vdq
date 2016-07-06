package com.tianyi.yw.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.tianyi.yw.dao.ServerMapper;
import com.tianyi.yw.model.Server;
import com.tianyi.yw.service.ServerService;

@Service("serverService")
public class ServerServiceImpl implements ServerService {

	
	@Resource
	private ServerMapper serverMapper;
	
	@Override
	public void insert(Server server) {
		// TODO Auto-generated method stub
		serverMapper.insert(server);
	}

	@Override
	public void updatebyselective(Server server) {
		// TODO Auto-generated method stub
		serverMapper.updateByPrimaryKeySelective(server);
	}

	@Override
	public List<Server> getList(Server server) {
		// TODO Auto-generated method stub
		return serverMapper.getList(server);
	}

	@Override
	public Server selectByPrimaryKey(int serverId) {
		// TODO Auto-generated method stub
		return serverMapper.selectByPrimaryKey(serverId);
	}

	@Override
	public Server selectByIp(Server server) {
		// TODO Auto-generated method stub
		return serverMapper.selectByIp(server);
	}

}
