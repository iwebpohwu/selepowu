package course.select.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import course.select.model.ApiResponse;
import course.select.service.SelectService;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SelectService selectService;
    
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public String sendMessage(@Payload Map<String, Object> param) throws Exception {
    	List<Map<String, Object>> data = selectService.findCourseOfferingInfo(param);
    	ApiResponse<List<Map<String, Object>>> response = new ApiResponse<List<Map<String,Object>>>(true, "查詢成功", data);
        // 創建 ObjectMapper 對象
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 將 response 對象轉換為 JSON 字符串
        String jsonResponse = objectMapper.writeValueAsString(response);
        
        // 返回 JSON 字符串給前端
        return jsonResponse;
    }

    // 後端主動發送請求給前端
    public void sendToClient(String message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}