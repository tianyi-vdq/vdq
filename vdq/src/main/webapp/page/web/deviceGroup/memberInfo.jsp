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
<base href="<%=basePath%>">
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
			$("#pager1").pager({
			    pagenumber:'${Device.pageNo}',                         /* 表示初始页数 */
			    pagecount:'${Device.pageCount}',                      /* 表示总页数 */
			    totalCount:'${Device.totalCount}',
			    buttonClickCallback:PageClick1                     /* 表示点击分页数按钮调用的方法 */                  
			});
			$("#treeList").tree({
				 url: 'area/jsonLoadAreaTreeList.do',
				 checkbox:true,
				 onBeforeExpand:function(node){
   				 	$('#treeList').tree('options').url = 'area/jsonLoadAreaTreeList.do?pid='+ node.id;
   				 }
				 /* onLoadSuccess:function(){
   				    var aId = $.trim($("#hid_areaId").val());
   				 	if(aId.length>0){
   				 		var node = $("#treeList").tree("find",aId); 
						$('#treeList').tree("select", node.target);
   				 	}  
   				 } */
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
	PageClick1 = function(pageclickednumber) {
		$("#pager1").pager({
		    pagenumber:pageclickednumber,                 /* 表示启示页 */
		    pagecount:'${Device.pageCount}',                  /* 表示最大页数pagecount */
		    buttonClickCallback:PageClick                 /* 表示点击页数时的调用的方法就可实现javascript分页功能 */            
		});
		
		$("#pageNum").val(pageclickednumber);          /* 给pageNumber从新赋值 */
		/* 执行Action */
		pagesearch1();
	}
	function pagesearch(){
		getDeviceList();
	}
	function pagesearch1(){
		getDeviceList1();
	}
	/* function deleteMemberById(id, groupId) {
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
	} */
	function getDeviceList(){
			var pageNumber = $("#pageNumber").val();
			var groupId = $("#groupId").val();
			$.ajax({
				url : "deviceGroup/jsonLoadExistDeviceList.do?pageNumber="+pageNumber+"&&groupId="+groupId,
				type : "post",  
				dataType:"json",
				success : function(data) { 
		  			if(data.code == 0){ 
		  				 $("#pager").pager({
						    pagenumber:data.obj.pageNo,                         /* 表示初始页数 */
						    pagecount:data.obj.pageCount,                      /* 表示总页数 */
						    totalCount:data.obj.totalCount,
						    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
						});
						$("#existDeviceList").html("");
						fillDeviceList(data.list);
		  			}else{
						$.messager.alert('错误信息',data.message,'error');
		  			} 
				}
			});
		}
		function fillDeviceList(lst){
			var html = "<tbody>";
			html += "<tr><th width='4%' style='display:none'>&nbsp;</th><th>设备ID</th><th>设备编号</th><th>设备名称</th><th>IP地址</th><th>所属区域</th></tr>";
			for(var i = 0; i<lst.length;i++){
				html += "<tr>";
				html += "<td style='display:none'>"+lst[i].id+"</td>"+"<td align='left'>"+(lst[i].pointId == null ? "" : lst[i].pointId)+"</td><td  align='left'>"+(lst[i].pointNumber == null ? "":lst[i].pointNumber)+"</td>";
				html += "<td>"+(lst[i].pointName == null ? "":lst[i].pointName)+"</td>"+"<td>"+(lst[i].ipAddress == null ? "" : lst[i].ipAddress)+"</td>"+"<td>"+(lst[i].areaName == null ? "" : lst[i].areaName)+"</td>";
				html += "</tr>";
			}
			html += "</tbody>";
			$("#existDeviceList").html(html);
		}
		function getDeviceList1(){
			var pageNumber = $("#pageNum").val();
			$.ajax({
				url : "deviceGroup/jsonLoadAllDeviceList.do?pageNumber="+pageNumber,
				type : "post",  
				dataType:"json",
				success : function(data) { 
		  			if(data.code == 0){ 
		  				 $("#pager1").pager({
						    pagenumber:data.obj.pageNo,                         /* 表示初始页数 */
						    pagecount:data.obj.pageCount,                      /* 表示总页数 */
						    totalCount:data.obj.totalCount,
						    buttonClickCallback:PageClick1                     /* 表示点击分页数按钮调用的方法 */                  
						});
						$("#allDeviceList").html("");
						fillDeviceList1(data.list);
		  			}else{
						$.messager.alert('错误信息',data.message,'error');
		  			} 
				}
			});
		}
		function fillDeviceList1(lst){
			var html = "<tbody>";
			html += "<tr><th width='4%' style='display:none'>&nbsp;</th><th>选择成员</th><th>设备ID</th><th>设备编号</th><th>设备名称</th><th>IP地址</th><th>所属区域</th></tr>";
			for(var i = 0; i<lst.length;i++){
				html += "<tr>";
				html += "<td style='display:none'>"+lst[i].id+"</td><td><input id='checkbox' type='checkbox'/></td>"+"<td align='left'>"+(lst[i].pointId == null ? "" : lst[i].pointId)+"</td><td  align='left'>"+(lst[i].pointNumber == null ? "":lst[i].pointNumber)+"</td>";
				html += "<td>"+(lst[i].pointName == null ? "":lst[i].pointName)+"</td>"+"<td>"+(lst[i].ipAddress == null ? "" : lst[i].ipAddress)+"</td>"+"<td>"+(lst[i].areaName == null ? "" : lst[i].areaName)+"</td>";
				html += "</tr>";
			}
			html += "</tbody>";
			$("#allDeviceList").html(html);
		}
		function saveGroup(){
			 var groupId = $("#groupId").val();
			 var deviceId = "";
			 $("input:checked").each(function(){ 
			   deviceId += $(this).parents("tr").find("td:first").text()+",";
			 });
			 $.ajax({
				url : "deviceGroup/jsonSaveMember.do?groupId="+groupId+"&&deviceId="+deviceId,
				type : "post",  
				dataType:"json",
				success : function(data) { 
		  			if(data.code == 0){ 
		  				$.messager.alert('保存信息',data.message,'info',function(){
		  					window.location.href = "deviceGroup/memberList.do?groupId="+groupId;
	        			});
		  			}else{
						$.messager.alert('错误信息',data.message,'error');
		  			} 
				}
			});
		}
		function getDeviceListByConditions(){
			var areaIds = $("#treeList").tree("getChecked");
			var s = "";
			for(var i=0;i<areaIds.length;i++){
				s += areaIds[i].id+",";
			}
			var deviceName = $("#deviceName").val();
			var deviceNumber = $("#deviceNumber").val();
			var pageNumber = $("#pageNumber").val();
			$.ajax({
				url : "deviceGroup/jsonLoadDeviceList.do?areaIds="+s+"&&deviceName="+deviceName+"&&deviceNumber="+deviceNumber+"&&pageNumber="+pageNumber,
				type : "post",  
				dataType:"json",
				success : function(data) { 
		  			if(data.code == 0){ 
		  				 $("#pager1").pager({
						    pagenumber:data.obj.pageNo,                         /* 表示初始页数 */
						    pagecount:data.obj.pageCount,                      /* 表示总页数 */
						    totalCount:data.obj.totalCount,
						    buttonClickCallback:PageClick1                     /* 表示点击分页数按钮调用的方法 */                  
						});
						$("#allDeviceList").html("");
						fillDeviceList1(data.list);
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
				<i id="i_back"  class="yw-icon icon-back" onclick="window.location.href='deviceGroup/groupList.do'"></i><span>组名：${group.name}</span>
			</div>
		</div>
		<div class="fl yw-lump mt10">
			<form id="memberForm" name="memberForm" action="memberList.do"
				method="get">
				<div class="yw-bi-rows">
					<div class="yw-bi-tabs mt5" id="ywTabs">
						<span onclick="window.location.href='deviceGroup/groupInfo.do?groupId=${group.id}'">基本信息</span>
						<span class="yw-bi-now" onclick="javaScript:void(0);">分组成员</span>
					</div>
					<div class="fr">
						<span class="yw-btn bg-red mr26" id="saveBtn" onclick="saveGroup();">保存</span>
					</div>
				</div>
				<input type="hidden" id="groupId" name="groupId"
					value="${group.id}" />	
			</form>
		</div>
		<div class="fl">
			<div class="fl yw-lump wid250 mt10">
				<div class="yw-cm-title">
					<span class="ml26">查询条件</span>
				</div>
				<div class="yw-tree-list" style="height: 639px;">
					<span>区域：</span><ul id="treeList" ></ul>
					<span>设备名称:</span><input id="deviceName" type="text"/><br>
					<span>设备编号:</span><input id="deviceNumber" type="text"/><br>
					<input type="button" value="查询" onclick="getDeviceListByConditions();"/>
				</div>
			</div>
			<div class="yw-lump wid-atuo ml260 mt10">
				<table id="allDeviceList" class="yw-cm-table yw-center yw-bg-hover" style="width:49%;float:left;">
					<tr style="background-color:#D6D3D3;font-weight: bold;">
						<th width="4%" style="display:none"></th>
						<th>操作</th>
						<th>设备ID</th>
						<th>设备编号</th>
						<th>设备名称</th>
						<th>IP地址</th> 
						<th>所属区域</th> 
					</tr>
					<c:forEach var="item" items="${DeviceAlllist}">
						<tr>
							<td width="4%" align="center" style="display:none">${item.id}</td>
							<td><input id='checkbox' type='checkbox'/></td>
							<td align="left">${item.pointId}</td>
							<td>${item.pointNumber}</td>
							<td>${item.pointName}</td>
							<td>${item.ipAddress}</td>
							<td>${item.areaName}</td>
						</tr>
					</c:forEach>
				</table>
				<input type="hidden" id="pageNum" name="pageNum"/>	
				<div class="page" id="pager1"></div>
			</div>
			<div class="yw-lump wid-atuo ml260 mt10">
				<table id = "existDeviceList" class="yw-cm-table yw-center yw-bg-hover" style="width:49%;float:right;">
					<tr style="background-color:#D6D3D3;font-weight: bold;">
						<th width="4%" style="display:none"></th>
						<th width="4%" style="display:none"></th>
						<th>设备ID</th>
						<th>设备编号</th>
						<th>设备名称</th>
						<th>IP地址</th> 
						<th>所属区域</th> 
					</tr>
					<c:forEach var="item" items="${Devicelist}">
						<tr>
							<td width="4%" align="center" style="display:none">${item.id}</td>
							<td width="4%" align="center" style="display:none">${group.id}</td>
							<td align="left">${item.pointId}</td>
							<td>${item.pointNumber}</td>
							<td>${item.pointName}</td>
							<td>${item.ipAddress}</td>
							<td>${item.areaName}</td>
						</tr>
					</c:forEach>
				</table>
				<input type="hidden" id="pageNumber" name="pageNo"
					value="${device.pageNo}" />
				<div class="page" id="pager"></div>
			</div>
		</div>
	</div>	
</body>
</html>
