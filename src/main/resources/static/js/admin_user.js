function _modify(id, loginId) {

    $("#modal_loginId").text(loginId);
    $("#input_loginId").val(loginId);

    $("label[for='authority_super_admin']").removeAttr("disabled");
    $("label[for='authority_admin']").removeAttr("disabled");
    $("label[for='authority_seller']").removeAttr("disabled");
    $("label[for='authority_user']").removeAttr("disabled");

    $("#authority_super_admin").prop("checked", false);
    $("#authority_admin").prop("checked", false);
    $("#authority_seller").prop("checked", false);
    $("#authority_user").prop("checked", false);

    $.ajax({
        url: "/user/authority",
        type: "GET",
        data: {
            "id": id,
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);

            let disabled = res.data.disabled;
            let checked = res.data.checked;

            if (disabled.super_admin != undefined) {
                $("label[for='authority_super_admin']").attr("disabled", true);
            }
            if (disabled.admin != undefined) {
                $("label[for='authority_admin']").attr("disabled", true);
            }
            if (disabled.seller != undefined) {
                $("label[for='authority_seller']").attr("disabled", true);
            }
            if (disabled.user != undefined) {
                $("label[for='authority_user']").attr("disabled", true);
            }

            if (checked.super_admin != undefined) {
                $("#authority_super_admin").prop("checked", true);
            }
            if (checked.admin != undefined) {
                $("#authority_admin").prop("checked", true);
            }
            if (checked.seller != undefined) {
                $("#authority_seller").prop("checked", true);
            }
            if (checked.user != undefined) {
                $("#authority_user").prop("checked", true);
            }

            $("#modifyAuthorityModal").prop("checked", true);
        },
        error: function(res) {
        }
    })
}

function _modifyAuthority() {
    $.ajax({
        url: "/user/authority",
        type: "POST",
        data: $("#userAuthorityForm").serialize(),
        beforeSend : function() {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            $("#" + res.data.id + "_authoritiesInline").text(res.data.authoritiesInline);
            $("#modifyAuthorityModal").prop("checked", false);
        },
        error: function(res) {
        }
    })
}

function _withdraw(id) {
    $("#alertModal-message").text("");
    $.ajax({
        url: "/user/withdraw",
        type: "POST",
        data: {
            "id": id,
        },
        beforeSend : function() {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            $("#" + res.data.id + "_authoritiesInline").text(res.data.authoritiesInline);
            $("#alertModal-message").text(res.message);
            $("#alertModal").prop("checked", true);
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            $("#alertModal-message").text(res.responseJSON.message);
            $("#alertModal").prop("checked", true);
        }
    })
}

function _blacklist(id) {
    $("#alertModal-message").text("");
    $.ajax({
        url: "/user/blacklist",
        type: "POST",
        data: {
            "id": id,
        },
        beforeSend : function() {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            $("#" + res.data.id + "_authoritiesInline").text(res.data.authoritiesInline);
            $("#alertModal-message").text(res.message);
            $("#alertModal").prop("checked", true);
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            $("#alertModal-message").text(res.responseJSON.message);
            $("#alertModal").prop("checked", true);
        }
    })
}