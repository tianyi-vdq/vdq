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
		 	/* var timeSize = $("#timeList").val();
		 	var array = new Array();
		 	if(timeSize != null && timeSize != "" && timeSize != undefined){
		 		timeSize = timeSize.substring(1,timeSize.length-1);
				array = timeSize.split(",");
				for(var i = 0;i < array.length;i++){
					var val = array[i];
					var newDate = new Date(val);
					var date = formatDate(newDate,"yyyy-MM-dd hh:mm:ss")
					alert("#time_"+$.trim(array[i]));
					$("#time_"+$.trim(array[i])).datetimebox({
			            required: true,
			            value: date,
			        });
				}
		 	}else{
		 		alert(2);
		 	} */
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
		 var count=1;
		  function addMoreTime(){
		  	var targetObj = $("#startTimeList").append("<input data-options='editable:false,onchange:function(){ $(" + "\"#" + count + "\").val(this.value) }'  onblur='valueTrim(this);'  doc='taskInfo' type='text' class='easyui-datetimebox' required='true'  style='width:254px;height:28px;'/>")
		  	.append("<input type='hidden' id='"+count+"'/>")
		  	.append("<span style='color:red'>*</span> <br>");
		  	$.parser.parse(targetObj);
		  	count++;
		  }
		function saveTask(obj){
			var startTimes = ""; //连接开始时间字符串
			var array = new Array();
			var timeSize = $("#timeList").val();
			timeSize = timeSize.substring(1,timeSize.length-1);
			array = timeSize.split(",");
			for(var i = 0;i < array.length;i++){
				var conn = $("#time_"+array[i]).datetimebox('getValue');
				startTimes+=conn+",";
			}
			alert(startTimes);
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
				  			}else{
								$.messager.alert('错误信息',data.message,'error',function(){
			        			});
								$(obj).attr("onclick", "saveTask(this);"); 
				  			}
				  		}
				  	 });
			}
		}
		//格式化日期,
      function formatDate(date,format){
        var paddNum = function(num){
          num += "";
          return num.replace(/^(\d)$/,"0$1");
        }
        //指定格式字符
        var cfg = {
           yyyy : date.getFullYear() //年 : 4位
          ,yy : date.getFullYear().toString().substring(2)//年 : 2位
          ,M  : date.getMonth() + 1  //月 : 如果1位的时候不补0
          ,MM : paddNum(date.getMonth() + 1) //月 : 如果1位的时候补0
          ,d  : date.getDate()   //日 : 如果1位的时候不补0
          ,dd : paddNum(date.getDate())//日 : 如果1位的时候补0
          ,hh : date.getHours()  //时
          ,mm : date.getMinutes() //分
          ,ss : date.getSeconds() //秒
        }
        format || (format = "yyyy-MM-dd hh:mm:ss");
        return format.replace(/([a-z])(\1)*/ig,function(m){return cfg[m];});
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
					<span id="btnAddStartTime" doc="taskInfo"  class="yw-btn bg-blue ml60 cur" onclick="addMoreTime();">添加执行时间</span>
					<span class="yw-btn bg-red" style="margin-left: 10px;" id="saveBtn" onclick="saveTask(this);">保存</span>
					<span class="yw-btn bg-green" style="margin-left: 10px;margin-right: 10px;" onclick="$('#i_back').click();">返回</span>
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
								<input type="hidden" id="timeList" value="${timeList}"/>
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
						<td id="startTimeList">
							<c:if test="${empty timeList}">
								<input type="hidden" id="emptyTime" style="width:254px;height:28px;"/>
								<input data-options="editable:false,onChange:function(value){ $('#emptyTime').val(value) }"  onblur="valueTrim(this);"  doc="taskInfo" type="text" class="easyui-datetimebox" required="true"  style="width:254px;height:28px;"/>
	        	                <span style="color:red">*</span> 
							</c:if>
							<c:if test="${!empty timeList}">
								<c:forEach var="item" items="${timeList }">
									<%-- <input id="time_${item}" type="text"  style="width:254px;height:28px;"/> --%>
									<input data-options="editable:false,onChange:function(value){ $('#time_${item}').val(value) }"  onblur="valueTrim(this);"  doc="taskInfo" type="text" value="${item}" class="easyui-datetimebox" required="true"  style="width:254px;height:28px;"/>
									<input type="text" id="time_${item}" value="${item}"/>
		        	                <span style="color:red">*</span> <br> 
								</c:forEach>
							</c:if>
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
