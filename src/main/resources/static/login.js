$(function() {
    $("#loginId, #password").keyup(function() {

        let loginId = $("#loginId").val();
        let password = $("#password").val();

        if (loginId.length >= 8 && password.length >= 8) {
            $("#login-btn").removeClass("deactivated");
        } else {
            $("#login-btn").addClass("deactivated");
        }
    });
});

function _login() {
    if (!$("#login-btn").hasClass("deactivated")) $("#login-form").submit();
}
