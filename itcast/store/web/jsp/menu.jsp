<%--
  Created by IntelliJ IDEA.
  User: nihaoa
  Date: 2018/7/8
  Time: 9:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
    $(function () {
        $.post("${pageContext.request.contextPath}/CategoryServlet",{"method":"findAll"},function (date) {
            $(date).each(function (a,b) {
               $("#menuID").append("<li><a href=" +
                   "'${pageContext.request.contextPath}/ProductServlet?method=findByCid&currPage=1&cid="+b.cid+"'>"+b.cname+"</a></li>")
            });
        },"json");
    });

</script>
<!--
时间：2015-12-30
描述：菜单栏
-->
<div class="container-fluid">
    <div class="col-md-4">
        <img src="${pageContext.request.contextPath}/img/logo2.png" />
    </div>
    <div class="col-md-5">
        <img src="${pageContext.request.contextPath}/img/header.png" />
    </div>
    <div class="col-md-3" style="padding-top:20px">
        <ol class="list-inline">
            <c:if test="${not empty user}">
                <li>你好:${user.name}</li>
                <li><a href="${pageContext.request.contextPath}/OrderServlet?method=findOrderByUid&currPage=1">我的订单</a></li>
                <li><a href="${pageContext.request.contextPath}/UserServlet?method=logOut">退出</a></li>
            </c:if>
            <c:if test="${ empty user}">
                <li><a href="${pageContext.request.contextPath}/UserServlet?method=loginUI">登录</a></li>
                <li><a href="${pageContext.request.contextPath}/UserServlet?method=registerUI">注册</a></li>
            </c:if>

            <li><a href="${pageContext.request.contextPath}/jsp/cart.jsp">购物车</a></li>
        </ol>
    </div>
</div>
<!--
时间：2015-12-30
描述：导航条
-->
<div class="container-fluid">
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${pageContext.request.contextPath}/">首页</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav" id="menuID">

                </ul>
                <form class="navbar-form navbar-right" role="search">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>

            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>
</div>