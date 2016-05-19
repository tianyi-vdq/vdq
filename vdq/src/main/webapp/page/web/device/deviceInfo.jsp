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
    
    <title>设备点位信息</title>
    
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
	<script type="text/javascript">
		
		function editProjectPoint() {
			$("#tab1 input[doc='pointInfo']").removeAttr("readonly");
			$("#editBtn").hide();
			$("#saveBtn").show();
		}
		function saveDevicePoint(obj) { 
		if ($('#devicePointForm').form('validate')) {
			$(obj).attr("onclick", ""); 
			showProcess(true, '温馨提示', '正在提交数据...'); 
			$('#devicePointForm').form('submit',{
				success : function(data) {
					showProcess(false);
					data = $.parseJSON(data);
					if (data.code == 0) {
						$.messager.alert('保存信息', data.message, 'info',function() {
							window.location.href ="device/deviceList.do";
						});
						//$("#i_back").click();
					} else {
						$.messager.alert('错误信息', data.message, 'error',function() {});
						$(obj).attr("onclick","saveDevicePoint(this);");
					}
				}
			});
		} 
	} 
	</script>
  </head>
  
  <body>
    <div class="con-right" id="conRight">
		<div class="fl yw-lump">
			<div class="yw-lump-title">
				<i  id="i_back"  class="yw-icon icon-back" onclick="javascript:history.back();"></i><span>设备：${Device.pointName }</span> 
			</div>
		</div>

		<div class="fl yw-lump mt10">
			<div class="yw-bi-rows">
				<div class="yw-bi-tabs mt5" id="ywTabs">
					<span class="yw-bi-now">基本信息</span>
				</div>
				<div class="fr">
					<span class="yw-btn bg-green mr26 hide" id="editBtn" onclick="editProjectPoint();">编辑</span> 
					<span class="yw-btn bg-red mr26" id="saveBtn" onclick="saveDevicePoint(this);">保存</span> 
					<span class="yw-btn bg-green mr26"  onclick="$('#i_back').click();">返回</span>
				</div>
			</div>
			<div>
				<form id="devicePointForm" name="devicePointForm" action="device/jsonSaveOrUpdatePoint.do" method="post">
					<div id="tab1" class="yw-tab">
						<table class="yw-cm-table font16">
							<tr>
								<td align="right">点位id：</td>
								<td><input  name="pointId" doc="pointInfo" onblur="valueTrim(this);"    type="text" value="${Device.pointId}" class="easyui-validatebox" required="true" validType="Length[1,10]" style="width:254px;height:28px;" />
								<span style="color:red">*</span>
								<input  name="id" type="hidden" value="${Device.id}" />
								</td>
								 
								
								<td align="right">点位编号：</td>
								<td><input  name="pointNumber" doc="pointInfo" onblur="valueTrim(this);"    type="text" value="${Device.pointNumber}" class="easyui-validatebox" required="true" validType="Length[1,10]" style="width:254px;height:28px;" />
								<span style="color:red">*</span>
								</td>
							
							</tr>
							<tr>
								<td align="right">点位名称：</td>
								<td><input  name="pointName" doc="pointInfo"  onblur="valueTrim(this);"   type="text" value="${Device.pointName}" class="easyui-validatebox" required="true" validType="Length[1,30]" style="width:254px;height:28px;" />
								<span style="color:red">*</span>
								</td>
								
								<td align="right">点位Naming值：</td>
								<td><input  name="pointNaming" doc="pointInfo"  onblur="valueTrim(this);"   type="text" value="${Device.pointNaming}" class="easyui-validatebox" required="true" validType="Length[1,30]" style="width:254px;height:28px;" />
								<span style="color:red">*</span>
								</td>
							
								
							</tr>
							<tr>
								<td align="right">点位类型：</td>
								<td><input  name="type" doc="pointInfo"  onblur="valueTrim(this);"   type="text" value="${Device.type}" class="easyui-validatebox" required="true" validType="Length[1,50]" style="width:254px;height:28px;" />
								<span style="color:red">*</span>
								</td>
								
								<td align="right">点位地址:</td>
								<td><input  name="address" doc="pointInfo"  type="text" value="${Device.address}" class="easyui-validatebox" required="true" validType="Length[1,50]" style="width:254px;height:28px;" />
								<span style="color:red">*</span>
								</td>
							
							</tr>
							<tr>
								<td align="right">点位IP地址:</td>
								<td><input  name="ipAddress" doc="pointInfo"  type="text" value="${Device.ipAddress}" class="easyui-validatebox" required="true" validType="IP" style="width:254px;height:28px;" />
								<span style="color:red">*</span> 
								</td>
							</tr>
						</table> 
					</div>  
				</form>
			</div>
		</div>
		<div class="cl"></div>
	</div>
	<div class="cl"></div> 
  </body>
</html>