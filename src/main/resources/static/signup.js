let isLoginIdChecked = false;
let isPasswordChecked = false;
let isPasswordConfirmChecked = false;

$(function() {
    $("#password").keyup(_checkPassword);
    $("#passwordConfirm").keyup(_checkPasswordConfirm);
});

function _checkLoginId() {
    $(".alert-loginId").text("");
    $.ajax({
        url: "/user/signup/loginId",
        type: "GET",
        data: {
          "loginId": $("#loginId").val()
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            $(".alert-loginId").removeClass("text-red-400");
            $(".alert-loginId").addClass("text-green-700");
            $(".alert-loginId").text(res.message);
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            $(".alert-loginId").removeClass("text-green-700");
            $(".alert-loginId").addClass("text-red-400");
            $(".alert-loginId").text(res.responseJSON.message);
        }
    })
}

function _checkPassword() {

    let password = $("#password").val();
    let passwordConfirm = $("#passwordConfirm").val();
    let passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9]{8,}$/;

    if (password.length >= 8) {
        if (passwordRegex.test(password)) {
            $(".alert-password").removeClass("text-red-400");
            $(".alert-password").addClass("text-green-700");
            $(".alert-password").text("사용 가능한 비밀번호입니다");
        } else {
            $(".alert-password").removeClass("text-green-700");
            $(".alert-password").addClass("text-red-400");
            $(".alert-password").text("영문과 숫자를 조합해서 8자 이상으로 입력해주세요");
        }
    } else {
        $(".alert-password").text("");
    }

    if (passwordConfirm.length > password.length) {
        $(".alert-passwordConfirm").text("");
    } else if (passwordConfirm.length >= 8) {
        if (password === passwordConfirm) {
            $(".alert-passwordConfirm").removeClass("text-red-400");
            $(".alert-passwordConfirm").addClass("text-green-700");
            $(".alert-passwordConfirm").text("입력한 비밀번호가 서로 일치합니다");
        } else {
            $(".alert-passwordConfirm").removeClass("text-green-700");
            $(".alert-passwordConfirm").addClass("text-red-400");
            $(".alert-passwordConfirm").text("입력한 비밀번호가 서로 일치하지 않습니다");
        }
    }
}

function _checkPasswordConfirm() {

    let password = $("#password").val();
    let passwordConfirm = $("#passwordConfirm").val();

    if (passwordConfirm.length >= 8 && passwordConfirm.length >= password.length) {
        if (password === passwordConfirm) {
            $(".alert-passwordConfirm").removeClass("text-red-400");
            $(".alert-passwordConfirm").addClass("text-green-700");
            $(".alert-passwordConfirm").text("입력한 비밀번호가 서로 일치합니다");
        } else {
            $(".alert-passwordConfirm").removeClass("text-green-700");
            $(".alert-passwordConfirm").addClass("text-red-400");
            $(".alert-passwordConfirm").text("입력한 비밀번호가 서로 일치하지 않습니다");
        }
    } else {
        $(".alert-passwordConfirm").text("");
    }
}

function _genCode() {
    $(".alert-email").text("");
    $.ajax({
        url: "/user/signup/email",
        type: "GET",
        data: {
          "email": $("#email").val()
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            $("#genCode").val(res.data);
            $(".alert-email").removeClass("text-red-400");
            $(".alert-email").addClass("text-green-700");
            $(".alert-email").text(res.message);
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            $(".alert-email").removeClass("text-green-700");
            $(".alert-email").addClass("text-red-400");
            $(".alert-email").text(res.responseJSON.message);
        }
    })
}

function _checkCode() {
    $(".alert-emailCode").text("");
    $.ajax({
        url: "/user/signup/code",
        type: "GET",
        data: {
        "email": $("#email").val(),
        "inputCode": $("#inputCode").val(),
        "genCode": $("#genCode").val()
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            $(".alert-emailCode").removeClass("text-red-400");
            $(".alert-emailCode").addClass("text-green-700");
            $(".alert-emailCode").text("이메일 인증이 완료되었습니다.");
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            $(".alert-emailCode").removeClass("text-green-700");
            $(".alert-emailCode").addClass("text-red-400");
            $(".alert-emailCode").text(res.responseJSON.message);
        }
    })
}

function _zoneCode() {
    new daum.Postcode({
        oncomplete: function (data) {
            var mainAddress = "";

            if (data.userSelectedType === "R") {
                mainAddress = data.roadAddress;
            } else {
                mainAddress = data.jibunAddress;
            }

            $(".zoneCode").val(data.zonecode);
            $(".mainAddress").val(mainAddress);
            $(".subAddress").focus();
        }
    }).open();
}

function _signup() {
    if (!$("#signup-btn").hasClass("deactivated")) {
        $.ajax({
            url: "/user/signup",
            type: "POST",
            data: $("#userSignupForm").serialize(),
            beforeSend : function() {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
            },
            success: function(data) {
                alert(data);
                location.href = "/user/login";
            },
            error: function(res) {
                let errorMsg = "";
                $.each(res.responseJSON, function(key, value){
                    errorMsg += value + "\n";
                });
                alert(errorMsg);
            }
        })
    }
}