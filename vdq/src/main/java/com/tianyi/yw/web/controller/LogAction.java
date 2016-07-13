package com.tianyi.yw.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.Constants;
import com.tianyi.yw.model.Log; 
import com.tianyi.yw.model.LogType;
import com.tianyi.yw.service.LogService; 

@Scope("prototype")
@Controller
@RequestMapping("/log")
public class LogAction extends BaseAction 
{	
	@Resource(name = "logService")
	private LogService logService;
	
	/**
	 * 
	 * 日志列表
	 * @param log
	 * @param request
	 * @param response
	 * @return web/log/logList
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/logList.do",method=RequestMethod.GET)
	public String logList(Log log,LogType logType,HttpServletRequest request, HttpServletResponse response) 
			throws UnsupportedEncodingException{ 
		//内容，描述
		if (log.getSearchName() != null	&& log.getSearchName().length() > 0) 
		{
			String searchName = new String(log.getSearchName().getBytes("iso8859-1"), "utf-8");
			log.setSearchName(searchName);			
		}
		//时间
		if (log.getSearchTimes() != null	&& log.getSearchTimes().length() > 0) 
		{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
			Date date1;			
			try {
				date1 = sdf.parse(log.getSearchTimes());
				log.setSearchTime(date1);
    			Calendar c = Calendar.getInstance();
	    		c.setTime(date1);
	    		c.add(Calendar.DATE, 1);
		    	log.setSearchTimeNext(c.getTime());  		    	
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//类型
		if (log.getSearchTypeIds() != null	&& log.getSearchTypeIds().length() > 0) 
		{
			int date2 = Integer.parseInt(log.getSearchTypeIds());			
			log.setSearchTypeId(date2);		
			
		}
		//分页
		if (log.getPageNo() == null)
			log.setPageNo(1);
		log.setPageSize(Constants.DEFAULT_PAGE_SIZE); 
		int totalCount = 0;
		SimpleDateFormat Format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式 
		List<Log> loglist = new ArrayList<Log>();
		List<LogType> logTypelist = new ArrayList<LogType>();
		try{
			logTypelist =  logService.getLogTypeList(logType);
			loglist =  logService.getLogList(log);
			totalCount = logService.getLogCount(log);
		}catch(Exception ex){ 
			ex.printStackTrace();
		}
		
		for(Log t:loglist)
		{
			
			if( t.getCreateTimes()==null ||  t.getCreateTimes().length() == 0 )
			{
				t.setCreateTimes( Format.format( t.getCreateTime() ));
			}
		    
		}
		log.setTotalCount(totalCount); 
		request.setAttribute("Log", log); 
		request.setAttribute("Loglist", loglist); 
		request.setAttribute("LogTypelist", logTypelist); 
		return "web/log/logList";
	}
	/**
	 * 
	 * 日志导出
	 * @param log
	 * @param request
	 * @param response
	 * @return js
	 * @throws IOException 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonloadLogExport.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
	public JsonResult<Log> RunTask(
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonResult<Log> js = new JsonResult<Log>();
		js.setCode(new Integer(1));
		js.setMessage("日志导出失败!");
		Log log = new Log();
		List<Log> loglist = new ArrayList<Log>();
		loglist =  logService.getLogList(log);
		SimpleDateFormat Format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
	        // 第一步，创建一个webbook，对应一个Excel文件  
	    HSSFWorkbook logbook = new HSSFWorkbook();  
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	    HSSFSheet sheet = logbook.createSheet("日志表一");    
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	    sheet.setDefaultColumnWidth(15);  
	    HSSFRow row = sheet.createRow(0);  
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	    HSSFCellStyle style = logbook.createCellStyle();  
	         style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
	         style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
	         style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	         style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	         style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	         style.setBorderTop(HSSFCellStyle.BORDER_THIN);  		    
	         style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	         HSSFFont font = logbook.createFont();  
	         font.setColor(HSSFColor.VIOLET.index);  
	         font.setFontHeightInPoints((short) 12);  
	         font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
	         style.setFont(font);  
	         HSSFCell cell = row.createCell(0);  
	         cell.setCellValue("日志ID");  
	         cell.setCellStyle(style);  
	         cell = row.createCell((int) 1);  
	         cell.setCellValue("日志内容");  
	         cell.setCellStyle(style);  
	         cell = row.createCell(2);  
	         cell.setCellValue("日志类型");  
	         cell.setCellStyle(style);  
	         cell = row.createCell(3);  
	         cell.setCellValue("创建时间");  
	         cell.setCellStyle(style);  
	         cell = row.createCell(4);  
	         cell.setCellValue("详细描述");  
	         cell.setCellStyle(style);  
	  		     
	        int i=1;
	        for (Log t:loglist)  
	        {  
	        	if( t.getCreateTimes()==null ||  t.getCreateTimes().length() == 0 )
				{
					t.setCreateTimes( Format.format( t.getCreateTime() ));
				}
	            row = sheet.createRow(i++);               
	            // 第四步，创建单元格，并设置值  
	         
	            row.createCell(0).setCellValue((Integer) t.getId());  
	            row.createCell(1).setCellValue((String) t.getContent());  
	            row.createCell(2).setCellValue((String) t.getTypeName());  
	            row.createCell(3).setCellValue((String) t.getCreateTimes()); 
	            row.createCell(4).setCellValue((String) t.getDescription()); 		             
	        }  	
	        //设置下载路径
	        String fileName = "视频诊断日志.xls";
	        @SuppressWarnings("deprecation")
			String filePath = request.getRealPath("/") + fileName;
	        //response.addHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "UTF-8"));    
	        //response.setContentType("application/vnd.ms-excel;charset=gb2312");    
	        try {
				FileOutputStream os = new FileOutputStream(filePath);
				logbook.write(os);
				//fileName = new String(fileName.getBytes("ISO-8859-1"), "utf-8");
				FileInputStream inStream = new FileInputStream(filePath);
				byte[] b = new byte[100];
				int len;
				while ((len = inStream.read(b)) > 0) {
					response.getOutputStream().write(b, 0, len);
					// this.getRes().getOutputStream().write(b,0,len);
				}
				inStream.close();
	        	os.close(); 			
			    js.setCode(new Integer(0));
			    js.setGotoUrl(filePath);
			    js.setMessage("日志导出成功!");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			/*FileInputStream inStream = new FileInputStream(logbook.write());
			byte[] b = new byte[100];
			int len;
			while ((len = inStream.read(b)) > 0) {
				response.getOutputStream().write(b, 0, len);
				// this.getRes().getOutputStream().write(b,0,len);
			}
			inStream.close();
			
			
			OutputStream os = response.getOutputStream();  
	        xls.getWorkbook().write(os);  
	        os.flush(); 
			
			filePath = new String(filePath.getBytes("ISO-8859-1"), "utf-8");
			FileInputStream inStream = new FileInputStream(filePath);
			byte[] b = new byte[100];
			int len;
			while ((len = inStream.read(b)) > 0) {
				response.getOutputStream().write(b, 0, len);
				// this.getRes().getOutputStream().write(b,0,len);
			}
			inStream.close();*/
	        /*File file = new File(filepath);
	        if(!file.mkdirs())
	        {	        		       
	        	try {
	        		 file.createNewFile();
	        		 } catch (IOException e) {
	        		 e.printStackTrace();
	        		 }
	        }
	        String filePath = filepath + "/" + fileName;
	        // 第六步，将文件存到指定位置       	         
	        try  
	        {  		        	
	        	FileOutputStream os = new FileOutputStream(filePath); 
	        	logbook.write(os); 
	        	os.close(); 			
			    js.setCode(new Integer(0));
			    js.setMessage("日志导出成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return js;
	}
	
	
	
}


