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
    
    <title>参数管理</title>
    
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
			    pagenumber:'${Param.pageNo}',                         /* 表示初始页数 */
			    pagecount:'${Param.pageCount}',                      /* 表示总页数 */
			    totalCount:'${Param.totalCount}',
			    buttonClickCallback:PageClick                     /* 表示点击分页数按钮调用的方法 */                  
			}); 
			$("#parameList tr").each(function(i){
				if(i>0){
					$(this).bind("click",function(){
						var parameId = $(this).find("td").first().text();
						window.location.href="parame/parameInfo.do?id="+parameId;
					});
				}
			}); 
		});
	PageClick = function(pageclickednumber) {
		$("#pager").pager({
			pagenumber : pageclickednumber, /* 表示启示页 */
			pagecount : '${Param.pageCount}', /* 表示最大页数pagecount */
			buttonClickCallback : PageClick
		/* 表示点击页数时的调用的方法就可实现javascript分页功能 */
		});

		$("#pageNumber").val(pageclickednumber); /* 给pageNumber从新赋值 */
		/* 执行Action */
		pagesearch();
	}
	function search() {
		$("#pageNumber").val("1");
		pagesearch();
	}

	function pagesearch() {
		if ($('#ParamForm').form('validate')) {
			ParamForm.submit();
		}
	}
	function showdialog(){
	var wz = getDialogPosition($('#parameInfoWindow').get(0),100);
	$('#parameInfoWindow').window({
		    onBeforeClose: function () {
		    },
		    onClose:function(){
		    	$('#saveParameForm .easyui-validatebox').val('');
		    }
	});
	$('#parameInfoWindow').window('open');
}
function savePoint(obj){
	if ($('#saveParameForm').form('validate')) {
		showProcess(true, '温馨提示', '正在提交数据...'); 
		$(obj).attr("onclick", ""); 
		 $('#saveParameForm').form('submit',{
		  		success:function(data){
					showProcess(false);
		  			data = $.parseJSON(data);
		  			if(data.code==0){
	  					$('#parameInfoWindow').window('close');
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
</script>
</head>
  <body>
    <div class="con-right" id="conRight">
    	<div class="fl yw-lump">
			<div class="yw-lump-title">
				<i class="yw-icon icon-partner"></i><span>参数信息</span>
			</div>
		</div>
    
  		<div class="fl yw-lump mt10">
			<form id="ParamForm" name="ParamForm">
				<div class="pd10">
					<div class="fl">
						<span class="ml26">参数信息</span>
						<input type="text" name="searchName"   validType="SpecialWord" class="easyui-validatebox" 
							placeholder="搜索" value="${Parame.searchName}" /> 
						<span class="yw-btn bg-blue ml30 cur" onclick="search();">搜索</span>
					</div>
					<div class="fr">
						<span class="fl yw-btn bg-green cur" onclick="window.location.href='parame/parameInfo.do?id='+0">新建参数</span>
					</div>
					<div class="cl"></div>
				</div>

				<input type="hidden" id="pageNumber" name="pageNo"
					value="${Param.pageNo}" />
			</form>
		</div>
		<div class="fl yw-lump"> 
				<table class="yw-cm-table yw-center yw-bg-hover" id="parameList">
					<tr style="background-color:#D6D3D3;font-weight: bold;">
						<th width="4%" style="display:none">id</th>
						<th>参数名称</th>
						<th>参数关键值</th>
						<th>参数值</th>
					</tr>
					<c:forEach var="item" items="${paramList}">
						<tr>
							<td align="center" style="display:none">${item.id}</td>
							<td align="left">${item.name}</td>
							<td>${item.key}</td>
							<td>${item.value}</td>
						</tr>
					</c:forEach>
				</table>
				<div class="page" id="pager"></div> 
		</div>	
	</div>
		
	<div id="parameInfoWindow" class="easyui-window" title="新添加参数" style="width:460px;height:280px;overflow:hidden;padding:10px;text-align:center;" iconCls="icon-info" closed="true" modal="true"   resizable="false" collapsible="false" minimizable="false" maximizable="false">
		<form id="saveParameForm" name ="saveParameForm" action="parame/jsonSaveOrUpdateParame.do"  method="post">
		<p style="display:none">
        	<span>id：</span><input name="id" type="hidden" value="0" class="easyui-validatebox"/>
        </p>
		<p class="yw-window-p">
        	<span>&nbsp;&nbsp;&nbsp;&nbsp;参数名：</span><input name="name" type="text"  onblur="valueTrim(this);"  value="" class="easyui-validatebox" required="true"  validType="Length[1,10]" style="width:254px;height:28px;"/>
			<span style="color:red">*</span>
        </p>
        <p class="yw-window-p">
        	<span>参数关键字：</span><input  name="key" type="text" value="" onblur="valueTrim(this);"   class="easyui-validatebox" required="true"  validType="Length[1,25]" style="width:254px;height:28px;"/>
			<span style="color:red">*</span>
        </p>
        <p class="yw-window-p">
        	<span>&nbsp;&nbsp;&nbsp;&nbsp;参数值：</span><input name="value" type="text" value="" onblur="valueTrim(this);"   class="easyui-validatebox" required="true"  validType="Length[1,30]" style="width:254px;height:28px;"/>
			<span style="color:red">*</span>
        </p>
        <div class="yw-window-footer txt-right">
        	<span id="btnCancel" class="yw-window-btn bg-lightgray mt12"  onclick="$('#saveParameForm .easyui-validatebox').val('');$('#parameInfoWindow').window('close');">退出</span>
        	<span class="yw-window-btn bg-blue mt12" onclick="savePoint(this);">保存</span>
        </div>
        </form>
	</div>
</body>
</html>
