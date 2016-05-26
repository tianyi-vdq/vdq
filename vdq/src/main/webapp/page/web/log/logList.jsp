<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8">
<title>日志管理</title>
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
			    pagenumber:'${Log.pageNo}',                         /* 表示初始页数 */
			    pagecount:'${Log.pageCount}',                      /* 表示总页数 */
			    totalCount:'${Log.totalCount}',
			    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
			});
			
		}); 
		
PageClick = function(pageclickednumber) {
	$("#pager").pager({
	    pagenumber:pageclickednumber,                 /* 表示启示页 */
	    pagecount:'${Log.pageCount}',                  /* 表示最大页数pagecount */
	    buttonClickCallback:PageClick                 /* 表示点击页数时的调用的方法就可实现javascript分页功能 */            
	});
	
	$("#pageNumber").val(pageclickednumber);          /* 给pageNumber从新赋值 */
	/* 执行Action */
	pagesearch();
}
function search(){
	$("#pageNumber").val("1");
	pagesearch(); 
} 
function pagesearch(){
	if ($('#LogForm').form('validate')) {
		LogForm.submit();
	}
}
function showdialog(){
	var wz = getDialogPosition($('#logInfoWindow').get(0),100);
	$('#logInfoWindow').window({
		  	top: 100,
		    left: wz[1],
		    onBeforeClose: function () {
		    },
		    onClose:function(){
		    	$('#saveLogForm .easyui-validatebox').val(''); 
		    }
	});
	$('#logInfoWindow').window('open');
}

function getDateModel(date){
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	if(month <10){
		month = "0"+month;
	}
	var day = date.getDate();
	if(day <10){
		day = "0"+day;
	}
	var dates = year+"-"+month+"-"+day;
	return dates;
}

function getSelectDate(date){
	var dates = getDateModel(date);
	alert(dates);
	$("#searchTimes").val(dates);
}
 function exportLog(){
	$.messager.confirm("导出确认","确认导出该任务?",function(r){  
		    if (r){  
		    //$.messager.alert('导出成功!');
		 	$.ajax({
				url : "jsonExportExcel.do",
				type : "post",  
				dataType:"json",
				success : function(data) { 
		  			if(data.code==0){ 
		  				$.messager.alert('导出信息',data.message,'info',function(){ 
		  		//		window.Location.href="log/jsonExportExcel.do";
		  					search();
		      		});
		  			}else{
		  			    
						$.messager.alert('错误信息',data.message,'error');
		  			} 
			}
			});
	    }  
	}); 
} 


</script>
</head>
<body>
	<div class="con-right" id="conRight">
		<div class="fl yw-lump">
			<div class="yw-lump-title">
				<i class="yw-icon icon-partner" style="left"></i><span>日志信息</span> 
		        	<span class="yw-btn bg-blue ml30 cur"  onclick="exportLog()">导出日志</span> 			 	
		  </div>
		  
		</div>
		
		
		
		<div class="fl yw-lump mt10">
			<form id="LogForm" name="LogForm"
				action="logList.do" method="get">				
			<div class="fr">  
					
						<span>日志信息查询：</span><input type="text" name="searchName"   validType="SpecialWord"
						 class="easyui-validatebox" placeholder="搜索" value="${Log.searchName}" type="hidden"/> 
					
						<span>日志时间查询：</span>
						<input id="searchTimes" name="searchTimes" value="${Log.searchTimes}" type="hidden" />
						 <input data-options="editable:false,onSelect:getSelectDate" type="text"  class="easyui-datebox" 
					    	 style="width:254px;height:28px;"/> 
					 
						<span class="fl wid82">日志类型查询：</span>
                       <input data-options="editable:false"  value="${Log.searchTypeIds}" type="hidden" /> 
					      <select class="easyui-combobox" style="height:28px;width:254px;" id="searchTypeIds"  name="searchTypeIds"  data-options="editable:false">
		             	<option name="searchTypeIds" value="">请选择日志类型</option>
		             	<c:forEach var="item" items="${LogTypelist}">	             	
				    	<option value="${item.id}">${item.name}</option>
				        </c:forEach>							
					</select>				
						<span class="yw-btn bg-blue ml30 cur" onclick="search();">搜索</span>
				     </div>
					<div class="cl"></div>
                <input type="hidden" id="pageNumber" name="pageNo" value="${log.pageNo}" />        
             </form>
             </div>
             
             	
             
				
           <div class="fl yw-lump"> 
				<table class="yw-cm-table yw-center yw-bg-hover">
					<tr style="background-color:#D6D3D3;font-weight: bold;">
						<th style="display:none">&nbsp;</th>
						<th>日志内容</th>  
						<th>日志类型</th> 
			    		<th>日志记录时间</th>
						<th>日志详细描述</th>
						
					</tr>
					<c:forEach var="item" items="${Loglist}">
						<tr>
							<td  style="display:none">${item.id}</td>
							<td>${item.content}</td>  
							<td>${item.type}</td> 
						  	<td>${item.createTimes}</td>
							<td>${item.description}</td> 
							
						</tr>
					</c:forEach>
				</table>
				<div class="page" id="pager"></div>
				</div>
				 </div>
	<%-- <div id="logInfoWindow" class="easyui-window" title="新建日志" style="width:560px;height:430px;overflow:hidden;padding:10px;"
   iconCls="icon-info" closed="true" modal="true" resizable="false" collapsible="false" minimizable="false" maximizable="false">
		<form id="saveLogForm" name ="saveLogForm" action="jsonSaveOrUpdateLog.do"  method="post">
		<p style="display:none">
        	<span class="fl ml80 wid102 line-hei30">id：</span><input name="id"  value="0" class="easyui-validatebox"/>
        </p>
        <p class="yw-window-p">
        	<span class="fl ml80 wid102 line-hei30"> 日志内容：</span><input name="content"  type="text" value="" class="easyui-validatebox" required="true"  style="width:254px;height:28px;"/>
        	<span style="color:red">*</span> 
        </p> 
       
        <p class="yw-window-p">
        	<span class="fl ml80 wid102 line-hei30">日志类型选择：</span>
                   <input data-options="editable:false" name="typeId" value="" type="hidden"  class="easyui-validatebox"  required="true" 
					     style="width:254px;height:28px;"/>
                   <select class="easyui-combobox" style="height:28px;width:254px;" id="typeId"  name="typeId"  data-options="editable:false">
		             	<option name="typeId" value="">请选择日志类型</option>
		<!-- 		    	<option value="1">网络</option>
				    	<option value="2">视屏捕获</option>
				    	<option value="3">视屏解析</option>
				    	<option value="4">视屏诊断</option>
				    	<option value="5">推送</option>
				    	<option value="6">服务器监控</option>
				    	<option value="7">光收发器监控</option>		
		 -->
		             	<c:forEach var="item" items="${LogTypelist}">		             	
				    	<option value="${item.id}">${item.name}</option>
				        </c:forEach>								
					</select>
            <span style="color:red">*</span>      
        </p>
         <p class="yw-window-p">
        	<span class="fl ml80 wid102 line-hei30">日志详细描述：</span><input name="description"  type="text" value="" class="easyui-validatebox" required="false" validType="Length[1,100]" style="width:254px;height:28px;"/>
        	
        </p> 
           <div class="yw-window-footer txt-right">
        	<span id="btnCancel" class="yw-window-btn bg-lightgray mt12"  onclick="$('#saveLogForm .easyui-validatebox').val('');$('#logInfoWindow').window('close');">退出</span>
        	<span class="yw-window-btn bg-blue mt12" onClick="savelog(this);">保存</span>
        </div>
        </form>
      </div>  --%>
      
  </body>
</html>