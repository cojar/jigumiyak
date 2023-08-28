package com.ll.jigumiyak.admin;

import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board.BoardService;
import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.board_comment.BoardCommentService;
import com.ll.jigumiyak.board_recomment.BoardRecomment;
import com.ll.jigumiyak.board_recomment.BoardRecommentService;
import com.ll.jigumiyak.faq.Faq;
import com.ll.jigumiyak.faq.FaqService;
import com.ll.jigumiyak.inquiry.Inquiry;
import com.ll.jigumiyak.inquiry.InquiryForm;
import com.ll.jigumiyak.inquiry.InquiryService;
import com.ll.jigumiyak.inquiry_answer.InquiryAnswerForm;
import com.ll.jigumiyak.purchase.Purchase;
import com.ll.jigumiyak.purchase.PurchaseService;
import com.ll.jigumiyak.notice.Notice;
import com.ll.jigumiyak.notice.NoticeService;
import com.ll.jigumiyak.notice_comment.NoticeComment;
import com.ll.jigumiyak.notice_comment.NoticeCommentService;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@PreAuthorize("hasAuthority('admin')")
@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final BoardService boardService;
    private final BoardCommentService boardCommentService;
    private final BoardRecommentService boardRecommentService;
    private final FaqService faqService;
    private final InquiryService inquiryService;
    private final UserService userService;
    private final PurchaseService purchaseService;
    private final NoticeService noticeService;
    private final NoticeCommentService noticeCommentService;

    @GetMapping("")
    public String admin() {
        return "admin/administration";
    }

    // 커뮤니티 구간
    @GetMapping("/board")
    public String board(Model model, @RequestParam(value="page", defaultValue="0") int page,
                        @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Board> paging = this.boardService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "admin/admin_board";
    }

    @GetMapping("/board/{id}")
    public String comment(Model model, @PathVariable("id") Long id, @RequestParam(value="page", defaultValue="0") int page,
                          @RequestParam(value = "cmtPage", defaultValue = "0") int cmtPage) {
        Board board = this.boardService.getBoard(id);
        model.addAttribute("board", board);

        Page<BoardComment> paging = this.boardCommentService.getList(board, 20,cmtPage);
        model.addAttribute("paging", paging);
        return "admin/admin_board_comment";
    }

    @GetMapping("/board/delete/{id}")
    public String boardDelete(@PathVariable("id") Long id) {
        Board board = this.boardService.getBoard(id);
        this.boardService.delete(board);
        return "redirect:/admin/board";
    }

    @GetMapping("/comment/delete/{id}")
    public String commentDelete( @PathVariable("id") Long id) {
        BoardComment boardComment = this.boardCommentService.getBoardComment(id);
        this.boardCommentService.delete(boardComment);
        return String.format("redirect:/admin/board/%s", boardComment.getBoard().getId());
    }

    @GetMapping("/comment/{id}")
    public String recomment(Model model, @PathVariable("id") Long id) {
        BoardComment boardComment = this.boardCommentService.getBoardComment(id);
        model.addAttribute("comment", boardComment);

        List<BoardRecomment> recommentList = this.boardRecommentService.getList(boardComment);
        model.addAttribute("recommentList", recommentList);
        return "admin/admin_board_recomment";
    }

    @GetMapping("/recomment/delete/{id}")
    public String recommentDelete( @PathVariable("id") Long id) {
        BoardRecomment boardRecomment = this.boardRecommentService.getBoareRecomment(id);
        this.boardRecommentService.delete(boardRecomment);
        return String.format("redirect:/admin/comment/%s", boardRecomment.getComment().getId());
    }

    // 문의
    @GetMapping("/inquiry")
    public String inquiry(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Inquiry> paging = this.inquiryService.getList(page, false);
        model.addAttribute("paging", paging);
        return "admin/admin_inquiry";
    }

    @GetMapping("/inquiry/{id}")
    public String inquiryDetail (Model model, @PathVariable("id") Long id, InquiryForm inquiryForm, InquiryAnswerForm inquiryAnswerForm) {
        Inquiry inquiry = this.inquiryService.getInquiry(id);
        model.addAttribute("inquiry", inquiry);
        return "admin/inquiry_detail";
    }

    @GetMapping("/inquiry/done")
    public String inquiryDone(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Inquiry> paging = this.inquiryService.getList(page,true);
        model.addAttribute("paging", paging);
        return "admin/inquiry_done";
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        List<Faq> faqList = this.faqService.getList();
        model.addAttribute("faqList", faqList);
        return "admin/admin_faq";
    }

    @GetMapping("/user")
    public String userManage(Model model) {

        List<SiteUser> userList = this.userService.getList();
        model.addAttribute("userList", userList);

        return "admin/admin_user";
    }

    @GetMapping("/purchase")
    public String purchaseManage(Model model) {

        List<Purchase> purchaseList = this.purchaseService.getList();
        model.addAttribute("purchaseList", purchaseList);

        return "admin/admin_purchase";
    }

    // 공지사항 관리
    @GetMapping("/notice")
    public String notice(Model model, @RequestParam(value="page", defaultValue="0") int page,
                        @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Notice> paging = this.noticeService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "admin/notice";
    }

    @GetMapping("/notice/{id}")
    public String noticeComment(Model model, @PathVariable("id") Long id, @RequestParam(value="page", defaultValue="0") int page,
                          @RequestParam(value = "cmtPage", defaultValue = "0") int cmtPage) {
        Notice notice = this.noticeService.getNotice(id);
        model.addAttribute("notice", notice);

        Page<NoticeComment> paging = this.noticeCommentService.getList(notice, 20, cmtPage);
        model.addAttribute("paging", paging);
        return "admin/notice_comment";
    }

    @GetMapping("/notice/delete/{id}")
    public String noticeDelete(@PathVariable("id") Long id) {
        Notice notice = this.noticeService.getNotice(id);
        this.noticeService.delete(notice);
        return "redirect:/admin/notice";
    }

    @GetMapping("/ncomment/delete/{id}")
    public String ncommentDelete( @PathVariable("id") Long id) {
        NoticeComment noticeComment = this.noticeCommentService.getCommentById(id);
        this.noticeCommentService.delete(noticeComment);
        return String.format("redirect:/admin/notice/%s", noticeComment.getNotice().getId());
    }
}
