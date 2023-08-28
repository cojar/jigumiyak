function _addCart(uri, isAuthenticated, productId) {
    if (isAuthenticated === 'false') {
        if (confirm("로그인이 필요한 서비스입니다.\n확인을 누르시면 로그인 페이지로 이동합니다.")) {
            location.href = uri;
        }
    } else {
        $.ajax({
            url: "/cart/add",
            type: "POST",
            data: {
                "productId": productId,
                "count": $("#countSelect").val(),
                "target": "cart",
            },
            beforeSend : function() {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
            },
            success: function(res) {
                console.log(res.code + ": " + res.message);
                if (confirm(res.message)) {
                    location.href = res.data;
                }
            },
            error: function(res) {
                console.log(res.responseJSON.code + ": " + res.responseJSON.message);
                alert(res.responseJSON.message);
            }
        })
    }
}

function _directPurchase(uri, isAuthenticated, productId) {
    if (isAuthenticated === 'false') {
        if (confirm("로그인이 필요한 서비스입니다.\n확인을 누르시면 로그인 페이지로 이동합니다.")) {
            location.href = uri;
        }
    } else {
        $.ajax({
            url: "/cart/add",
            type: "POST",
            data: {
                "productId": productId,
                "count": $("#countSelect").val(),
                "target": "purchase",
            },
            beforeSend : function() {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
            },
            success: function(res) {
                console.log(res.code + ": " + res.message);
                location.href = res.data;
            },
            error: function(res) {
                console.log(res.responseJSON.code + ": " + res.responseJSON.message);
                alert(res.responseJSON.message);
            }
        })
    }
}

