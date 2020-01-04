<%--
  Created by IntelliJ IDEA.
  User: nihaoa
  Date: 2018/7/7
  Time: 20:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<jsp:forward page="/IndexServlet">
    <jsp:param name="method" value="index"/>
</jsp:forward>
</body>
</html>
