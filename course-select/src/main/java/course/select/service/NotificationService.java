package course.select.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import course.select.controller.WebSocketController;
import course.select.model.ApiResponse;

@Service
public class NotificationService {

    private final WebSocketController webSocketController;

    @Autowired
    private SelectService selectService;
    
    public NotificationService(WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
    }

    // 每10秒钟发送一次消息
    @Scheduled(fixedRate = 100)
    public void sendPeriodicMessages() throws Exception {
    	Map<String, Object> map = new HashMap<>();
    	map.put("courseRequired", 2);
    	map.put("depId", "IM");
    	map.put("courseYear", LocalDate.now().getYear()-1911);
    	map.put("courseSemester", 1);
    	
    	List<Map<String, Object>> data = selectService.findCourseOfferingInfo(map);
    	ApiResponse<List<Map<String, Object>>> response = new ApiResponse<List<Map<String,Object>>>(true, "查詢成功", data);
        // 創建 ObjectMapper 對象
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 將 response 對象轉換為 JSON 字符串
        String jsonResponse = objectMapper.writeValueAsString(response);
        
        webSocketController.sendToClient(jsonResponse);
    }
}