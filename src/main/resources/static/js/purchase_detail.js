function _purchaseConfirm(id) {
    if (confirm("구매확정 후에는 결제상태를 되돌릴 수 없습니다.\n정말 진행하시겠습니까?")) {
        $.ajax({
            url: "/purchase/confirm",
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
                $("#purchaseState").text(res.data);
                $("#confirm-btn").remove();
                alert(res.message);
            },
            error: function(res) {
            }
        })
    }
}