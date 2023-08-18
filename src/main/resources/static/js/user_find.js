function _findLoginId() {

    $(".alert-id-email").text("");

    $.ajax({
        url: "/user/find/loginId",
        type: "GET",
        data: {
          "email": $("#id-email").val()
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            $(".alert-id-email").removeClass("text-red-400");
            $(".alert-id-email").addClass("text-green-700");
            $(".alert-id-email").text(res.message);
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            $(".alert-id-email").removeClass("text-green-700");
            $(".alert-id-email").addClass("text-red-400");
            $(".alert-id-email").text(res.responseJSON.message);
        }
    })
}

function _findPassword() {

    $(".alert-loginId").text("");
    $(".alert-pw-email").text("");

    $.ajax({
        url: "/user/find/password",
        type: "POST",
        data: {
            "loginId": $("#loginId").val(),
            "email": $("#pw-email").val()
        },
        beforeSend : function() {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            $(".alert-loginId").removeClass("text-red-400");
            $(".alert-loginId").addClass("text-green-700");
            $(".alert-loginId").text(res.message);
        },
        error: function(res) {
            if (res.responseJSON.loginId !== undefined) {
                console.log(res.responseJSON.loginId.code + ": " + res.responseJSON.loginId.message);
                $(".alert-loginId").removeClass("text-green-700");
                $(".alert-loginId").addClass("text-red-400");
                $(".alert-loginId").text(res.responseJSON.loginId.message);
            }
            if (res.responseJSON.email !== undefined) {
                console.log(res.responseJSON.email.code + ": " + res.responseJSON.email.message);
                $(".alert-pw-email").removeClass("text-green-700");
                $(".alert-pw-email").addClass("text-red-400");
                $(".alert-pw-email").text(res.responseJSON.email.message);
            }
        }
    })
}