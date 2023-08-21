package com.ll.jigumiyak.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModifyPasswordForm {

    @NotEmpty(message = "기존 비밀번호는 필수 항목입니다")
    private String oldPassword;

    @NotEmpty(message = "신규 비밀번호는 필수 항목입니다")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{8,}", message = "영문과 숫자를 조합해서 8자 이상으로 입력해주세요")
    private String newPassword; // 영문 + 숫자, 8자 이상

    @NotEmpty(message = "신규 비밀번호 확인은 필수 항목입니다")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{8,}", message = "영문과 숫자를 조합해서 8자 이상으로 입력해주세요")
    private String newPasswordConfirm; // 영문 + 숫자, 8자 이상
}
