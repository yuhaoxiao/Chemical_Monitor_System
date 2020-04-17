package cn.nju.edu.chemical_monitor_system.utils.socket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import cn.nju.edu.chemical_monitor_system.utils.common.SpringContextUtil;
import cn.nju.edu.chemical_monitor_system.utils.kafka.KafkaUtil;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/websocket")//原型模式，不会变成单例，springboot默认给每个socket都创建一个新的连接
@Component
public class WebSocketUtil {

    //记录当前在线连接数。
    private static int onlineCount = 0;

    //每个客户端对应的WebSocket对象
    private static CopyOnWriteArraySet<WebSocketUtil> webSocketSet = new CopyOnWriteArraySet<>();

    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        KafkaUtil kafkaUtil = (KafkaUtil) SpringContextUtil.getBean("kafkaUtil");
        if (webSocketSet.size() == 0) {
            kafkaUtil.start();
        }
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        try {
            sendMessage("当前在线人数为" + getOnlineCount());
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        KafkaUtil kafkaUtil = (KafkaUtil) SpringContextUtil.getBean("kafkaUtil");
        webSocketSet.remove(this);
        if (webSocketSet.size() == 0) {
            kafkaUtil.stop();
        }
        subOnlineCount();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public void sendInfo(String message) {
        for (WebSocketUtil item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException ignored) {
            }
        }
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketUtil.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketUtil.onlineCount--;
    }
}
