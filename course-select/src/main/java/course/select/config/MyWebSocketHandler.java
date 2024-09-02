package course.select.config;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 從 session 中獲取使用者資訊
        String userId = (String) session.getAttributes().get("userId");
        // 根據 userId 或 token 做相應的操作
        String depId = userId.substring(0, 2);
        System.out.println(depId);
    }

}
