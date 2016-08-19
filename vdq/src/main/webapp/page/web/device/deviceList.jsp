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
  
function excelChange(file){
	  if(!(/(?:xls)$/i.test(file.value)) && !(/(?:xlsx)$/i.test(file.value)) ) {
	        $.messager.alert('错误', "只允许上传xl或xlsx的文档", 'error'); 
	        if(window.ActiveXObject) {//for IE
	            file.select();//select the file ,and clear selection
	            document.selection.clear();
	        } else if(window.opera) {//for opera
	            file.type="text";file.type="file";
	        } else file.value="";//for FF,Chrome,Safari
	    } else {	
			showProcess(true, '温馨提示', '正在提交数据...'); 
	   		fileForms.submit();
	    	/* $('#fileForms').form('submit',{
				success : function(data) {
					data = $.parseJSON(data);
					if (data.code == 0) {
						$.messager.alert('保存信息', data.message, 'info',function(){
							search();
						});
						
					} else {
						$.messager.alert('错误信息', data.message, 'error');
					}  
				}
			});	  */
	    }
}
function stopOrStartDevice(id,flag){
	var str = "";
	if(flag == 0){
		str = "确认停用该点位设备?"
	}else{
		str = "确认移除该点位设备?"
	}
	$.messager.confirm("操作确认",str,function(r){  
	    if (r){  
			showDialog(id,flag);
	    }  
	}); 
} 
function showDialog(id,flag){
	$("#hid_form_deviceId").val(id);
	$("#hid_form_flag").val(flag);
	var wz = getDialogPosition($('#descriptionWindow').get(0),150);
	$('#descriptionWindow').window({
		    onBeforeClose: function () {
		    },
		    onClose:function(){
		    	$('#changeCase').val('');
		    }
	});
	$('#descriptionWindow').window('open');
}
function changeStatusAction(){
	
	var id = $("#hid_form_deviceId").val();
	var flag = $("#hid_form_flag").val(); 
	var description = $("#changeCase").val();
	if($.trim(description).length == 0){
		$.messager.alert('操作提示', "请输入变更事由!", 'error');
		return;
	}
	var desc =encodeURI(description);
	$.ajax({
		url:"device/jsonLoadStopOrStartDevice.do?deviceId="+id+"&&flag="+flag+"&&description="+desc,
		type:"post",
		dataType:"json",
		success:function(data) {
			if (data.code == 0) {
				$("#btnCancel").click();
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
</script>
  </head>
  
  <body>
    <div class="con-right" id="conRight">
		<div class="fl yw-lump">
			<div class="yw-lump-title">
				<i class="yw-icon icon-partner"></i><span>设备信息</span>
				<span class="fr yw-btn bg-green line-hei22 mr10 mt9 cur" onclick="window.location.href='${pageContext.request.contextPath}/fileUpload/downfile.do?filepath=source/excel/点位设备导入模板.xls'">下载模板</span> 
				<span class="fr yw-btn bg-orange line-hei22 mr10 mt9 cur">导入设备
					<div class="temp">
					<form id="fileForms" name="fileForms" action="${pageContext.request.contextPath}/fileUpload/uploadDeviceExcel.do"  enctype="multipart/form-data" method="post" style="margin:0;padding:0;">
				       	<input type="file" name="file" id="jfile" class="yw-upload-file" onChange="excelChange(this);">
					</form>
					</div>
				</span>
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
						<input type="hidden" name="flag" value="0"/> 
						<span class="yw-btn bg-blue ml30 cur" onclick="search();">搜索</span>
					</div>
					<div class="fr">
						<span class="fl yw-btn bg-green cur" onclick="window.location.href='device/deviceInfo.do?pointId='+ 0">新建设备</span>
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
						<!-- <th>平台ID</th>			 -->		
						 <th width="10%" >操作</th> 
					</tr>
					<c:forEach var="item" items="${Devicelist}">
						<tr>
							<td align="center" style="display:none">${item.id}</td>
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
							 <td>
							<c:if test="${item.flag == 0}">
								<a href="javascript:void(0);" onclick="stopOrStartDevice(${item.id},${item.flag});" style="color:blue">停用</a>
								<a href="javascript:void(0);" onclick="stopOrStartDevice(${item.id},2);" style="color:red;margin-left:15px;">删除</a>
							</c:if>
							<%-- <c:if test="${item.flag == 1}"> 	 
								<a href="javascript:void(0);" onclick="stopOrStartDevice(${item.id},${item.flag});" style="color:blue">启用设备</a>
							</c:if> --%>
							</td> 
						</tr>
					</c:forEach>
				</table>
				<div class="page" id="pager"></div> 
		</div>	
		</div> 
		
	<div id="descriptionWindow" class="easyui-window" title="操作事由" style="width:560px;height:280px;overflow:hidden;padding:10px;text-align:center;" iconCls="icon-info" closed="true" modal="true"   resizable="false" collapsible="false" minimizable="false" maximizable="false">
	 
		<p style="display:none">
        	<input name="deviceId" type="hidden" id="hid_form_deviceId"  class="easyui-validatebox" />
        	<input name="flag" type="hidden" id="hid_form_flag" class="easyui-validatebox"  />
        </p>
		<p class="yw-window-p" style="text-align:left">
        	<span>变更原因：</span> 
        </p> 
		<p class="yw-window-p"> 
			<textarea id="changeCase" name="description"  style="width:532px;height:125px;"></textarea> 
        </p> 
        <div class="yw-window-footer txt-right">
        	<span id="btnCancel" class="yw-window-btn bg-lightgray mt12"  onclick="$('#changeCase').val('');$('#descriptionWindow').window('close');">取消</span>
        	<span class="yw-window-btn bg-blue mt12" onclick="changeStatusAction(this);">确定</span>
        </div> 
	</div>
  </body>
</html>
