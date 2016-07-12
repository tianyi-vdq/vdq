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
		 		var allTime = $("#hid_allTimes").val();
		 		var runTimes = $("#hid_runTimes").val();
		 		var time = allTime.split(",");
		 		var array = itemTypeId.split(",");		 		
		 		$.each(time,function(index,arr){
		 			addMoreTime(obj[0],arr);
		 		});
		 		$.each(array,function(index,arr){
		 			$("#itemType"+arr).attr("checked","checked");
		 		});
		 	}
		 	var flag = $("#flag").val();
		 	if(flag == 1){
		 		/* $("#taskTable").attr("readonly",true); */
		 		$("#taskName").attr("disabled",true);
		 		$("#runIntervals").attr("disabled",true);
		 		$("#runTimes").attr("disabled",true);
		 		$("#runCount").attr("disabled",true);
		 		/* $("#digType").onclick = function(){return false;} */
		 	}
		 });
function saveTask(obj){
	if ($('#taskInfoForm').form('validate')) {
		$(obj).attr("onclick", ""); 
		showProcess(true, '温馨提示', '正在提交数据...'); 
		 $('#taskInfoForm').form('submit',{
		  		success:function(data){ 
					showProcess(false);
		  			data = $.parseJSON(data);
		  			if(data.code==0){	  					
		  				$.messager.alert('保存信息',data.message,'info',function(){
		  					window.location.href="task/taskList.do";
	        			});
	  		/* 			success : function(data) {
										showProcess(false);
										data = $.parseJSON(data);
										if (data.code == 0) {
											$.messager.alert('保存信息',data.message,'info',
															function() {
																window.location.href = "device/deviceList.do";
															});
											//$("#i_back").click();	 */
		  			}else{
						$.messager.alert('错误信息',data.message,'error',function(){
	        			});
						$(obj).attr("onclick", "saveTask(this);"); 
		  			}
		  		}
		  	 });  
	}

}   
//新增更多执行时间
	var count = 0;
	function addMoreTime(obj,value){
		count++;
		var str = 
            '<tr>'+
		'<td width="10%" align="right">启动时间：</td>'+
		'<td><input id="startTimes'+count+'" name="aTimes'+count+'" data-options="editable:false"  onblur="valueTrim(this);"  doc="taskInfo" type="text" value="'+value+'" class="easyui-datetimebox" required="true"  style="width:254px;height:28px;"/>'+
	     ' <span style="color:red">*</span>'+
	      '<span id="btnAddStartTime'+count+'" doc="taskInfo" class="yw-btn bg-blue ml60 cur" title="添加新的执行时间" onClick="addMoreTime(this,'');">+添加执行时间</span>'+
		'<span doc="btn_action" class="yw-btn bg-red ml26 cur" onclick="deleteTime('+count+');">删除</span>'+
		'</td></tr>'
				
		/* if(value!=""){
			 str += ' disabled="true" ';
		}  */
		
		/* if(value!=""){
			 str += ' disabled="true" ';
		} */
		str += ' onfocus="openWin('+count+');"  name="areaUserName" class="easyui-validatebox"  style="width:554px;height:28px;" readonly="readonly"  /><span style="color:red">*</span>'+
			'<span doc="btn_action" class="yw-btn bg-red ml26 cur" onclick="deleteArea('+count+');">删除</span>'+
		 '</p>';
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
				<span class="yw-bi-now">基本信息</span>
					 <%-- <span class="yw-bi-now" >任务名称：${Task.id == 0?"新建任务":Task.name}</span> <span onclick="window.location.href='taskList.do?id=${task.id}'?editBtn:">id</span> --%>
				</div>
				<div class="fr">
					<!-- <span class="yw-btn bg-green mr26 hide" id="editBtn"  onclick="editTask();">编辑</span> -->
					<span class="yw-btn bg-red mr26" id="saveBtn" onclick="saveTask(this);">保存</span>
					<span class="yw-btn bg-green mr26"  onclick="$('#i_back').click();">返回</span>
				</div>
			</div>
			
			
				<form id="taskInfoForm" name="taskInfoForm" action="task/jsonSaveOrUpdateTask.do" method="post">
					<div id="tab1" class="yw-tab">
					<table class="yw-cm-table font16" id="taskTable">
						<tr>
							<td width="8%" align="right">任务名称：</td>
							<td><input id="taskName" name="name" type="text" onblur="valueTrim(this);"  doc="taskInfo" value="${Task.name}" class="easyui-validatebox" required="true"  validType="Length[1,50]" style="width:254px;height:28px;"/>
								<%-- <input name="name" type="hidden" doc="taskInfo" value="${task.name}"/> --%>
								<input type="hidden" id="hid_taskId" name="id" doc="taskInfo" value="${Task.id}"/>
								<input type="hidden" id="flag" value="${Task.flag}"/>
								<span style="color:red">*</span>
							</td>
						</tr> 
						
						
						
						
					<%-- 	<div id="tab2" class="yw-bi-rows">
						<div class="yw-bi-tabs mt5">
							<span class="yw-bi-now">项目区域设置</span>
						</div> 
					</div>
					<div id="tab3" class="yw-tab">  
						<p class="hide">
							<input id="hid_areaId" value="${project.areaId}"/> 
							<input id="hid_areaName" value="${project.areaName}"/> 
						</p>
						 <p class="yw-window-p mt30">
						 	<span id="btnAddArea" doc="btn_add_action" class="yw-btn bg-blue ml60 cur" title="添加新的维护区域" onClick="addMoreArea(this,'');">+添加维护区域</span>
						 	
						 <div class="cl"></div>
						 </p>
					</div> --%>
					
					<tr>
						<td width="10%" align="right">启动时间：</td>
						<td><input id="startTimes" name="aTimes" data-options="editable:false"  onblur="valueTrim(this);"  doc="taskInfo" type="text" value="${Task.startTimes}" class="easyui-datetimebox" required="true"  style="width:254px;height:28px;"/>
        	                 <input id="hid_runTimes" name="runTimes" doc="taskInfo"  type="hidden" value="${Task.runTimes}" />
        	                 <input id="hid_allTimes"name="allTimes"   doc="taskInfo" type="hidden" value="${Task.allTimes}"/> 
        	                 <span style="color:red">*</span> 
        	                 <span id="btnAddStartTime" doc="taskInfo" class="yw-btn bg-blue ml60 cur" title="添加新的执行时间" onClick="addMoreTime(this,'');">+添加执行时间</span>
						</td>
						</tr>						
						<%-- <tr>
							<td align="right">执行次数：</td>
							<td><input id="runTimes" name="runTimes" doc="taskInfo" onblur="valueTrim(this);" type="text" value="${Task.runTimes}" class="easyui-validatebox"  required="true"   validType="number" style="width:254px;height:28px;"/>
                            <input name="runTimes" doc="taskInfo" type="hidden" value="${task.runTimes}"/>
                        	<span style="color:red">*</span> </td>
						</tr> --%>
						<%-- <tr>
							<td align="right">运行次数：</td>
							<td><input id="runCount" name="runCount" onblur="valueTrim(this);" doc="taskInfo" type="text" value="${Task.runCount}" class="easyui-validatebox"  required="true"   validType="number" style="width:254px;height:28px;"/>
                            <span style="color:red">*</span>
                        	</td>
                         </tr>  --%>
                         <tr>
							<td align="right">是否执行：</td>
							<td>
								 	 <c:if test="${Task.flag == 1 }">
								 	 	<label><input type="radio" name="flag" value="1" checked="checked" />是</label> 
		 								<label><input type="radio" name="flag" value="0" />否</label> 
								 	 </c:if>  
								 	 <c:if test="${Task.flag == 0 || Task.flag == null}">
								 	 	<label><input type="radio" name="flag" value="1" />是</label> 
		 								<label><input type="radio" name="flag" value="0" checked="checked" />否</label> 
								 	 </c:if> 
							</td>
						</tr>
                         <tr>
							<td align="right">诊断项目：</td>
                         	<td>
								<div class="yw-window-p" id="digType">
        							<input type="hidden" id="hid_itemTypeId" value="${Task.itemTypeId }" />
	                         		<c:forEach var="item"  items="${TaskItemTypelist}" >
			                    		<label><input id="itemType${item.id}"  type="checkbox" name="itemTypeId"  value="${item.id}" />${item.name}</label> 
			                    	</c:forEach>
		                    	</div>
                         	</td> 
                         </tr>           
					</table>  
					</div>
				</form>
			</div> 
		
		<div class="cl"></div>
	</div>
	<div class="cl"></div>
	</div>
</body>
</html>  
