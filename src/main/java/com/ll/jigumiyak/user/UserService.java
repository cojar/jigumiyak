package com.ll.jigumiyak.user;

import com.ll.jigumiyak.address.Address;
import com.ll.jigumiyak.inquiry.Inquiry;
import com.ll.jigumiyak.security.CustomRole;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    public SiteUser create(String loginId, String password, String email, Address address) {

        SiteUser user = SiteUser.builder()
                .authority(CustomRole.USER.getDecCode())
                .isTemp(false)
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .email(email)
                .address(address)
                .build();

        this.userRepository.save(user);

        return user;
    }

    public String[] genSecurityCode(String prev, int length) {

        String candidateCode = "0123456789abcdefghijklmnopqrstuvwxyz!@#$%^&";
        SecureRandom secureRandom = new SecureRandom();

        String code = "";
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(candidateCode.length());
            code += candidateCode.charAt(index);
        }

        return new String[] {code, this.passwordEncoder.encode(prev + code)};
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
    public boolean sendEmail(Inquiry inquiry, String email, String titleType) {
        Long id = inquiry.getId();

        String url = String.format("https://jigumiyak.com/inquiry/list/%s", id);

        try {
            MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String title = "지금이약 " + titleType + " 발송 메일입니다.";
            String content = "<div style='text-align: center'>";
            content += "<div style='border: 2px solid black; display: inline-block; padding: 10px; text-align: start'>";
            content += "<span>지금이약 [" + titleType + "] 입니다.</span><br>";
            content += "<h2 style='color: indigo; text-align: center'>답변이 등록되었습니다.</h2>";
            content += "<a href='" + url + "' style='color: indigo; text-align: center;'>지금이약 홈페이지</a>";
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

    public SiteUser getUser(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public List<SiteUser> getList() {
        return this.userRepository.findAll();
    }

    public SiteUser getUserByLoginId(String loginId) {
        return this.userRepository.findByLoginId(loginId).orElse(null);
    }

    public SiteUser getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
    }

    public SiteUser getUserByLoginIdAndEmail(String loginId, String email) {
        return this.userRepository.findByLoginIdAndEmail(loginId, email).orElse(null);
    }

    public void modifyPassword(SiteUser user, String password, boolean isTemp) {

        user = user.toBuilder()
                .isTemp(isTemp)
                .password(passwordEncoder.encode(password))
                .build();

        this.userRepository.save(user);
    }

    public void saveAddress(SiteUser user, Address address) {

        user = user.toBuilder()
                .address(address)
                .build();

        this.userRepository.save(user);
    }

    public void modifyAuthorities(SiteUser user, Integer authority) {

        user = user.toBuilder()
                .authority(authority)
                .build();

        this.userRepository.save(user);
    }

    public void withdrawUser(SiteUser user) {

        user = user.toBuilder()
                .authority(CustomRole.WITHDRAWAL_USER.getDecCode())
                .build();

        this.userRepository.save(user);
    }

    public void blacklistUser(SiteUser user) {

        user = user.toBuilder()
                .authority(CustomRole.BLACKLIST.getDecCode())
                .build();

        this.userRepository.save(user);
    }
}
