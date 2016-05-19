<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8">
<title>任务管理</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" />
<script
	src="${pageContext.request.contextPath}/source/js/pager/jquery.pager.js"></script>
<link
	href="${pageContext.request.contextPath}/source/js/pager/Pager.css"
	rel="stylesheet" />
<script type="text/javascript">
		$(document).ready(function(){
			$("#pager").pager({
			    pagenumber:'${Task.pageNo}',                         /* 表示初始页数 */
			    pagecount:'${Task.pageCount}',                      /* 表示总页数 */
			    totalCount:'${Task.totalCount}',
			    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
			});
	 			$("#taskList tr").each(function(i){
				if(i>0){
					$(this).bind("click",function(){
						var name = $(this).find("td").first().text();
						 window.location.href="taskInfo.do?name="+name;
					});
				}
			});	
		}); 
		
PageClick = function(pageclickednumber) {
	$("#pager").pager({
	    pagenumber:pageclickednumber,                 /* 表示启示页 */
	    pagecount:'${Task.pageCount}',                  /* 表示最大页数pagecount */
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
	if ($('#taskForm').form('validate')) {
		taskForm.submit();
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
function saveTask(obj){
	if ($('#saveTaskForm').form('validate')) {
		$(obj).attr("onclick", ""); 
		showProcess(true, '温馨提示', '正在提交数据...'); 
		 $('#saveTaskForm').form('submit',{
		  		success:function(data){ 
					showProcess(false);
		  			data = $.parseJSON(data);
		  			if(data.code==0){
	  					$('#taskInfoWindow').window('close');
		  				$.messager.alert('保存信息',data.message,'info',function(){
	        			});
	  					search();
		  			}else{
						$.messager.alert('错误信息',data.message,'error',function(){
	        			});
						$(obj).attr("onclick", "saveTask(this);"); 
		  			}
		  		}
		  	 });  
	}

}  
function getDateModel(date){
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	if(month <10){
		month = "0"+month;
	}
	var day = date.getDate();
	if(day <10){
		day = "0"+day;
	}
	var dates = year+"-"+month+"-"+day;
	return dates;
}

function getSelectDate(date){
	var dates = getDateModel(date);
	$("#startTimes").val(dates);
}
function sltSchStime(date){
	var dates = getDateModel(date);
	$("#startedTimes").val(dates);
}
function sltSchEtime(date){
	var dates = getDateModel(date);
	$("#endTimes").val(dates);
}
</script>
</head>
<body>
	<div class="con-right" id="conRight">
	
		<div class="fl yw-lump">
			<div class="yw-lump-title">
				<i class="yw-icon icon-partner"></i><span>任务信息</span> 
			</div>
		</div>
		<div class="fl yw-lump mt10">
			<form id="taskForm" name="taskForm"
				action="taskList.do" method="get">
				<div class="pd10">
					<div class="fr">
			 			<span class="fl yw-btn bg-green cur" onclick="showdialog();">新建任务</span>
					</div>	
				</div>
					<div class="fr">  
						<span>任务名称：</span><input type="text" name="searchName"   validType="SpecialWord"
						 class="easyui-validatebox" 
							placeholder="搜索" value="${Task.searchName}" type="hidden"/> 
						<span>开始时间：</span>
						<input id="startedTimes" name="startedTimes" value="${Task.startedTimes}" type="hidden" />
						 <input data-options="editable:false,onSelect:sltSchStime" type="text"  class="easyui-datebox" 
					    	 style="width:254px;height:28px;"/>  
						<span>结束时间：</span>
						<input id="endTimes" name="endTimes" value="${Task.endTimes}" type="hidden"  />
						 <input data-options="editable:false,onSelect:sltSchEtime"   type="text"  class="easyui-datebox"
						   style="width:254px;height:28px;"/> 
						<span class="yw-btn bg-blue ml30 cur" onclick="search();">搜索</span>
					</div>
					<div class="cl"></div>
					
                     <input type="hidden" id="pageNumber" name="pageNo"
				    	value="${Task.pageNo}" />
		     	</form>
		     	</div>

	
				
           <div class="fl yw-lump"> 
				<table class="yw-cm-table yw-center yw-bg-hover" id="taskList">
					<tr style="background-color:#D6D3D3;font-weight: bold;">
						<th width="4%" style="display:none">&nbsp;</th>
						<th>状态</th>  
						<th>任务名称</th> 
						<th>启动时间</th> 
						<th>执行间隔</th>
						<th>执行次数</th>
						<th>诊断并发路数</th>
						<th>创建时间</th>
					</tr>
					<c:forEach var="item" items="${Tasklist}">
						<tr>
							<td align="right" style="display:none">${item.id}</td>
							<td align="right" >${item.flag}</td>  
							<td align="right" >${item.name}</td> 
							<td align="right" >${item.startTimes}</td> 
							<td align="right" >${item.runIntervals}</td> 
							<td align="right" >${item.runTimes}</td> 
							<td align="right" >${item.runCount}</td> 
							<td align="right" >${item.createTimes}</td> 
						</tr>
					</c:forEach>
				</table>
				<div class="page" id="pager"></div>
				</div>
			</div>

 			<div id="taskInfoWindow" class="easyui-window" title="新建任务" style="width:560px;height:430px;overflow:hidden;padding:10px;"
               iconCls="icon-info" closed="true" modal="true" resizable="false" collapsible="false" minimizable="false" maximizable="false">
		<form id="saveTaskForm" name ="saveTaskForm" action="jsonSaveOrUpdateTask.do"  method="post">
		<p style="display:none">
        	<span class="fl ml80 wid102 line-hei30">id：</span><input name="id"  value="0" class="easyui-validatebox"/>
        </p>
        <p class="yw-window-p">
        	<span class="fl ml80 wid102 line-hei30"> 任务名称：</span><input name="name"  type="text" value="" class="easyui-validatebox" required="true" validType="Length[1,10]" style="width:254px;height:28px;"/>
        	<span style="color:red">*</span> 
        </p> 
       
		<p class="yw-window-p">
        	<span class="fl ml80 wid102 line-hei30">启动时间：</span><input  name="startTimes" data-options="editable:false"  onblur="valueTrim(this);"  type="text" value="" class="easyui-datetimebox" required="true"  style="width:254px;height:28px;"/>
        	<span style="color:red">*</span> 
        </p>
        <p lass="yw-window-p">
        	<span class="fl ml80 wid102 line-hei30">执行间隔：</span><input name="runIntervals" onblur="valueTrim(this);"  type="text" value="" class="easyui-validatebox" required="true"  validType="number" style="width:254px;height:28px;"/>
        	<span style="color:red">*</span> 
        </p> 
        <p class="yw-window-p">
        	<span class="fl ml80 wid102 line-hei30">执行次数：</span><input name="runTimes" onblur="valueTrim(this);" type="text" value="" class="easyui-validatebox"  required="true"   validType="number" style="width:254px;height:28px;"/>
        	<span style="color:red">*</span> 
        </p>
        <p class="yw-window-p">
        	<span class="fl ml80 wid102 line-hei30">并发路数：</span><input name="runCount" onblur="valueTrim(this);" type="text" value="" class="easyui-validatebox"  required="true"   validType="number" style="width:254px;height:28px;"/>
            <span style="color:red">*</span>
        </p>
        <p class="yw-window-p">
        	<span class="fl ml80 wid102 line-hei30">是否有效任务：</span>
        	<label><input type="radio" name="flag" value="0" checked="checked" />无效</label> 
		 	<label><input type="radio" name="flag" value="1" checked="checked" />有效</label> 
            <span style="color:red">*</span>      
        </p>
           <div class="yw-window-footer txt-right">
        	<span id="btnCancel" class="yw-window-btn bg-lightgray mt12"  onclick="$('#saveTaskForm .easyui-validatebox').val('');$('#taskInfoWindow').window('close');">退出</span>
        	<span class="yw-window-btn bg-blue mt12" onClick="saveTask(this);">保存</span>
        </div>
        </form>
      </div> 

  </body>
</html>












