package com.ll.jigumiyak.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {

    private static List<String> onlineList = new ArrayList<>();
    private static List<WebSocketSession> sessionList = new ArrayList<>();
    Map<String, WebSocketSession> userSession = new HashMap<>();

    ObjectMapper json = new ObjectMapper();

    // message
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // websocket으로 수신한 텍스트 메시지 처리
        // 메세지 데이터 저장
        Map<String, Object> dataMap = new HashMap<>();

        // master status
        String masterStatus = null;
        if (userSession.containsKey("master")) {
            masterStatus = "online";
        } else {
            masterStatus = "offline";
        }

        // sending time
        LocalDateTime currentTime = LocalDateTime.now();
        String time = currentTime.format(DateTimeFormatter.ofPattern("hh:mm a, E"));

        // message data 메시지 보낸 아이디와 메시지 내용을 변수에 저장
        String senderId = (String) session.getAttributes().get("sessionId");
        String payload = message.getPayload();

        System.out.println("payload >>> " + payload);

        // json문자열을 dataMap으로 변환하고 추가 데이터 저장
        dataMap = jsonToMap(payload);
        dataMap.put("senderId", senderId);
        dataMap.put("time", time);
        dataMap.put("masterStatus", masterStatus);
        dataMap.put("onlineList", onlineList);

        //수신자 id
        String receiverId = (String) dataMap.get("receiverId");

        log.info("final dataMap >>> " + dataMap);

        // send a message
        System.out.println("receiver session >>> " + userSession.get(receiverId));
        String msg = json.writeValueAsString(dataMap);

        if (userSession.get(receiverId) != null) {
            userSession.get(receiverId).sendMessage(new TextMessage(msg)); // send to receiver
        }

        // send a message myself
        if(!senderId.equals(receiverId)) {
            dataMap.put("receiverId", senderId);
            msg = json.writeValueAsString(dataMap);
            session.sendMessage(new TextMessage(msg)); // send to myself
        }
    }

    // 웹소켓 연결되고 사용자가 서버 연결되었을 때 메세지 출력
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // 세션 목록에 저장
        String senderId = (String) session.getAttributes().get("sessionId");
        sessionList.add(session);
        log.info("sessionId >>> " + senderId);
        onlineList.add(senderId);
        userSession.put(senderId, session);

        // 마스터 접속 시 모든 사용자에 메시지 전송
        if (senderId.equals("master")) {
            TextMessage msg = new TextMessage(senderId + " 님이 접속했습니다.");
            sendToAll(msg, senderId);

        } else {
            // 일반 사용자인 경우 마스터에게 접속 메시지 전송
            Map<String, Object> data = new HashMap<>();
            data.put("message", senderId + "님이 접속하셨습니다.");
            data.put("receiverId", "master");
            data.put("newOne", senderId);

            // 메시지를 json 형식으로 바꾸고 마스터에 전송
            TextMessage msgToMaster = new TextMessage(json.writeValueAsString(data));
            handleMessage(session, msgToMaster);
        }

        log.info(session + " client connected");
    }

    // 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        // 세션목록에서 제거하고 온라인 사용자 목록, 사용자 세션에서 제거
        String senderId = (String) session.getAttributes().get("sessionId");
        sessionList.remove(session);
        onlineList.remove(senderId);
        userSession.remove(senderId);


        // 마스터 퇴장시 모든 사용자에게 메시지 전송
        if (senderId.equals("master")) {
            TextMessage msg = new TextMessage(senderId + " 님이 퇴장했습니다.");
            sendToAll(msg, senderId);

        } else {
            // 일반 사용자 퇴장시 마스터에게 전송
            Map<String, Object> data = new HashMap<>();
            data.put("message", senderId + "님이 퇴장하셨습니다.");
            data.put("receiverId", "master");
            data.put("outOne", senderId );

            TextMessage msg = new TextMessage(json.writeValueAsString(data));
            handleMessage(session, msg);
        }
        log.info(session + "client disconnected");
    }

    public void getOnlineList() throws IOException {
    }

    // json 문자열을 map형식으로 변환
    public Map<String, Object> jsonToMap(String jsonString) throws JsonMappingException, JsonProcessingException {
        Map<String, Object> map = new HashMap<>();

        // ObjectMapper를 사용하여 JSON 문자열을 Map으로 변환
        ObjectMapper jmapper = new ObjectMapper();
        map = jmapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
        });

        return map;
    }

    // 모든 사용자에게 메시지 전송
    public void sendToAll(TextMessage message, String senderId) throws Exception {
        // map 생성
        Map<String, Object> dataMap = new HashMap<>();

        // master status
        String masterStatus = null;
        if (userSession.containsKey("master")) {
            masterStatus = "online";
        } else {
            masterStatus = "offline";
        }

        // sending time
        LocalDateTime currentTime = LocalDateTime.now();
        String time = currentTime.format(DateTimeFormatter.ofPattern("hh:mm a E"));

        // message data
        String payload = message.getPayload();

        log.info("payload >>> " + payload);

        dataMap.put("message", message.getPayload());
        dataMap.put("senderId", senderId);
        dataMap.put("time", time);
        dataMap.put("masterStatus", masterStatus);
        dataMap.put("onlineList", onlineList);	// user online status
        dataMap.put("newOne", "master");

        String receiverId = (String) dataMap.get("receiverId");

        log.info("final dataMap >>> " + dataMap);

        // send a message
        log.info("receiver session >>> " + userSession.get(receiverId));

        // 모든 사용자에게 전송
        for (String r : userSession.keySet()) {
            dataMap.put("receiverId", r);
            String msg = json.writeValueAsString(dataMap);
            userSession.get(r).sendMessage(new TextMessage(msg));
        }
    }

    // 온라인 사용자 목록을 클라이언트에 전송
    public void sendOnlineList(WebSocketSession session) throws IOException {
        Map<String, Object> map = new HashMap<>();
        
        // 온라인 사용자 목록 저장
        map.put("onlineList", onlineList);
        String list = json.writeValueAsString(map);

        log.info("map >>> " + map);
        session.sendMessage(new TextMessage(list));
    }
}
