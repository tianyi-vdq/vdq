package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.Server;
@MyBatisRepository
public interface ServerMapper {
	void deleteByPrimaryKey(Integer id);

	void insert(Server record);

	void insertSelective(Server record);

    Server selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(Server record);

    void updateByPrimaryKey(Server record);

	List<Server> getList(Server server);
}