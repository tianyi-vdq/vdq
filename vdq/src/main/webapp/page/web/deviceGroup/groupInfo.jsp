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
<title>设备分组管理</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" /> 
<script type="text/javascript">
	$(document).ready(function(){
		var id = $("#groupId").val();
		if(id == 0){
			$("#memberList").attr("onclick", "");
		}
	});
	function saveGroup(obj){
		if ($('#groupForm').form('validate')) {
			$(obj).attr("onclick", ""); 
			showProcess(true, '温馨提示', '正在提交数据...');  
			 $('#groupForm').form('submit',{
			  		success:function(data){ 
						showProcess(false);
			  			data = $.parseJSON(data);
			  			if(data.code==0){
			  				$.messager.alert('保存信息',data.message,'info',function(){ 
								//setShowStates();
								$("#i_back").click();
		        			});
			  			}else{
							$.messager.alert('错误信息',data.message,'error',function(){
								 
		        			});
								$(obj).attr("onclick", "saveCompany(this);"); 
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
				<i id="i_back"  class="yw-icon icon-back" onclick="window.location.href='groupList.do'"></i><span>组名：${group.name}</span>
			</div>
		</div>
		<div class="fl yw-lump mt10">
			<div class="yw-bi-rows">
				<div class="yw-bi-tabs mt5" id="ywTabs">
					<span class="yw-bi-now" onclick="javaScript:void(0);">基本信息</span> <span id="memberList" onclick="window.location.href='memberList.do?groupId=${group.id}'">分组成员</span>
				</div>
				<div class="fr">
					<span class="yw-btn bg-red mr26" id="saveBtn" onclick="saveGroup(this);">保存</span>
					<span class="yw-btn bg-green mr26"  onclick="$('#i_back').click();">返回</span>
				</div>
			</div>
			<div id="tab1" class="yw-tab">
				<form id="groupForm" name="groupForm"
						action="jsonSaveOrupdateGroup.do" method="post">
					<table class="yw-cm-table font16">
						<tr>
							<td width="10%" align="center">分组名称：</td>
							<td><input id="groupName" name="name" type="text" onblur="valueTrim(this);"  doc="companyInfo" value="${group.name}" class="easyui-validatebox" required="true"  validType="Length[1,50]" style="width:254px;height:28px;"/>
								<span style="color:red">*</span>
								<input name="id" type="hidden" id="groupId" value="${group.id}" />
							</td>
						</tr> 
						<tr>
							<td width="10%" align="center">组名描述：</td>
							<td>
								<input id="description" name="description" class="easyui-validatebox" required="true" value="${group.description}" validType="Length[1,50]" style="width:254px;height:32px;" />
							    <span style="color:red">*</span>
							</td>
						</tr>
					</table>
				</form>
			</div> 
		</div>
		<div class="cl"></div>
	</div>
	<div class="cl"></div>
	</div>
</body>
</html>
