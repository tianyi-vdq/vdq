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

	@Resource(name = "taskService")
	private TaskService taskService;

	@Resource(name = "taskRunService")
	private TaskRunning taskRunService;

	@Resource(name = "dignosisService")
	private DignosisService diagnosisService;

	public void work() throws ParseException { 
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
			/*SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");*/
			//int result = diagnosisService.getCheckResultList();
			if(resultList.size()==0){ 
				//Calendar rightNow = Calendar.getInstance();
				/*String s = sdf.format(now);
				System.out.println(s);
				Date d = sdf.parse(s);
				System.out.println(d);
				rightNow.setTime(d);*/
				//忽略年月日，秒
				//rightNow.set(Calendar.YEAR,0);
				//rightNow.set(Calendar.MONTH,0);
				//rightNow.set(Calendar.DAY_OF_YEAR,0);
				/*rightNow.set(Calendar.SECOND,0);
				rightNow.set(Calendar.MILLISECOND, 0);
				System.out.println(rightNow.getTime());*/
				//遍历任务开始时间列表，如当前时间等于开始时间，则启动任务
				Date now = new Date();
				for(TaskTime taskTime : timeList){
					//Calendar cal = Calendar.getInstance();
					//cal.setTime(tasktime);
					//cal.set(Calendar.SECOND,0);
					//cal.set(Calendar.MILLISECOND, 0);
					//System.out.println(cal.getTime());
					Date tasktime = taskTime.getStartTime();
					int i = compare_date(now,tasktime);
					if(i == 0 ){
						//System.out.println(i);
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
