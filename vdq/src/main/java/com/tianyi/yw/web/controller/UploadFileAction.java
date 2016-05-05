package com.tianyi.yw.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 








import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.tianyi.yw.model.Device;

@Scope("prototype")
@Controller
@RequestMapping("/fileUpload")
public class UploadFileAction extends BaseAction {
 

	/**
	 * 导入故障现象
	 * 
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonUploadFaultExcel.do")
	public JsonResult<Device> uploadFaultExcelFile(
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		JsonResult<Device> json = new JsonResult<Device>();
		List<Device> list = new ArrayList<Device>();
		//List<ProjectFault> defaultlist = new ArrayList<ProjectFault>();
		json.setCode(new Integer(1));
		json.setMessage("导入失败!");
		try {

			String path = request.getSession().getServletContext()
					.getRealPath("upload");
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
				file.transferTo(targetFile);
				InputStream stream = new FileInputStream(targetFile.getPath());
				json.setGotoUrl(targetFile.getPath());
				Workbook wb = WorkbookFactory.create(stream);
				try { 
					stream = new FileInputStream(targetFile.getPath()); 
					wb = new XSSFWorkbook(stream);
					list = getXSSFFaultResult(wb);
				} catch (Exception ex) { 
					stream = new FileInputStream(targetFile.getPath()); 
					wb = new HSSFWorkbook(stream);
					list = getHSSFFaultResult(wb);
				}

				/*Workbook rwb = null;
				// 获取Excel文件对象
				rwb = Workbook.getWorkbook(stream);

				// 获取文件的指定工作表 默认的第一个
				Sheet sheet = rwb.getSheet(0);

				// 行数(表头的目录不需要，从1开始)
				for (int i = 1; i < sheet.getRows(); i++) {

					ProjectFault fault = new ProjectFault();
					// 列数 sheet.getColumns();
					Cell cell0 = sheet.getCell(0, i);
					fault.setFault(cell0.getContents());

					Cell cell1 = sheet.getCell(1, i);
					fault.setProjectName(cell1.getContents());

					Cell cell2 = sheet.getCell(2, i);
					fault.setComment(cell2.getContents());

					// 把刚获取的列存入list
					list.add(fault);
				}*/
				// stream.close();
				IOUtils.closeQuietly(stream);
				int len = list.size();
				int count = insertFaultListToDatabase(list);
				if (count > 0) {
					json.setCode(new Integer(0)); 
					json.setMessage("共计 "+len+ "个故障现象，其中  " + count + "  个故障现象导入成功!");
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
		return json;
	}

	private List<Device> getHSSFFaultResult(Workbook wb) {
		// TODO Auto-generated method stubF
		List<Device> result = new ArrayList<Device>(); 
		 for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++)
	        {
			 HSSFSheet st = (HSSFSheet) wb.getSheetAt(sheetIndex);
        	 // 第一行为标题，不取
            for (int rowIndex = 1; rowIndex <= st.getLastRowNum(); rowIndex++)
            {
            	HSSFRow row = st.getRow(rowIndex);
                if (row == null)
                {
                    continue;
                } 
                Device fault = new Device();
				// 列数 sheet.getColumns();
                HSSFCell cell0 = row.getCell(0);
//				if(cell0==null){
//                    continue;
//				}else{
//					fault.setFault(cell0.getStringCellValue());
//				}
//
//				HSSFCell cell1 = row.getCell(1);
//				if(cell1==null){
//                    continue;
//				}else{
//					fault.setProjectName(cell1.getStringCellValue());
//				}
//
//				HSSFCell cell2 = row.getCell(2);
//				if(cell2==null){
//					fault.setComment("");
//				}else{
//					fault.setComment(cell2.getStringCellValue());
//				}

				// 把刚获取的列存入list
				result.add(fault);
            }
        }
        return result;
	}

	private List<Device> getXSSFFaultResult(Workbook wb) {
		// TODO Auto-generated method stub
		List<Device> result = new ArrayList<Device>(); 
        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++)
        {
            XSSFSheet st = (XSSFSheet) wb.getSheetAt(sheetIndex);
            // 第一行为标题，不取
            for (int rowIndex = 1; rowIndex <= st.getLastRowNum(); rowIndex++)
            {
                XSSFRow row = st.getRow(rowIndex);
                if (row == null)
                {
                    continue;
                } 
                Device fault = new Device();
				// 列数 sheet.getColumns();
                XSSFCell cell0 = row.getCell(0);
//				if(cell0==null){
//                    continue;
//				}else{
//					fault.setFault(cell0.getStringCellValue());
//				} 
//
//				XSSFCell cell1 = row.getCell(1);
//				if(cell1==null){
//                    continue;
//				}else{
//					fault.setProjectName(cell1.getStringCellValue());
//				} 
//
//				XSSFCell cell2 = row.getCell(2);
//				if(cell2==null){
//					fault.setComment("");
//				}else{
//					fault.setComment(cell2.getStringCellValue());
//				} 

				// 把刚获取的列存入list
				result.add(fault);
            }
        }
        return result;
	}

	/**
	 * 保存故障现象
	 * 
	 * @param list
	 * @return
	 */
	private int insertFaultListToDatabase(List<Device> list) {
		// TODO Auto-generated method stub
		int count = 0;
//		try {
//			List<Device> deviceList = deviceService
//					.getProjectPageList(new Device());
//			for (Device device : list) {
//				for (Device d : deviceList) {
//					if (device.getProjectName() != null) {
//						if (device.getProjectName().equals(p.getName())) {
//							device.setProjectId(p.getId());
//							break;
//						}
//					} else {
//						break;
//					}
//				}
//				if(fault.getProjectId()==null){
//					continue;
//				}
//				fault.setId(0);
//				fault.setCreatetTime(new Date());
//				fault.setCreateUser(this.getLoginUser().getId());
//				configService.saveOrUpdateProjectFault(fault);
//				count++;
//			}
			return count;
//		} catch (Exception ex) {
///			ex.printStackTrace();
//		}
//		return count;
	}
    

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
}
