function _checkLoginId() {
    $(".alert-loginId").text("");
    $(".alert-loginId").removeClass("text-green-700");
    $(".alert-loginId").removeClass("text-red-400");
    $.ajax({
        url: "/user/signup/loginId",
        type: "GET",
        data: {
          "loginId": $("#loginId").val()
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            $(".alert-loginId").text(res.message);
            $(".alert-loginId").addClass("text-green-700");
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            $(".alert-loginId").text(res.responseJSON.message);
            $(".alert-loginId").addClass("text-red-400");
        }
    })
}

function _genCode() {
    $(".alert-email").text("");
    $(".alert-email").removeClass("text-green-700");
    $(".alert-email").removeClass("text-red-400");
    $.ajax({
        url: "/user/signup/email",
        type: "GET",
        data: {
          "email": $("#email").val()
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            $("#genCode").val(res.data);
            $(".alert-email").text(res.message);
            $(".alert-email").addClass("text-green-700");
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            $(".alert-email").text(res.responseJSON.message);
            $(".alert-email").addClass("text-red-400");
        }
    })
}

function _checkCode() {
    $(".alert-emailCode").text("");
    $(".alert-emailCode").removeClass("text-green-700");
    $(".alert-emailCode").removeClass("text-red-400");
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
            $(".alert-emailCode").text("이메일 인증이 완료되었습니다.");
            $(".alert-emailCode").addClass("text-green-700");
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            $(".alert-emailCode").text(res.responseJSON.message);
            $(".alert-emailCode").addClass("text-red-400");
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