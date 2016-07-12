package com.tianyi.yw.QuartzJob;
 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
 
import com.tianyi.yw.model.Task; 
import com.tianyi.yw.service.TaskRunning;
import com.tianyi.yw.service.TaskService;

public class MyJobA {

	@Resource(name = "taskService")
	private TaskService taskService;

	@Resource(name = "taskRunService")
	private TaskRunning taskRunService;

	public void work() {
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

	private List<Date> getIntevelTime(Task temp) {
		// TODO Auto-generated method stub  
		List<Date> list = new ArrayList<Date>();
		Date startTime = temp.getStartTime();
				
		Calendar todayEnd = Calendar.getInstance();  
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);  
        todayEnd.set(Calendar.MINUTE, 59);   
        Date end = todayEnd.getTime(); //时限范围限于当天之内；小于23:59:00
          
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(startTime); 
        rightNow.add(Calendar.HOUR_OF_DAY,temp.getRunIntervals());//日期加10天 
		Date nextTime = rightNow.getTime();//计算下一个时间间隔点
		while(nextTime.compareTo(end)==-1){
			list.add(nextTime);
			nextTime = new Date();//计算下一个间隔时间
		}
		return list;
	}
}
