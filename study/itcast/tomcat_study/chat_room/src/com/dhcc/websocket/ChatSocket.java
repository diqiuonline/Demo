package com.dhcc.websocket;

import com.alibaba.fastjson.JSON;
import com.dhcc.util.MessageUtil;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/websocket", configurator = GetHttpSessionConfigurator.class)
public class ChatSocket {
    private Session session;
    private HttpSession httpSession;
    //保存当前系统中登陆的用户的httpsession信息 ，及其对应的Endpoint 实例信息
    private static Map<HttpSession, ChatSocket> onlineUsers = new HashMap<>();
    private static int onlineCount = 0;

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        //1记录websocker的会话信息的session
        this.session = session;
        //2获取当前登陆用户的httpsession信息
        HttpSession httpSession = (HttpSession) endpointConfig.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;
        System.out.println("当前登陆用户：" + httpSession.getAttribute("username") + ",Endpoint:" + hashCode());
        //3 记录当前登陆信息，及其对应的Endpoint实例
        if (httpSession.getAttribute("username") != null) {
            onlineUsers.put(httpSession, this);
        }
        //4 获取当前所登陆用户-------》
        String names = getNames();
        //5 组装消息 -- 》 {"data":"HEIMA,Deng,ITCAST","toName":"","fromName":"","type":"user"}
        String message = MessageUtil.getContent(MessageUtil.TYPE_USER, "", "", names);
        //6 以广播的形式发送消息
        broadcastAllUsers(message);
        //7 记录当前用户登陆数
        incrCount();
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("onMessage:name=" + httpSession.getAttribute("username") + ",message=" + message);
        //从客户端获取信息内容 并解析
        Map<String, String> messageMap = JSON.parseObject(message, Map.class);
        String fromName = messageMap.get("fromName");
        String toName = messageMap.get("toName");
        String content = messageMap.get("content");
        //判定是否有接收人
        if (toName == null || toName.isEmpty()) {
            return;
        }
        //3 判定接受人是否是广播，如果是，则说明是广播信息
        String messageContent = MessageUtil.getContent(MessageUtil.TYPE_MESSAGE, fromName, toName, content);
        System.out.println("服务端给客户端发送消息，消息内容：" + messageContent);
        if ("all".equals(toName)) {
            //组装消息内容
            broadcastAllUsers(messageContent);
        } else {
            //不是 则给指定的用户推送消息
            singlePushMessage(messageContent,fromName,toName);
        }
    }
    //给指定的用户推送消息
    private void singlePushMessage(String messageContent, String fromName, String toName) throws IOException {
        boolean isOnline = false;
        //1判定接收人是否在线
        for (HttpSession httpSession : onlineUsers.keySet()) {
            if (toName.equals(httpSession.getAttribute("username"))) {
                isOnline = true;
            }
        }
        //2 如果存在 发送消息
        if (isOnline) {
            for (HttpSession httpSession : onlineUsers.keySet()) {
                if (httpSession.getAttribute("username").equals(fromName) || httpSession.getAttribute
                        ("username").equals(toName)) {
                    onlineUsers.get(httpSession).session.getBasicRemote().sendText(messageContent);
                }
            }
        }
    }

    //发送广播信息
    private void broadcastAllUsers(String message) {
        for (HttpSession httpSession : onlineUsers.keySet()) {
            try {
                onlineUsers.get(httpSession).session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //获取所有在线用户
    private String getNames() {
        String names = "";
        if (onlineUsers.size() > 0) {
            for (HttpSession httpSession : onlineUsers.keySet()) {
                String username = (String) httpSession.getAttribute("username");
                names += username + ",";
            }
        }
        return names.substring(0, names.length() - 1);
    }

    public int getOnlineCount(){
        return onlineCount;
    }


    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        decrCount();
        System.out.println("客户端关闭了一个连接，当前在线人数："+onlineCount);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
        System.out.println("服务异常");
    }

    public synchronized void incrCount(){
        onlineCount ++;
    }

    public synchronized void decrCount(){
        onlineCount --;
    }
}
