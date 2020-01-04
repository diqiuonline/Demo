<%--
  Created by IntelliJ IDEA.
  User: nihaoa
  Date: 2018/7/11
  Time: 21:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>购物车</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css" />
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- 引入自定义css文件 style.css -->
    <link rel="stylesheet" href="css/style.css" type="text/css" />
    <style>
        body {
            margin-top: 20px;
            margin: 0 auto;
        }

        .carousel-inner .item img {
            width: 100%;
            height: 300px;
        }

        .container .row div {
            /* position:relative;
 float:left; */
        }

        font {
            color: #3164af;
            font-size: 18px;
            font-weight: normal;
            padding: 0 10px;
        }
    </style>
    <script>
        $(function () {
            $(".delete").click(function () {
                var value = $(this).attr("name");
                $(this).prop("href","${pageContext.request.contextPath}/CartServlet?method=removeCart&pid="+value)
            });
        });

    </script>
</head>

<body>


<%@include file="menu.jsp"%>

<div class="container">


    <c:if test="${empty cart.map}">
        <div style="text-align: center">
            <h3 >请去挑选心仪的商品</h3>
            <img src="${pageContext.request.contextPath}/img/62cfc382gy1fo4oomjzz5j20p011i7hs.jpg" alt="" style="width: 200px;height: 300px;">
        </div>
    </c:if>
    <c:if test="${not empty cart.map}">
        <div class="row">
                <div style="margin:0 auto; margin-top:10px;width:950px;">
                    <strong style="font-size:16px;margin:5px 0;">订单详情</strong>
                    <table class="table table-bordered">
                        <tbody>
                        <tr class="warning">
                            <th>图片</th>
                            <th>商品</th>
                            <th>价格</th>
                            <th>数量</th>
                            <th>小计</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach items="${cart.map}" var="map">
                            <tr class="active">
                                <td width="60" width="40%">
                                    <input type="hidden" name="id" value="22">
                                    <img src="${pageContext.request.contextPath}/${map.value.product.pimage}" width="70" height="60">
                                </td>
                                <td width="30%">
                                    <a target="_blank">${map.value.product.pname} </a>
                                </td>
                                <td width="20%">
                                    ￥${map.value.product.shop_price}
                                </td>
                                <td width="10%">
                                    <input type="text" name="quantity" value="${map.value.count}" maxlength="4" size="10">
                                </td>
                                <td width="15%">
                                    <span class="subtotal">￥${map.value.subtotal}</span>
                                </td>
                                <td>
                                    <a href="#" id="del" class="delete" name="${map.value.product.pid}" >删除</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

        </div>

        <div style="margin-right:130px;">
            <div style="text-align:right;">
                <em style="color:#ff6600;">
                    登录后确认是否享有优惠&nbsp;&nbsp;
                </em> 赠送积分: <em style="color:#ff6600;">${cart.total}</em>&nbsp; 商品金额: <strong style="color:#ff6600;">￥${cart.total}元</strong>
            </div>
            <div style="text-align:right;margin-top:10px;margin-bottom:10px;">
                <a href="${pageContext.request.contextPath}/CartServlet?method=clearCaet" id="clear" class="clear">清空购物车</a>
                <a href="${pageContext.request.contextPath}/OrderServlet?method=saveOrder">
                    <input type="submit" width="100" value="提交订单" name="submit" border="0" style="background:
                            url('${pageContext.request.contextPath}/images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0);
                            height:35px;width:100px;color:white;">
                </a>
            </div>
        </div>
    </c:if>
</div>

<div style="margin-top:50px;">
    <img src="${pageContext.request.contextPath}/image/footer.jpg" width="100%" height="78" alt="我们的优势" title="我们的优势" />
</div>

<div style="text-align: center;margin-top: 5px;">
    <ul class="list-inline">
        <li><a>关于我们</a></li>
        <li><a>联系我们</a></li>
        <li><a>招贤纳士</a></li>
        <li><a>法律声明</a></li>
        <li><a>友情链接</a></li>
        <li><a target="_blank">支付方式</a></li>
        <li><a target="_blank">配送方式</a></li>
        <li><a>服务声明</a></li>
        <li><a>广告声明</a></li>
    </ul>
</div>
<div style="text-align: center;margin-top: 5px;margin-bottom:20px;">
    Copyright &copy; 2005-2016 传智商城 版权所有
</div>

</body>

</html>