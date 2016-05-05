<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.tianyi.yw.model.User" %>
<%@ page import="com.tianyi.yw.common.utils.Constants" %> 
	<div class="fl head-logo">
        <img src="${pageContext.request.contextPath}/source/images/logo2.png" class="ml26" />
    </div>
    <div class="fr head-menu">
    	<ul class="fl">
    		<%-- <c:forEach var="item" items="${sessionScope.userFunctions}">
    			
    			<li <c:if test="${item.id == sessionScope.userInfo.selectedMainMemu}">class="head-menu-now"</c:if> onclick="gotoMainMenu('${pageContext.request.contextPath}${item.url}','${item.id}')">${item.name}</li>
    		    
    		</c:forEach> --%>
        	<!-- <li class="head-menu-now">个人工作台</li><li>统计分析</li><li>客户管理</li><li>员工管理</li><li>项目管理</li><li>知识库</li> -->
        </ul>
        <div class="fr head-menu-right ml40">
            <!-- <a href="#"><i class="fl yw-icon icon-dot"></i><span>你有新消息</span></a> -->
            <a href="${pageContext.request.contextPath}/logout.do"><i class="fl yw-icon icon-fork"></i><span>退出</span></a>
        </div>
    </div>
