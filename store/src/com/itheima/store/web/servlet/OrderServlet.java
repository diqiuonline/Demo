package com.itheima.store.web.servlet;

import com.itheima.store.domain.*;
import com.itheima.store.service.OrderService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.BeasServlet;
import com.itheima.store.utils.PaymentUtil;
import com.itheima.store.utils.UUIDUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.*;

/**
 * User: 李锦卓
 * Time: 2018/7/12 11:40
 */
@WebServlet(urlPatterns="/OrderServlet")
public class OrderServlet extends BeasServlet {

    public String saveOrder(HttpServletRequest request, HttpServletResponse response) {
        try {
            Order order = new Order();
            //设置订单ID
            order.setOid(UUIDUtils.getUUID());
            //设置订单时间
            order.setOrdertime(new Date());
            //获取订单金额
            Cart cart = (Cart) request.getSession().getAttribute("cart");
            if (cart==null){
                request.setAttribute("msg","购物车是空的哦");
                return "/jsp/msg.jsp";
            }
            order.setTotal(cart.getTotal());
            //设置订单状态
            order.setState(1); //未付款
            //设置用户信息
            User user = (User) request.getSession().getAttribute("user");
            if (user==null){
                request.setAttribute("msg","您还没有登陆哦");
                return "jsp/msg.jsp";
            }
            order.setUser(user);
            //设置订单项集合
            List<OrderItem>orderItems = new ArrayList<OrderItem>();
            Map<String, CartIntem> map = cart.getMap();
            Collection<CartIntem> cartIntems = map.values();
            for (CartIntem cartIntem:cartIntems){
                OrderItem orderItem = new OrderItem();
                //设置订单项ID
                orderItem.setItemid(UUIDUtils.getUUID());
                //设置商品数量
                orderItem.setCount(cartIntem.getCount());
                //订单项所属ID
                orderItem.setOrder(order);
                //订单项价格
                orderItem.setSubtotal(cartIntem.getSubtotal());
                //商品
                orderItem.setProduct(cartIntem.getProduct());
                //将订单项存入到集合中
                orderItems.add(orderItem);
            }
            order.setOrderItems(orderItems);
            //调用业务层
            OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
            orderService.save(order);

            //清空购物车
            cart.clear();

            //页面跳转
            request.getSession().setAttribute("order",order);
            response.sendRedirect(request.getContextPath()+"/jsp/order_info.jsp");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    //查询自己的订单
    public String findOrderByUid(HttpServletRequest request, HttpServletResponse response){
        try {
            //获取当前页
            Integer currpage = Integer.parseInt(request.getParameter("currPage"));
            //获取Uid
            User user = (User) request.getSession().getAttribute("user");
            String uid = user.getUid();
            //调用业务层分装PageBean
            OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
            PageBean pageBean = orderService.findByUid(currpage,uid);
            request.setAttribute("pageBean",pageBean);
            return "/jsp/order_list.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //查询某一个订单
    public String findByOid(HttpServletRequest request, HttpServletResponse response){
        try {
            String oid = request.getParameter("oid");
            OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
            Order order = orderService.findByOid(oid);
            request.setAttribute("order",order);
            return "/jsp/order_info.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //订单支付
    public String payOrder(HttpServletRequest request, HttpServletResponse response){
        try {
            //获取订单id
            String oid = request.getParameter("oid");
            //获取收货人姓名
            String name = request.getParameter("name");
            //获取收货人地址
            String address = request.getParameter("address");
            //获取收货人电话
            String telephone = request.getParameter("telephone");
            //获取支付通道银行编码
            String pd_FrpId = request.getParameter("pd_FrpId");
            //根据oid获取订单
            OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
            Order order = orderService.findByOid(oid);
            order.setName(name);;
            order.setAddress(address);
            order.setTelephone(telephone);
            //修改订单
            orderService.update(order);

            //付款 准备跳转到网银页面
            String p0_Cmd = "Buy";
            String p1_MerId = "10001126856";
            String p2_Order = oid;
            String p3_Amt = "0.01";
            String p4_Cur = "CNY";
            String p5_Pid = "";
            String p6_Pcat = "";
            String p7_Pdesc = "";
            String p8_Url = "http://localhost:8080/OrderServlet?method=callBack";
            String p9_SAF = "";
            String pa_MP = "";
            String pr_NeedResponse = "1";
            String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
            String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
                    p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
            StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
            sb.append("p0_Cmd=").append(p0_Cmd).append("&");
            sb.append("p1_MerId=").append(p1_MerId).append("&");
            sb.append("p2_Order=").append(p2_Order).append("&");
            sb.append("p3_Amt=").append(p3_Amt).append("&");
            sb.append("p4_Cur=").append(p4_Cur).append("&");
            sb.append("p5_Pid=").append(p5_Pid).append("&");
            sb.append("p6_Pcat=").append(p6_Pcat).append("&");
            sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
            sb.append("p8_Url=").append(p8_Url).append("&");
            sb.append("p9_SAF=").append(p9_SAF).append("&");
            sb.append("pa_MP=").append(pa_MP).append("&");
            sb.append("pd_FrpId=").append(pd_FrpId).append("&");
            sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
            sb.append("keyValue=").append(keyValue).append("&");
            sb.append("hmac=").append(hmac);
            response.sendRedirect(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //支付返回
    public String callBack(HttpServletRequest request, HttpServletResponse response){
        try {
            //接受参数
            String oid = request.getParameter("r6_Order");
            String money = request.getParameter("r3_Amt");
            //修改订单状态
            OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
            Order order = orderService.findByOid(oid);
            order.setState(2);
            orderService.update(order);
            request.setAttribute("msg", "您的订单:"+oid+"付款成功,付款的金额为:"+money);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/jsp/msg.jsp";
    }
}