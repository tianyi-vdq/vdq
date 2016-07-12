package com.tianyi.yw.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tianyi.yw.dao.DeviceDiagnosisMapper;
import com.tianyi.yw.dao.DeviceMapper;
import com.tianyi.yw.dao.LogMapper;
import com.tianyi.yw.dao.TaskMapper;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.Log;
import com.tianyi.yw.model.Task;
import com.tianyi.yw.service.TaskRunning;

@Service("taskRunService")
public class TaskRunningImpl implements TaskRunning {

	@Resource
	private TaskMapper taskMapper;

	@Resource
	private DeviceDiagnosisMapper dignosisMapper;

	@Resource
	private DeviceMapper deviceMapper;
	@Resource
	private LogMapper logMapper;

	@Override
	public void TaskRun(Integer taskId) {
		// TODO Auto-generated method stub
		boolean isOk = true;
		try {
			Task task = taskMapper.selectByPrimaryKey(taskId);
			if (task != null) {
				while (isOk) {
//					int count = task.getRunTimes();
//					while (count > 0) {
//						count--;
						List<Device> deviceList = new ArrayList<Device>();
						dignosisMapper.clear();
						deviceList = deviceMapper.getAllDeviceList();
						// 遍历，给device对象追加任务id,设置默认服务器id,初始化检测次数
						for (Device device : deviceList) {
							device.setTaskId(taskId);
							device.setServerId(0);
							device.setCheckTimes(0);
						}
						// 批量插入数据到临时表
						dignosisMapper.insertDeviceList(deviceList);
						int result = 0;
						boolean isOver= true;
						while(isOver){
							result = dignosisMapper.getCheckResultList();
							// System.out.println(result);
							if (result == 0) {
								//可能存在其他任务开始时间
								//Task t = taskMapper.getTaskById(taskId);
								//t.setFlag(0);
								//taskMapper.updateByPrimaryKeySelective(t);
								//任务执行成功，清空临时表
								dignosisMapper.clear();
								isOver= false;
							}
						}
						isOk = false;
				}
			}
		} catch (Exception ex) {
			isOk = false;
			ex.printStackTrace();
			Log log = new Log();
			log.setId(0);
			log.setContent("调度任务失败");
			log.setDescription("调度任务失败，详细："+ex.getMessage());
			log.setCreateTime(new Date());
			log.setTypeId(1);
			logMapper.insert(log);
		}
	}
}
