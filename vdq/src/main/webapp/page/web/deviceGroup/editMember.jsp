<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>分组成员编辑</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script
	src="${pageContext.request.contextPath}/source/js/pager/jquery.pager.js"></script>
<link
	href="${pageContext.request.contextPath}/source/js/pager/Pager.css"
	rel="stylesheet" />
	<script type="text/javascript">
		$(document).ready(function(){
			/* $("#pager").pager({
			    pagenumber:'${device.pageNo}',                         
			    pagecount:'${device.pageCount}',                     
			    totalCount:'${device.totalCount}',
			    buttonClickCallback:PageClick                                      
			}); */
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
		function pagesearch(){
			getDeviceList();
		}
		function getDeviceList(){
			var groupId = $("#groupId").val();
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
		  				 $("#pager").pager({
						    pagenumber:data.obj.pageNo,                         /* 表示初始页数 */
						    pagecount:data.obj.pageCount,                      /* 表示总页数 */
						    totalCount:data.obj.totalCount,
						    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
						});
						$("#deviceMemberList").html("");
						fillDeviceList(data.list);
		  			}else{
						$.messager.alert('错误信息',data.message,'error');
		  			} 
				}
			});
		}
		function fillDeviceList(lst){
			var html = "<tbody>";
			html += "<tr><th width='4%' style='display:none'>&nbsp;</th><th>选择成员</th><th>设备ID</th><th>设备编号</th><th>设备名称</th><th>Naming</th><th>RTSP</th><th>设备类型</th><th>设备地址</th><th>IP地址</th><th>所属区域</th></tr>";
			for(var i = 0; i<lst.length;i++){
				html += "<tr>";
				html += "<td style='display:none'>"+lst[i].id+"</td><td><input id='checkbox' type='checkbox'/>"+"</td><td align='left'>"+(lst[i].pointId == null ? "" : lst[i].pointId)+"</td><td  align='left'>"+(lst[i].pointNumber == null ? "":lst[i].pointNumber)+"</td>";
				html += "<td>"+(lst[i].pointName == null ? "":lst[i].pointName)+"</td><td>"+(lst[i].pointNaming == null ? "":lst[i].pointNaming)+"</td>"+"<td>"+(lst[i].rtspUrl == null ? "" : lst[i].rtspUrl)+"</td><td>"+(lst[i].type == null ? "" : lst[i].type)+"</td>"+"<td>"+(lst[i].address == null ? "" : lst[i].address)+"</td><td>"+(lst[i].ipAddress == null ? "" : lst[i].ipAddress)+"</td>"+"<td>"+(lst[i].areaName == null ? "" : lst[i].areaName)+"</td>";
				html += "</tr>";
			}
			html += "</tbody>";
			$("#deviceMemberList").html(html);
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
	</script>
  </head>
  
  <body>
    <div class="con-right" id="conRight">
		<div class="fl yw-lump">
			<div class="yw-lump-title">
				<i class="yw-icon icon-partner"></i><span>编辑成员</span>
			</div>
		</div>
		<div class="fl yw-lump mt10">
			<form id="deviceForm" name="deviceForm" action="deviceGroup/jsonLoadDeviceList.do"
					method="get">
				<div class="pd10-28">
					<div class="fl"> 
					</div>
					<div class="fr">
					<span class="yw-btn bg-red mr26" id="saveBtn" onclick="saveGroup();">保存</span>
					<span class="yw-btn bg-green mr26"  onclick="history.back(-1)">返回</span>
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
					<span class="ml26">查询条件</span>
				</div>
				<div class="yw-tree-list" style="height: 639px;">
					<span>区域：</span><ul id="treeList" ></ul>
					<span>设备名称:</span><input id="deviceName" type="text"/><br>
					<span>设备编号:</span><input id="deviceNumber" type="text"/><br>
					<input type="button" value="查询" onclick="getDeviceList();"/>
				</div>
			</div>  
			<div class="yw-lump wid-atuo ml260 mt10">
				<div class="yw-cm-title">
					<span class="ml26">编辑成员</span><input id="groupId" type="hidden" value="${group.id}"/>
				</div>
				<table class="yw-cm-table yw-center yw-bg-hover" style="width:49%;float:left;" id="deviceMemberList">
				<tr style="background-color:#D6D3D3;font-weight: bold;">
							<th width="4%" style="display:none">&nbsp;</th>
							<th>设备ID</th>
							<th>设备编号</th>
							<th>设备名称</th>
							<th>Naming</th>
							<th>RTSP</th>
							<th>IP地址</th>
						</tr>
				</table>
				<table class="yw-cm-table yw-center yw-bg-hover" style="width:49%;float:right;" id="deviceMemberList">
				<tr style="background-color:#D6D3D3;font-weight: bold;">
							<th width="4%" style="display:none">&nbsp;</th>
							<th>设备ID</th>
							<th>设备编号</th>
							<th>设备名称</th>
							<th>Naming</th>
							<th>RTSP</th>
							<th>IP地址</th>
						</tr>
				</table>
				<div class="page" id="pager"></div>
			</div>
		</div>
  </body>
</html>
