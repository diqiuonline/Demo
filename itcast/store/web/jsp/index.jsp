<%--
  Created by IntelliJ IDEA.
  User: nihaoa
  Date: 2018/7/7
  Time: 20:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>黑马商城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css" />
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>

</head>

<body>
<div class="container-fluid">

<%@  include file="menu.jsp"%>

    <!--
        作者：ci2713@163.com
        时间：2015-12-30
        描述：轮播条
    -->
    <div class="container-fluid">
        <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
            <!-- Indicators -->
            <ol class="carousel-indicators">
                <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                <li data-target="#carousel-example-generic" data-slide-to="2"></li>
            </ol>

            <!-- Wrapper for slides -->
            <div class="carousel-inner" role="listbox">
                <div class="item active">
                    <img src="${pageContext.request.contextPath}/img/1.jpg">
                    <div class="carousel-caption">

                    </div>
                </div>
                <div class="item">
                    <img src="${pageContext.request.contextPath}/img/2.jpg">
                    <div class="carousel-caption">

                    </div>
                </div>
                <div class="item">
                    <img src="${pageContext.request.contextPath}/img/3.jpg">
                    <div class="carousel-caption">

                    </div>
                </div>
            </div>

            <!-- Controls -->
            <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
                <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
                <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                <span class="sr-only">Next</span>
            </a>
        </div>
    </div>
    <!--
        作者：ci2713@163.com
        时间：2015-12-30
        描述：商品显示
    -->
    <div class="container-fluid">
        <div class="col-md-12">
            <h2>热门商品&nbsp;&nbsp;<img src="${pageContext.request.contextPath}/img/title2.jpg"/></h2>
        </div>
        <div class="col-md-2" style="border:1px solid #E7E7E7;border-right:0;padding:0;">
            <img src="${pageContext.request.contextPath}/products/hao/big01.jpg" width="205" height="404" style="display: inline-block;"/>
        </div>
        <div class="col-md-10">
            <div class="col-md-6" style="text-align:center;height:200px;padding:0px;">
                <a href="#">
                    <img src="${pageContext.request.contextPath}/products/hao/middle01.jpg" width="516px" height="200px" style="display: inline-block;">
                </a>
            </div>


            <c:forEach items="${hotlist}" var="h">
                <div class="col-md-2" style="text-align:center;height:200px;padding:10px 0px;">
                    <a href="${pageContext.request.contextPath}/ProductServlet?method=findByPid&pid=${h.pid}">
                        <img src="${pageContext.request.contextPath}/${h.pimage}" width="130" height="130" style="display: inline-block;">
                    </a>
                    <p><a href="${pageContext.request.contextPath}/ProductServlet?method=findByPid&pid=${h.pid}" style='color:#666'>${h.pname}</a></p>
                    <p><font color="#E4393C" style="font-size:16px">&yen;&yen;${h.shop_price}</font></p>
                </div>
            </c:forEach>

        </div>
    </div>
    <!--
        作者：ci2713@163.com
        时间：2015-12-30
        描述：广告部分
    -->
    <div class="container-fluid">
        <img src="${pageContext.request.contextPath}/products/hao/ad.jpg" width="100%"/>
    </div>
    <!--
        作者：ci2713@163.com
        时间：2015-12-30
        描述：商品显示
    -->
    <div class="container-fluid">
        <div class="col-md-12">
            <h2>最新商品&nbsp;&nbsp;<img src="${pageContext.request.contextPath}/img/title2.jpg"/></h2>
        </div>
        <div class="col-md-2" style="border:1px solid #E7E7E7;border-right:0;padding:0;">
            <img src="${pageContext.request.contextPath}/products/hao/big01.jpg" width="205" height="404" style="display: inline-block;"/>
        </div>
        <div class="col-md-10">
            <div class="col-md-6" style="text-align:center;height:200px;padding:0px;">
                <a href="#">
                    <img src="${pageContext.request.contextPath}/products/hao/middle01.jpg" width="516px" height="200px" style="display: inline-block;">
                </a>
            </div>
            <c:forEach items="${newlist}" var="n">
                <div class="col-md-2" style="text-align:center;height:200px;padding:10px 0px;">
                    <a href="${pageContext.request.contextPath}/ProductServlet?method=findByPid&pid=${n.pid}">
                        <img src="${pageContext.request.contextPath}/${n.pimage}" width="130" height="130" style="display: inline-block;">
                    </a>
                    <p><a href="${pageContext.request.contextPath}/ProductServlet?method=findByPid&pid=${n.pid}" style='color:#666'>${n.pname}</a></p>
                    <p><font color="#E4393C" style="font-size:16px">&yen;${n.shop_price}</font></p>
                </div>
            </c:forEach>


        </div>
    </div>
    <!--
        作者：ci2713@163.com
        时间：2015-12-30
        描述：页脚部分
    -->
    <div class="container-fluid">
        <div style="margin-top:50px;">
            <img src="${pageContext.request.contextPath}/img/footer.jpg" width="100%" height="78" alt="我们的优势" title="我们的优势" />
        </div>

        <div style="text-align: center;margin-top: 5px;">
            <ul class="list-inline">
                <li><a href="info.html">关于我们</a></li>
                <li><a>联系我们</a></li>
                <li><a>招贤纳士</a></li>
                <li><a>法律声明</a></li>
                <li><a>友情链接</a></li>
                <li><a>支付方式</a></li>
                <li><a>配送方式</a></li>
                <li><a>服务声明</a></li>
                <li><a>广告声明</a></li>
            </ul>
        </div>
        <div style="text-align: center;margin-top: 5px;margin-bottom:20px;">
            Copyright &copy; 2005-2016 传智商城 版权所有
        </div>
    </div>
</div>
</body>

</html>
