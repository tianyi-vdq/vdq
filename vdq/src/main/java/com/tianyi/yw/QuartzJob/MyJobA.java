package com.tianyi.yw.QuartzJob;
 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
 


import com.tianyi.yw.model.DeviceDiagnosis;
import com.tianyi.yw.model.Task; 
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
			List<DeviceDiagnosis> result = diagnosisService.getList(new DeviceDiagnosis());
			if(result.size()==0){ 
				Calendar rightNow = Calendar.getInstance();
				Date now = rightNow.getTime();
				
				//List<Date> nextTime = new ArrayList<Date>();
//				if(temp.getRunIntervals()!= null&& temp.getRunIntervals()>0){
//					nextTime = getIntevelTime(temp);
//				}
				
//				if (now.compareTo(temp.getStartTime())==0||nextTime.contains(now)) {
//					taskRunService.TaskRun(temp.getId());
//				}
			}
		}
	}
 
}
