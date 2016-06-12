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
	$("#searchTimes").val(dates);
}    
function exportLog(){
    var filepath = "d:/temp";
	$.messager.confirm("执行确认","确认导出全部日志?导出路径:"+filepath,function(r){  
		    if (r){  
		  //  $.messager.alert('导出开始!');
			$.ajax({
				url : "jsonloadLogExport.do?filepath="+filepath,
				type : "post",  
		    	dataType : "json",								
				success : function(data) { 									
		  			if(data.code == 0){ 
		  				$.messager.alert('导出信息',data.message,'info',function(){ 
		  					search();
		      		});
		  			}else{		  			    
						$.messager.alert('错误信息',data.massage,'error');
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
		<i class="yw-icon icon-partner"></i><span>日志信息</span>		
		<!-- <span class="fr yw-btn bg-orange line-hei22 mr10 mt9 cur" onclick="exportLog();">导出日志</span> -->
	    </div>
	    </div>	 	 	
		  
	
		<div class="fl yw-lump mt10">
			<form id="LogForm" name="LogForm"
				action="logList.do" method="get">
				<div class=pd10>							
		        	<div class="fr">  
					
						<span>日志信息查询：</span><input type="text" name="searchName"   validType="SpecialWord"
						 class="easyui-validatebox" placeholder="搜索" value="${Log.searchName}" type="hidden"/> 
					
						<span>日志时间查询：</span>
						<input id="searchTimes" name="searchTimes" value="${Log.searchTimes}" style="width:180px;height:32px;"
						  data-options="editable:false,onSelect:getSelectDate" type="text"  class="easyui-datebox" /> 
					 
						<span>日志类型查询：</span>
                       <input data-options="editable:false"  value="${Log.searchTypeIds}" type="hidden" /> 
					      <select class="easyui-combobox"  id="searchTypeIds"  name="searchTypeIds" style="width:180px;height:32px;" data-options="editable:false">
		             	<option name="searchTypeIds" value="" >请选择日志类型</option>
		             	<c:forEach var="item" items="${LogTypelist}">	             	
				    	<option value="${item.id}">${item.name}</option>
				        </c:forEach>							
					</select>		
						
						<span class="yw-btn bg-blue ml30 cur" onclick="search();">搜索</span>
					
				     </div>
					<div class="cl"></div>
                <input type="hidden" id="pageNumber" name="pageNo" value="${log.pageNo}" />    
                </div>    
             </form>
             </div>
             
             	
             
				
           <div class="fl yw-lump"> 
				<table class="yw-cm-table yw-center yw-bg-hover">
					<tr style="background-color:#D6D3D3;font-weight: bold;">
						<th style="display:none">&nbsp;</th>
						<th width="20%">日志内容</th>  
						<th width="14%">日志类型</th> 
			    		<th width="14%">日志记录时间</th>
						<th>日志详细描述</th>
						
					</tr>
					<c:forEach var="item" items="${Loglist}">
						<tr>
							<td  style="display:none">${item.id}</td>
							<td>${item.content}</td>  
							<td>${item.typeName}</td> 
						  	<td>${item.createTimes}</td>
							<td>${item.description}</td> 
							
						</tr>
					</c:forEach>
				</table>
				<div class="page" id="pager"></div>
				</div>
			 </div>

      
  </body>
</html>