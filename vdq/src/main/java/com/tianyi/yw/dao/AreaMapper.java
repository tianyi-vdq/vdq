package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.Area;
@MyBatisRepository
public interface AreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);

	List<Area> getAreaList(Area area);

	int getTotalCount(Area area);

	List<Area> getExistArea(Area a);

	void deleteAreaById(Integer areaId);

	void updateAreaById(Area a);

}