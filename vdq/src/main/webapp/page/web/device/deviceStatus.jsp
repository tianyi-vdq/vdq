<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8"%>
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
<title>设备状态</title>
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
			    pagenumber:'${DeviceStatus.pageNo}',                         /* 表示初始页数 */
			    pagecount:'${DeviceStatus.pageCount}',                      /* 表示总页数 */
			    totalCount:'${DeviceStatus.totalCount}',
			    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
			});
			$("#cmbParentArea").combotree({
				 url: 'area/jsonLoadAreaTreeList.do',  
   				 required: false,
   				 onSelect:function(record){ 
 				 	 	$("#areaId").val(record.id); 
   				 },
   				 onBeforeExpand:function(node){
   				 	$('#cmbParentArea').combotree('tree').tree('options').url = 'area/jsonLoadAreaTreeList.do?pid='+ node.id;
   				 },
   				 onLoadSuccess:function(){
   				 	var deviceId = $("#deviceId").val();
   				 	
   				 	if(deviceId>0){
   				 		var pId = $("#areaId").val();
   				 		$("#cmbParentArea").combotree("setValue",pId);
   				 	}else{
						//$("#cmbParentArea").combotree("disable",true);
	   				 	$("#cmbParentArea").combotree("setText","=请选择所属区域=");
					}
   				 }
			});
			//loadCompanyList();
		});
function getAreaListByParentId(pid){
	$.ajax({
 		url:'area/jsonLoadAreaTreeList.do?pid='+pid
	});
} 
PageClick = function(pageclickednumber) {
	$("#pager").pager({
	    pagenumber:pageclickednumber,                 /* 表示启示页 */
	    pagecount:'${DeviceStatus.pageCount}',                  /* 表示最大页数pagecount */
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
	if ($('#DeviceStatusForm').form('validate')) {
		DeviceStatusForm.submit();
	}  
}

</script>
  </head>  
 <body>
   <div class="con-right" id="conRight">
   
       <form id="DeviceStatusForm" name="DeviceStatusForm"
				action="device/deviceStatus.do" method="get">
		<div class="fl yw-lump">
			<div class="yw-lump-title">
				<i class="yw-icon icon-partner"></i><span>设备状态</span>								
			</div>
		</div>
		<div class=pd10>
		      <div class="fl">
		      <%-- <input id ="areaId" name ="searchAreaId" type="hidden" value="${DeviceStatus.searchAreaId}"/> --%>
			  <span>区域选择：</span><input  id ="cmbParentArea" name ="searchAreaId" type="text"  class="easyui-combotree" required="true" style="width:254px;height:28px;" />
			  </div>
		<div class="fr">  
			<span>设备编号：</span><input type="text" id="pointNumber" name="searchPointNumber"   validType="SpecialWord"
				 class="easyui-validatebox" placeholder="搜索" value="${DeviceStatus.searchPointNumber}" type="hidden"/> 
			<span>设备状态：</span>
			<select class="easyui-combobox"  id="statusId"  name="searchStatusId" value="${DeviceStatus.searchStatusId}"
					style="width:180px;height:32px;" data-options="editable:false">
		    <option name="searchStatusId" value="" >请选择设备状态</option>	             	
			<option value="1">异常</option>	
			<option value="2">警告</option>	
			<option value="3">正常</option>	
			<option value="4">失败</option>				        							
			</select>		
			 <span class="yw-btn bg-blue ml30 cur" onclick="search();">搜索</span>						
		</div>			
          <input type="hidden" id="pageNumber" name="pageNo" value="${DeviceStatus.pageNo}" />
      </div>
	</form>
		
           <div class="fl yw-lump">           
				<table class="yw-cm-table yw-center yw-bg-hover" id="deviceStatusList">
					<tr style="background-color:#D6D3D3;font-weight: bold;">
						<th width="4%" style="display:none">&nbsp;</th>
						<th>设备ID</th>
						<th>网络连接状态</th>
						<th>拉流</th>
						<th>雪花噪音</th>
						<th>信号状态</th> 
						<th>色彩状态</th>
						<th>画面冻结</th> 
						<th>画面遮挡</th>
						<th>画面模糊</th>
						<th>画面移位</th> 
						<th>画面彩条</th>
						<th>画面偏色</th> 
						<th>亮度异常</th>
						<th width="10%">记录时间</th> 
						<th width="10%">创建时间</th>
					</tr>
					<c:forEach var="item" items="${DeviceStatuslist}">
						<tr>
							<td align="center" style="display:none">${item.id}</td>
							<td>${item.deviceId}</td>                          
							<td>
							<c:if test="${item.networkStatus==1}">异常</c:if>
							<c:if test="${item.networkStatus==2}"><img alt="警告" src="basePath/source/images/yw-icon1.png"></c:if>
							<c:if test="${item.networkStatus==3}">正常</c:if>
							<c:if test="${item.networkStatus==4}">失败</c:if>
							</td>
							<td>
							<c:if test="${item.streamStatus==1}">异常</c:if>
							<c:if test="${item.streamStatus==2}">警告</c:if>
							<c:if test="${item.streamStatus==3}">正常</c:if>
							<c:if test="${item.streamStatus==4}">失败</c:if>
							</td>
							<td>
							<c:if test="${item.noiseStatus==1}">异常</c:if>
							<c:if test="${item.noiseStatus==2}">警告</c:if>
							<c:if test="${item.noiseStatus==3}">正常</c:if>
							<c:if test="${item.noiseStatus==4}">失败</c:if>
							</td>
							<td>
							<c:if test="${item.signStatus==1}">异常</c:if>
							<c:if test="${item.signStatus==2}">警告</c:if>
							<c:if test="${item.signStatus==3}">正常</c:if>
							<c:if test="${item.signStatus==4}">失败</c:if>
							</td>
							<td>
							<c:if test="${item.colorStatus==1}">异常</c:if>
							<c:if test="${item.colorStatus==2}">警告</c:if>
							<c:if test="${item.colorStatus==3}">正常</c:if>
							<c:if test="${item.colorStatus==4}">失败</c:if>
							</td> 
							<td>
							<c:if test="${item.frameFrozenStatus==1}">异常</c:if>
							<c:if test="${item.frameFrozenStatus==2}">警告</c:if>
							<c:if test="${item.frameFrozenStatus==3}">正常</c:if>
							<c:if test="${item.frameFrozenStatus==4}">失败</c:if>
							</td> 
							<td>
							<c:if test="${item.frameShadeStatus==1}">异常</c:if>
							<c:if test="${item.frameShadeStatus==2}">警告</c:if>
							<c:if test="${item.frameShadeStatus==3}">正常</c:if>
							<c:if test="${item.frameShadeStatus==4}">失败</c:if>
							</td>
							<td>
							<c:if test="${item.frameFuzzyStatus==1}">异常</c:if>
							<c:if test="${item.frameFuzzyStatus==2}">警告</c:if>
							<c:if test="${item.frameFuzzyStatus==3}">正常</c:if>
							<c:if test="${item.frameFuzzyStatus==4}">失败</c:if>
                            </td>
							<td>
							<c:if test="${item.frameDisplacedStatus==1}">异常</c:if>
							<c:if test="${item.frameDisplacedStatus==2}">警告</c:if>
							<c:if test="${item.frameDisplacedStatus==3}">正常</c:if>
							<c:if test="${item.frameDisplacedStatus==4}">失败</c:if>
							</td>
							<td>
							<c:if test="${item.frameStripStatus==1}">异常</c:if>
							<c:if test="${item.frameStripStatus==2}">警告</c:if>
							<c:if test="${item.frameStripStatus==3}">正常</c:if>
							<c:if test="${item.frameStripStatus==4}">失败</c:if>
							</td> 
							<td>
							<c:if test="${item.frameColorcaseStatus==1}">异常</c:if>
							<c:if test="${item.frameColorcaseStatus==2}">警告</c:if>
							<c:if test="${item.frameColorcaseStatus==3}">正常</c:if>
							<c:if test="${item.frameColorcaseStatus==4}">失败</c:if>
							</td> 
							<td>
							<c:if test="${item.lightExceptionStatus==1}">异常</c:if>
							<c:if test="${item.lightExceptionStatus==2}">警告</c:if>
							<c:if test="${item.lightExceptionStatus==3}">正常</c:if>
							<c:if test="${item.lightExceptionStatus==4}">失败</c:if>
							</td>
							<td>${item.recordTimes}</td> 
							<td>${item.createTimes}</td>
						</tr>
					</c:forEach>
				</table>
			
				<div class="page" id="pager"></div> 
		</div>	
		</div>
		
  </body>
</html>
