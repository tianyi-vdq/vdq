package com.tianyi.yw.web.controller;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.model.DiagnosisItemType;
import com.tianyi.yw.service.DignosisService;

@Scope("prototype")
@Controller
@RequestMapping("/diagnosis")
public class DiagnosisAction extends BaseAction {
	
	@Resource(name = "dignosisService")
	private DignosisService dignosisService;
	
	@RequestMapping(value = "/diagnosisList.do", method=RequestMethod.GET)
	public String initdiagnosisList(
			HttpServletRequest request,HttpServletResponse response){
	
		//创建集合，用于接收诊断表的数据
		List<DiagnosisItemType> dt = new ArrayList<DiagnosisItemType>();
		//创建集合，存储诊断的类型名称
		List<String> nameList = new ArrayList<String>();
		//创建集合,用于接收诊断标准的最小值
		List<Integer> value1List = new ArrayList<Integer>();
		//创建集合,用于接收诊断标准的最大值
		List<Integer> value2List = new ArrayList<Integer>();
		
		try {
			//把后台的数据赋值给DiagnosisItemType
			dt = dignosisService.getDiagnosisList();
			//循环遍历所得数据
			for(DiagnosisItemType d : dt){
				//通过判断把诊断类型名称加入到集合中
				if(d.getItemTypeId() != null){
					nameList.add(d.getItemTypeId().getName());
				}else{
					nameList.add("网路异常");
				}	
				//把诊断标准参考值存入到集合中
				if(d != null){
					value1List.add(d.getValue1());
					value2List.add(d.getValue2());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("list", dt);
		request.setAttribute("namelist", nameList);
		request.setAttribute("value1List", value1List);
		request.setAttribute("value2List", value2List);
		return "web/diagnosis/diagnosisList";
	}
	
	@ResponseBody
	@RequestMapping(value = "/saveValue.do", method=RequestMethod.POST)
	public String saveValues(
			@RequestParam(value = "values", required = false) String values,
			HttpServletRequest request,HttpServletResponse response){

		//将字符传切割成为数组
		String s = values.substring(0, values.lastIndexOf(","));
		System.out.println(s+"--");
		String[] valuesArray = s.split(",");
		//将数组拆分，分别装入到集合中，list1代表数据库中value1的集合，list2代码数据库中value2的集合
		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		DiagnosisItemType diagnosisItemType = new DiagnosisItemType();
		for(int i = 0; i < valuesArray.length; i++){
			String[] str = valuesArray[i].split(";");
			list1.add(Integer.parseInt(str[0]));
			list2.add(Integer.parseInt(str[1]));
			diagnosisItemType.setValue1(list1.get(i));
			diagnosisItemType.setValue2(list2.get(i));
			diagnosisItemType.setId(i+1);
			dignosisService.updateValue(diagnosisItemType);
		}
		return "web/diagnosis/diagnosisList";
	}

}
