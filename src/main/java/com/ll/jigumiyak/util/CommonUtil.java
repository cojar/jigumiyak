package com.ll.jigumiyak.util;

import com.ll.jigumiyak.file.FileService;
import com.ll.jigumiyak.file.GenFile;
import com.ll.jigumiyak.security.CustomRole;
import com.ll.jigumiyak.social_account.SocialAccount;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Principal;

@RequiredArgsConstructor
@Component
public class CommonUtil {

    private final UserService userService;
    private final FileService fileService;

    public SiteUser usernameToUser(String loginId) {
        return this.userService.getUserByLoginId(loginId);
    }

    public String getFilePath(Long id) {
        GenFile file = this.fileService.getFile(id);
        return this.fileService.getFilePath(file);
    }

    public boolean isLinked(String provider, Principal principal) {

        SiteUser user = this.userService.getUserByLoginId(principal.getName());

        for (SocialAccount socialAccount : user.getSocialAccountList()) {
            if (socialAccount.getProviderId().startsWith(provider)) return true;
        }

        return false;
    }

    public boolean isAdmin(Principal principal) {

        SiteUser user = this.userService.getUserByLoginId(principal.getName());

        if (user == null) return false;

        return user.getAuthorities().contains(new SimpleGrantedAuthority(CustomRole.ADMIN.getType()));
    }

    public String markdown(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
