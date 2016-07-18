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
		 		var array = itemTypeId.split(",");
		 		$.each(array,function(index,arr){
		 			$("#itemType"+arr).attr("checked","checked");
		 		}); 
		 		showOtherTime();
		 	}	
		 	var flag = $("#flag").val();
		 	if(flag == 1){ 
		 		$("#taskName").attr("disabled",true);
		 		$("#ftimes").attr("disabled",true); 
		 	}
		 });
		 var arry = [];
		 function showOtherTime(){
		 	var stStr = $("#hid_timestr").val();
		 	if(stStr.length>0){
		 		if(stStr.endsWith(',')){
		 			stStr = stStr.substring(0,stStr.length-4);
		 		}
		 		var stArry = stStr.split(',');
		 		$.each(stArry,function(index,item){
		 			$("#ltimes"+(index+1)).timespinner("setValue", item);  
		 			$("#tr"+(index+1)).removeAttr("style");
		 		}); 
		 	}
		 }   
		 function test(taskId){
		 	var count = $("#hid_timeCount").val(); 
		 	if(taskId >0){
	 			if(count<5){ 
			 		$("#tr"+count).removeAttr("style");
	 				count++;
			 		$("#hid_timeCount").val(count);
		 		}else{
		 			$.messager.alert("操作提示","其他执行时间最多定义5个","error"); 
		 		}
		 	}else{
		 		if(count<5){ 
	 				count++;
			 		$("#tr"+count).removeAttr("style");
			 		$("#hid_timeCount").val(count);
		 		}else{
		 			$.messager.alert("操作提示","其他执行时间最多定义5个","error"); 
		 		}
		 	} 
		 	setCountArry(count); 
		 }
		 function setCountArry(obj){
		 	if(obj == 1){
		 		arry = [1];
		 	}else if(obj ==2){
		 		arry = [1,2];
		 	}else if(obj ==3){
		 		arry = [1,2,3];
		 	}else if(obj ==4){
		 		arry = [1,2,3,4];
		 	}else if(obj ==5){
		 		arry = [1,2,3,4,5];
		 	}
		 }
		 function checkSelect(){
		 	var st1h = $("#ftimes").timespinner("getHours");
		 	var st1m = $("#ftimes").timespinner("getMinutes");
		 	
		 		var st2h = $("#ltimes1").timespinner("getHours");
		 		var st2m = $("#ltimes1").timespinner("getMinutes");
		 		if(!isNaN(st2h)){ 
					if(st1h>st2h || (st1h==st2h&&st1m>=st2m)){
						$.messager.alert("操作提示","执行时间1不能小于等于首次执行时间","error");
						return false;
					} 
				}
				var st3h = $("#ltimes2").timespinner("getHours");
		 		var st3m = $("#ltimes2").timespinner("getMinutes");
		 		if(!isNaN(st3h)){ 
					if(st2h>st3h || (st2h==st3h&&st2m>=st3m)){
						$.messager.alert("操作提示","执行时间2不能小于执行时间1","error");
						return false;
					} 
				}
				var st4h = $("#ltimes3").timespinner("getHours");
		 		var st4m = $("#ltimes3").timespinner("getMinutes");
		 		if(!isNaN(st4h)){ 
					if(st3h>st4h || (st3h==st4h&&st3m>=st4m)){
						$.messager.alert("操作提示","执行时间3不能小于执行时间2","error");
						return false;
					} 
				}
				var st5h = $("#ltimes4").timespinner("getHours");
		 		var st5m = $("#ltimes4").timespinner("getMinutes");
		 		if(!isNaN(st5h)){ 
					if(st4h>st5h || (st4h==st5h&&st4m>=st5m)){
						$.messager.alert("操作提示","执行时间4不能小于执行时间3","error");
						return false;
					} 
				}
		  
		 	return true;
		 }
		function saveTask(obj){ 
			var taskName = $("#taskName").val();
			if($.trim(taskName).length == 0){
				$.messager.alert("操作提示","任务名称不能为空!","error");
				return;
			}
			if(!checkSelect()){
				return;
			}
			if ($('#taskInfoForm').form('validate')) {  
				$(obj).attr("onclick", ""); 
				//showProcess(true, '温馨提示', '正在提交数据...'); 
				 $('#taskInfoForm').form('submit',{
				  		success:function(data){ 
							showProcess(false);
				  			data = $.parseJSON(data);
				  			if(data.code==0){	  					
				  				$.messager.alert('保存信息',data.message,'info',function(){
				  					window.location.href="task/taskList.do";
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
				<span class="yw-bi-now">基本信息</span>
					 <%-- <span class="yw-bi-now" >任务名称：${Task.id == 0?"新建任务":Task.name}</span> <span onclick="window.location.href='taskList.do?id=${task.id}'?editBtn:">id</span> --%>
				</div>
				<div class="fr">
					<!-- <span class="yw-btn bg-green mr26 hide" id="editBtn"  onclick="editTask();">编辑</span> -->
					
					<span class="yw-btn bg-red" style="margin-left: 10px;" id="saveBtn" onclick="saveTask(this);">保存</span>
					<span class="yw-btn bg-green" style="margin-left: 10px;margin-right: 10px;" onclick="$('#i_back').click();">返回</span>
				</div>
			</div>
				<form id="taskInfoForm" name="taskInfoForm" action="task/jsonSaveOrUpdateTask.do" method="post">
					<div id="tab1" class="yw-tab">
					<table class="yw-cm-table font16" id="taskTable">
						<tr>
							<td width="8%" align="right">任务名称：</td>
							<td> 
								<input id="taskName" name="name" type="text" onblur="valueTrim(this);"  doc="taskInfo" value="${Task.name}" required="true"  validType="Length[1,50]" style="width:254px;height:28px;"/>
								<input type="hidden" id="hid_taskId" name="id" doc="taskInfo" value="${Task.id}"/>
								<input type="hidden" id="flag" value="${Task.flag}"/>
								<input type="hidden" id="hid_timeCount" value="${TimeListCount }" />
								<input type="hidden" id="hid_timestr" value="${Task.startedTimes }" />
								<span style="color:red">*</span>
							</td>
							<td width="8%"></td>
						</tr> 
						
						<tr>
							<td width="8%" align="right">首次执行时间：</td>
							<td><input id="ftimes" name="firstTimes" data-options="require:true"  onblur="valueTrim(this);"  doc="taskInfo" type="text" value="${Task.firstTimes}" class="easyui-timespinner"  style="width:254px;height:28px;"/>							
								<span style="color:red">*</span>
								<span class="yw-btn bg-green" style="margin-left: 10px;" id="saveBtn" onclick="test(${Task.id});">+</span>
							</td>
							<td width="8%"></td>
						</tr> 
					   <%-- <c:if test="${Task.id >0 }">
							<c:forEach  var="item" items="${taskTimelist}"  varStatus="status">
								<tr id="tr${status.index + 1}">
									<td width="8%" align="right">执行时间：</td>
									<td>
										<input id="ltimes" name="lastTimes" data-options="require:false"    doc="taskInfo" type="text" value="${item.startTimes}" class="easyui-timespinner"  style="width:254px;height:28px;"/>	  
									</td>
									<td width="8%"></td>
								</tr> 
							</c:forEach> 
						</c:if> --%> 
						<tr id="tr1" style="display:none">
							<td width="8%" align="right">执行时间1：</td>
							<td>
								<input id="ltimes1" name="lastTimes" data-options="require:false"    doc="taskInfo" type="text" class="easyui-timespinner"  style="width:254px;height:28px;"/>	 
						  	</td>
							<td width="8%"></td>
						</tr>  
						<tr id="tr2" style="display:none">
							<td width="8%" align="right">执行时间2：</td>
							<td>
								<input id="ltimes2" name="lastTimes" data-options="require:false"    doc="taskInfo" type="text" class="easyui-timespinner"  style="width:254px;height:28px;"/>	 
							</td>
							<td width="8%"></td>
						</tr>  
						<tr id="tr3" style="display:none">
							<td width="8%" align="right">执行时间3：</td>
							<td>
								<input id="ltimes3" name="lastTimes" data-options="require:false"    doc="taskInfo" type="text" class="easyui-timespinner"  style="width:254px;height:28px;"/>	 
							</td>
							<td width="8%"></td>
						</tr>  
						<tr id="tr4" style="display:none">
							<td width="8%" align="right">执行时间4：</td>
							<td>
								<input id="ltimes4" name="lastTimes" data-options="require:false"    doc="taskInfo" type="text" class="easyui-timespinner"  style="width:254px;height:28px;"/>	 
							</td>
							<td width="8%"></td>
						</tr>    
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
							<td width="8%"></td>
						</tr>
                         <tr>
							<td align="right">诊断项目：</td>
                         	<td>
								<div class="yw-window-p" id="digType">
        							<input type="hidden" id="hid_itemTypeId" value="${Task.itemTypeId }" />
	                         		 <c:forEach var="item"  items="${TaskItemTypelist}" varStatus="status"  >
			                    		<label style="padding:3px;"><input id="itemType${item.id}"  type="checkbox" name="itemTypeId"  value="${item.id}" />${item.name}</label> 
			                    		<c:if test="${status.index ==11 }">
			                    			<br />
			                    		</c:if>
			                    	</c:forEach>
		                    	</div>
                         	</td> 
							<td width="8%"></td>
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
