package com.tianyi.yw.web.controller;

import java.io.UnsupportedEncodingException; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.Constants;
import com.tianyi.yw.model.Area; 
import com.tianyi.yw.model.Device;
import com.tianyi.yw.service.AreaService;
import com.tianyi.yw.service.DeviceService;

@Scope("prototype")
@Controller
@RequestMapping("/area")
public class AreaAction  extends BaseAction {
	@Resource(name = "areaService")
	private AreaService areaService;
	@Resource(name = "deviceService")
	private DeviceService deviceService;
	/**
	 * 区域管理
	 * @param area
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/areaList.do")
	public String areaList(
			Area area,
			@RequestParam(value = "areaId", required = false) Integer areaId,
			@RequestParam(value = "serialCode", required = false) String serialCode,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		//判断搜索栏是否为空，不为空则转为utf-8编码
		if(area.getSearchName() != null && area.getSearchName() != ""){
			String searchName = new String(area.getSearchName().getBytes(
					"iso8859-1"), "utf-8");
			area.setSearchName(searchName);
		}
		//设置页面初始值及页面尺寸
		if (area.getPageNo() == null)
			area.setPageNo(1);
		area.setPageSize(Constants.DEFAULT_PAGE_SIZE); 
		if (areaId != null) {
			area.setId(areaId);
		}
		if (serialCode != null) {
			area.setSerialCode(serialCode);
		}
		List<Area> arealist = new ArrayList<Area>();
		int countTotal = 0;
		try {
			arealist = areaService.getAreaList(area);
			countTotal = areaService.getTotalCount(area);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//遍历取出来的时间数据，并格式化输出
		for (Area p : arealist) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = sdf.format(p.getCreateTime());
			p.setCreatTime(dateString);
		}
		//通过request绑定对象传到前台
		area.setTotalCount(countTotal);
		request.setAttribute("area", area);
		request.setAttribute("arealist",arealist);
		return "web/area/areaList";
	}	
	/**
	 * 区域信息管理：新增、编辑
	 * @param area
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/areainfo.do")
	public String areaInfo(
			@RequestParam(value = "areaId", required = false) Integer areaId,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		//判断是区域新增还是区域编辑，新增，不传值；编辑，传值
		if(areaId != null && areaId != 0){
			Area area = new Area();
			area.setId(areaId);
			try{
				area = areaService.getAreaById(area);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			request.setAttribute("area", area);
		}
		return "web/area/areaInfo";
	}
	/**
	 * 加载公司上级机构，显示所有机构，按照树形结构显示
	 * 
	 * @param company
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonLoadAreaTreeList.do")
	public List<Area> getAreaList(
			@RequestParam(value = "pid", required = false) Integer pid,
			HttpServletRequest request, HttpServletResponse response) {
		Area area = new Area();
		if (pid != null){
			area.setParentId(pid);
		}else {
			area.setParentId(new Integer(0));
		}
		List<Area> list = new ArrayList<Area>();
		list = areaService.getAreaListByParentId(area);
		//加载子节点，方式一，无子节点则无展开按钮
		for(Area a:list){
			a.setText(a.getName());
			Area areaCh = new Area();
			areaCh.setParentId(a.getId());
			List<Area> list1 = new ArrayList<Area>();
			list1 = areaService.getAreaListByParentId(areaCh);
			if(list1.size() > 0){
				a.setState("closed");
			}else{
				a.setChildren(new ArrayList<Area>());
				a.setState("open");
			}
		}
		//加载子节点，方式二，无子节点仍有展开按钮，加载速度快
		/*if(list.size() > 0){
			for(Area a:list){
				a.setText(a.getName());
				a.setState("closed");
			}
		}*/
		return list;// json.toString();
	}
	/**
	 * 新增，编辑区域
	 * @param area
	 * @param req
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonSaveOrUpdateArea.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
	public JsonResult<Area> updateOrInsertArea(Area area,
			HttpServletRequest req,HttpServletResponse res){
		JsonResult<Area> js = new JsonResult<Area>();
		js.setCode(new Integer(1));
		js.setMessage("保存失败！");
		try {
			if (area.getId() == null || area.getId() == 0) {
				area.setId(0);
				String serialCode = getAreaSerialCode(area.getParentId());
				if(serialCode.equals("")){
					js.setMessage("创建区域编码出错!");
					return js;
				}
				area.setSerialCode(serialCode);
			}else{
				//判断编辑时，是否改变了节点
				if(area.getOldParentId()!=area.getParentId()){
					Area temparea = new Area();
					temparea.setParentId(area.getId());
					List<Area> ar = areaService.getAreaListByParentId(temparea);
					if(ar.size()>0){
						js.setMessage("该区域存在子节点，不能迁移至其他节点!");
						return js;
					}else{
						String serialCode = getAreaSerialCode(area.getParentId());
						if(serialCode.equals("")){
							js.setMessage("创建区域编码出错!");
							return js;
						}
						area.setSerialCode(serialCode);
					}
				}
			}
			Area a = new Area();
			a.setName(area.getName());
			if (area.getId() > 0) {
				a.setId(area.getId());
			}
			List<Area> lc = areaService.getExistArea(a);
			if(lc.size()==0){
				a.setFlag(0);
				a.setId(area.getId());
				a.setCreateTime(new Date());
				a.setParentId(area.getParentId());
				a.setSerialCode(area.getSerialCode());
				areaService.updateOrInsert(a);
				js.setCode(new Integer(0));
				js.setMessage("保存成功!");
			}else{
				js.setMessage("区域名称已存在!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return js;
	}
	//创建区域编码
	private String getAreaSerialCode(Integer parentId) {
		// TODO Auto-generated method stub
		String serialCode = "";
		Area a = new Area();
		a.setParentId(parentId);
		List<Area> list = areaService.getAreaListByParentId(a);
		int ltCount = list.size();
		if (parentId == 0) {
			if (ltCount == 0) {
//				serialCode = "0001";
				serialCode = "01";
			} else {
				int maxIntge = list.size() - 1;
				String lastMaxCodeStr = list.get(maxIntge).getSerialCode().trim().replace(",", "");
				int maxCodeInt = Integer.parseInt(lastMaxCodeStr);
				maxCodeInt = maxCodeInt + 1;
				String maxCodeStr = maxCodeInt + "";
				if (maxCodeStr.length() == 1) {
					serialCode = "0" + maxCodeStr;
				} else if (maxCodeStr.length() == 2) {
					serialCode = maxCodeStr;
				/*} else if (maxCodeStr.length() == 3) {
					serialCode = "0" + maxCodeStr;
				} else if (maxCodeStr.length() == 4) {
					serialCode = maxCodeStr;*/
				}
			}
		} else {
			Area a1 = new Area();
			a1 = areaService.getAreaById(parentId);
			if (a1 != null) {
				String parentCode = a1.getSerialCode();
				if (ltCount == 0) {
//					serialCode = parentCode + "0001";
					serialCode = parentCode + "01";
				} else {
					int maxIntge = list.size() - 1;
					String lastMaxCodeStr = list.get(maxIntge).getSerialCode().trim().replace(",", "");
					int lastMaxCodeStrLen = lastMaxCodeStr.length();
					String last4MaxCodeStr = lastMaxCodeStr.substring(lastMaxCodeStr.length()-4);

					String frontMaxCodeStr = lastMaxCodeStr.substring(0,lastMaxCodeStr.length()-4);
					
					String newStr = last4MaxCodeStr.replaceAll("^(0+)", "");
					int maxCodeInt = Integer.parseInt(newStr);
					maxCodeInt = maxCodeInt + 1;
					String maxCodeStr =  maxCodeInt+""; 
					int maxCodeStrLen = frontMaxCodeStr.length() + maxCodeStr.length();
					int diff = lastMaxCodeStrLen - maxCodeStrLen;
					if (diff == 1) {
						serialCode = frontMaxCodeStr + "0" + maxCodeStr;
					} else if (diff == 2) {
						serialCode = frontMaxCodeStr + maxCodeStr;
//						serialCode = frontMaxCodeStr + "00" + maxCodeStr;
//					} else if (diff == 3) {
//						serialCode = frontMaxCodeStr + "000" + maxCodeStr;
					}
				}
			}
		}
		return serialCode;
	}

	/**
	 * 删除区域
	 * 
	 * @param areaId
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonDeleteArea.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
	public JsonResult<Area> deleteAreaById(
			@RequestParam(value = "areaId", required = false) Integer areaId,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<Area> js = new JsonResult<Area>();
		js.setCode(new Integer(1));
		js.setMessage("删除失败!");
		try {
			Area temparea = new Area();
			temparea.setParentId(areaId);
			Device device = new Device();
			device.setAreaId(areaId);
			//根据id查询数据库里面是否有关联的子节点
			List<Area> ar = areaService.getAreaListByParentId(temparea);
			//根据id查询数据里面是否有关联设备
			List<Device> de = deviceService.getAreaListByAreaId(device);
			if(ar.size()>0){
				js.setMessage("该区域存在子节点，不能删除!");
				return js;
			}else if(de.size()>0){
				js.setMessage("该区域存在关联设备，不能删除!");
				return js;
			}else{
				Area a = new Area();
				a.setDeleteTime(new Date());
				a.setId(areaId);
				a.setFlag(1);  //删除成功，标志位置1
				areaService.deleteAreaById(a);
				js.setCode(new Integer(0));
				js.setMessage("删除成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return js;
	}
	
	/**
	 * 根据父级编码获取区域列表
	 * @param serialCode
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonLoadAreaListByCode.do")
	public JsonResult<Area> jsonLoadAreaListByCode(
			@RequestParam(value = "serialCode", required = false) String serialCode,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<Area> js = new JsonResult<Area>();
		js.setCode(new Integer(1));
		js.setMessage("获取数据失败!");
		Area area = new Area();
		if (pageNumber != null && !pageNumber.endsWith("undefined") && pageNumber != ""){
			area.setPageNo(Integer.valueOf(pageNumber));
		}else{
			area.setPageNo(1);
		}
		area.setPageSize(Constants.DEFAULT_PAGE_SIZE); 
		if (serialCode != null) {
			area.setSerialCode(serialCode);
		} 
		try{
			//根据serialCode去数据库里模糊匹配，返回当前节点信息及子节点信息
			List<Area> lc = areaService.getAreaList(area);
			for (Area p : lc) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = sdf.format(p.getCreateTime());
				p.setCreatTime(dateString);
				if(p.getParentName()==null){
					p.setParentName("");
				}
			}
			int totalCount = areaService.getTotalCount(area);
			area.setTotalCount(totalCount);
			request.setAttribute("Area", area);
			js.setObj(area); 
			js.setCode(0);
			js.setList(lc); 
			js.setMessage("获取数据成功!");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return js;
	}
	
}
