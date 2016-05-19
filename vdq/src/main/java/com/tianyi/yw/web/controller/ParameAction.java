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
	
	@Resource(name = "paramService")
	private ParamService paramService;
	/**
	 * 视频点位检测参数设置
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */  
	@RequestMapping(value = "/parameList.do")
	public String parameList( 
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		 
		return "web/parame/parameList";
	}	
	/**
	 * 视频点位检测参数设置
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/parameInfo.do")
	public String paramInfo(  
			Parame parame,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		if (parame.getPageNo() == null)
			parame.setPageNo(1);
		parame.setPageSize(Constants.DEFAULT_PAGE_SIZE);  
		int totalCount =  0;
		List<Parame> listParam = new ArrayList<Parame>();
		try{
			listParam =  paramService.getParameList(parame);
			totalCount = paramService.getParameCount(parame);
		}catch(Exception ex){ 
			ex.printStackTrace();
		}
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
		JsonResult<Parame> js = new JsonResult<Parame>();
		js.setCode(new Integer(1));
		js.setMessage("保存失败");
		try{
			if(parame.getId() == null || parame.getId() == 0){
				parame.setId(0);
			}
			if(parame.getName() != null){
				Parame p = new Parame();
				p.setName(parame.getName());
				if (parame.getId() > 0) {
					p.setId(parame.getId());
				}
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
	 * 编辑参数
	 */
	@RequestMapping(value="/parameInfo.do", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String editParame(
			@RequestParam(value="id",required = false)Integer parameId,
			HttpServletRequest req,HttpServletResponse res){
		Parame parame = new Parame();
		try{
			parame = paramService.getParameById(parameId);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		req.setAttribute("Parame", parame);
		return "web/parame/parameInfo";
	}
}
