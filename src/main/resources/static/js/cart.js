function _toggleAllItemSelect() {
    if ($("#select-all:checked").length == 1) {
        $("input.item-select").prop("checked", true);
        $("input.item-value").attr("name", "cartItemId");
    } else {
        $("input.item-select").prop("checked", false);
        $("input.item-value").removeAttr("name");
    }
}

function _toggleItemSelect(_this) {
    let totalCount = $("input.item-select").length;
    let checkedCount = $("input.item-select:checked").length;

    let value = "#" + $(_this).attr("id") + "_value";

    if ($(value).attr("name") == "cartItemId") {
        $(value).removeAttr("name");
    } else {
        $(value).attr("name", "cartItemId");
    }

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

function _directPurchase(id) {
    $("#directPurchaseForm").children("#cartItemId").val(id);
    $("#directPurchaseForm").submit();
}

function _selectPurchase() {

    let count = $("input.item-value[name=cartItemId]").length;

    if (count > 0) {
        $("#selectPurchaseForm").submit();
    } else {
        alert("구매할 상품을 1개 이상 선택해주세요");
    }
}

function _selectDelete() {

    let count = $("input.item-value[name=cartItemId]").length;

    if (count > 0) {
        $.ajax({
            url: "/cartItem/delete",
            type: "POST",
            data: $("#selectPurchaseForm").serialize(),
            beforeSend : function() {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
            },
            success: function(res) {
                console.log(res.code + ": " + res.message);
                $.each(res.data, function(key, value){
                    $(value).remove();
                });
            },
            error: function(res) {
                console.log(res.responseJSON.code + ": " + res.responseJSON.message);
                alert(res.responseJSON.message);
            }
        })
    } else {
        alert("삭제할 상품을 1개 이상 선택해주세요");
    }
}