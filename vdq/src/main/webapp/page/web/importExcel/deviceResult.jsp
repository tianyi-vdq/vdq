<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<meta charset="utf-8"> 
<base href="<%=basePath%>">
<title>设备导入信息</title> 
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" />
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" />  
</head>

<body>

	<div class="con-right" id="conRight">
		<div class="fl yw-lump">
			<div class="yw-lump-title">
				<!-- <i class="yw-icon icon-back" onclick="javascript:history.back();"></i><span>设备导入结果</span> -->
				<i id="i_back" class="yw-icon icon-back" onclick="window.location.href='device/deviceList.do'"></i><span>设备导入结果</span>
				<span class="fr yw-btn bg-green line-hei22 mr10 mt9 cur" onclick="window.location.href='device/deviceList.do'">返回</span>
			</div>
		</div>
		<div class="fl yw-lump mt10">
			<div class="pd10">
				<div class="fl">
					<span class="ml26">设备导入信息</span>
					<span class="ml26">共计导入</span><span>${totalCount}</span><span>条记录，其中导入成功</span><span>${rightCount}</span><span>条记录，</span>
					<span>导入失败</span><span>${subCount}</span><span>条记录，</span><span>未导入数据详细如下：</span>
				</div> 
				<div class="cl"></div>
			</div> 
		</div>
           <div class="fl yw-lump"> 
				<table class="yw-cm-table yw-center yw-bg-hover" id="deviceinfoList">
					<tr>
						<th width="4%" style="display:none">&nbsp;</th>
						<th>设备ID</th>
						<th>设备编号</th>
						<th>设备名称</th>
						<th>设备Naming</th> 
						<th>设备类型</th> 
						<th>设备地址</th>
						<th>Ip地址</th> 
						<th>所属区域</th> 
					</tr>
					<c:forEach var="item" items="${pointlist}">
						<tr>
							<td align="center" style="display:none">${item.id}</td>
							<td align="left">${item.pointId}</td>
							<td>${item.pointNumber}</td>
							<td>${item.pointName}</td>
							<td>${item.pointNaming}</td>
							<td>${item.type}</td>
							<td>${item.address}</td> 
							<td>${item.ipAddress}</td> 
							<td>${item.areaName}</td>
						</tr>
					</c:forEach>
				</table> 
		</div> 
	</div>
</body>
</html>
