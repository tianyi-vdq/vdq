package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.Parame;
@MyBatisRepository
public interface ParameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Parame record);

    int insertSelective(Parame record);

    Parame selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Parame record);

    int updateByPrimaryKey(Parame record);
    
    List<Parame> getParameList(Parame parame);

	int getParameCount(Parame parame);

	List<Parame> getExistParame(Parame p);
}