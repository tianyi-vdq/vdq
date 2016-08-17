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
    <title>设备管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script
	src="${pageContext.request.contextPath}/source/js/pager/jquery.pager.js"></script>
	<link
	href="${pageContext.request.contextPath}/source/js/pager/Pager.css"
	rel="stylesheet" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		$(document).ready(function(){
			$("#pager").pager({
			    pagenumber:'${Device.pageNo}',                         /* 表示初始页数 */
			    pagecount:'${Device.pageCount}',                      /* 表示总页数 */
			    totalCount:'${Device.totalCount}',
			    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
			});  
			/* $("#deviceList tr").each(function(i){
				if(i>0){
					$(this).bind("click",function(){
						var pointId = $(this).find("td").first().text();
						 window.location.href="device/deviceInfo.do?pointId="+pointId;
					});
				}
			}); */
			  
		});
		
PageClick = function(pageclickednumber) {
	$("#pager").pager({
	    pagenumber:pageclickednumber,                 /* 表示启示页 */
	    pagecount:'${Device.pageCount}',                  /* 表示最大页数pagecount */
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
	if ($('#DeviceForm').form('validate')) {
		DeviceForm.submit();
	}  
}
 
function stopOrStartDevice(id,flag){
	$.messager.confirm("操作确认","确认恢复该点位设备到使用状态?",function(r){  
		    if (r){  
				$.ajax({
					url:"device/jsonLoadStopOrStartDevice.do?deviceId="+id+"&&flag="+flag,
					type:"post",
					dataType:"json",
					success:function(data) {
						if (data.code == 0) {
							$.messager.alert('成功信息', data.message, 'info',function(){
								location.reload(true);
							});
						} else {
							$.messager.alert('错误信息', data.message, 'error');
						}
					},
					error:function(XMLResponse){alert(XMLResponse.responseText)}
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
				<i class="yw-icon icon-partner"></i><span>设备信息</span> 
			</div>
		</div>
		
						
		<div class="fl yw-lump mt10">
			<form id="DeviceForm" name="DeviceForm"
				action="device/deviceList.do" method="get">
				<div class="pd10">
					<div class="fl">
						<span class="ml26">设备信息</span> 
						<input type="text" name="searchName"   validType="SpecialWord" class="easyui-validatebox" 
							placeholder="搜索" value="${Device.searchName}" /> 
						<input type="hidden" name="flag" value="1"/> 
						<span class="yw-btn bg-blue ml30 cur" onclick="search();">搜索</span>
					</div> 
					<div class="cl"></div>
				</div>

				<input type="hidden" id="pageNumber" name="pageNo"
					value="${Device.pageNo}" />
			</form>
		</div>
           <div class="fl yw-lump"> 
				<table class="yw-cm-table yw-center yw-bg-hover" id="deviceList">
					<tr style="background-color:#D6D3D3;font-weight: bold;">
						<th width="4%" style="display:none">&nbsp;</th>
						<th width="10%" >状态</th>
						<th width="10%" >设备名称</th>
						<th width="10%" >设备ID</th>
						<th width="5%" >设备类型</th>  
						<th width="10%" >设备编号</th>
						<th>Naming</th>
						<!-- <th>RTSP</th> -->
						<th width="5%" >IP</th>  
						<th width="5%" >端口</th> 
						<th width="10%" >设备地址</th>
						<th width="10%" >所属区域</th>	
						<th width="10%" >停用日期</th>	
						<th width="10%" >停用原因</th>	
						<!-- <th>平台ID</th>			 -->		
						 <th width="10%" >操作</th> 
					</tr>
					<c:forEach var="item" items="${Devicelist}">
						<tr>
							<td align="center" style="display:none">${item.id}</td>
							<td onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'">已停用</td>
							<td onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'">${item.pointName}</td>
							<td align="left" onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'" >${item.pointId}</td>
							<td onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'">${item.type}</td> 
							<td onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'">${item.pointNumber}</td>
							<td onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'">${item.pointNaming}</td>
							<%-- <td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.rtspUrl}</td> --%>
							<td onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'">${item.ipAddress}</td> 
							<td onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'">${item.port}</td> 
							<td onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'">${item.address}</td> 
							<td onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'">${item.areaName}</td>
							<%-- <td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.platformId}</td> --%>
							  
							<td onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'">${item.lockTimes}</td> 
							<td onclick="window.location.href='<%=basePath%>device/deviceInfo.do?pointId=${item.id}'">${item.description}</td> 
							<td>
								<a href="javascript:void(0);" onclick="stopOrStartDevice(${item.id},${item.flag});" style="color:blue">启用设备</a> 
							</td> 
						</tr>
					</c:forEach>
				</table> 
				<div class="page" id="pager"></div> 
		</div>	
		</div> 
  </body>
</html>
