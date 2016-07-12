<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>诊断参数</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--> 
	<script
	src="${pageContext.request.contextPath}/source/js/slider/ion.rangeSlider.js"></script>
	<link
	href="${pageContext.request.contextPath}/source/js/slider/css/ion.rangeSlider.skinNice.css"
	rel="stylesheet" />	<link
	href="${pageContext.request.contextPath}/source/js/slider/css/normalize.min.css"
	rel="stylesheet" /><link
	href="${pageContext.request.contextPath}/source/js/slider/css/ion.rangeSlider.css"
	rel="stylesheet" />
	<script type="text/javascript">
		var str = "";	
		var arry = new Array();
	    $(function(){
	  		var nameStr = $("#nameList").val();
	  		var value1 = $("#value1List").val();
	  		var value2 = $("#value2List").val();
	  		nameStr = nameStr.substring(1,nameStr.length-1);
	  		value1 = value1.substring(1,value1.length-1);
	  		value2 = value2.substring(1,value2.length-1);
	  		 arry = nameStr.split(",");
	  		var v1arry = value1.split(",");
	  		var v2arry = value2.split(",");
		   for(var i= 0;i<arry.length;i++){ 
		   		//把string转换成为number类型
		   		var val1 = parseInt(v1arry[i]);
		   		var val2 = parseInt(v2arry[i]);
			   $("#range_"+$.trim(arry[i])).ionRangeSlider({
					min: 0,
					max: 100,
					from:val1,
					to: val2,			
					type: 'double',//设置类型
					step: 1,
					prefix: "",//设置数值前缀
					postfix: "",//设置数值后缀
					prettify: false,
					hasGrid: false
				}); 
			 }	 
		});
		
	  	function saveDiagnosis(){
	 	   for(var i= 0;i<arry.length;i++){
	  		 	var valueStr = $("#range_"+$.trim(arry[i])).val();
	  		 	str += valueStr+",";
	  			 }
	  		  $.ajax({
					url : "diagnosis/jsonSaveValue.do?values="+str,
					type : "post",
					dataType : "json",
					success : function(data) { 
		  			if(data.code == 0){ 
		  				 $.messager.alert('保存信息',data.message,'info',function(){
	        				history.go(0);
	        			});
		  			}else{
						$.messager.alert('错误信息',data.message,'error');
		  			} 
				}
			}); 
		}
	</script>
  </head>
  
  <body>
    <div class="con-right" id="conRight">
		<div class="fl yw-lump">
			<div class="yw-lump-title">
				<i class="yw-icon icon-partner"></i><span>诊断参数设置</span> 
			</div>
		</div>

		<div class="fl yw-lump mt10">
			<div class="yw-bi-rows">
				<div class="yw-bi-tabs mt5" id="ywTabs">
					<span class="yw-bi-now">诊断参数设置</span>
				</div> 
				<div class="yw-bi-tab mt5" id="ywTabs">
					<input class="yw-btn bg-red mr26" type="button" id="button"  value="保存" name="button" onclick="saveDiagnosis(this);" style="float:right;" />
				</div> 
				<div>
					
				</div>
			</div>
			<div class="fl yw-lump" style="height:600px;">
			 <input type="hidden" id="nameList" value="${namelist}" />
			 <input type="hidden" id="value1List" value="${value1List}"/>
			 <input type="hidden" id="value2List" value="${value2List}"/> 
				<form id="diagnosisForm" name="diagnosisForm" action="#" style="padding-top:20px;" method="POST">
 					<c:forEach var="item" items="${namelist}">
 						<div style="width:100%;text-align:left;">
	 						<div style="width:20%;float:left;text-align:right;margin-right:25px;">
	 							<label>${item}:</label>	
	 						</div>
	 					 	<div style="width:60%;float:left;text-align:left">
	 					 		<input type="text" id="range_${item}"/>
	 					 	</div>
 					 	</div>
 					</c:forEach>
  				</form>
 					
			</div>
		</div>
		<div class="cl"></div>
	</div>
	<div class="cl"></div>
  </body>
</html> 
