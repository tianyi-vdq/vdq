 
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
    
    <title>参数信息</title>
    
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
		
		function editParame() {
			$("#tab1 input[doc='deviceInfo']").removeAttr("readonly");
			$("#editBtn").hide();
			$("#saveBtn").show();
		}
		function saveParame(obj) { 
		if ($('#parameForm').form('validate')) {
			$(obj).attr("onclick", ""); 
			showProcess(true, '参数保存中...'); 
			$('#parameForm').form('submit',{
				success : function(data) {
					showProcess(false);
					data = $.parseJSON(data);
					if (data.code == 0) {
						$.messager.alert('成功信息', data.message, 'info',function() {
							window.location.href ="parame/parameList.do";
						});
					} else {
						$.messager.alert('失败信息', data.message, 'error',function() {});
						$(obj).attr("onclick","saveParame(this);");
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
				<i  id="i_back"  class="yw-icon icon-back" onclick="javascript:history.back();"></i><span>参数名：${Parame.name }</span> 
			</div>
		</div>

		<div class="fl yw-lump mt10">
			<div class="yw-bi-rows">
				<div class="yw-bi-tabs mt5" id="ywTabs">
					<span class="yw-bi-now">参数信息</span>
				</div>
				<div class="fr">
					<span class="yw-btn bg-green mr26 hide" id="editBtn" onclick="editParame();">编辑</span> 
					<span class="yw-btn bg-red mr26" id="saveBtn" onclick="saveParame(this);">保存</span> 
					<span class="yw-btn bg-green mr26"  onclick="$('#i_back').click();">退出</span>
				</div>
			</div>
			<div>
				<form id="parameForm" name="parameForm" action="parame/jsonSaveOrUpdateParame.do" method="post">
					<div id="tab1" class="yw-tab">
						<table class="yw-cm-table font16">
							<tr>
								<td align="right">参数名称</td>
								<td><input  name="name" doc="deviceInfo" onblur="valueTrim(this);"    type="text" value="${Parame.name}" class="easyui-validatebox" required="true" validType="Length[1,10]" style="width:254px;height:28px;" />
								<span style="color:red">*</span>
								<input type="hidden" name="id" value="${Parame.id }" />
								</td>
							</tr>
							<tr>
								<td align="right">参数关键字</td>
								<td><input  name="key" doc="deviceInfo"  onblur="valueTrim(this);"   type="text" value="${Parame.key}" class="easyui-validatebox" required="true" validType="Length[1,30]" style="width:254px;height:28px;" />
								<span style="color:red">*</span></td>
							</tr>
							<tr>
								<td align="right">参数值</td>
								<td><input  name="value" doc="deviceInfo"  onblur="valueTrim(this);"   type="text" value="${Parame.value}" class="easyui-validatebox" required="true" validType="Length[1,50]" style="width:254px;height:28px;" />
								<span style="color:red">*</span></td>
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
