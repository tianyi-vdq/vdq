<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
<meta charset="utf-8">
<title>设备诊断历史</title> 
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" />
<script
	src="${pageContext.request.contextPath}/source/js/pager/jquery.pager.js"></script>
<link
	href="${pageContext.request.contextPath}/source/js/pager/Pager.css"
	rel="stylesheet" />
	<script type="text/javascript">
		$(document).ready(function(){
			$("#pager").pager({
			    pagenumber:'${StatisticResult.pageNo}',                         /* 表示初始页数 */
			    pagecount:'${StatisticResult.pageCount}',                      /* 表示总页数 */
			    totalCount:'${StatisticResult.totalCount}',
			    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
			});
			var hid_searchMonthId = $("#hid_searchMonthId").val();
			if(hid_searchMonthId !=""){
				$("#MonthId").combobox("setValue",hid_searchMonthId);
			} 
			 var sch_stime = $("#hid_sch_starttime").val();
            if($.trim(sch_stime).length>0){
                $("#dbx_startTime").datebox("setValue",sch_stime);
            }
            var sch_etime = $("#hid_sch_endtime").val();
            if($.trim(sch_etime).length>0){
                $("#dbx_endTime").datebox("setValue",sch_etime);
            }
		}); 

PageClick = function(pageclickednumber) {
	$("#pager").pager({
	    pagenumber:pageclickednumber,                 /* 表示启示页 */
	    pagecount:'${StatisticResult.pageCount}',                  /* 表示最大页数pagecount */
	    buttonClickCallback:PageClick                 /* 表示点击页数时的调用的方法就可实现javascript分页功能 */            
	});
	
	$("#pageNumber").val(pageclickednumber);          /* 给pageNumber从新赋值 */
	/* 执行Action */
	pagesearch();
}
function search(){
	$("#pageNumber").val("1"); 
	var searchMonthId =  $.trim($("#hid_searchMonthId").val());
	if(searchMonthId.length == 0){
		var schBeginTime = $.trim($("#hid_sch_starttime").val());
		var schEndTime = $.trim($("#hid_sch_endtime").val());
		if(schBeginTime.length >0 ){
			if(schEndTime.length == 0){
				$.messager.alert('操作提示', "请选择结束时间", 'info'); 
				return; 
			}
		}else if(schEndTime.length >0 ){
			if(schBeginTime.length == 0){
				$.messager.alert('操作提示', "请选择开始时间", 'info');
				return;
			}
		}
	}
	pagesearch();
}

function pagesearch(){
	if ($('#DeviceStatusForm').form('validate')) { 
		DeviceStatusForm.submit();
	}  
}
function reset(){  
	$("#MonthId").combobox("setValue",""); 
	$("#dbx_startTime").datebox("setValue",""); 
	$("#dbx_endTime").datebox("setValue","");   
	$("#hid_searchMonthId").val("");
	$("#hid_sch_starttime").val("");
	$("#hid_sch_endtime").val("");
}
 function loadExceptionInfo(){ 
 	$.messager.confirm("操作提示","因数据量较大 ,导出数据建议在未进行诊断任务时操作 , 以防占用过多资源影响诊断质量!<br />是否立即执行导出?",function(r){  
		    if (r){  
			 	var jsonData ={}; 
			 	jsonData.searchMonthId = $("#hid_searchMonthId").val();
			 	jsonData.schBeginTime = $("#hid_sch_starttime").val(); 
			 	jsonData.schEndTime = $("#hid_sch_endtime").val();
				showProcess(true, '温馨提示', '正在导出数据,预计花费时间5--8分钟,请稍后...'); 
			          $.ajax({
			              url : "<%=basePath %>statistic/exportExcel.do",
			              type : "post",
			              dataType:"json", 
			              data: jsonData,
			              //async:false,
			              success : function(data) { 
							showProcess(false); 
			                  if(data.code == 0){
			                      $.messager.alert('获取信息', data.message, 'info',function() {
			                          window.location.href="<%=basePath %>statistic/downExceptionfile.do";
			                      });
			                  }else{
			                      $.messager.alert('操作信息', data.message, 'error');
			                  }
			              }
			          }); 
    }});
 }
</script>
  </head>  
 <body>
   <div class="con-right" id="conRight">
		<div class="fl yw-lump">
			<div class="yw-lump-title">
				<i class="yw-icon icon-partner"></i><span>设备检测统计信息</span>								
			</div>
		</div>
		<div class="fl yw-lump mt10">
		<form id="DeviceStatusForm" name="DeviceStatusForm"
				action="<%=basePath %>statistic/faultStatistic.do" method="get">
		<div class="pd10"> 
		      <div class="fl">	 
		       <span>选择月份：</span>
		<input type="hidden" name="searchMonthId" id="hid_searchMonthId" value="${StatisticResult.searchMonthId}" />
		  <select class="easyui-combobox"  id="MonthId" style="width:80px;height:32px;" data-options="editable:false,onSelect:function(record){$('#hid_searchMonthId').val(record.value);}">
			    <option value="" >月份</option>	              	
				<option value="1">一月</option>	               	 
				<option value="2">二月</option>	  
				<option value="3">三月</option>	   
				<option value="4">四月</option>	  
				<option value="5">五月</option>	   
				<option value="6">六月</option>	   
				<option value="7">七月</option>	  
				<option value="8">八月</option>	  
				<option value="9">九月</option>	  
				<option value="10">十月</option>	  
				<option value="11">十一月</option>	   
				<option value="12">十二月</option>	       							
		</select> 	 
			<span>开始时间: </span>
	        <input id="hid_sch_starttime" type="hidden"  name="schBeginTime" value="${StatisticResult.schBeginTime}"  />
	        <input id="dbx_startTime"  style="width:100px;height:32px;" data-options="editable:false,onSelect:function(date){$('#hid_sch_starttime').val($('#dbx_startTime').datebox('getValue'));}" type="text"  class="easyui-datebox" />
	        <span>结束时间: </span>
	        <input id="hid_sch_endtime" type="hidden"  name="schEndTime"  value="${StatisticResult.schEndTime}"/>
	        <input id="dbx_endTime" style="width:100px;height:32px;" data-options="editable:false,onSelect:function(date){$('#hid_sch_endtime').val($('#dbx_endTime').datebox('getValue'));}" type="text"  class="easyui-datebox" />
			 <span class="yw-btn bg-blue ml30 cur" onclick="search();">搜索</span>
			 <span class="yw-btn bg-red ml10 cur" onclick="reset();">清空</span> 
		</div>			
			 <div class="fr">
			 <span class="yw-btn bg-green ml20 cur" onclick="loadExceptionInfo();">导出Excel</span>  </div> 
			 <div class="cl"></div>						
          <input type="hidden" id="pageNumber" name="pageNo" value="${StatisticResult.pageNo}" />
      </div>
		</form>
      </div>
		
   <div class="fl yw-lump">           
   <table class="yw-cm-table yw-center yw-bg-hover" id="deviceStatusList">
		<tr style="background-color:#D6D3D3;font-weight: bold;"> 
			<th width="8%">编号</th>  
			<th width="8%">Ip地址</th>   
			<c:forEach var="item" items="${DateStrList }"> 
				<th>${item }日</th>  
			</c:forEach>
		</tr>  
		<c:forEach var="item" items="${StatisticResultlist}">
			<tr> 
				 <td>${item.deviceNumber }</td> 
				 <td>${item.ipAddress }</td> 
				 <c:forEach var="value" items="${item.valueList}">
				 	<c:if test="${value == '0'}">
				 		<td><img src="${pageContext.request.contextPath}/source/images/exception.png"/></td>
				 	</c:if> 
				 	<c:if test="${value == '1'}">
				 		<td><img src="${pageContext.request.contextPath}/source/images/good.png"/></td>
				 	</c:if>
				 	<c:if test="${value == '2'}">
				 		<td><img src="${pageContext.request.contextPath}/source/images/good.png"/></td>
				 	</c:if> 
				 </c:forEach>
			</tr>  
		</c:forEach>
	</table> 
		<div class="page" id="pager"></div> 
</div>	 
</div>
  </body>
</html>
