package com.tianyi.yw.QuartzJob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.Task;
import com.tianyi.yw.service.DeviceService;
import com.tianyi.yw.service.TaskService;

public class MyJobA {
	
	@Resource(name = "taskService")
	private TaskService taskService;
	
	@Resource(name = "deviceService")
	private DeviceService deviceService;
	
	private static Thread thread;
	 
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
	    			   createNewThread(temp);
	    		   }
	    	   }else{ 
	    		   System.out.printf("未执行任何任务，执行当前有效任务 \n\r");
    			   createNewThread(temp);
	    	   }
	       } else{
	    	   if(thread != null){ 
	    		 System.out.printf("没有任务执行，继续监听任务状态 \n\r");
	    		 thread.stop(); 
	    	   }
	       }
	 }

	private void createNewThread(final Task temp) {
		// TODO Auto-generated method stub
	    thread =   new Thread(new Runnable() {   
	    @Override  
	    public void run() {    
//	    		List<Device> list = new ArrayList<Device>();
//	    		list = deviceService.getDeviceList();
	    		try {
	    			int count = 100;
	    			while(count>0){
	    				count--;
			    		System.out.printf("thread "+thread.getName()+" is start \n\r");
						Thread.sleep(2*1000);
	    			}
	    			temp.setFlag(0);
	    			taskService.saveOrUpdateTask(temp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }  
		});  
		thread.setName(temp.getId()+""); 
		thread.start();
	}
}
