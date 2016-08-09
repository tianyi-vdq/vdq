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
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1  ,maximum-scale=1, user-scalable=no" />
<script
	src="${pageContext.request.contextPath}/source/js/pager/jquery.pager.js"></script>
<link
	href="${pageContext.request.contextPath}/source/js/pager/Pager.css"
	rel="stylesheet" />
<script type="text/javascript">
		$(document).ready(function(){
			$("#pager").pager({
			    pagenumber:'${area.pageNo}',                         /* 表示初始页数 */
			    pagecount:'${area.pageCount}',                      /* 表示总页数 */
			    totalCount:'${area.totalCount}',
			    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
			});
			 $("#treeList").tree({
				 url: 'jsonLoadAreaTreeList.do',   
   				 onClick:function(node){
   				 	getAreaListByCode(node.serialCode);
   				 	//window.location.href = 'areaList.do?areaId=' + node.id + '&serialCode=' + node.serialCode;
   				 	/* if(node.children.length==0){
   				 	 	window.location.href = 'areaList.do';
   				 	}else{
   				 	  	window.location.href = 'areaList.do?parentId='+node.id;
   				 	} */
   				 },
   				 onBeforeExpand:function(node){
   				 	$('#treeList').tree('options').url = 'jsonLoadAreaTreeList.do?pid='+ node.id;
   				 },
   				 onLoadSuccess:function(){
					showProcess(false); 
			 
   				    var root = $("#treeList").tree("getRoot");
   				 	if(root!= null){
   				 		$("#treeList").tree("expand",root.target);
   				 	}  
   				    var aId = $.trim($("#hid_areaId").val());
   				 	if(aId.length>0){
   				 		var node = $("#treeList").tree("find",aId); 
						$('#treeList').tree("select", node.target);
   				 	}  
   				  
   				 }
			}); 
			//loadCompanyList();
		});
function getAreaListByParentId(pid){
	$.ajax({
 		url:'jsonLoadAreaTreeList.do?pid='+pid
	});
} 
function appendParentNode(){
	var html = "<li><div id='_easyui_tree_0' class='tree-node'>";
	html+="<span class='tree-indent'></span>";
	html+="<span class='tree-icon tree-file '></span>";
	html+="<span class='tree-title'>=请选择所属区域=</span>";
	html+="</div></li>";
	$(".panel.combo-p .tree").prepend(html)
	//$(html).appendTo($(".tree"));
}
function getAreaListByCode(serialCode){
	  var pageNumber = $("#pageNumber").val();
	  $.ajax({
		url : "jsonLoadAreaListByCode.do?serialCode="+serialCode+"&&pageNumber="+pageNumber,
		type : "post",  
		dataType:"json",
		success : function(data) { 
  			if(data.code == 0){ 
  				 /* $("#pageNumber").val(1);  */
  				 $("#pager").pager({
				    pagenumber:data.obj.pageNo,                         /* 表示初始页数 */
				    pagecount:data.obj.pageCount,                      /* 表示总页数 */
				    totalCount:data.obj.totalCount,
				    buttonClickCallback:PageClick2                     /* 表示点击分页数按钮调用的方法 */                  
				});
				$("#areainfoList").html("");
				$("#pageNumber").val("");
				$("#serialCode").val(data.obj.serialCode);
				fillAreaList(data.list);
  			}else{
				$.messager.alert('错误信息',data.message,'error');
  			} 
		}
	});
};
function fillAreaList(lst){
	var html = "<tbody>";
	html += "<tr><th width='4%' style='display:none'>&nbsp;</th><th>区域名称</th><th>所属区域</th><th>创建时间</th><th>操作</th></tr>";
	for(var i = 0; i<lst.length;i++){
		html += "<tr ondblclick=window.location.href='areainfo.do?areaId='+"+lst[i].id+">";
		html += "<td style='display:none'>"+lst[i].id+"</td><td align='left'  onclick=window.location.href='areainfo.do?areaId="+lst[i].id+"'  >"+lst[i].name+"</td><td  align='left'   onclick=window.location.href='areainfo.do?areaId="+lst[i].id+"'  >"+lst[i].parentName+"</td>";
		html += "<td   onclick=window.location.href='areainfo.do?areaId="+lst[i].id+"' >"+lst[i].creatTime+"</td><td><span class='yw-btn bg-orange cur' onclick='deleteAreaById("+lst[i].id+");'>×</span></td>";
		html += "</tr>";
	}
	html += "</tbody>";
	$("#areainfoList").html(html);
}
PageClick = function(pageclickednumber) {
	$("#pager").pager({
	    pagenumber:pageclickednumber,                 /* 表示启示页 */
	    pagecount:'${area.pageCount}',                  /* 表示最大页数pagecount */
	    buttonClickCallback:PageClick                 /* 表示点击页数时的调用的方法就可实现javascript分页功能 */            
	});
	
	$("#pageNumber").val(pageclickednumber);          /* 给pageNumber从新赋值 */
	/* 执行Action */
	pagesearch();
}
PageClick2 = function(pageclickednumber) {
	$("#pager").pager({
	    pagenumber:pageclickednumber,                 /* 表示启示页 */
	    pagecount:'${Area.pageCount}',                  /* 表示最大页数pagecount */
	    buttonClickCallback:PageClick2                 /* 表示点击页数时的调用的方法就可实现javascript分页功能 */            
	});
	
	$("#pageNumber").val(pageclickednumber);          /* 给pageNumber从新赋值 */
	/* 执行Action */
	pagesearch2();
}
function search(){
	$("#pageNumber").val("1");
	pagesearch();
}

function pagesearch(){
	userAreaForm.submit();
}
function pagesearch2(){
	var serialCode = $("#serialCode").val();
	getAreaListByCode(serialCode);	
	$("#serialCode").val("");
}
function showdialog(){
	var wz = getDialogPosition($('#areaInfoWindow').get(0),100);
	$('#areaInfoWindow').window({
		  	top: '20%',
		    left: wz[1],
		    onBeforeClose: function () {
		    },
		    onClose:function(){
		    	$('#txt_areaname').val('');
		    	$('#cmbParentArea').val('setText','=请选择所属区域=');
		    }
	});
	$('#areaInfoWindow').window('open');
}
function saveArea(obj){
	if ($('#saveAreaForm').form('validate')) {
		$(obj).attr("onclick", ""); 
		showProcess(true, '温馨提示', '正在提交数据...'); 
		 $('#saveAreaForm').form('submit',{
		  		success:function(data){
					showProcess(false);
		  			data = $.parseJSON(data);
		  			if(data.code==0){
	  					$('#areaInfoWindow').window('close');
		  				$.messager.alert('保存信息',data.message,'info',function(){
	        			});
	  					search();
		  			}else{
						$.messager.alert('错误信息',data.message,'error',function(){
	        			});
						$(obj).attr("onclick", "saveArea(this);"); 
		  			}
		  		}
		  	 });  
	}
}
function deleteAreaById(id){
	$.messager.confirm("删除确认","确认删除该区域?",function(r){  
		    if (r){  
			$.ajax({
				url : "jsonDeleteArea.do?areaId="+id,
				type : "post",  
				dataType:"json",
				success : function(data) { 
		  			if(data.code==0){ 
		  				$.messager.alert('删除信息',data.message,'info',function(){ 
		  					//search();
		  					var node = $("#treeList").tree("getSelected");
		  					if(node == null || node == undefined){
		  						node = $("#treeList").tree("getRoot");
		  						getAreaListByCode(node.serialCode);
		  					}else{ 
		  						getAreaListByCode(node.serialCode);
		  					}
		  					var targetNode = $("#treeList").tree("find",id);
		  					if(targetNode != null){
		  						var target = targetNode.target;
		  						$("#treeList").tree("remove",target);
		  					}
		       			});
		  			}else{
						$.messager.alert('错误信息',data.message,'error');
		  			} 
				}
			});
	    }  
	}); 
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
</script>
</head>

<body>

	<div class="con-right" id="conRight">
		<div class="fl yw-lump">
			<div class="yw-lump-title">
				<i class="yw-icon icon-partner"></i><span>区域列表</span><input type="hidden" value="${area.id}" id="hid_areaId" />
				<%-- <span class="fr yw-btn bg-orange line-hei22 mr10 mt9 cur">导入区域
					<div class="temp">
					<form id="fileForms" name="fileForms" action="${pageContext.request.contextPath}/fileUpload/uploadAreaExcel.do"  enctype="multipart/form-data" method="post" style="margin:0;padding:0;">
				       	<input type="file" name="file" id="jfile" class="yw-upload-file" onChange="excelChange(this);">
					</form>
					</div>
				</span> --%>	
			</div>
		</div>
		<div class="fl yw-lump mt10">
			<form id="userAreaForm" name="userAreaForm"
				action="areaList.do" method="get">
				<div class="pd10">
					<div class="fl"> 
						<span class="ml26">区域信息</span>
						<input type="text" name="searchName"   validType="SpecialWord" class="easyui-validatebox" 
							placeholder="搜索" value="${area.searchName}" /> 
						<span class="yw-btn bg-blue ml30 cur" onclick="search();">搜索</span>
					</div>
					<div class="fr">
						<span class="fl yw-btn bg-green cur" onclick="window.location.href='areainfo.do?areaId='+ 0">新建区域</span>
					</div>
					<div class="cl"></div>
				</div>

				<input type="hidden" id="pageNumber" name="pageNo"
					value="${area.pageNo}" />
				<input type="hidden" id="serialCode" name="serialCode"
				value="${Area.serialCode}" />
			</form>
		</div>
		<div class="fl">
			<div class="fl yw-lump wid250 mt10">
				<div class="yw-cm-title">
					<span class="ml26">区域列表</span>
				</div>
				<div class="yw-tree-list" style="height: 639px;">
					<ul  id="treeList" ></ul>
				</div>
			</div>
			<div class="yw-lump wid-atuo ml260 mt10">
				<div class="yw-cm-title">
					<span class="ml26">全部区域</span>
				</div>
				<table class="yw-cm-table yw-center yw-bg-hover" id="areainfoList">
					<tr style="background-color:#D6D3D3;font-weight: bold;">
						<th width="4%" style="display:none"></th>
						<th>区域名称</th> 
						<th>所属区域</th>
						<th>创建时间</th> 
						<th>操作</th> 
					</tr>
					<c:forEach var="item" items="${arealist}">
						<tr>
							<td align="center" style="display:none">${item.id}</td>
							<td align="left" onclick="window.location.href='areainfo.do?areaId=${item.id}'" >${item.name}</td> 
							<td align="left" onclick="window.location.href='areainfo.do?areaId=${item.id}'"  >${item.parentName}</td> 
							<td onclick="window.location.href='areainfo.do?areaId=${item.id}'" >${item.creatTime}</td>
							<td><a style="color:blue" onclick="deleteAreaById(${item.id});">删除</a></td>
						</tr>
					</c:forEach>
				</table>
				<div class="page" id="pager"></div>
			</div>
		</div>
 	  <!-- <div id="areaInfoWindow" class="easyui-window" title="新添加区域" style="width:560px;height:260px;overflow:hidden;padding:10px;text-align:center;" iconCls="icon-info" closed="true" modal="true"   resizable="false" collapsible="false" minimizable="false" maximizable="false">
		<form id="saveAreaForm" name ="saveAreaForm" action="jsonSaveOrUpdateArea.do"  method="post">
		<p style="display:none">
        	<span>id：</span><input name="id" type="text" value="0" class="easyui-validatebox"/>
        </p> 
		<p class="yw-window-p">
        	<span>区域名称：</span><input id="txt_areaname" name="name" onblur="valueTrim(this);"  type="text" value="" class="easyui-validatebox" required="true"  validType="Length[1,30]" style="width:254px;height:28px;"/>
        </p>
		<p class="yw-window-p">
        	<span>区域编码：</span><input name="serialCode" type="text" value="" class="easyui-validatebox" required="true"  validType="Length[1,25]" style="width:254px;height:28px;"/>
        </p>
        <p class="yw-window-p">
        	<span>所属区域：</span><input id="cmbParentArea" class="easyui-combotree"  style="width:254px;height:32px;" /><input id="parentId" name="parentId" type="hidden" value="0"/>
        </p> 
        <div class="yw-window-footer txt-right">
        	<span id="btnCancel" class="yw-window-btn bg-lightgray mt12"  onclick="$('#txt_areaname').val('');$('#cmbParentArea').val('setText','=请选择所属区域=');$('#areaInfoWindow').window('close');">退出</span>
        	<span class="yw-window-btn bg-blue mt12" onclick="saveArea(this);">保存</span>
        </div>
        </form>
	</div> -->
</body>
</html>
