package com.tianyi.yw.QuartzJob;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.Task;
import com.tianyi.yw.service.DeviceService;
import com.tianyi.yw.service.DignosisService;
import com.tianyi.yw.service.TaskService;

public class MyJobA {

	@Resource(name = "taskService")
	private TaskService taskService;

	@Resource(name = "deviceService")
	private DeviceService deviceService;

	@Resource(name = "dignosisService")
	private DignosisService dignosisService;

	private static Thread thread;
	
	private Object lock = new Object();
	
	public void work() {
		List<Task> list = new ArrayList<Task>();
		list = taskService.getTaskList(new Task());
		Task temp = new Task();
		for(Task t:list){
			if(t.getFlag() == 1){
				temp = t;
				break;
			} 
		}
		if(temp.getName() != null){
			if(thread != null){
				if(!thread.getName().equals(temp.getId()+"")){
					System.out.printf("选择执行任务为其他任务，停止当前任务，执行新任务 \n\r");
					thread.stop();
					//开启新线程，同时清空临时表
					dignosisService.clear();
					int taskId = temp.getId();
					List<Device> deviceList = new ArrayList<Device>();
					deviceList = deviceService.getAllDeviceList();
					//遍历，给device对象追加任务id,设置默认服务器id,初始化检测次数
					for(Device device : deviceList){
						device.setTaskId(taskId);
						device.setServerId(0);
						device.setCheckTimes(0);
					}
					//批量插入数据到临时表
					dignosisService.insertDeviceList(deviceList);
					createNewThread(temp);
				} else {
					System.out.println("继续执行当前任务\n\r");
					//thread.start();
				}
			}else{ 
					System.out.printf("未执行任何任务，执行当前有效任务 \n\r");
					//开启新线程，同时清空临时表
					dignosisService.clear();
					int taskId = temp.getId();
					List<Device> deviceList = new ArrayList<Device>();
					deviceList = deviceService.getAllDeviceList();
					//遍历，给device对象追加任务id,设置默认服务器id,初始化检测次数
					for(Device device : deviceList){
						device.setTaskId(taskId);
						device.setServerId(0);
						device.setCheckTimes(0);
					}
					//批量插入数据到临时表
					dignosisService.insertDeviceList(deviceList);
					createNewThread(temp);
			}
			
		} else{
			if(thread != null){ 
				System.out.printf("没有任务执行，继续监听任务状态 \n\r");
				thread.stop(); 
			} else {
				System.out.printf("没有任务执行，继续监听任务状态 \n\r");
			}
		}
	}

	private void createNewThread(final Task temp) {
		thread = new Thread(new Runnable() {   
			boolean isOk = true;
			@Override  
			public void run() { 
				while(isOk){
					int count = temp.getRunTimes();
					while(count>0){
						count --;
						Date now = new Date();
						if(now.equals(temp.getStartTime())){
							System.out.println("执行任务");
							int result = dignosisService.getCheckResultList();
							System.out.println(result);
							while(result == 0){
								break;
							}
							try {
								thread.sleep(30*1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else if(now.after(temp.getStartTime())){
							System.out.println("任务时候已过");
							if(temp.getRunIntervals() == 0 || temp.getRunIntervals() == null){
								temp.setFlag(0);
								taskService.saveOrUpdateTask(temp);
								break;
							} else {
								try {
									thread.sleep(30*1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}else if(now.before(temp.getStartTime())){
							System.out.println("任务时候未到，请等待");
							try {
								thread.sleep(30*1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					try {
						long runIntervals = 0;
						if(temp.getRunIntervals() > 0){
							long newStartTime = temp.getStartTime().getTime() + new Date(runIntervals).getTime();
							Date newStartDate = new Date(newStartTime);
							temp.setStartTime(newStartDate);
							createNewThread(temp);
							runIntervals = temp.getRunIntervals() * 3600;
							Thread.sleep(runIntervals);
						} else {
							temp.setFlag(0);
							taskService.saveOrUpdateTask(temp);
						}
						thread.stop();
						isOk = false;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		thread.setName(temp.getId()+""); 
		thread.start();
		
//		thread = new Thread(new Runnable() {   
//			@Override  
//			public void run() { 
//				try {
//					/*int count = 100;
//	    			while(count>0){
//	    				count--;
//			    		System.out.printf("thread "+thread.getName()+" is start \n\r");
//						Thread.sleep(2*1000);
//	    			}*/
//					Date startDate = temp.getStartTime();  //开始时间
//					Date nowDate = new Date();
//					if (startDate.before(nowDate)){
//						System.out.println("任务时候已过，请重新设置");
//						temp.setFlag(0);
//						taskService.saveOrUpdateTask(temp);
//					} else if(startDate.after(nowDate)){
//						//long waitTime = startDate.getTime() - nowDate.getTime();
//						
//						System.out.println("任务时候未到，请等待");
//					} else if (startDate.equals(nowDate)){
//						System.out.println("任务开始执行!");
//						int checkResult = 0; //检查结果
//						int count = temp.getRunTimes(); //运行次数
//						long runIntervals = 0; //运行间隔
//						boolean isOk = false;
//						if(temp.getRunIntervals() > 0){
//							isOk = true;
//							runIntervals = temp.getRunIntervals() * 3600;
//						}
//						while(count > 0){ 
//							count--;
//							//获取临时表checkResult为空的记录数
//							checkResult = dignosisService.getCheckResultList();
//						}
//						//运行间隔不为空
//						while(isOk){
//							//等待
//							long newStartTime = temp.getStartTime().getTime() + new Date(runIntervals).getTime();
//							Date newStartDate = new Date(newStartTime);
//							temp.setStartTime(newStartDate);
//							createNewThread(temp);
//							Thread.sleep(runIntervals);
//						}
//						//检测结果
//						if(checkResult == 0){
//							if(!isOk) {
//								temp.setFlag(0);
//								taskService.saveOrUpdateTask(temp);
//							}
//						}
//					} 
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		//synchronized(lock){
//			
//			if(temp.getStartTime().after(new Date())){
//				//thread.stop();
//				System.out.println("任务时间未到，请等待");
//				try {
//					thread.setName(temp.getId()+"");
//					//lock.wait();
//					thread.start();
//					thread.sleep(30*1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			} else {
//				thread.setName(temp.getId()+""); 
//				thread.start();
//			}
//		}
	}
}
