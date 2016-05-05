package com.tianyi.yw.dao;

import com.tianyi.yw.model.Parame;
@MyBatisRepository
public interface ParameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Parame record);

    int insertSelective(Parame record);

    Parame selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Parame record);

    int updateByPrimaryKey(Parame record);
}