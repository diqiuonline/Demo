<%@ page import="com.itheima.autologin.domin.UserPojo" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="${pageContext.request.contextPath}/Recoad/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<meta http-equiv="Refresh" content="3;url =${pageContext.request.contextPath}/SkipServlet"/>
<script type="text/javascript">
    var time = 3;
    window.onload = function(){
        setInterval('changTime()', 1000);
    }
    function changTime(){
        time--;
        document.getElementById("s1").innerHTML = time;
    }

</script>
</head>
<body>

<c:if test="${empty userLogin1}">
	<h1>您还没有登陆 ！请先去<a href = "${pageContext.request.contextPath}/login/login.jsp">登陆</a>！</h1>
</c:if>
<c:if test="${not empty userLogin1 }">
	<h1>登录成功！</h1>
	<h3>页面将在<span id="s1">3</span>秒后跳转！</h3>
</c:if>
</body>
</html>