package com.ll.jigumiyak.user;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.address.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    public SiteUser create(String loginId, String password, String email, Address address) {

        SiteUser user = new SiteUser();

        List<String> authorityList = new ArrayList<>();
        if(loginId.equals("admin_jigumiyak")) authorityList.add(UserRole.ADMIN.getValue());
        authorityList.add(UserRole.USER.getValue());

        user.setAuthorityList(authorityList);
        user.setLoginId(loginId);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setAddress(address);
        user.setCreateDate(LocalDateTime.now());

        this.userRepository.save(user);

        return user;
    }

    public String[] genSecurityCode(String email, int length) {

        String candidateCode = "0123456789abcdefghijklmnopqrstuvwxyz!@#$%^&";
        SecureRandom secureRandom = new SecureRandom();

        String code = "";
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(candidateCode.length());
            code += candidateCode.charAt(index);
        }

        return new String[] {code, this.passwordEncoder.encode(email + code)};
    }

    public boolean sendEmail(String email, String code, String titleType, String contentType) {

        try {
            MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String title = "지금이약 " + titleType + " 발송 메일입니다.";
            String content = "<div style='text-align: center'>";
            content += "<div style='border: 2px solid black; display: inline-block; padding: 10px; text-align: start'>";
            content += "<span>지금이약 [" + titleType + "] 입니다.</span><br>";
            content += "<span>아래 " + contentType + " 입력해주세요.</span>";
            content += "<h2 style='color: indigo; text-align: center'>" + code + "</h2>";
            content += "</div>";
            content += "</div>";

            helper.setTo(email);
            helper.setSubject(title);
            helper.setText(content, true);

            this.javaMailSender.send(mimeMessage);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public boolean isMatched(String raw, String encoded) {
        return this.passwordEncoder.matches(raw, encoded);
    }

    public boolean isDuplicatedLoginId(String loginId) {
        Optional<SiteUser> _user = this.userRepository.findByLoginId(loginId);
        return _user.isPresent();
    }

    public boolean isDuplicatedEmail(String email) {
        Optional<SiteUser> _user = this.userRepository.findByEmail(email);
        return _user.isPresent();
    }

    public SiteUser getUserByLoginId(String loginId) {
        return this.userRepository.findByLoginId(loginId).orElse(null);
    }

    public SiteUser getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
    }
}
