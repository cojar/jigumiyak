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

        // json test
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

        // message data
        String senderId = (String) session.getAttributes().get("sessionId");
        String payload = message.getPayload();

        System.out.println("payload >>> " + payload);

        dataMap = jsonToMap(payload);
        dataMap.put("senderId", senderId);
        dataMap.put("time", time);
        dataMap.put("masterStatus", masterStatus);
        dataMap.put("onlineList", onlineList);

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

    // connection established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // save into session list
        String senderId = (String) session.getAttributes().get("sessionId");
        sessionList.add(session);
        log.info("sessionId >>> " + senderId);
        onlineList.add(senderId);
        userSession.put(senderId, session);

        // as master send message to all
        if (senderId.equals("master")) {
            TextMessage msg = new TextMessage(senderId + " 님이 접속했습니다.");
            sendToAll(msg, senderId);

        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("message", senderId + "님이 접속하셨습니다.");
            data.put("receiverId", "master");
            data.put("newOne", senderId);

            TextMessage msgToMaster = new TextMessage(json.writeValueAsString(data));
            handleMessage(session, msgToMaster);
        }

        log.info(session + " client connected");
    }

    // connection closed
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String senderId = (String) session.getAttributes().get("sessionId");
        sessionList.remove(session);
        onlineList.remove(senderId);
        userSession.remove(senderId);


        // as master send to all
        if (senderId.equals("master")) {
            TextMessage msg = new TextMessage(senderId + " 님이 퇴장했습니다.");
            sendToAll(msg, senderId);

        } else {
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

    // json string parsing to map
    public Map<String, Object> jsonToMap(String jsonString) throws JsonMappingException, JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper jmapper = new ObjectMapper();
        map = jmapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
        });

        return map;
    }

    // send a message to all
    public void sendToAll(TextMessage message, String senderId) throws Exception {
        // json test
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

        //
        for (String r : userSession.keySet()) {
            dataMap.put("receiverId", r);
            String msg = json.writeValueAsString(dataMap);
            userSession.get(r).sendMessage(new TextMessage(msg));
        }
    }

    public void sendOnlineList(WebSocketSession session) throws IOException {
        Map<String, Object> map = new HashMap<>();

        map.put("onlineList", onlineList);
        String list = json.writeValueAsString(map);

        log.info("map >>> " + map);
        session.sendMessage(new TextMessage(list));
    }
}
