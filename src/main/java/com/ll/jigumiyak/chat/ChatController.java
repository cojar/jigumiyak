package com.ll.jigumiyak.chat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Log4j2
public class ChatController {

    @GetMapping("/chat")
    public void getChat(HttpServletRequest request, @RequestParam String id) {

        HttpSession session = request.getSession();
        System.out.println("chat param id 값 출력 : "  + id);

        if (id.equals("guest")) {
            String name = "guest" + session.toString().substring(session.toString().indexOf("@"));
            session.setAttribute("sessionId", name);
        } else if(id.equals("master")) {
            String name = "master";
            session.setAttribute("sessionId", name);
        }

        log.info("@ChatController, getChat()");
    }

    @GetMapping("/chat/master")
    public String enterChatAsMaster(HttpServletRequest request) {

        log.info("@ChatController, getChat()");
        return "/chat";
    }
}
