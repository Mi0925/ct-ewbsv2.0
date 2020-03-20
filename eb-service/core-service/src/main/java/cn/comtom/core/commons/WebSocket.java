package cn.comtom.core.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangbingyan
 * @date 2020/1/9
 * @desc websockt操作类
 */
@ServerEndpoint("/websocket/{userId}")
@Component
@Slf4j
public class WebSocket {

    private Session session;
    private String userId = "";

    private static ConcurrentHashMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        webSocketMap.put(userId, this);
        this.userId = userId;
        log.info("【websocket消息】有新的连接, 总数:{}", webSocketMap.size());
    }

    @OnClose
    public void onClose() {
        webSocketMap.remove(this.userId);
        log.info("【websocket消息】连接断开, 总数:{}", webSocketMap.size());
    }


    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端发来的消息1:{}", message); // 群发消息
        this.sendMessage(message);
    }


	/*@OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:" + message);
        //可以自己约定字符串内容，比如 内容|0 表示信息群发，内容|X 表示信息发给id为X的用户
    }*/


    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (WebSocket item : webSocketMap.values()) {
            item.sendMessage(message);
        }
    }

    /**
     * 群发自定义消息
     */
    public static void sendJson(String type, String message) throws IOException {
        String jsonStr = "{\"type\":" + type + ",\"value\":" + message + "}";
        for (WebSocket item : webSocketMap.values()) {
            item.sendMessage(jsonStr);
        }
    }

}
