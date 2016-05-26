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
<title>区域管理</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" /> 
<script type="text/javascript">
		$(document).ready(function(){ 
			//setShowStates();
			
			 $("#cmbParentArea").combotree({
				 url: 'jsonLoadAreaTreeList.do?rootId='+0,  
   				 required: false,
   				 onSelect:function(record){
   				 	 var areaId = $("#areaId").val();
   				 	 if(areaId != undefined && areaId != 0 && areaId !="" && areaId != null){
   				 	 if(record!=null){
	   				 	 var serialCode = $("#serialCode").val();
	   				 	 var pcode = record.serialCode;
	   				 	 if(pcode.indexOf(serialCode)==0){
		   				 	 	$.messager.alert('错误信息',"不能选择下级或当前区域为所属区域",'error',function(){
			        			});
		   				 	 	$("#cmbParentArea").combotree("clear");
		   				 	 	$("#parentId").val(0);
	   				 	 		$("#cmbParentArea").combotree("reload",'jsonLoadAreaTreeList.do?rootId='+0);
		   				 	 	$("#cmbParentArea").combotree("setText","=请选择所属区域=");
	   				 	 }else{ 
		   				 	 var arId = $("#areaId").val();
		   				 	 if(arId == record.id){
		   				 	 	$.messager.alert('错误信息',"不能选择当前区域为所属区域",'error',function(){
			        			});
		   				 	 	$("#cmbParentArea").combotree("clear");
		   				 	 	$("#parentId").val(0);
	   				 	 		$("#cmbParentArea").combotree("reload",'jsonLoadAreaTreeList.do?rootId='+0);
		   				 	 	$("#cmbParentArea").combotree("setText","=请选择所属区域=");
		   				 	 }else{
			   				 	 var parId = $("#parentId").val();
			   				 	 if(record.id  == parId){
			   				 	 	//$("#cmbParentCompany").combotree("setValue",0);
			   				 	 	$("#cmbParentArea").combotree("clear");
			   				 	 	$("#parentId").val(0);
		   				 	 		$("#cmbParentArea").combotree("reload",'jsonLoadAreaTreeList.do?rootId='+0);
			   				 	 	$("#cmbParentArea").combotree("setText","=请选择所属区域=");
			   				 	 }else{  
			   				 	 	$("#parentId").val(record.id);
			   				 	 }   
		   				 	 }
	   				 	 }
	   				 } else{
   				 	 	$("#cmbParentArea").combotree("clear");
   				 	 	$("#parentId").val(0);
  				 	 	$("#cmbParentArea").combotree("reload",'jsonLoadAreaTreeList.do?rootId='+0);
   				 	 	$("#cmbParentArea").combotree("setText","=请选择所属区域=");
	   				 } 
					 //appendParentNode();
					 }else{
			   				 	 var parId = $("#parentId").val();
			   				 	 if(record.id  == parId){
			   				 	 	//$("#cmbParentCompany").combotree("setValue",0);
			   				 	 	$("#cmbParentArea").combotree("clear");
			   				 	 	$("#parentId").val(0);
		   				 	 		$("#cmbParentArea").combotree("reload",'jsonLoadAreaTreeList.do?rootId='+0);
			   				 	 	$("#cmbParentArea").combotree("setText","=请选择所属区域=");
			   				 	 }else{  
			   				 	 	$("#parentId").val(record.id);
			   				 	 }   
		   				 	 }
   				 },
   				 onLoadSuccess:function(){
					//$("#cmbParentArea").combotree("disable",true);
					var parentId = $("#parentId").val();
					if(parentId==0){
   				 		$("#cmbParentArea").combotree("setText","=请选择所属区域=");
					}else{
						//appendParentNode();
						$("#cmbParentArea").combotree("setValue",parentId);
					}
   				 }
			}); 
		});   
function appendParentNode(){
	var html = "<li><div id='_easyui_tree_0' class='tree-node'>";
	html+="<span class='tree-indent'></span>";
	html+="<span class='tree-icon tree-file '></span>";
	html+="<span class='tree-title'>=请选择所属区域=</span>";
	html+="</div></li>";
	$(".tree").prepend(html)
	//$(html).appendTo($(".tree"));
}
function setShowStates(){
	$("#tab1 input[doc='areaInfo']").attr("readonly","readonly"); 
	$("#cmbParentArea").combotree("disable",true);
	$("#saveBtn").hide();
	$("#editBtn").show();
};  
function editArea(){
	$("#tab1 input[doc='areaInfo']").removeAttr("readonly"); 
	$("#cmbParentArea").combotree("enable",true);
	$("#editBtn").hide();
	$("#saveBtn").show();
};
function saveArea(obj){
	if ($('#areaForm').form('validate')) {
		$(obj).attr("onclick", ""); 
		showProcess(true, '温馨提示', '正在提交数据...'); 
		 $('#areaForm').form('submit',{
		  		success:function(data){
					showProcess(false);
		  			data = $.parseJSON(data);
		  			if(data.code==0){
		  				$.messager.alert('保存信息',data.message,'info',function(){ 
							window.location.href ="areaList.do";
	        			});
		  			}else{
						$.messager.alert('错误信息',data.message,'error',function(){
	        			});
						$(obj).attr("onclick", "saveArea(this);"); 
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
				<%-- <i class="yw-icon icon-back" onclick="javascript:history.back();"></i><span>区域：${area.name}</span> --%>
				<i  id="i_back"  class="yw-icon icon-back" onclick="window.location.href='areaList.do'"></i><span>区域：${area.name}</span>
			</div>
		</div>
		<div class="fl yw-lump mt10">
			<div class="yw-bi-rows">
				<div class="yw-bi-tabs mt5" id="ywTabs">
					<span class="yw-bi-now" onclick="javaScript:void(0);">基本信息</span>
				</div>
				<div class="fr">
					<span class="yw-btn bg-green mr26 hide" id="editBtn"  onclick="editArea();">编辑</span>
					<span class="yw-btn bg-red mr26" id="saveBtn" onclick="saveArea(this);">保存</span>
					<span class="yw-btn bg-green mr26"  onclick="$('#i_back').click();">返回</span>
				</div>
			</div>
			<div id="tab1" class="yw-tab">
				<form id="areaForm" name="areaForm"
						action="jsonSaveOrUpdateArea.do" method="post">
					<table class="yw-cm-table font16">
						<tr>
							<td width="10%" align="center">区域名称：</td>
							<td><input id="areaName" name="name" type="text" doc="areaInfo" onblur="valueTrim(this);"  value="${area.name}" class="easyui-validatebox" required="true"  validType="Length[1,30]" style="width:254px;height:28px;"/>
								<input name="id" type="hidden" id="areaId" value="${area.id}" />
								<input id="serialCode"  name="serialCode" type="hidden" value="${area.serialCode}" />
							</td>
						</tr>
						<%-- <tr style="display:none">
							<td width="10%" align="center">区域编码：</td>
							<td><input id="serialCode"  name="serialCode" type="text" doc="areaInfo"  value="${area.serialCode}" class="easyui-validatebox" required="true"  validType="Length[1,25]" style="width:254px;height:28px;"/></td>
						</tr> --%>
						<tr>
							<td width="10%" align="center">所属区域：</td>
							<td>
								<input id="cmbParentArea" class="easyui-combotree"  style="width:254px;height:32px;" />
								<input id="parentId" name="parentId" type="hidden" value="${area.parentId}"/>
								<input name="oldParentId" type="hidden"  value="${area.parentId}" />
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
