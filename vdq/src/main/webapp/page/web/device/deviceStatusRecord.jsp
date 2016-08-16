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
<title>设备诊断历史</title>
<meta http-equiv="refresh" content="30">
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
			    pagenumber:'${DeviceStatusRecord.pageNo}',                         /* 表示初始页数 */
			    pagecount:'${DeviceStatusRecord.pageCount}',                      /* 表示总页数 */
			    totalCount:'${DeviceStatusRecord.totalCount}',
			    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
			});
			var hid_searchStatusId = $("#hid_searchStatusId").val();
			if(hid_searchStatusId!=""){
				$("#statusId").combobox("setValue",hid_searchStatusId);
			}
			/* var hid_searchExceptionId = $("#hid_searchExceptionId").val();
			if(hid_searchExceptionId !=""){
				$("#exceptionstatusId").combobox("setValue",hid_searchExceptionId);
			} */
		});
		
		
		//指定3分钟刷新一次,这种刷新有点问题，我采用的另一种方式<meta http-equiv="refresh" content="180">
		/* function myrefresh(){ 
			window.location.reload(); 
		} 
		setTimeout('myrefresh()',180000); */ 

PageClick = function(pageclickednumber) {
	$("#pager").pager({
	    pagenumber:pageclickednumber,                 /* 表示启示页 */
	    pagecount:'${DeviceStatusRecord.pageCount}',                  /* 表示最大页数pagecount */
	    buttonClickCallback:PageClick                 /* 表示点击页数时的调用的方法就可实现javascript分页功能 */            
	});
	
	$("#pageNumber").val(pageclickednumber);          /* 给pageNumber从新赋值 */
	/* 执行Action */
	pagesearch();
}
function search(){
	$("#pageNumber").val("1");
	$("#hid_serarch").val(encodeURI($("#seaarchNameTemp").val()));
	pagesearch();
}

function pagesearch(){
	if ($('#DeviceStatusForm').form('validate')) {
		$("#hid_serarch").val(encodeURI($("#seaarchNameTemp").val()));
		DeviceStatusForm.submit();
	}  
}

</script>
  </head>  
 <body>
   <div class="con-right" id="conRight">
		<div class="fl yw-lump">
			<div class="yw-lump-title">
				<i class="yw-icon icon-partner"></i><span>设备状态</span>								
			</div>
		</div>
		<div class="fl yw-lump mt10">
		<form id="DeviceStatusForm" name="DeviceStatusForm"
				action="device/deivceStatusRecord.do" method="get">
		<div class="pd10">
		      <div class="fl">	 	
				<span>设备编号：</span>	<input type="text" id="seaarchNameTemp" validType="SpecialWord" class="easyui-validatebox" value="${DeviceStatusRecord.searchPointNumber}"  /> 
				<input type="hidden" name="searchPointNumber" id="hid_serarch" /> 	 
			  <span>设备状态：</span>
			  <input type="hidden" name="searchStatusId" id="hid_searchStatusId" value="${DeviceStatusRecord.searchStatusId}" />
			  <select class="easyui-combobox"  id="statusId" style="width:180px;height:32px;" data-options="editable:false,onSelect:function(record){$('#hid_searchStatusId').val(record.value);}">
				    <option value="" >请选择设备状态</option>	             	
					<option value="1">异常</option>	
					<!-- <option value="2">警告</option>	 -->
					<option value="3">正常</option>	 	
					<!-- <option value="4">失败</option>	 -->			        							
			</select>	 
			<%-- <span>诊断状态：</span>
		<input type="hidden" name="searchExceptionId" id="hid_searchExceptionId" value="${DeviceStatusRecord.searchExceptionId}" />
		  <select class="easyui-combobox"  id="exceptionstatusId" style="width:180px;height:32px;" data-options="editable:false,onSelect:function(record){$('#hid_searchExceptionId').val(record.value);}">
			    <option value="" >请选择诊断状态</option>	             	
				<option value="5">雪花噪声</option>	 
				<option value="2">信号缺失</option>	 
				<option value="4">色彩丢失</option>	  
				<option value="3">画面冻结</option>	   
				<option value="6">画面遮挡</option>	  
				<option value="7">画面模糊</option>	  
				<option value="8">画面移位</option>	  
				<option value="9">画面彩条</option>	  
				<option value="10">画面偏色</option>	  
				<option value="11">亮度异常</option>	   
				<option value="14">黑屏</option>	       							
		</select> --%>			
			 <span class="yw-btn bg-blue ml30 cur" onclick="search();">搜索</span>
		</div>			
			 <div class="fr"></div>
			 <div class="cl"></div>						
          <input type="hidden" id="pageNumber" name="pageNo" value="${DeviceStatusRecord.pageNo}" />
      </div>
		</form>
      </div>
		
   <div class="fl yw-lump">           
   <table class="yw-cm-table yw-center yw-bg-hover" id="deviceStatusList">
		<tr style="background-color:#D6D3D3;font-weight: bold;">
			<th width="4%" style="display:none">&nbsp;</th>
			<th>设备编号</th>
			<th width="8%">网络连接状态</th>
			<th>拉流</th>
			<th>雪花噪音</th>
			<th>信号缺失</th> 
			<th>色彩丢失</th>
			<th>画面冻结</th> 
			<th>画面遮挡</th>
			<th>画面模糊</th>
			<th>画面移位</th> 
			<th>画面彩条</th>
			<th>画面偏色</th> 
			<th>亮度异常</th> 
			<th>黑屏</th>
			<th width="12%">诊断时间</th>
		</tr>
		<c:forEach var="item" items="${DeviceStatusRecordlist}">
		<tr> 
			<td align="center" style="display:none">${item.id}</td>
			<td>${item.pointId}</td>                          
			<td>
			<c:if test="${item.networkStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.networkStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.networkStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.networkStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.networkStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td>
			<td> 
			<c:if test="${item.streamStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.streamStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.streamStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.streamStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.streamStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td>
			<td>
			<c:if test="${item.noiseStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.noiseStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.noiseStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.noiseStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.noiseStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td>
			<td>
			<c:if test="${item.signStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.signStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.signStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.signStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.signStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td>
			<td>
			<c:if test="${item.colorStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.colorStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.colorStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.colorStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.colorStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td> 
			<td>
			<c:if test="${item.frameFrozenStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.frameFrozenStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.frameFrozenStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.frameFrozenStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.frameFrozenStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td> 
			<td>
			<c:if test="${item.frameShadeStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.frameShadeStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.frameShadeStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.frameShadeStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.frameShadeStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td>
			<td>
			<c:if test="${item.frameFuzzyStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.frameFuzzyStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.frameFuzzyStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.frameFuzzyStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.frameFuzzyStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
                        </td>
			<td>
			<c:if test="${item.frameDisplacedStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.frameDisplacedStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.frameDisplacedStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.frameDisplacedStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.frameDisplacedStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td>
			<td>
			<c:if test="${item.frameStripStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.frameStripStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.frameStripStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.frameStripStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.frameStripStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td> 
			<td>
			<c:if test="${item.frameColorcaseStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.frameColorcaseStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.frameColorcaseStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.frameColorcaseStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.frameColorcaseStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td> 
			<td>
			<c:if test="${item.lightExceptionStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.lightExceptionStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.lightExceptionStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.lightExceptionStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.lightExceptionStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td>
			<td>
			<c:if test="${item.blackScreenStatus==null}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			<c:if test="${item.blackScreenStatus==1}"><img src="${pageContext.request.contextPath}/source/images/exception.png"/></c:if>
			<c:if test="${item.blackScreenStatus==2}"><img src="${pageContext.request.contextPath}/source/images/warming.png"/></c:if>
			<c:if test="${item.blackScreenStatus==3}"><img src="${pageContext.request.contextPath}/source/images/good.png"/></c:if>
			<c:if test="${item.blackScreenStatus==4}"><img src="${pageContext.request.contextPath}/source/images/fail.png"/></c:if>
			</td>
			<td>
				${item.recordTimes}
			</td>
		</tr>
		</c:forEach>
	</table>
	
		<div class="page" id="pager"></div> 
</div>	 
</div>
		
  </body>
</html>