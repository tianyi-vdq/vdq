package com.tianyi.yw.QuartzJob;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

	@Resource 
	private TaskService taskService;

	@Resource 
	private TaskRunning taskRunService;

	@Resource 
	private DignosisService diagnosisService;

	public void work() throws ParseException { 
		List<Task> list = new ArrayList<Task>();
		list = taskService.getTaskList(new Task());
		Task temp = new Task();
		for (Task t : list) {
			if (t.getFlag() == 1) {
				temp = t;
				//System.out.println(t.getName());
				break;
			}
		}
		if (temp != null && temp.getName() != null) { 
			List<TaskTime> timeList = taskService.getAllTaskTimeList(temp.getId());
			int result  = diagnosisService.getCheckResultList();
			if(result == 0){  
				//遍历任务开始时间列表，如当前时间等于开始时间，则启动任务
				Date now = new Date();
				for(TaskTime taskTime : timeList){ 
					Date tasktime = taskTime.getStartTime();
					int i = compare_date(now,tasktime);
					if(i == 0 ){ 
						taskRunService.TaskRun(temp.getId());
					}
				}
			}
		}
	}
	public int compare_date(Date now, Date tasktime) {
		int result = 0;
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		try {
			String s1 = df.format(now);
			String s2 = df.format(tasktime); 
			result = s1.compareTo(s2); 
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return result;
	}
}
