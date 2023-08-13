function _findLoginId() {

//    $(".alert-email").text("");
//    $(".alert-inputCode").text("");

    $.ajax({
        url: "/user/find/loginId",
        type: "GET",
        data: {
          "email": $("#id-email").val()
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
//            $(".alert-email").removeClass("text-red-400");
//            $(".alert-email").addClass("text-green-700");
//            $(".alert-email").text(res.message);
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
//            $(".alert-email").removeClass("text-green-700");
//            $(".alert-email").addClass("text-red-400");
//            $(".alert-email").text(res.responseJSON.message);
        }
    })
}

function _findPassword() {

//    $(".alert-email").text("");
//    $(".alert-inputCode").text("");

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
//            $(".alert-email").removeClass("text-red-400");
//            $(".alert-email").addClass("text-green-700");
//            $(".alert-email").text(res.message);
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
//            $(".alert-email").removeClass("text-green-700");
//            $(".alert-email").addClass("text-red-400");
//            $(".alert-email").text(res.responseJSON.message);
        }
    })
}