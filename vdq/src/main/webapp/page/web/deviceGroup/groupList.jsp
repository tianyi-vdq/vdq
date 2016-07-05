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
<title>设备分组</title> 
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" />
<script
	src="${pageContext.request.contextPath}/source/js/pager/jquery.pager.js"></script>
<link
	href="${pageContext.request.contextPath}/source/js/pager/Pager.css"
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
		var groupId = $("#groupId").text();
		var pageNumber = $("#pageNumber").val();
		doClick(groupId,pageNumber);
	}
	var dcTime=250;       // doubleclick time 
	function handleWisely(which,id) {
		switch (which) {
		case "click":
			setTimeout(doClick(id), dcTime);
			break;
		case "dblclick":
			doDoubleClick(id);
			break;
		default:
		}
	}

	function doClick(id) {
	$.ajax({
		url : "jsonLoadDeviceListById.do?groupId="+id,
		type : "post",  
		dataType:"json",
		success : function(data) {
  			if(data.code == 0){ 
  				 $("#pageNumber").val(1); 
  				 $("#pager").pager({
				    pagenumber:data.obj.pageNo,                         /* 表示初始页数 */
				    pagecount:data.obj.pageCount,                      /* 表示总页数 */
				    totalCount:data.obj.totalCount,
				    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
				});
				$("#groupinfoList").html("");
				fillDeviceList(data.list);
  			}else{
				$.messager.alert('错误信息',data.message,'error');
  			} 
		}
	});
	}
	function doClick(groupId,Number){
		$.ajax({
		url : "jsonLoadDeviceListById.do?groupId="+groupId+"&&Number="+Number,
		type : "post",  
		dataType:"json",
		success : function(data) {
  			if(data.code == 0){ 
  				 $("#pageNumber").val(1); 
  				 $("#pager").pager({
				    pagenumber:data.obj.pageNo,                         /* 表示初始页数 */
				    pagecount:data.obj.pageCount,                      /* 表示总页数 */
				    totalCount:data.obj.totalCount,
				    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
				});
				$("#groupinfoList").html("");
				fillDeviceList(data.list);
  			}else{
				$.messager.alert('错误信息',data.message,'error');
  			} 
		}
	});
	};
	function fillDeviceList(lst){
			var html = "<tbody>";
			html += "<tr><th width='4%' style='display:none'>&nbsp;</th><th>设备ID</th><th>设备编号</th><th>设备名称</th><th>Naming</th><th>RTSP</th><th>设备类型</th><th>设备地址</th><th>IP地址</th><th>所属区域</th><th>操作</th></tr>";
			for(var i = 0; i<lst.length;i++){
				html += "<tr>";
				html += "<td id='groupId' style='display:none'>"+lst[i].groupId+"</td><td style='display:none'>"+lst[i].id+"</td>"+"<td align='left'>"+(lst[i].pointId == null ? "" : lst[i].pointId)+"</td><td  align='left'>"+(lst[i].pointNumber == null ? "":lst[i].pointNumber)+"</td>";
				html += "<td>"+(lst[i].pointName == null ? "":lst[i].pointName)+"</td><td>"+(lst[i].pointNaming == null ? "":lst[i].pointNaming)+"</td>"+"<td>"+(lst[i].rtspUrl == null ? "" : lst[i].rtspUrl)+"</td><td>"+(lst[i].type == null ? "" : lst[i].type)+"</td>"+"<td>"+(lst[i].address == null ? "" : lst[i].address)+"</td><td>"+(lst[i].ipAddress == null ? "" : lst[i].ipAddress)+"</td>"+"<td>"+(lst[i].areaName == null ? "" : lst[i].areaName)+"</td>";
				html += "<td><span class='yw-btn bg-orange cur'	onclick='deleteMemberById("+lst[i].id+","+lst[i].groupId+");'>×</span></td></tr>";
			}
			html += "</tbody>";
			$("#groupinfoList").html(html);
		}

	function doDoubleClick(id) {
		window.location.href="groupInfo.do?groupId="+id;
	}
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
										doClick(groupId); 
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
				<i class="yw-icon icon-partner"></i><span>设备分组</span>
			</div>
		</div>
		<div class="fl yw-lump mt10">
		<form id="deviceForm" name="deviceForm"
				action="groupList.do" method="get">
			<div class="pd10-28">
				<div class="fl"></div>
				<div class="fr">
					<span class="fl yw-btn bg-green cur" onclick="window.location.href='groupInfo.do?groupId='+ 0">新建分组</span>
				</div>
				<div class="cl"></div>
			</div>
			<input type="hidden" id="pageNumber" name="pageNo"
					value="${device.pageNo}" />
		</form>
		</div>
		<div class="fl">
			<div class="fl yw-lump wid250 mt10">
				<div class="yw-cm-title">
					<span class="ml26">设备分组</span>
				</div>
				<div class="yw-tree-list" style="height: 639px;">
					<table class="yw-cm-table yw-center yw-bg-hover" id="groupName">
						<c:forEach var="item" items="${groupName}">
							<tr style="font-weight: bold;">
								<td width="0%" align="center" style="display:none">${item.id}</td>
								<td id="name" width="100%" align="center"
									onclick=" handleWisely(event.type,'${item.id}')" 
									ondblclick=" handleWisely(event.type,'${item.id}')" >${item.name}</td>
								<td  width="0%" align="center" style="display:none">${item.description}</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
			<div class="yw-lump wid-atuo ml260 mt10">
				<div class="yw-cm-title">
					<span class="ml26">分组成员</span>
				</div>
				<table class="yw-cm-table yw-center yw-bg-hover" id="groupinfoList">
					<tr style="background-color:#D6D3D3;font-weight: bold;">
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
							<td align="center" style="display:none">${item.id}</td>
							<td align="left">${item.pointId}</td>
							<td>${item.pointNumber}</td>
							<td>${item.pointName}</td>
							<td>${item.pointNaming}</td>
							<td>${item.rtspUrl}</td>
							<td>${item.type}</td>
							<td>${item.address}</td>
							<td>${item.ipAddress}</td>
							<td>${item.areaName}</td>
							<td><a href="javascript:void(0);" onclick="deleteMemberById(${item.id},${item.groupId});">删除</a></td>
						</tr>
					</c:forEach>
				</table>
				<div class="page" id="pager"></div>
			</div>
		</div>
 	  
</body>
</html>
