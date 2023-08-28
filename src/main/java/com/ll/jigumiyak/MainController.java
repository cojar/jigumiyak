package com.ll.jigumiyak;

import com.ll.jigumiyak.notice.Notice;
import com.ll.jigumiyak.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final NoticeService noticeService;

    @GetMapping("/")
    public String root(){
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        Page<Notice> noticePaging = this.noticeService.getNoticeList(0, 1, "", "공지사항", "", "");
        model.addAttribute("paging", noticePaging);
        return "index";
    }
}
