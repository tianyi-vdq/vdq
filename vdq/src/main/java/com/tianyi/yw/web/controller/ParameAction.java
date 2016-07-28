package com.tianyi.yw.web.controller;

import java.io.UnsupportedEncodingException; 
import java.util.ArrayList;
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
import com.tianyi.yw.model.Parame;
import com.tianyi.yw.service.ParamService;


@Scope("prototype")
@Controller
@RequestMapping("/parame")
public class ParameAction  extends BaseAction{
	
	@Resource
	private ParamService paramService;
	/**
	 * 视频点位检测参数设置
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/parameList.do", method=RequestMethod.GET)
	public String parameList(Parame parame, 
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		//判断搜索名是否为空，不为空则转为utf-8编码
		if(parame.getSearchName() != null && parame.getSearchName() != ""){
			String searchName = new String(parame.getSearchName().getBytes(
					"iso8859-1"), "utf-8");
			parame.setSearchName(searchName);
		}
		//设置页面初始值及页面大小
		if (parame.getPageNo() == null)
			parame.setPageNo(1);
		parame.setPageSize(Constants.DEFAULT_PAGE_SIZE);  
		int totalCount =  0;
		List<Parame> listParam = new ArrayList<Parame>();
		try{
			//t_parame取满足要求的参数数据
			listParam =  paramService.getParameList(parame);
			//t_parame取满足要求的记录总数
			totalCount = paramService.getParameCount(parame);
		}catch(Exception ex){ 
			ex.printStackTrace();
		}
		//通过request对象传值到前台
		parame.setTotalCount(totalCount); 
		request.setAttribute("Param", parame); 
		request.setAttribute("paramList", listParam); 
		return "web/parame/parameList";
	}
	/**
	 * 新增参数
	 */
	@ResponseBody
	@RequestMapping(value ="/jsonSaveOrUpdateParame.do", method=RequestMethod.POST)
	public JsonResult<Parame> SaveOrUpdateDevice(Parame parame,
			HttpServletRequest request, HttpServletResponse response){
		//新建json对象并赋初值
		JsonResult<Parame> js = new JsonResult<Parame>();
		js.setCode(new Integer(1));
		js.setMessage("保存失败");
		try{
			//如为新增，则给id置0
			if(parame.getId() == null || parame.getId() == 0){
				parame.setId(0);
			}
			//判断参数名是否为空
			if(parame.getName() != null){
				Parame p = new Parame();
				p.setName(parame.getName());
				//如为编辑，则给p对象赋前台传来的参数id值
				if (parame.getId() > 0) {
					p.setId(parame.getId());
				}
				//根据参数名，参数id去t_parame匹配数据，如新增，则只匹配参数名是否重复；如编辑，则可以直接保存
				List<Parame> lc = paramService.getExistParame(p);
				if (lc.size() == 0) {
					paramService.saveOrUpdateParame(parame);
					js.setCode(new Integer(0));
					js.setMessage("保存成功!");
				} else {
					js.setMessage("参数名已存在!");
				}
			} else {
				js.setMessage("参数名字不能为空!");
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return js;
	}
	/**
	 * 新增，编辑参数
	 */
	@RequestMapping(value="/parameInfo.do", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String editParame(
			@RequestParam(value="id",required = false)Integer parameId,
			HttpServletRequest req,HttpServletResponse res){
		//根据参数id判断是新增还是编辑，新增为0，不用传值；编辑为选中的参数id值，取对象，传值
		if(parameId != null && parameId != 0){
			Parame parame = new Parame();
			try{
				parame = paramService.getParameById(parameId);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			req.setAttribute("Parame", parame);
		}
		return "web/parame/parameInfo";
	}
}
