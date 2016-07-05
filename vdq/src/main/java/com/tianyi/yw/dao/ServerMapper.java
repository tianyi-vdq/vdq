package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.Server;

public interface ServerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Server record);

    int insertSelective(Server record);

    Server selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Server record);

    int updateByPrimaryKey(Server record);

	List<Server> getList(Server server);
}