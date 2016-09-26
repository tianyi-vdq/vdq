package com.tianyi.yw.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.Constants;
import com.tianyi.yw.common.utils.StringUtil;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceStatus;
import com.tianyi.yw.model.StatisticResultModel;
import com.tianyi.yw.service.DeviceService;

@Scope("prototype")
@Controller
@RequestMapping("/statistic")
public class StatisticAction  extends BaseAction{
	
	@Resource
	private DeviceService deviceService;

	/**
	 * 统计模块 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/faultStatistic.do", method=RequestMethod.GET)
	public String deviceStatusList(
			StatisticResultModel statistic,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ParseException{ 	

		Device device = new Device();
		if (statistic.getPageNo() == null){
			statistic.setPageNo(1);
			device.setPageNo(1);
		}else{
			device.setPageNo(statistic.getPageNo());
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		SimpleDateFormat shortSdf = new SimpleDateFormat("dd");		
		Calendar   theCa=Calendar.getInstance();//获取当前日期
		List<String> dateStrList = new ArrayList<String>();
		List<String> dateStrList1 = new ArrayList<String>();
		if(statistic.getSearchMonthId()==null&&StringUtil.isEmpty(statistic.getSchBeginTime())&&StringUtil.isEmpty(statistic.getSchEndTime())){ 
			theCa.setTime(new Date());
			theCa.add(theCa.DATE, -30);//没有选择日期 , 则 默认加载 
	        String schBeginTime  = sdf.format(theCa.getTime())+" 00:00:00";
	        String schEndTime = sdf.format(new Date())+" 23:59:59";
	        statistic.setSchBeginTime(schBeginTime);
	        statistic.setSchEndTime(schEndTime);
	        for(int i = 0;i<31;i++){
	        	String dates = shortSdf.format(theCa.getTime());  
	        	dateStrList.add(dates.replaceAll("^(0+)", ""));
	        	dateStrList1.add(sdf.format(theCa.getTime()));
	        	theCa.add(theCa.DATE, 1);
	        }
	    }else if(statistic.getSearchMonthId()!= null &&statistic.getSearchMonthId()>0){
			String year = theCa.get(theCa.YEAR)+"";
			String month = "";
			String day = "";
			if(statistic.getSearchMonthId()>7){
				if(statistic.getSearchMonthId() % 2 ==0){
					day = "31";
					if(statistic.getSearchMonthId() > 9){
						month = statistic.getSearchMonthId()+""; 
					}else{
						month = "0"+statistic.getSearchMonthId();
					}

					for(int i = 1;i<32;i++){
						if(i<10){
							dateStrList.add((i+"").replace("0", ""));
				        	dateStrList1.add(year+"-"+month+"-0"+i);
						}else{
							dateStrList.add(i+"");
				        	dateStrList1.add(year+"-"+month+"-"+i);
						}
					}
				}else{
					day = "30";
					if(statistic.getSearchMonthId() < 10){
						month = "0"+statistic.getSearchMonthId()+""; 
					}else{
						month = statistic.getSearchMonthId()+""; 
					}
					for(int i = 1;i<31;i++){
						if(i<10){
							dateStrList.add((i+"").replace("0", ""));
				        	dateStrList1.add(year+"-"+month+"-0"+i);
						}else{
							dateStrList.add(i+"");
				        	dateStrList1.add(year+"-"+month+"-"+i);
						}
			        }
				}
			}else{
				if(statistic.getSearchMonthId() % 2 ==1){
					day = "31"; 
					month = "0"+statistic.getSearchMonthId(); 

					for(int i = 1;i<32;i++){
						if(i<10){
							dateStrList.add((i+"").replace("0", ""));
				        	dateStrList1.add(year+"-"+month+"-0"+i);
						}else{
							dateStrList.add(i+"");
				        	dateStrList1.add(year+"-"+month+"-"+i);
						}
			        }
				}else{
					month = "0"+statistic.getSearchMonthId();
					if(statistic.getSearchMonthId() == 2){
						day = "28"; 
						for(int i = 1;i<29;i++){
							if(i<10){
								dateStrList.add((i+"").replace("0", ""));
					        	dateStrList1.add(year+"-"+month+"-0"+i);
							}else{
								dateStrList.add(i+"");
					        	dateStrList1.add(year+"-"+month+"-"+i);
							}
				        }
					}else{
						day = "30"; 
						for(int i = 1;i<31;i++){
							if(i<10){
								dateStrList.add((i+"").replace("0", ""));
					        	dateStrList1.add(year+"-"+month+"-0"+i);
							}else{
								dateStrList.add(i+"");
					        	dateStrList1.add(year+"-"+month+"-"+i);
							}
				        }
					}
				}
			}
			String schBeginTime = year + "-" + month + "-01 00:00:00";
		    String schEndTime = year + "-" + month + "-" + day + " 23:59:59";
	        statistic.setSchBeginTime(schBeginTime);
	        statistic.setSchEndTime(schEndTime);
		}else if(!StringUtil.isEmpty(statistic.getSchBeginTime())&&!StringUtil.isEmpty(statistic.getSchEndTime())){
			Date startDate = sdf.parse(statistic.getSchBeginTime());
			Date endDate = sdf.parse(statistic.getSchEndTime());
			int subDay = daysBetween(startDate,endDate);
			theCa.setTime(endDate);
			theCa.add(theCa.DATE, subDay*-1);//没有选择日期 , 则 默认加载
			for(int i = 0;i<=subDay;i++){
	        	String dates = shortSdf.format(theCa.getTime());  
	        	dateStrList.add(dates.replaceAll("^(0+)", ""));
	        	dateStrList1.add(sdf.format(theCa.getTime()));
	        	theCa.add(theCa.DATE, 1);
	        }
	        statistic.setSchBeginTime(statistic.getSchBeginTime()+" 00:00:00");
	        statistic.setSchEndTime(statistic.getSchEndTime()+" 23:59:59");
		}

		statistic.setPageSize(Constants.DEFAULT_PAGE_SIZE);  
		device.setPageSize(Constants.DEFAULT_PAGE_SIZE);
		device.setFlag(0);
		List<StatisticResultModel> resultList = new ArrayList<StatisticResultModel>();
	
		int totalCount =  0;
		try{			
			List<Device> dList = deviceService.getDeviceListWithPage(device);
			totalCount = deviceService.getDeviceCount(device); 
			 
			for(Device d : dList){
				List<StatisticResultModel> statisticList = new ArrayList<StatisticResultModel>();
				StatisticResultModel rl = new StatisticResultModel(); 
				rl.setDeviceNumber(d.getPointId()); 
				rl.setDateStrList(dateStrList1);
				rl.setIpAddress(d.getIpAddress());
				statistic.setDeviceId(d.getId());
				statisticList =  deviceService.getStatisticResultList(statistic);
				List<String> valueList = new ArrayList<String>();
				for(String dtr : dateStrList1){
					String resultStr = "";
					for(int i=0;i<statisticList.size();i++){ 
						String datr1 = statisticList.get(i).getRecordDate();
						if(dtr.equals(datr1)){ 
							resultStr = statisticList.get(i).getResultValue();
						}
					}
					if(resultStr.length() == 0){
						resultStr = "2";
					}
					valueList.add(resultStr);
				}
				rl.setValueList(valueList);
				resultList.add(rl);
			}  
//			for(int i= 0;i<10;i++){
//				StatisticResultModel s = new StatisticResultModel();
//				s.setDeviceId(1);
//				s.setDeviceNumber("005016");
//				s.setId(i);
//				s.setRecordDate("2016-09-12");  
//				statisticList.add(s);
//			}
		}catch(Exception ex){ 
			ex.printStackTrace();
		}	
		statistic.setSchBeginTime(statistic.getSchBeginTime().substring(0, statistic.getSchBeginTime().length()-9));
		statistic.setSchEndTime(statistic.getSchEndTime().substring(0, statistic.getSchEndTime().length()-9));
		statistic.setTotalCount(totalCount); 
		request.setAttribute("StatisticResult", statistic); 
		request.setAttribute("StatisticResultlist", resultList); 
		request.setAttribute("DateStrList", dateStrList);
		return "web/statistic/faultStatistic";
	}

	private int daysBetween(Date smdate, Date bdate) throws ParseException {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));
	}	 
		
	
	
	/**
	 * 下载汇总数据到excel
	 * @param request
	 * @param response
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping(value = "/exportExcel.do")
	public JsonResult<StatisticResultModel> exportExcel(
			StatisticResultModel statistic,
			HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		JsonResult<StatisticResultModel> js = new JsonResult<StatisticResultModel>();
		js.setCode(new Integer(1));
		js.setMessage("导出失败!");
		 
		List<StatisticResultModel> list = getStatisticResultList(statistic);
		try {
			 
			if(list.size()>0){
				String filePath = createExceptionPoint(list,statistic);
				if(!StringUtil.isEmpty(filePath)){
					js.setCode(0);
					js.setGotoUrl(filePath);
					js.setMessage("点位诊断信息导出成功");
				}else{
					js.setMessage("创建Excle文件出错!");
				}
			}else{
				js.setMessage("没有结果数据!");
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return js;
	}
	private List<StatisticResultModel> getStatisticResultList(
			StatisticResultModel statistic) throws ParseException {
		// TODO Auto-generated method stub 
		Device device = new Device(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		SimpleDateFormat shortSdf = new SimpleDateFormat("dd");		
		Calendar   theCa=Calendar.getInstance();//获取当前日期 
		List<String> dateStrList1 = new ArrayList<String>();
		if(statistic.getSearchMonthId()==null&&StringUtil.isEmpty(statistic.getSchBeginTime())&&StringUtil.isEmpty(statistic.getSchEndTime())){ 
			theCa.setTime(new Date());
			theCa.add(theCa.DATE, -30);//没有选择日期 , 则 默认加载 
	        String schBeginTime  = sdf.format(theCa.getTime())+" 00:00:00";
	        String schEndTime = sdf.format(new Date())+" 23:59:59";
	        statistic.setSchBeginTime(schBeginTime);
	        statistic.setSchEndTime(schEndTime);
	        for(int i = 0;i<31;i++){
	        	String dates = shortSdf.format(theCa.getTime());   
	        	dateStrList1.add(sdf.format(theCa.getTime()));
	        	theCa.add(theCa.DATE, 1);
	        }
	    }else if(statistic.getSearchMonthId()!= null &&statistic.getSearchMonthId()>0){
			String year = theCa.get(theCa.YEAR)+"";
			String month = "";
			String day = "";
			if(statistic.getSearchMonthId()>7){
				if(statistic.getSearchMonthId() % 2 ==0){
					day = "31";
					if(statistic.getSearchMonthId() > 9){
						month = statistic.getSearchMonthId()+""; 
					}else{
						month = "0"+statistic.getSearchMonthId();
					}

					for(int i = 1;i<32;i++){
						if(i<10){ 
				        	dateStrList1.add(year+"-"+month+"-0"+i);
						}else{ 
				        	dateStrList1.add(year+"-"+month+"-"+i);
						}
					}
				}else{
					day = "30";
					if(statistic.getSearchMonthId() < 10){
						month = "0"+statistic.getSearchMonthId()+""; 
					}else{
						month = statistic.getSearchMonthId()+""; 
					}
					for(int i = 1;i<31;i++){
						if(i<10){ 
				        	dateStrList1.add(year+"-"+month+"-0"+i);
						}else{ 
				        	dateStrList1.add(year+"-"+month+"-"+i);
						}
			        }
				}
			}else{
				if(statistic.getSearchMonthId() % 2 ==1){
					day = "31"; 
					month = "0"+statistic.getSearchMonthId(); 

					for(int i = 1;i<32;i++){
						if(i<10){ 
				        	dateStrList1.add(year+"-"+month+"-0"+i);
						}else{ 
				        	dateStrList1.add(year+"-"+month+"-"+i);
						}
			        }
				}else{
					month = "0"+statistic.getSearchMonthId();
					if(statistic.getSearchMonthId() == 2){
						day = "28"; 
						for(int i = 1;i<29;i++){
							if(i<10){ 
					        	dateStrList1.add(year+"-"+month+"-0"+i);
							}else{ 
					        	dateStrList1.add(year+"-"+month+"-"+i);
							}
				        }
					}else{
						day = "30"; 
						for(int i = 1;i<31;i++){
							if(i<10){ 
					        	dateStrList1.add(year+"-"+month+"-0"+i);
							}else{ 
					        	dateStrList1.add(year+"-"+month+"-"+i);
							}
				        }
					}
				}
			}
			String schBeginTime = year + "-" + month + "-01 00:00:00";
		    String schEndTime = year + "-" + month + "-" + day + " 23:59:59";
	        statistic.setSchBeginTime(schBeginTime);
	        statistic.setSchEndTime(schEndTime);
		}else if(!StringUtil.isEmpty(statistic.getSchBeginTime())&&!StringUtil.isEmpty(statistic.getSchEndTime())){
			Date startDate = sdf.parse(statistic.getSchBeginTime());
			Date endDate = sdf.parse(statistic.getSchEndTime());
			int subDay = daysBetween(startDate,endDate);
			theCa.setTime(endDate);
			theCa.add(theCa.DATE, subDay*-1);//没有选择日期 , 则 默认加载
			for(int i = 0;i<=subDay;i++){
	        	//String dates = shortSdf.format(theCa.getTime());   
	        	dateStrList1.add(sdf.format(theCa.getTime()));
	        	theCa.add(theCa.DATE, 1);
	        }
	        statistic.setSchBeginTime(statistic.getSchBeginTime()+" 00:00:00");
	        statistic.setSchEndTime(statistic.getSchEndTime()+" 23:59:59");
		} 
		device.setFlag(0);
		//int totalCount = deviceService.getDeviceCount(device);
		List<StatisticResultModel> resultList = new ArrayList<StatisticResultModel>();
		//int PageCount = 0;
		//if(totalCount % Constants.DEFAULT_PAGE_SIZE ==0){
		//	PageCount = totalCount / Constants.DEFAULT_PAGE_SIZE;
		//}else{
		//	PageCount = totalCount / Constants.DEFAULT_PAGE_SIZE + 1 ; 
		//}
		try{			
			//for(int p = 1; p<=PageCount;p++){
				//device.setPageNo(p);
				//device.setPageSize(Constants.DEFAULT_PAGE_SIZE);  
				List<Device> dList = deviceService.getDeviceListWithPage(device);   
				//List<Integer> idList = new ArrayList<Integer>();
				//for(Device d : dList){
				//	idList.add(d.getId());
				//}
				List<StatisticResultModel> statisticList = new ArrayList<StatisticResultModel>();
				//List<StatisticResultModel> tempList = new ArrayList<StatisticResultModel>();
				statisticList =  deviceService.getStatisticResultList(statistic);
				//statisticList = tempList;
				for(Device d : dList){
					StatisticResultModel rl = new StatisticResultModel(); 
					rl.setDeviceNumber(d.getPointId()); 
					rl.setDateStrList(dateStrList1);
					rl.setIpAddress(d.getIpAddress());
					//statistic.setDeviceId(d.getId());
					//statistic.setIdList(idList);
					List<String> valueList = new ArrayList<String>();
					for(String dtr : dateStrList1){
						String resultStr = "";
						for(int i=0;i<statisticList.size();i++){ 
							String datr1 = statisticList.get(i).getRecordDate();
							if(dtr.equals(datr1)){ 
								resultStr = statisticList.get(i).getResultValue();
								//tempList.remove(i);
								if(i<(statisticList.size()-1)){
									String tempStr = statisticList.get(i+1).getRecordDate();
									if(!dtr.equals(tempStr)){
										break;
									}
								}
							} 
						}
						//statisticList = tempList;
						if(resultStr.length() == 0){
							resultStr = "2";
						}
						valueList.add(resultStr);
					}
					rl.setValueList(valueList);
					resultList.add(rl);
				}  
			//}
		}catch(Exception ex){ 
			ex.printStackTrace();
		}	
		return resultList;
	}

	private String createExceptionPoint(List<StatisticResultModel> list,StatisticResultModel statistic) {
		// TODO Auto-generated method stub
		String filePath ="";
	    String filename = "";
	    Workbook workbook = null;
	    try {
	        workbook = new HSSFWorkbook();
	        if (workbook != null) { 
	            Sheet sheet = workbook.createSheet("点位诊断汇总信息");
	
	            Row row0 = sheet.createRow(0);
	            Cell cell0_0 = row0.createCell(0, Cell.CELL_TYPE_STRING);
	            cell0_0.setCellValue("点位诊断信息(0: 表示诊断结果为异常 , 1: 表示诊断结果正常, 2: 表示没有诊断数据)");
	            //sheet.autoSizeColumn(0);
	            

	            Row row1 = sheet.createRow(1);
	            Cell cell1_0 = row1.createCell(0, Cell.CELL_TYPE_STRING);
	            cell1_0.setCellValue("开始时间:");
	            //sheet.autoSizeColumn(0);
	            Cell cell1_1 = row1.createCell(1, Cell.CELL_TYPE_STRING);
	            cell1_1.setCellValue(statistic.getSchBeginTime());
	            //sheet.autoSizeColumn(0);
	            Cell cell1_2 = row1.createCell(2, Cell.CELL_TYPE_STRING);
	            cell1_2.setCellValue("结束时间");
	            //sheet.autoSizeColumn(0);
	            Cell cell1_3 = row1.createCell(3, Cell.CELL_TYPE_STRING);
	            cell1_3.setCellValue(statistic.getSchEndTime());
	            //sheet.autoSizeColumn(0);

                if(list.size()>0){
                	List<String> dateList = list.get(0).getDateStrList();
                	Row row2 = sheet.createRow(2);

    	            Cell cell2_0 = row2.createCell(0, Cell.CELL_TYPE_STRING);
    	            cell2_0.setCellValue("点位编号");
    	            //sheet.autoSizeColumn(0);

    	            Cell cell2_1 = row2.createCell(1, Cell.CELL_TYPE_STRING);
    	            cell2_1.setCellValue("ip地址");
    	            //sheet.autoSizeColumn(0);
                	 for (int i = 0; i < dateList.size(); i++) {
                		Cell cellx = row2.createCell(2+i, Cell.CELL_TYPE_STRING);
                		cellx.setCellValue(dateList.get(i));
         	            //sheet.autoSizeColumn(0);
                	 }
                	 List<Row> rowList = new ArrayList<Row>();
                	 for(int i = 0; i<list.size(); i++){
                		 Row row = sheet.createRow(3 + i);
                		 rowList.add(row);
                	 }
                	 for(int i = 0; i<list.size(); i++){
                		 Row rowx = rowList.get(i);
                		 Cell cellx_0 = rowx.createCell(0, Cell.CELL_TYPE_STRING);
                		 cellx_0.setCellValue(list.get(i).getDeviceNumber());
                         //sheet.autoSizeColumn(0);
                		 Cell cellx_1 = rowx.createCell(1, Cell.CELL_TYPE_STRING);
                		 cellx_1.setCellValue(list.get(i).getIpAddress());
                         //sheet.autoSizeColumn(0);
                         for(int j = 0; j<list.get(i).getValueList().size(); j++){ 
                    		 Cell cellx_x = rowx.createCell(2+j, Cell.CELL_TYPE_STRING);
                    		 cellx_x.setCellValue(list.get(i).getValueList().get(j));
                             //sheet.autoSizeColumn(0);
                         }
                	 }
	            }
	
	
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	            java.util.Date date = new java.util.Date();
	            String str = sdf.format(date);
	            filePath = "tempfile";
	            String serverPath = getClass().getResource("/").getFile().toString();
	            serverPath = serverPath.substring(0, (serverPath.length() - 16));
	
	            filePath = serverPath+filePath;
	            File file = new File(filePath);
	
	            if(!file.exists()){
	                file.mkdir();
	            }
	            filePath += "/"+ str + "_StatisticInfo.xls";
	            filename = str + "_StatisticInfo.xls";
	            File realFile = new File(filePath);
	            if(realFile.exists()){
	                realFile.delete();
	            }
	            try {
	                FileOutputStream outputStream = new FileOutputStream(filePath);
	                workbook.write(outputStream);
	                outputStream.flush();
	                outputStream.close();
	            } catch (Exception e) {
	                return "";
	            }
	        }
	    }catch (Exception ex){
	
	    }
	    return filename;
	}
	/**
	 * 下载导出的数据文件
	 * @param request
	 * @param response
	 * @param filepath
	 */
	@ResponseBody
	@RequestMapping(value = "/downExceptionfile.do")
	public void downExceptionfile(HttpServletRequest request,
			HttpServletResponse response) { 
		try {
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
             java.util.Date date = new java.util.Date();
             String str = sdf.format(date);
             String filename = str +"_StatisticInfo.xls";
            String filePath = request.getSession().getServletContext().getRealPath("tempfile");
            filePath += "/"+filename;
            response.reset();
            response.setContentType("APPLICATION/OCTET-STREAM; charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=\""
                    + new String(filename.getBytes("GB2312"), "ISO-8859-1")
                    + "\"");

            FileInputStream inStream = new FileInputStream(filePath);
            byte[] b = new byte[100];
            int len;
            while ((len = inStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
