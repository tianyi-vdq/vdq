package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.Area;

public interface AreaService {

	List<Area> getAreaList(Area area);

	int getTotalCount(Area area);

	List<Area> getAreaListByParentId(Area a1);

	Area getAreaById(Area area);

	List<Area> getExistArea(Area a);

	void updateOrInsert(Area a);

	Area getAreaById(Integer parentId);

	void deleteAreaById(Area a);



}
