package com.tianyi.yw.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tianyi.yw.dao.DeviceDiagnosisMapper;
import com.tianyi.yw.dao.DeviceMapper;
import com.tianyi.yw.dao.DeviceStatusMapper;
import com.tianyi.yw.dao.LogMapper;
import com.tianyi.yw.dao.TaskMapper;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.Log;
import com.tianyi.yw.model.MediaCenterConfig;
import com.tianyi.yw.model.Task;
import com.tianyi.yw.service.TaskRunning;

@Service
public class TaskRunningImpl implements TaskRunning {
 
	private final static String m_center_ipaddress = MediaCenterConfig.getInstance().getIp();

    private final static String m_center_port = MediaCenterConfig.getInstance().getPort();

    private final static String m_center_connect_key = MediaCenterConfig.getInstance().getConnectionKey();
 
    private final static String m_center_connect_session = MediaCenterConfig.getInstance().getConnectionSession();
	
	@Resource
	private TaskMapper taskMapper;

	@Resource
	private DeviceDiagnosisMapper dignosisMapper;

	@Resource
	private DeviceMapper deviceMapper;
	@Resource
	private DeviceStatusMapper deviceStatusMapper;
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
						List<Device> deviceList = new ArrayList<Device>();
						dignosisMapper.clear();
						deviceStatusMapper.clear();
						deviceList = deviceMapper.getAllDeviceList();
						// 遍历，给device对象追加任务id,设置默认服务器id,初始化检测次数
						for (Device device : deviceList) {
							if(device.getRtspUrl()==null||device.getRtspUrl().equals("")){
								UUID uuid = UUID.randomUUID();
								String rtspUrl = m_center_ipaddress+":"+m_center_port+m_center_connect_key + device.getPointNaming()+m_center_connect_session+uuid.toString();
								device.setRtspUrl(rtspUrl);
							}
							device.setTaskId(taskId);
							device.setServerId(0);
							device.setCheckTimes(1);
						}
						// 批量插入数据到临时表
						dignosisMapper.insertDeviceList(deviceList);
						int result = 0;
						boolean isOver= true;
						while(isOver){
							result = dignosisMapper.getCheckResultList();
							if (result == 0) {
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
