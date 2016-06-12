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
<title>设备分组成员</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" />
	<script
	src="/vdq/source/js/pager/jquery.pager.js"></script>
<link
	href="/vdq/source/js/pager/Pager.css"
	rel="stylesheet" /> 
<script type="text/javascript">
$(document).ready(function(){
			$("#pager").pager({
			    pagenumber:'${device.pageNo}',                         /* 表示初始页数 */
			    pagecount:'${device.pageCount}',                      /* 表示总页数 */
			    totalCount:'${device.totalCount}',
			    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
			});
	});
	PageClick = function(pageclickednumber) {
	$("#pager").pager({
	    pagenumber:pageclickednumber,                 /* 表示启示页 */
	    pagecount:'${device.pageCount}',                  /* 表示最大页数pagecount */
	    buttonClickCallback:PageClick                 /* 表示点击页数时的调用的方法就可实现javascript分页功能 */            
	});
	
	$("#pageNumber").val(pageclickednumber);          /* 给pageNumber从新赋值 */
	/* 执行Action */
	pagesearch();
	}
	function pagesearch(){
		memberForm.submit();
		/* var groupId = $("#groupId").text();
		var pageNumber = $("#pageNumber").val();
		search(groupId,pageNumber); */
	}
	/* function search(groupId,Number){
		$.ajax({
		url : "jsonLoadDeviceListById.do?groupId="+groupId+"&&Number="+Number,
		type : "post",  
		dataType:"json",
		success : function(data) {
  			if(data.code == 0){ 
  				 $("#pageNumber").val(1); 
  				 $("#pager").pager({
				    pagenumber:data.obj.pageNo,                         
				    pagecount:data.obj.pageCount,                     
				    totalCount:data.obj.totalCount,
				    buttonClickCallback:PageClick                                       
				});
				$("#groupinfoList").html("");
				fillDeviceList(data.list);
  			}else{
				$.messager.alert('错误信息',data.message,'error');
  			} 
		}
	});
	}; */
	function deleteMemberById(id, groupId) {
		$.messager.confirm("删除确认", "确认删除该成员?", function(r) {
			if (r) {
				$.ajax({
					url : "jsonDeleteMember.do?Id=" + id + "&&groupId="
							+ groupId,
					type : "post",
					dataType : "json",
					success : function(data) {
						if (data.code == 0) {
							$.messager.alert('删除信息', data.message, 'info',
									function() {
										history.go(0);
									});
						} else {
							$.messager.alert('错误信息', data.message, 'error');
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
				<i id="i_back"  class="yw-icon icon-back" onclick="window.location.href='groupList.do'"></i><span>组名：${group.name}</span>
			</div>
		</div>
		<div class="fl yw-lump mt10">
			<form id="memberForm" name="memberForm" action="memberList.do"
				method="get">
				<div class="yw-bi-rows">
					<div class="yw-bi-tabs mt5" id="ywTabs">
						<span class="yw-bi-now"
							onclick="window.location.href='groupInfo.do?groupId=${group.id}'">基本信息</span>
						<span onclick="javaScript:void(0);">分组成员</span>
					</div>
					<div class="fr">
						<span class="yw-btn bg-red mr26" id="saveBtn"
							onclick="window.location.href='editMember.do?groupId=${group.id}'">编辑成员</span>
					</div>
				</div>
				<input type="hidden" id="pageNumber" name="pageNo"
					value="${device.pageNo}" />
				<input type="hidden" id="groupId" name="groupId"
					value="${group.id}" />	
			</form>
		</div>
		<div class="fl yw-lump">
					<table class="yw-cm-table yw-center yw-bg-hover">
					<tr style="background-color:#D6D3D3;font-weight: bold;">
						<th width="4%" style="display:none"></th>
						<th width="4%" style="display:none"></th>
						<th>设备ID</th>
						<th>设备编号</th>
						<th>设备名称</th>
						<th>Naming</th>
						<th>RTSP</th>
						<th>设备类型</th> 
						<th>设备地址</th>
						<th>IP地址</th> 
						<th>所属区域</th> 
						<th>操作</th> 
					</tr>
					<c:forEach var="item" items="${Devicelist}">
						<tr>
							<td width="4%" align="center" style="display:none">${item.id}</td>
							<td width="4%" align="center" style="display:none">${item.groupId}</td>
							<td align="left">${item.pointId}</td>
							<td>${item.pointNumber}</td>
							<td>${item.pointName}</td>
							<td>${item.pointNaming}</td>
							<td>${item.rtspUrl}</td>
							<td>${item.type}</td>
							<td>${item.address}</td>
							<td>${item.ipAddress}</td>
							<td>${item.areaName}</td>
							<td><span class="yw-btn bg-orange cur"
								onclick="deleteMemberById(${item.id},${item.groupId});">×</span></td>
						</tr>
					</c:forEach>
					</table>
					<div class="page" id="pager"></div> 
			</div>	
	</div>
	<div class="easyui-window" style="width:560px;height:580px;overflow:hidden;padding:10px;text-align:center;" iconCls="icon-info" closed="true" modal="true"   resizable="false" collapsible="false" minimizable="false" maximizable="false">
	</div>
</body>
</html>
