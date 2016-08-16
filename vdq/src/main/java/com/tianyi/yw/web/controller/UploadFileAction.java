package com.tianyi.yw.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.StringUtil;
import com.tianyi.yw.model.Area;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceStatus;
import com.tianyi.yw.model.Log;
import com.tianyi.yw.service.AreaService;
import com.tianyi.yw.service.DeviceService;
import com.tianyi.yw.service.LogService;

@Scope("prototype")
@Controller
@RequestMapping("/fileUpload")
public class UploadFileAction extends BaseAction {

	@Resource
	private DeviceService deviceService;
	@Resource
	private AreaService areaService;
	@Resource
	private LogService logService;

	/**
	 * 插入点位设备到数据库
	 * 
	 * @param list
	 * @return
	 */
	private List<Device> insertListToDatabase(List<Device> list) {
		// TODO Auto-generated method stub
		List<Device> lst = new ArrayList<Device>();
		try {
			for (Device device : list) {

				if (device.getPointId() == null || device.getAddress() == null) {
					lst.add(device);
				} else {
					device.setId(0);
					device.setType("点位");
					device.setPort(9554);
					device.setFlag(0);
					device.setPlatformId("1");
					try {
						deviceService.saveDevicepoint(device);
					} catch (Exception ex) {
						lst.add(device);
						ex.printStackTrace();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lst;
	}

	/**
	 * 导入点位设备信息 导入成功后，跳转到导入结果临时页面
	 * 
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/uploadDeviceExcel.do")
	public String uploadDeviceExcelFile(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		// 导入总记录数
		List<Device> defaultlist = new ArrayList<Device>();
		// 导入成功记录数
		List<Device> list = new ArrayList<Device>();
		// 导入失败记录
		List<Device> nulllist = new ArrayList<Device>();
		// 导入总数
		int totalCount = 0;
		// 导入成功数
		int rightCount = 0;
		// 导入失败数
		int subCount = 0;

		try {
			// 项目在容器中的实际发布运行的upload路径
			String path = request.getSession().getServletContext()
					.getRealPath("upload");
			// 获取文件名
			String fileName = file.getOriginalFilename();
			File targetFile = new File(path, fileName);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			// 保存
			try {
				File f = new File(targetFile.getPath());
				if (f.exists()) {
					f.delete();
				}
				// 把MultipartFile转换成File类型,MultipartFile自带的transferTo
				file.transferTo(targetFile);
				InputStream stream = new FileInputStream(targetFile.getPath());
				Workbook wb = WorkbookFactory.create(stream);
				// HSSFWorkbook是解析出来excel 2007 以前版本的，后缀名为xls的;
				// XSSFWorkbook是解析excel 2007 版的，后缀名为xlsx。
				try {
					stream = new FileInputStream(targetFile.getPath());
					wb = new XSSFWorkbook(stream);
					defaultlist = getXSSFResult(wb);
				} catch (Exception ex) {
					stream = new FileInputStream(targetFile.getPath());
					wb = new HSSFWorkbook(stream);
					defaultlist = getHSSFResult(wb);
				}

				for (Device point : defaultlist) {
					// 把刚获取的列存入list,判断获取的对象是否按照规则
					if (point.getPointId() == null
							|| "".equals(point.getPointId())
							|| point.getPointNumber() == null
							|| "".equals(point.getPointNumber())
							|| point.getPointNaming() == null
							|| "".equals(point.getPointNaming())
							|| point.getPointName() == null
							|| "".equals(point.getPointName())
							|| point.getIpAddress() == null
							|| "".equals(point.getIpAddress())) {
						nulllist.add(point);
					} else { 
						list.add(point);
					}
				}
				// stream.close();
				IOUtils.closeQuietly(stream);
				totalCount = list.size() + nulllist.size();
				if (totalCount > 0) {
					List<Device> lst = insertListToDatabase(list);
					rightCount = list.size() - lst.size();
					subCount = totalCount - rightCount;
					if (lst.size() > 0) {
						for (Device d : lst) {
							nulllist.add(d);
						}
					}
					File files = new File(targetFile.getPath());
					if (files.exists()) {
						files.delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("pointlist", nulllist);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("rightCount", rightCount);
		request.setAttribute("subCount", subCount);

		return "web/importExcel/deviceResult";
	}
	 
	// 解析excel文件
	private List<Device> getXSSFResult(Workbook wb) {
		// TODO Auto-generated method stub
		List<Device> result = new ArrayList<Device>();
		List<Area> areaList = new ArrayList<Area>();
		areaList = areaService.getAllAreaList();
		for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
			XSSFSheet st = (XSSFSheet) wb.getSheetAt(sheetIndex);
			// 第一行为标题，不取
			for (int rowIndex = 1; rowIndex <= st.getLastRowNum(); rowIndex++) {
				XSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				Device point = new Device();
				XSSFCell cell0 = row.getCell(0);
				if (cell0 != null || "".equals(cell0)) {
					cell0.setCellType(Cell.CELL_TYPE_STRING);
					point.setPointId(cell0.getStringCellValue());
				}
				XSSFCell cell1 = row.getCell(1);
				if (cell1 != null || "".equals(cell1)) {
					cell1.setCellType(Cell.CELL_TYPE_STRING);
					point.setPointName(cell1.getStringCellValue());
				}
				XSSFCell cell2 = row.getCell(2);
				if (cell2 != null || "".equals(cell2)) {
					cell2.setCellType(Cell.CELL_TYPE_STRING);
					point.setPointNumber(cell2.getStringCellValue());
				}
				XSSFCell cell3 = row.getCell(3);
				if (cell3 != null || "".equals(cell3)) {
					cell3.setCellType(Cell.CELL_TYPE_STRING);
					point.setPointNaming(cell3.getStringCellValue());
				}
				XSSFCell cell4 = row.getCell(4);
				if (cell4 != null || "".equals(cell4)) {
					cell4.setCellType(Cell.CELL_TYPE_STRING);
					point.setIpAddress(cell4.getStringCellValue());
				}
				XSSFCell cell5 = row.getCell(5);
				if (cell5 != null || "".equals(cell5)) {
					cell5.setCellType(Cell.CELL_TYPE_STRING);
					point.setType(cell5.getStringCellValue());
				}else{
					point.setType("点位");
				}
				XSSFCell cell6 = row.getCell(6);
				if (cell6 != null || "".equals(cell6)) {
					cell6.setCellType(Cell.CELL_TYPE_STRING);
					point.setRtspUrl(cell6.getStringCellValue());
				}
				XSSFCell cell7 = row.getCell(7);

				if (cell7 != null || "".equals(cell7)) {
					cell7.setCellType(Cell.CELL_TYPE_STRING);
					point.setAddress(cell7.getStringCellValue());
				}
				XSSFCell cell8 = row.getCell(8);
				if (cell8 != null || "".equals(cell8)) {
					cell8.setCellType(Cell.CELL_TYPE_STRING);
					for(Area a:areaList){
						if(a.getName().equals(cell8.getStringCellValue().trim())){
							point.setAreaId(a.getId());
							point.setAreaName(a.getName());
							break;
						}
					}
				}else{
					point.setAreaId(areaList.get(0).getId());
					point.setAreaName(areaList.get(0).getName());
				} 
				result.add(point);
			}
		}
		return result;
	}

	private List<Device> getHSSFResult(Workbook wb) {
		// TODO Auto-generated method stub
		List<Device> result = new ArrayList<Device>();
		List<Area> areaList = new ArrayList<Area>();
		areaList = areaService.getAllAreaList();
		for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
			HSSFSheet st = (HSSFSheet) wb.getSheetAt(sheetIndex);
			// 第一行为标题，不取
			for (int rowIndex = 1; rowIndex <= st.getLastRowNum(); rowIndex++) {
				HSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				Device point = new Device();
				HSSFCell cell0 = row.getCell(0); 
				if (cell0 != null || "".equals(cell0)) {
					cell0.setCellType(Cell.CELL_TYPE_STRING);
					point.setPointId(cell0.getStringCellValue());
				}
				HSSFCell cell1 = row.getCell(1);
				if (cell1 != null || "".equals(cell1)) {
					cell1.setCellType(Cell.CELL_TYPE_STRING);
					point.setPointName(cell1.getStringCellValue());
				}
				HSSFCell cell2 = row.getCell(2);
				if (cell2 != null || "".equals(cell2)) {
					cell2.setCellType(Cell.CELL_TYPE_STRING);
					point.setPointNumber(cell2.getStringCellValue());
				}
				HSSFCell cell3 = row.getCell(3);
				if (cell3 != null || "".equals(cell3)) {
					cell3.setCellType(Cell.CELL_TYPE_STRING);
					point.setPointNaming(cell3.getStringCellValue());
				}
				HSSFCell cell4 = row.getCell(4);
				if (cell4 != null || "".equals(cell4)) {
					cell4.setCellType(Cell.CELL_TYPE_STRING);
					point.setIpAddress(cell4.getStringCellValue());
				}
				HSSFCell cell5 = row.getCell(5);
				if (cell5 != null || "".equals(cell5)) {
					cell5.setCellType(Cell.CELL_TYPE_STRING);
					point.setType(cell5.getStringCellValue());
				}else{
					point.setType("B1");
				}
				HSSFCell cell6 = row.getCell(6);
				if (cell6 != null || "".equals(cell6)) {
					cell6.setCellType(Cell.CELL_TYPE_STRING);
					point.setRtspUrl(cell6.getStringCellValue());
				}
				HSSFCell cell7 = row.getCell(7);

				if (cell7 != null || "".equals(cell7)) {
					cell7.setCellType(Cell.CELL_TYPE_STRING);
					point.setAddress(cell7.getStringCellValue());
				}
				HSSFCell cell8 = row.getCell(8);
				if (cell8 != null || "".equals(cell8)) {
					cell8.setCellType(Cell.CELL_TYPE_STRING);
					for(Area a:areaList){
						if(a.getName().equals(cell8.getStringCellValue().trim())){
							point.setAreaId(a.getId());
							point.setAreaName(a.getName());
							break;
						}
					}
				}else{
					point.setAreaId(areaList.get(0).getId());
					point.setAreaName(areaList.get(0).getName());
				} 
				result.add(point);
			}
		}
		return result;
	}
	
	
	/**
	 * 下载导入模板
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
             String filename = str +"_ExceptionInfo.xls";
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
	/**
	 * 下载导入模板
	 * @param request
	 * @param response
	 * @param filepath
	 */
	@ResponseBody
	@RequestMapping(value = "/downfile.do")
	public void downFile(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "filepath", required = true) String filepath) {

		try {
//			String moduleName = new String(filepath.getBytes("iso8859-1"),
//					"utf-8");
//			String filePath = request.getRealPath("/") + moduleName;
			String filePath = request.getRealPath("/") + filepath;
			String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

			response.reset();
			response.setContentType("APPLICATION/OCTET-STREAM; charset=UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=\""
					+ new String(fileName.getBytes("GB2312"), "ISO-8859-1")
					+ "\"");

			FileInputStream inStream = new FileInputStream(filePath);
			byte[] b = new byte[100];
			int len;
			while ((len = inStream.read(b)) > 0) {
				response.getOutputStream().write(b, 0, len);
				// this.getRes().getOutputStream().write(b,0,len);
			}
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载导入模板
	 * @param request
	 * @param response
	 * @param filepath
	 */
	@ResponseBody
	@RequestMapping(value = "/exportExcel.do")
	public JsonResult<DeviceStatus> exportExcel(HttpServletRequest request,
			HttpServletResponse response) {
		JsonResult<DeviceStatus> js = new JsonResult<DeviceStatus>();
		js.setCode(new Integer(1));
		js.setMessage("导出失败!");
		List<DeviceStatus> list = new ArrayList<DeviceStatus>();
		DeviceStatus deviceStatus = new DeviceStatus();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟  
		try {
			deviceStatus.setSearchStatusId(1);
			list = deviceService.getDeviceStatusList(deviceStatus);
			for(DeviceStatus ds :list){
				ds.setRecordTimes(sdf.format(ds.getRecordTime()));
			}
			if(list.size()>0){
				String filePath = createExceptionPoint(list);
				if(!StringUtil.isEmpty(filePath)){
					js.setCode(0);
					js.setGotoUrl(filePath);
					js.setMessage("异常点位信息导出成功");
				}else{
					js.setMessage("创建Excle文件出错!");
				}
			}else{
				js.setMessage("没有异常结果数据!");
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return js;
	}

	private String createExceptionPoint(List<DeviceStatus> list) {
		// TODO Auto-generated method stub
		String filePath ="";
        String filename = "";
        Workbook workbook = null;
        try {
            workbook = new HSSFWorkbook();
            if (workbook != null) { 
                Sheet sheet = workbook.createSheet("异常点位信息");

                Row row0 = sheet.createRow(0);
                Cell cell0_0 = row0.createCell(0, Cell.CELL_TYPE_STRING);
                cell0_0.setCellValue("点位编号");
                sheet.autoSizeColumn(0);
                Cell cell0_1 = row0.createCell(1, Cell.CELL_TYPE_STRING);
                cell0_1.setCellValue("网络连接状态");
                sheet.autoSizeColumn(1);
                Cell cell0_2 = row0.createCell(2, Cell.CELL_TYPE_STRING);
                cell0_2.setCellValue("拉流");
                sheet.autoSizeColumn(2);
                Cell cell0_3 = row0.createCell(3, Cell.CELL_TYPE_STRING);
                cell0_3.setCellValue("雪花噪声");
                sheet.autoSizeColumn(3);
                Cell cell0_4 = row0.createCell(4, Cell.CELL_TYPE_STRING);
                cell0_4.setCellValue("信号缺失");
                sheet.autoSizeColumn(4);	
                Cell cell0_5 = row0.createCell(5, Cell.CELL_TYPE_STRING);
                cell0_5.setCellValue("色彩丢失");
                sheet.autoSizeColumn(5);
                Cell cell0_6 = row0.createCell(6, Cell.CELL_TYPE_STRING);
                cell0_6.setCellValue("画面冻结");
                sheet.autoSizeColumn(6);
                Cell cell0_7 = row0.createCell(7, Cell.CELL_TYPE_STRING);
                cell0_7.setCellValue("画面遮挡");
                sheet.autoSizeColumn(7);
                Cell cell0_8 = row0.createCell(8, Cell.CELL_TYPE_STRING);
                cell0_8.setCellValue("画面模糊");
                sheet.autoSizeColumn(8);
                Cell cell0_9 = row0.createCell(9, Cell.CELL_TYPE_STRING);
                cell0_9.setCellValue("画面移位");
                sheet.autoSizeColumn(9);
                Cell cell0_10 = row0.createCell(10, Cell.CELL_TYPE_STRING);
                cell0_10.setCellValue("画面彩条");
                sheet.autoSizeColumn(10);
                Cell cell0_11 = row0.createCell(11, Cell.CELL_TYPE_STRING);
                cell0_11.setCellValue("画面偏色");
                sheet.autoSizeColumn(11);
                Cell cell0_12 = row0.createCell(12, Cell.CELL_TYPE_STRING);
                cell0_12.setCellValue("亮度异常");
                sheet.autoSizeColumn(12);
                Cell cell0_13 = row0.createCell(13, Cell.CELL_TYPE_STRING);
                cell0_13.setCellValue("亮度异常");
                sheet.autoSizeColumn(13);
                Cell cell0_14 = row0.createCell(14, Cell.CELL_TYPE_STRING);
                cell0_14.setCellValue("黑屏");
                sheet.autoSizeColumn(14);
                Cell cell0_15 = row0.createCell(15, Cell.CELL_TYPE_STRING);
                cell0_15.setCellValue("诊断时间");
                sheet.autoSizeColumn(15);
  

                if(list.size()>0){
                    try {
                        for (int i = 0; i < list.size(); i++) {
                            Row row = sheet.createRow(1 + i);
                            Cell cell1 = row.createCell(0, Cell.CELL_TYPE_STRING);
                            cell1.setCellValue(list.get(i).getPointId());
                            sheet.autoSizeColumn(0);
                            
                             
                            Cell cell2 = row.createCell(1, Cell.CELL_TYPE_STRING);
                            cell2.setCellValue(list.get(i).getNetworkStatus()==3?"√":"×");
                            sheet.autoSizeColumn(1);
                            
                            Cell cell3 = row.createCell(2, Cell.CELL_TYPE_STRING);
                            cell3.setCellValue(list.get(i).getStreamStatus()==3?"√":"×");
                            sheet.autoSizeColumn(2);
                            
                            Cell cell4 = row.createCell(3, Cell.CELL_TYPE_STRING);
                            cell4.setCellValue(list.get(i).getNoiseStatus()==null?"":list.get(i).getNoiseStatus()==3?"√":"×");
                            sheet.autoSizeColumn(3);
                            
                            Cell cell5 = row.createCell(4, Cell.CELL_TYPE_STRING);
                            cell5.setCellValue(list.get(i).getSignStatus()==null?"":list.get(i).getNoiseStatus()==3?"√":"×");
                            sheet.autoSizeColumn(4);
                            
                            Cell cell6 = row.createCell(5, Cell.CELL_TYPE_STRING);
                            cell6.setCellValue(list.get(i).getColorStatus()==null?"":list.get(i).getColorStatus()==3?"√":"×");
                            sheet.autoSizeColumn(5);
                            
                            Cell cell7 = row.createCell(6, Cell.CELL_TYPE_STRING);
                            cell7.setCellValue(list.get(i).getFrameFrozenStatus()==null?"":list.get(i).getFrameFrozenStatus()==3?"√":"×");
                            sheet.autoSizeColumn(6);
                            
                            Cell cell8 = row.createCell(7, Cell.CELL_TYPE_STRING);
                            cell8.setCellValue(list.get(i).getNoiseStatus()==null?"":list.get(i).getNoiseStatus()==3?"√":"×");
                            sheet.autoSizeColumn(7);
                            
                            Cell cell9 = row.createCell(8, Cell.CELL_TYPE_STRING);
                            cell9.setCellValue(list.get(i).getFrameShadeStatus()==null?"":list.get(i).getFrameShadeStatus()==3?"√":"×");
                            sheet.autoSizeColumn(8);
                            
                            Cell cell10 = row.createCell(9, Cell.CELL_TYPE_STRING);
                            cell10.setCellValue(list.get(i).getFrameFuzzyStatus()==null?"":list.get(i).getFrameFuzzyStatus()==3?"√":"×");
                            sheet.autoSizeColumn(9);
                            
                            Cell cell11 = row.createCell(10, Cell.CELL_TYPE_STRING);
                            cell11.setCellValue(list.get(i).getFrameDisplacedStatus()==null?"":list.get(i).getFrameDisplacedStatus()==3?"√":"×");
                            sheet.autoSizeColumn(10);
                            
                            Cell cell12 = row.createCell(11, Cell.CELL_TYPE_STRING);
                            cell12.setCellValue(list.get(i).getFrameStripStatus()==null?"":list.get(i).getFrameStripStatus()==3?"√":"×");
                            sheet.autoSizeColumn(11);
                            
                            Cell cell13 = row.createCell(12, Cell.CELL_TYPE_STRING);
                            cell13.setCellValue(list.get(i).getFrameColorcaseStatus()==null?"":list.get(i).getFrameColorcaseStatus()==3?"√":"×");
                            sheet.autoSizeColumn(12);
                            
                            Cell cell14 = row.createCell(13, Cell.CELL_TYPE_STRING);
                            cell14.setCellValue(list.get(i).getLightExceptionStatus()==null?"":list.get(i).getLightExceptionStatus()==3?"√":"×");
                            sheet.autoSizeColumn(13);
                            
                            Cell cell15 = row.createCell(14, Cell.CELL_TYPE_STRING);
                            cell15.setCellValue(list.get(i).getBlackScreenStatus()==null?"":list.get(i).getBlackScreenStatus()==3?"√":"×");
                            sheet.autoSizeColumn(14);
                            
                            Cell cell16 = row.createCell(15, Cell.CELL_TYPE_STRING);
                            cell16.setCellValue(list.get(i).getRecordTimes());
                            sheet.autoSizeColumn(15);
                             
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
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
                filePath += "/"+ str + "_ExceptionInfo.xls";
                filename = str + "__ExceptionInfo.xls";
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
	
}
