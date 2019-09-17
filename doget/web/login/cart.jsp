<%@page import="com.itheima.autologin.domin.UserPojo"%>
<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<c:if test="${empty userLogin1}">
	<h1>您还没有登陆 ！请先去<a href = "${pageContext.request.contextPath}/login/login.jsp">登陆</a>！</h1>
</c:if>
<c:if test="${not empty userLogin1}">
<h1>购物车列表</h1>
<%
	Map<String,Integer> map = (Map<String,Integer>)request.getSession().getAttribute("cart");
	for(String name:map.keySet()){
		Integer count = map.get(name);
%>
    <h4>您购买的商品是:<%=name %>, 对应购买的数量:<%= count %></h4>
    
<%
	}
%>

<a href = "${pageContext.request.contextPath}/ClearServletCaet">清空购物车</a>
<a href = "${pageContext.request.contextPath}/login/product_list.jsp">继续购物</a>
</c:if>
</body>
</html>