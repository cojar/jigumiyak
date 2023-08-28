package com.ll.jigumiyak.user;

import com.ll.jigumiyak.address.AddressForm;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupForm {

    @NotEmpty(message = "아이디는 필수 항목입니다")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{8,20}", message = "영문과 숫자를 조합해서 8자 이상 20자 이하로 입력해주세요")
    private String loginId; // 영문 + 숫자, 8자 이상 20자 이하

    @NotEmpty(message = "비밀번호는 필수 항목입니다")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{8,}", message = "영문과 숫자를 조합해서 8자 이상으로 입력해주세요")
    private String password; // 영문 + 숫자, 8자 이상

    @NotEmpty(message = "비밀번호 확인은 필수 항목입니다")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{8,}", message = "영문과 숫자를 조합해서 8자 이상으로 입력해주세요")
    private String passwordConfirm;

    @NotEmpty(message = "이메일은 필수 항목입니다")
    @Email
    private String email;

    @NotEmpty(message = "발송된 인증번호를 입력해주세요.")
    private String inputCode;

    @NotEmpty
    private String genCode;

    private AddressForm address;
}
