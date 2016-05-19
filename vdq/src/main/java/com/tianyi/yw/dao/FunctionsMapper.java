package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.Functions;
@MyBatisRepository
public interface FunctionsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Functions record);

    int insertSelective(Functions record);

    Functions selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Functions record);

    int updateByPrimaryKey(Functions record);

	List<Functions> getFunctionByUserId(Integer id);

	List<Functions> getFunctionByParentId(Integer id);
}