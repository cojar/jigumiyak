let isOldPasswordChecked = false;
let isNewPasswordChecked = false;
let isNewPasswordConfirmChecked = false;

$(function() {
    $("#oldPassword").keyup(_checkOldPassword);
    $("#newPassword").keyup(_checkNewPassword);
    $("#newPasswordConfirm").keyup(_checkNewPasswordConfirm);
});

function _checkOldPassword() {

    isOldPasswordChecked = false;
    _checkModifyPasswordAvailable();

    let oldPassword = $("#oldPassword").val();

    if (oldPassword.length >= 8) {
        $(".alert-oldPassword").text("");
        $.ajax({
            url: "/user/modify/oldPassword",
            type: "GET",
            data: {
              "oldPassword": $("#oldPassword").val()
            },
            success: function(res) {
                console.log(res.code + ": " + res.message);
                $(".alert-oldPassword").removeClass("text-red-400");
                $(".alert-oldPassword").addClass("text-green-700");
                $(".alert-oldPassword").text(res.message);
                isOldPasswordChecked = true;
                _checkModifyPasswordAvailable();

            },
            error: function(res) {
                console.log(res.responseJSON.code + ": " + res.responseJSON.message);
                $(".alert-oldPassword").removeClass("text-green-700");
                $(".alert-oldPassword").addClass("text-red-400");
                $(".alert-oldPassword").text(res.responseJSON.message);
            }
        })
    } else {
        $(".alert-oldPassword").text("");
    }
}

function _checkNewPassword() {

    isNewPasswordChecked = false;
    isNewPasswordConfirmChecked = false;
    _checkModifyPasswordAvailable();

    let newPassword = $("#newPassword").val();
    let newPasswordConfirm = $("#newPasswordConfirm").val();
    let passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9]{8,}$/;

    if (newPassword.length >= 8) {
        if (passwordRegex.test(newPassword)) {
            $(".alert-newPassword").removeClass("text-red-400");
            $(".alert-newPassword").addClass("text-green-700");
            $(".alert-newPassword").text("사용 가능한 비밀번호입니다");
            isNewPasswordChecked = true;
            _checkModifyPasswordAvailable();
        } else {
            $(".alert-newPassword").removeClass("text-green-700");
            $(".alert-newPassword").addClass("text-red-400");
            $(".alert-newPassword").text("영문과 숫자를 조합해서 8자 이상으로 입력해주세요");
        }
    } else {
        $(".alert-newPassword").text("");
    }

    if (newPasswordConfirm.length > newPassword.length) {
        $(".alert-newPasswordConfirm").text("");
    } else if (newPasswordConfirm.length >= 8) {
        if (newPassword === newPasswordConfirm) {
            $(".alert-newPasswordConfirm").removeClass("text-red-400");
            $(".alert-newPasswordConfirm").addClass("text-green-700");
            $(".alert-newPasswordConfirm").text("입력한 비밀번호가 서로 일치합니다");
            isNewPasswordChecked = true;
            isNewPasswordConfirmChecked = true;
            _checkModifyPasswordAvailable();
        } else {
            $(".alert-newPasswordConfirm").removeClass("text-green-700");
            $(".alert-newPasswordConfirm").addClass("text-red-400");
            $(".alert-newPasswordConfirm").text("입력한 비밀번호가 서로 일치하지 않습니다");
        }
    }
}

function _checkNewPasswordConfirm() {

    isNewPasswordConfirmChecked = false;
    _checkModifyPasswordAvailable();

    let newPassword = $("#newPassword").val();
    let newPasswordConfirm = $("#newPasswordConfirm").val();

    if (newPasswordConfirm.length >= 8 && newPasswordConfirm.length >= newPassword.length) {
        if (newPassword === newPasswordConfirm) {
            $(".alert-newPasswordConfirm").removeClass("text-red-400");
            $(".alert-newPasswordConfirm").addClass("text-green-700");
            $(".alert-newPasswordConfirm").text("입력한 비밀번호가 서로 일치합니다");
            isNewPasswordConfirmChecked = true;
            _checkModifyPasswordAvailable();
        } else {
            $(".alert-newPasswordConfirm").removeClass("text-green-700");
            $(".alert-newPasswordConfirm").addClass("text-red-400");
            $(".alert-newPasswordConfirm").text("입력한 비밀번호가 서로 일치하지 않습니다");
        }
    } else {
        $(".alert-newPasswordConfirm").text("");
    }
}

function _checkModifyPasswordAvailable() {

    if (isOldPasswordChecked && isNewPasswordChecked && isNewPasswordConfirmChecked) {
        console.log("checked");
        $("#modify-password-btn").removeClass("deactivated");
    } else {
        $("#modify-password-btn").addClass("deactivated");
    }
}

function _modifyPassword() {
    if (!$("#modify-password-btn").hasClass("deactivated")) {
        $.ajax({
            url: "/user/modify/password",
            type: "POST",
            data: $("#userModifyPasswordForm").serialize(),
            beforeSend : function() {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
            },
            success: function(res) {
                alert(res.message);
                location.href = res.data;
            },
            error: function(res) {
            }
        })
    }
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
            $(".subAddress").val("");
            $(".subAddress").focus();
        }
    }).open();
}