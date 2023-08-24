function _increaseCount(id) {

    let count = $("#" + id + "_count");
    let price = $("#" + id + "_price");
    let disabled = $("#" + id + "_decrease");

    $.ajax({
        url: "/cartItem/increase",
        type: "POST",
        data: {
            "cartItemId": id,
        },
        beforeSend : function() {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            count.text(res.data.count);
            price.text(res.data.price);
            if (res.data.count > 1) {
                disabled.removeAttr("disabled");
            }
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            alert(res.responseJSON.message);
        }
    })
}

function _decreaseCount(id) {

    let count = $("#" + id + "_count");
    let price = $("#" + id + "_price");
    let disabled = $("#" + id + "_decrease");

    $.ajax({
        url: "/cartItem/decrease",
        type: "POST",
        data: {
            "cartItemId": id,
        },
        beforeSend : function() {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            count.text(res.data.count);
            price.text(res.data.price);
            if (res.data.count <= 1) {
                disabled.attr("disabled", true);
            }
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            alert(res.responseJSON.message);
        }
    })
}