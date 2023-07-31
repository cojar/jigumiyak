package com.ll.jigumiyak.notice;

import com.ll.jigumiyak.notice_comment.CommentForm;
import com.ll.jigumiyak.notice_comment.NoticeComment;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("")
    public String main(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                       @RequestParam(value = "keywordCategory", required = false) String keywordCategory,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "order",  required = false) String order,
                       @RequestParam(value = "category", required = false) String category){
        // 페이징 + 페이지에 표시할 수 + 검색 키워드 + 정렬 기준(?) + 게시판의 카테고리
        Page<Notice> noticePaging = this.noticeService.getNoticeList(page, pageSize, keywordCategory, keyword, order, category);
        model.addAttribute("noticePaging", noticePaging);
        return "noticeList";
    }
    @PostMapping("/create")
    public String createNotice(){
        return "redirect:noticeList";
    }
}
