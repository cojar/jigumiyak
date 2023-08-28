$(function() {

    $(window).scroll(function() {

        let scroll = window.scrollY;
        let subTitleHeight = $("h5#sub-title").outerHeight();

        if (scroll > subTitleHeight) {
            $("#total-amount-info").css("top", scroll);
        } else {
            $("#total-amount-info").css("top", "56px");
        }
    })
})

function _toggleAllItemSelect() {
    if ($("#select-all:checked").length == 1) {
        $("input.item-select").prop("checked", true);
        $("input.item-value").attr("name", "cartItemId");
    } else {
        $("input.item-select").prop("checked", false);
        $("input.item-value").removeAttr("name");
    }

    _refreshTotalAmount();
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

    _refreshTotalAmount();
}

function _increaseCount(id) {

    let count = $("#" + id + "_count");
    let amount = $("#" + id + "_amount");
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
            amount.text(res.data.amount);
            if (res.data.count > 1) {
                disabled.removeAttr("disabled");
            }
            _refreshTotalAmount();
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            alert(res.responseJSON.message);
        }
    })
}

function _decreaseCount(id) {

    let count = $("#" + id + "_count");
    let amount = $("#" + id + "_amount");
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
            amount.text(res.data.amount);
            if (res.data.count <= 1) {
                disabled.attr("disabled", true);
            }
            _refreshTotalAmount();
        },
        error: function(res) {
            console.log(res.responseJSON.code + ": " + res.responseJSON.message);
            alert(res.responseJSON.message);
        }
    })
}

function _directPurchase(id) {
    location.href = "/purchase?cartItemId=" + id;
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
                _refreshTotalAmount();

                if ($("div.item-dom").length == 0) {
                    $("#empty-cart").removeClass("hidden");
                }
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

function _refreshTotalAmount() {

    let totalAmount = 0;

    $("input.item-value[name=cartItemId]").each(function() {

        let count = $("#" + $(this).val() + "_count");
        let price = $("#" + $(this).val() + "_price");
        let amount = $("#" + $(this).val() + "_amount");

        totalAmount += count.text() * price.val();
    })

    $("#total-amount").text(totalAmount.toLocaleString() + "원");
}