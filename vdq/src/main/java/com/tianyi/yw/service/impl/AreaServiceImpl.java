package com.tianyi.yw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tianyi.yw.dao.AreaMapper;
import com.tianyi.yw.model.Area;
import com.tianyi.yw.service.AreaService;

@Service("areaService")
public class AreaServiceImpl implements AreaService {
	
	@Resource
	private AreaMapper areaMapper;
	@Override
	public List<Area> getAreaList(Area area) {
		// TODO Auto-generated method stub
		return areaMapper.getAreaList(area);
	}

	@Override
	public int getTotalCount(Area area) {
		// TODO Auto-generated method stub
		return areaMapper.getTotalCount(area);
	}

	@Override
	public List<Area> getAreaListByParentId(Area a1) {
		// TODO Auto-generated method stub
		return areaMapper.getAreaList(a1);
	}

	@Override
	public Area getAreaById(Area area) {
		// TODO Auto-generated method stub
		return areaMapper.selectByPrimaryKey(area.getId());
	}

	@Override
	public List<Area> getExistArea(Area a) {
		// TODO Auto-generated method stub
		return areaMapper.getExistArea(a);
	}

	@Override
	public void updateOrInsert(Area a) {
		// TODO Auto-generated method stub
		if(a.getId() > 0){
			areaMapper.updateByPrimaryKeySelective(a);
		}else{
			areaMapper.insert(a);
		}
	}

	@Override
	public Area getAreaById(Integer parentId) {
		// TODO Auto-generated method stub
		return areaMapper.selectByPrimaryKey(parentId);
	}

	@Override
	public void deleteAreaById(Area a) {
		// TODO Auto-generated method stub
		areaMapper.updateAreaById(a);
	}

	@Override
	public List<Area> getAllAreaList() {
		// TODO Auto-generated method stub
		return areaMapper.getAllAreaList();
	}

}
