package com.ll.jigumiyak.notice;

import com.ll.jigumiyak.notice_category.NoticeCategory;
import com.ll.jigumiyak.notice_category.NoticeCategoryRepository;
import com.ll.jigumiyak.notice_category.NoticeCategoryService;
import com.ll.jigumiyak.notice_comment.CommentForm;
import com.ll.jigumiyak.notice_comment.NoticeComment;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;
    private final UserService userService;
    private final NoticeCategoryService noticeCategoryService;

    @GetMapping("")
    public String main(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                       @RequestParam(value = "keywordCategory", required = false) String keywordCategory,
                       @RequestParam(value = "category", required = false) String category,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "order",  required = false) String order){
        // 페이징 + 페이지에 표시할 수 + 검색 키워드 + 정렬 기준(?) + 게시판의 카테고리
        Page<Notice> noticePaging = this.noticeService.getNoticeList(page, pageSize, keywordCategory, category, keyword, order);
        List<NoticeCategory> categoryList = this.noticeCategoryService.getNoticeCategoryList();
        model.addAttribute("paging", noticePaging);
        model.addAttribute("categoryList", categoryList);
        return "notice_list";
    }
    @GetMapping("/create")
    public String createNotice(NoticeForm noticeForm, Model model){
        List<NoticeCategory> categoryList = this.noticeCategoryService.getNoticeCategoryList();
        model.addAttribute("categoryList", categoryList);
        return "notice_form";
    }

    @PostMapping("/create")
    public String createNotice(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            return "notice_form";
        }
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());
        NoticeCategory category = noticeCategoryService.getCategoryByName(noticeForm.getCategory());
        this.noticeService.create(category, noticeForm.getTitle(), noticeForm.getContent(), siteUser);
        return "redirect:/notice";
    }
}
