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
    <title>任务管理</title>
    
	<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" /> 
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		 $(document).ready(function(){
		 	var taskId = $("#hid_taskId").val();
		 	if(taskId>0){
		 		var itemTypeId = $("#hid_itemTypeId").val();
		 		var array = itemTypeId.split(",");
		 		$.each(array,function(index,arr){
		 			$("#itemType"+arr).attr("checked","checked");
		 		});
		 	}
		 });
function saveTask(obj){
	if ($('#taskForm').form('validate')) {
		$(obj).attr("onclick", ""); 
		showProcess(true, '温馨提示', '正在提交数据...'); 
		 $('#taskForm').form('submit',{
		  		success:function(data){ 
					showProcess(false);
		  			data = $.parseJSON(data);
		  			if(data.code==0){
	  					$('#taskInfoWindow').window('close');
		  				$.messager.alert('保存信息',data.message,'info',function(){
		  					window.Location.href="task/taskList.do";
	        			});
	  					
		  			}else{
						$.messager.alert('错误信息',data.message,'error',function(){
	        			});
						$(obj).attr("onclick", "saveTask(this);"); 
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

					<i id="i_back" class="yw-icon icon-back" onclick="window.location.href='task/taskList.do'"></i><span>任务列表</span>
			</div>
		</div>
		<div class="fl yw-lump mt10">
			<div class="yw-bi-rows">
				<div class="yw-bi-tabs mt5" id="ywTabs">
					<span class="yw-bi-now" >任务名称：${Task.id == 0?"新建任务":Task.name}</span> <%--  <span onclick="window.location.href='taskList.do?id=${task.id}'?editBtn:">id</span> --%>
				</div>
				<div class="fr">
					<span class="yw-btn bg-green mr26 hide" id="editBtn"  onclick="editTask();">编辑</span>
					<span class="yw-btn bg-red mr26" id="saveBtn" onclick="saveTask(this);">保存</span>
					<span class="yw-btn bg-green mr26"  onclick="$('#i_back').click();">返回</span>
				</div>
			</div>
			
			<div id="tab1" class="yw-tab">
				<form id="taskForm" name="taskForm" action="task/jsonSaveOrUpdateTask.do" method="post">
					<table class="yw-cm-table font16">
						<tr>
							<td width="8%" align="right">任务名称：</td>
							<td><input id="taskName" name="name" type="text" onblur="valueTrim(this);"  doc="taskInfo" value="${Task.name}" class="easyui-validatebox" required="true"  validType="Length[1,50]" style="width:254px;height:28px;"/>
								<%-- <input name="name" type="hidden" doc="taskInfo" value="${task.name}"/> --%>
								<input type="hidden" id="hid_taskId" name="id" doc="taskInfo" value="${Task.id}"/>
								<span style="color:red">*</span>
							</td>
						</tr> 
							
						<tr>
							<td width="10%" align="right">启动时间：<td>
							<input id="startTimes" name="startTimes" data-options="editable:false"  onblur="valueTrim(this);"  doc="taskInfo" type="text" value="${Task.startTimes}" class="easyui-datetimebox" required="true"  style="width:254px;height:28px;"/>
        	                 <%-- <input name="startTimes"   doc="taskInfo" type="hidden" value="${task.startTime}"/> --%>
        	                 <span style="color:red">*</span> 
								
							</td>
						</tr>
						<tr> 
							<td align="right">执行间隔： </td>
							<td><input  id="runIntervals" name="runIntervals" doc="taskInfo" onblur="valueTrim(this);" type="text" value="${Task.runIntervals}" class="easyui-validatebox"  required="true"   validType="number" style="width:254px;height:28px;"/>
        	                <%-- <input name="runTimes" value="${task.runTimes}" type="hidden" /> --%>
        	                <span style="color:red">*</span>
                         	</td>
                         	 </tr>
						<tr>
							<td align="right">运行时间：</td>
							<td><input id="runTimes" name="runTimes" doc="taskInfo" onblur="valueTrim(this);" type="text" value="${Task.runTimes}" class="easyui-validatebox"  required="true"   validType="number" style="width:254px;height:28px;"/>
                           <%--  <input name="runTimes" doc="taskInfo" type="hidden" value="${task.runTimes}"/> --%>
                        	<span style="color:red">*</span> </td>
						</tr>
						<tr>
							<td align="right">运行次数：</td>
							<td><input id="runCount" name="runCount" onblur="valueTrim(this);" doc="taskInfo" type="text" value="${Task.runCount}" class="easyui-validatebox"  required="true"   validType="number" style="width:254px;height:28px;"/>
                            <span style="color:red">*</span>
                        	</td>
                         </tr> 
                         <tr>
							<td align="right">诊断项目：</td>
                         	<td>
								<p class="yw-window-p">
        							<input type="hidden" id="hid_itemTypeId" value="${Task.itemTypeId }" />
	                         		<c:forEach var="item"  items="${TaskItemTypelist}" >
			                    		<label><input id="itemType${item.id}"  type="checkbox" name="itemTypeId"  value="${item.id}" />${item.name}</label> 
			                    	</c:forEach>
		                    	</p>
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
