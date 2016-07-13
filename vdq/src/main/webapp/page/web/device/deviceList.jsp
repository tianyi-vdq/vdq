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

function showdialog(){
	var wz = getDialogPosition($('#pointInfoWindow').get(0),100);
	$('#pointInfoWindow').window({
		    onBeforeClose: function () {
		    },
		    onClose:function(){
		    	$('#savePointForm .easyui-validatebox').val('');
		    }
	});
	$('#pointInfoWindow').window('open');
}
function importPoint(){
	
};
function savePoint(obj){
	if ($('#savePointForm').form('validate')) {
		showProcess(true, '温馨提示', '正在提交数据...'); 
		$(obj).attr("onclick", ""); 
		 $('#savePointForm').form('submit',{
		  		success:function(data){
					showProcess(false);
		  			data = $.parseJSON(data);
		  			if(data.code==0){
	  					$('#pointInfoWindow').window('close');
		  				$.messager.alert('保存信息',data.message,'info',function(){
	        			});
	  					search();
		  			}else{
						$.messager.alert('错误信息',data.message,'error',function(){
	        			});
						$(obj).attr("onclick", "savePoint(this);"); 
		  			}
		  		}
		  	 });  
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
	$.ajax({
		url:"device/jsonLoadStopOrStartDevice.do?deviceId="+id+"&&flag="+flag,
		type:"post",
		dataType:"json",
		success:function(data) {
			if (data.code == 0) {
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
							placeholder="按设备ID搜索" value="${Device.searchName}" /> 
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
						<th>设备ID</th>
						<th>平台ID</th>
						<th>设备标志ID</th>
						<th>设备编号</th>
						<th>设备名称</th>
						<th>Naming</th>
						<th>RTSP</th>
						<th>设备类型</th> 
						<th>设备地址</th>
						<th>IP地址</th> 
						<th>所属区域</th>						
						<th>操作</th>
					</tr>
					<c:forEach var="item" items="${Devicelist}">
						<tr>
							<td align="center" style="display:none">${item.id}</td>
							<td align="left" onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'" >${item.pointId}</td>
							<td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.platformId}</td>
							<td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.deviceKey}</td>
							<td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.pointNumber}</td>
							<td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.pointName}</td>
							<td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.pointNaming}</td>
							<td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.rtspUrl}</td>
							<td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.type}</td>
							<td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.address}</td> 
							<td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.ipAddress}</td> 
							<td onclick="window.location.href='device/deviceInfo.do?pointId=${item.id}'">${item.areaName}</td>
							<td>
							<c:if test="${item.flag == 0}">
								<a href="javascript:void(0);" onclick="stopOrStartDevice(${item.id},${item.flag});">停用设备</a>
							</c:if>
							<c:if test="${item.flag == 1}">
								<a href="javascript:void(0);" onclick="stopOrStartDevice(${item.id},${item.flag});">启用设备</a>
							</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
				<div class="page" id="pager"></div> 
		</div>	
		</div>
 	  <div id="pointInfoWindow" class="easyui-window" title="新添加设备" style="width:560px;height:580px;overflow:hidden;padding:10px;text-align:center;" iconCls="icon-info" closed="true" modal="true"   resizable="false" collapsible="false" minimizable="false" maximizable="false">
		<form id="savePointForm" name ="savePointForm" action="device/jsonSaveOrUpdatePoint.do"  method="post">
		<p style="display:none">
        	<span>id：</span><input name="id" type="hidden" value="0" class="easyui-validatebox"/>
        </p>
		<p class="yw-window-p">
        	<span>&nbsp;&nbsp;&nbsp;&nbsp;设备ID：</span><input name="pointId" type="text"  onblur="valueTrim(this);"  value="" class="easyui-validatebox" required="true"  validType="Length[1,10]" style="width:254px;height:28px;"/>
			<span style="color:red">*</span>
        </p>
        <p class="yw-window-p">
        	<span>设备编号：</span><input  name=pointNumber type="text" value="" onblur="valueTrim(this);"   class="easyui-validatebox" required="true"  validType="Length[1,25]" style="width:254px;height:28px;"/>
			<span style="color:red">*</span>
        </p>
        <p class="yw-window-p">
        	<span>设备名称：</span><input name="pointName" type="text" value="" onblur="valueTrim(this);"   class="easyui-validatebox" required="true"  validType="Length[1,30]" style="width:254px;height:28px;"/>
			<span style="color:red">*</span>
        </p>
        <p class="yw-window-p">
        	<span>&nbsp;&nbsp;Naming：</span><input name="pointNaming" type="text" value="" onblur="valueTrim(this);"   class="easyui-validatebox" required="true"  validType="Length[1,30]" style="width:254px;height:28px;"/>
			<span style="color:red">*</span>
        </p>
        <p class="yw-window-p">
        	<span>设备类型：</span><input name="type" type="text" value="" onblur="valueTrim(this);"   class="easyui-validatebox" required="true"  validType="Length[1,30]" style="width:254px;height:28px;"/>
			<span style="color:red">*</span>
        </p>
  
        <p class="yw-window-p">
        	<span>设备地址：</span><input name="address" type="text" value="" onblur="valueTrim(this);"  class="easyui-validatebox" required="true"  validType="Length[1,50]" style="width:254px;height:28px;"/>
			<span style="color:red">*</span>
        </p>
        <p class="yw-window-p">
        	<span>&nbsp;&nbsp;&nbsp;&nbsp;IP地址：</span><input name="ipAddress" type="text" value="" onblur="valueTrim(this);"  class="easyui-validatebox" required="true"  validType="IP" style="width:254px;height:28px;"/>
			<span style="color:red">*</span>
        </p>
        <div class="yw-window-footer txt-right">
        	<span id="btnCancel" class="yw-window-btn bg-lightgray mt12"  onclick="$('#savePointForm .easyui-validatebox').val('');$('#pointInfoWindow').window('close');">退出</span>
        	<span class="yw-window-btn bg-blue mt12" onclick="savePoint(this);">保存</span>
        </div>
        </form>
	</div>
  </body>
</html>
