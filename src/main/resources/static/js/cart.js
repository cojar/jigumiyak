function _toggleAllItemSelect() {
    if ($("#select-all:checked").length == 1) {
        $("input.item-select").prop("checked", true);
    } else {
        $("input.item-select").prop("checked", false);
    }
}

function _toggleItemSelect() {
    let totalCount = $("input.item-select").length;
    let checkedCount = $("input.item-select:checked").length;

    if (totalCount != checkedCount) {
        $("#select-all").prop("checked", false);
    } else {
        $("#select-all").prop("checked", true);
    }
}

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