package com.tianyi.yw.QuartzJob;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;





import com.tianyi.yw.dao.DeviceDiagnosisMapper;
import com.tianyi.yw.model.DeviceDiagnosis;
import com.tianyi.yw.model.Task; 
import com.tianyi.yw.model.TaskTime;
import com.tianyi.yw.service.DignosisService;
import com.tianyi.yw.service.TaskRunning;
import com.tianyi.yw.service.TaskService;

public class MyJobA {

	@Resource(name = "taskService")
	private TaskService taskService;

	@Resource(name = "taskRunService")
	private TaskRunning taskRunService;

	@Resource(name = "dignosisService")
	private DignosisService diagnosisService;

	public void work() { 
		List<Task> list = new ArrayList<Task>();
		list = taskService.getTaskList(new Task());
		Task temp = new Task();
		for (Task t : list) {
			if (t.getFlag() == 1) {
				temp = t;
				break;
			}
		}
		if (temp != null && temp.getName() != null) { 
			List<TaskTime> timeList = taskService.getAllTaskTimeList(temp.getId());
			List<DeviceDiagnosis> resultList = diagnosisService.getList(new DeviceDiagnosis());
			//int result = diagnosisService.getCheckResultList();
			if(resultList.size()==0){ 
				Calendar rightNow = Calendar.getInstance();
				Date now = new Date();
				rightNow.setTime(now);
				//忽略秒
				rightNow.set(Calendar.SECOND,0);
				rightNow.set(Calendar.MILLISECOND,0);
				//遍历任务开始时间列表，如当前时间等于开始时间，则启动任务
				for(TaskTime taskTime : timeList){
					Calendar cal = Calendar.getInstance();
					Date tasktime = taskTime.getStartTime();
					cal.setTime(tasktime);
					cal.set(Calendar.SECOND,0);
					cal.set(Calendar.MILLISECOND, 0);
					if(rightNow.compareTo(cal)==0 ){
						taskRunService.TaskRun(temp.getId());
					}
				}
			}
				//		List<Task> list = new ArrayList<Task>();
				//		list = taskService.getTaskList(new Task());
				//		Task temp = new Task();
				//		for (Task t : list) {
				//			if (t.getFlag() == 1) {
				//				temp = t;
				//				break;
				//			}
				//		}
				//		if (temp != null && temp.getName() != null) {
				//			Calendar rightNow = Calendar.getInstance();
				//			Date now = rightNow.getTime();
				//			List<Date> nextTime = new ArrayList<Date>();
				//			if(temp.getRunIntervals()!= null&& temp.getRunIntervals()>0){
				//				nextTime = getIntevelTime(temp);
				//			}
				//			if (now.compareTo(temp.getStartTime())==0||nextTime.contains(now)) {
				//				taskRunService.TaskRun(temp.getId());
				//			}
				//		}
			

		}
	}
}
