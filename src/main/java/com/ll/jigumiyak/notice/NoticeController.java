package com.ll.jigumiyak.notice;

import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board.BoardForm;
import com.ll.jigumiyak.notice_category.NoticeCategory;
import com.ll.jigumiyak.notice_category.NoticeCategoryService;
import com.ll.jigumiyak.notice_comment.NoticeCommentForm;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
                       @RequestParam(value = "keywordCategory", defaultValue = "") String keywordCategory,
                       @RequestParam(value = "noticeCategory", defaultValue = "") String noticeCategory,
                       @RequestParam(value = "keyword", defaultValue = "") String keyword,
                       @RequestParam(value = "order",  required = false) String order){
        // 페이징 + 페이지에 표시할 수 + 검색 키워드 + 정렬 기준(?) + 게시판의 카테고리
        Page<Notice> noticePaging = this.noticeService.getNoticeList(page, pageSize, keywordCategory, noticeCategory, keyword, order);
        List<NoticeCategory> categoryList = this.noticeCategoryService.getNoticeCategoryList();
        model.addAttribute("paging", noticePaging);
        model.addAttribute("categoryList", categoryList);
        // 검색 값 유지를 위한 modeling
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keywordCategory", keywordCategory);
        model.addAttribute("noticeCategory", noticeCategory);
        model.addAttribute("keyword", keyword);
        model.addAttribute("order", order);
        return "notice_list";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createNotice(NoticeForm noticeForm, Model model){
        List<NoticeCategory> categoryList = this.noticeCategoryService.getNoticeCategoryList();
        model.addAttribute("categoryList", categoryList);
        return "notice_form";
    }

    @PreAuthorize("isAuthenticated()")
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

    @GetMapping("/{id}")
    public String detail(Model model, @PathVariable("id") Long id, NoticeCommentForm noticeCommentForm) {
        Notice notice = this.noticeService.getNotice(id);
        model.addAttribute("notice", notice);
        return "notice_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String boardModify(Model model, NoticeForm noticeForm, @PathVariable("id") Long id, Principal principal) {
        Notice notice = this.noticeService.getNotice(id);

        if(!notice.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        List<NoticeCategory> categoryList = this.noticeCategoryService.getNoticeCategoryList();
        model.addAttribute("categoryList", categoryList);

        noticeForm.setTitle(notice.getTitle());
        noticeForm.setContent(notice.getContent());
        noticeForm.setCategory(notice.getCategory().getName());
        return "notice_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String boardModify(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "notice_form";
        }
        Notice notice = this.noticeService.getNotice(id);
        if (!notice.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        NoticeCategory category = this.noticeCategoryService.getCategoryByName(noticeForm.getCategory());
        this.noticeService.modify(notice, noticeForm.getTitle(), noticeForm.getContent(), category);
        return String.format("redirect:/notice/%d", id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String boardDelete(Principal principal, @PathVariable("id") Long id) {
        Notice notice = this.noticeService.getNotice(id);
        if (!notice.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.noticeService.delete(notice);
        return "redirect:/notice";
    }
}
