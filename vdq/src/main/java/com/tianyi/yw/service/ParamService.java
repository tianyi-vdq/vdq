package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.Parame;

public interface ParamService {
	List<Parame> getParameList(Parame parame);

	int getParameCount(Parame parame);

	List<Parame> getExistParame(Parame p);

	void saveOrUpdateParame(Parame parame);

	Parame getParameById(Integer parameId);

}
