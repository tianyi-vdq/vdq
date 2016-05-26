
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
    <meta charset="utf-8">
    <title>ä»»å¡ä¿¡æ¯</title>
    
	<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" /> 
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		
		function editTask() {
			$("#tab1 input[doc='taskInfo']").removeAttr("readonly");
			$("#editBtn").hide();
			$("#saveBtn").show();
		}
		function saveTask(obj) { 
		if ($('#taskForm').form('validate')) {
			$(obj).attr("onclick", ""); 
			showProcess(true, 'æ¸©é¦¨æç¤º', 'æ­£å¨æäº¤æ°æ®...'); 
			$('#taskForm').form('submit',{
				success : function(data) {
					showProcess(false);
					data = $.parseJSON(data);
					if (data.code == 0) {
						$.messager.alert('ä¿å­ä¿¡æ¯', data.message, 'info',function() {});
						$("#i_back").click();
					} else {
						$.messager.alert('éè¯¯ä¿¡æ¯', data.message, 'error',function() {});
						$(obj).attr("onclick","saveTask(this);");
					}
				}
			});
		} 
	} 
	function showdialog(){
	var wz = getDialogPosition($('#taskInfoWindow').get(0),100);
	$('#taskInfoWindow').window({
		  	top: 100,
		    left: wz[1],
		    onBeforeClose: function () {
		    },
		    onClose:function(){
		    	$('#saveTaskForm .easyui-validatebox').val(''); 
		    }
	});
	$('#taskInfoWindow').window('open');
}

	</script>
  </head> 
  <body>
	<div class="con-right" id="conRight">
		<div class="fl yw-lump">
			<div class="yw-lump-title">

				<i  id="i_back"  class="yw-icon icon-back" onClick="window.location.href='taskList.do'"></i><span>ä»»å¡åç§°ï¼${task.name }</span> 
			</div>
		</div>
		<div class="fl yw-lump mt10">
			<div class="yw-bi-rows">
				<div class="yw-bi-tabs mt5" id="ywTabs">
					<span class="yw-bi-now" onclick="javaScript:void(0);">åºæ¬ä¿¡æ¯</span> <span onclick="window.location.href='taskList.do?id=${task.id}'">ä»»å¡id</span>
				</div>
				<div class="fr">
					<span class="yw-btn bg-green mr26 hide" id="editBtn"  onclick="editTask();">ç¼è¾</span>
					<span class="yw-btn bg-red mr26" id="saveBtn" onclick="saveTask(this);">ä¿å­</span>
								<span class="yw-btn bg-green mr26"  onclick="$('#i_back').click();">è¿å</span>
				</div>
			</div>
			
			<div id="tab1" class="yw-tab">
				<form id="taskForm" name="taskForm"
						action="jsonSaveOrupdateTask.do" method="post">
					<table class="yw-cm-table font16">
						<tr>
							<td width="10%" align="center"> ä»»å¡åç§°ï¼</td>
							<td><input id="taskName" name="name" type="text" onblur="valueTrim(this);"  doc="taskInfo" value="${task.name}" class="easyui-validatebox" required="true"  validType="Length[1,50]" style="width:254px;height:28px;"/>
								<span style="color:red">*</span>
							</td>
						</tr> 
						<tr>
							<td width="10%" align="center">å¯å¨æ¶é´ï¼</td>
							<td>
							<input id="startTimes" name="startTimes" data-options="editable:false"  onblur="valueTrim(this);"  doc="taskInfo" type="text" value="${task.startTime}" class="easyui-datetimebox" required="true"  style="width:254px;height:28px;"/>
        	                     <span style="color:red">*</span> 
								
							</td>
						</tr>
						<tr> 
							<td align="center">æ§è¡é´éï¼</td>
							<td></span><input id="runTimes" name="runTimes" doc="taskInfo" onblur="valueTrim(this);" type="text" value="${task.runTimes}" class="easyui-validatebox"  required="true"   validType="number" style="width:254px;height:28px;"/>
        	<span style="color:red">*</span>
                         	</td>
                         	 </tr>
						<tr>
							<td align="center">æ§è¡æ¬¡æ°ï¼</td>
							<td></span><input id="runTimes" name="runTimes" doc="taskInfo" onblur="valueTrim(this);" type="text" value="${task.runTimes}" class="easyui-validatebox"  required="true"   validType="number" style="width:254px;height:28px;"/>
        	<span style="color:red">*</span> </td>
						</tr>
						<tr>
							<td align="center">å¹¶åè·¯æ°ï¼</td>
							<td><input id="runCount" name="runCount" onblur="valueTrim(this);" doc="taskInfo" type="text" value="${task.runCount}" class="easyui-validatebox"  required="true"   validType="number" style="width:254px;height:28px;"/>
            <span style="color:red">*</span>
            </td>
            </tr>
						<tr style="display:none">
			<td align="center">æ¯å¦ææä»»å¡ï¼</td>
			<td><input id="flag" value="${task.flag}" doc="taskInfo" type="hidden"/>
        	<label><input type="radio" name="flag" value="0" checked="checked" />æ æ</label> 
		 	<label><input type="radio" name="flag" value="1" checked="checked" />ææ</label> 
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
