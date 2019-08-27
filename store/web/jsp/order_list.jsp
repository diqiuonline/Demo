<%--
  Created by IntelliJ IDEA.
  User: nihaoa
  Date: 2018/7/13
  Time: 19:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>我的订单</title>
    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
    <script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="js/bootstrap.min.js" type="text/javascript"></script>
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
    </style>
</head>

<body>


<%@include file="menu.jsp"%>


<div class="container">
    <div class="row">

        <div style="margin:0 auto; margin-top:10px;width:950px;">
            <strong>我的订单</strong>
            <table class="table table-bordered">
                <tbody>
                <c:forEach items="${pageBean.list}" var="order">
                    <tr class="success">
                        <th colspan="5">
                            订单编号:${order.oid} &nbsp;&nbsp;&nbsp;
                            <c:if test="${order.state == 1}">
                                <a href="${pageContext.request.contextPath}/OrderServlet?method=findByOid&oid=${order.oid}">去付款</a>
                            </c:if>
                            <c:if test="${order.state == 2}">
                                卖家未发货
                            </c:if>
                            <c:if test="${order.state == 3}">
                                <a href="#">确认收货</a>
                            </c:if>
                            <c:if test="${order.state == 4}">
                                订单关闭
                            </c:if>
                        </th>
                    </tr>
                    <tr class="warning">
                        <th>图片</th>
                        <th>商品</th>
                        <th>价格</th>
                        <th>数量</th>
                        <th>小计</th>
                    </tr>
                    <c:forEach items="${order.orderItems}" var="orderitem">
                        <tr class="active">
                            <td width="60" width="40%">
                                <input type="hidden" name="id" value="22">
                                <img src="${pageContext.request.contextPath}/${orderitem.product.pimage}" width="70" height="60">
                            </td>
                            <td width="30%">
                                <a target="_blank"> ${orderitem.product.pname}</a>
                            </td>
                            <td width="20%">
                                ￥${orderitem.product.shop_price}
                            </td>
                            <td width="10%">
                                ${orderitem.count}
                            </td>
                            <td width="15%">
                                <span class="subtotal">￥${orderitem.subtotal}</span>
                            </td>
                        </tr>
                    </c:forEach>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div style="text-align: center;">
        <ul class="pagination">

            <c:if test="${pageBean.currPage == 1}">
                <li class="disabled">
                    <a href="#" aria-label="Next" class="active">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>
            <c:if test="${ pageBean.currPage != 1 }">
                <li>
                    <a href="${ pageContext.request.contextPath }/OrderServlet?method=findOrderByUid&currPage=${pageBean.currPage-1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <c:forEach var="i" begin="1" end="${pageBean.totoPage}">
                <li <c:if test="${ pageBean.currPage == i }">class="active"</c:if>>
                    <a href="${pageContext.request.contextPath}/OrderServlet?method=findOrderByUid&currPage=${i}" >
                        ${i}
                    </a>
                </li>
            </c:forEach>

            <c:if test="${pageBean.currPage == pageBean.totoPage}">
                <li  >
                    <a href="#" aria-label="Next" class="active">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
            <c:if test="${ pageBean.currPage != pageBean.totoPage }">
                <li>
                    <a href="${ pageContext.request.contextPath }/OrderServlet?method=findOrderByUid&currPage=${pageBean.currPage+1}" aria-label="Previous">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
        </ul>
    </div>
</div>

<div style="margin-top:50px;">
    <img src="./image/footer.jpg" width="100%" height="78" alt="我们的优势" title="我们的优势" />
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