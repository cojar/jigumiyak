let tossPayments = TossPayments("test_ck_GePWvyJnrKbJWJDe6DqVgLzN97Eo");
let path = "/purchase/payment/";
let successUrl = window.location.origin + path + "success";
let failUrl = window.location.origin + path + "fail";
let callbackUrl = window.location.origin + path + "va_callback";

function _payment() {

    // 결제 전 주문 엔티티 생성 및 orderId 반환
    $("input[id='receiverAddress.zoneCode']").prop("disabled", false);
    $("input[id='receiverAddress.mainAddress']").prop("disabled", false);
    $.ajax({
        url: "/purchase/payment/before",
        type: "POST",
        data: $("#purchaseForm").serialize(),
        beforeSend : function() {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);
            $("input[id='receiverAddress.zoneCode']").prop("disabled", true);
            $("input[id='receiverAddress.mainAddress']").prop("disabled", true);

            let requestJson = {
                "amount": res.data.amount,
                "orderId": res.data.orderId,
                "orderName": res.data.orderName,
                "successUrl": successUrl,
                "failUrl": failUrl,
                "cardCompany": null,
                "cardInstallmentPlan": null,
                "maxCardInstallmentPlan": null,
                "useCardPoint": false,
                "customerName": res.data.customerName,
                "customerEmail": null,
                "customerMobilePhone": null,
                "taxFreeAmount": null,
                "useInternationalCardOnly": false,
                "flowMode": "DEFAULT",
                "discountCode": null,
                "appScheme": null
            }

            tossPayments.requestPayment("카드", requestJson).catch(function (error) {
                $.ajax({
                    url: "/purchase/payment/cancel",
                    type: "POST",
                    data: {
                        "orderId": res.data.orderId,
                    },
                    beforeSend : function() {
                        var token = $("meta[name='_csrf']").attr("content");
                        var header = $("meta[name='_csrf_header']").attr("content");
                        $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
                    },
                    success: function(res) {
                        console.log(res.code + ": " + res.message);
                        if (error.code === "USER_CANCEL") {
                            alert("사용자가 결제를 취소했습니다.");
                        } else {
                            alert(error.message);
                        }
                    },
                    error: function(res) {
                    }
                })
            });
        },
        error: function(res) {
            $("input[id='receiverAddress.zoneCode']").prop("disabled", true);
            $("input[id='receiverAddress.mainAddress']").prop("disabled", true);
        }
    })
}

function _copyPurchaserInfo() {
    $("#receiverPhoneNumber").val($("#purchaserPhoneNumber").val());
    $("#receiverName").val($("#purchaserName").val());
}

function _isCustomRequest() {
    if ($("#deliveryRequest option:selected").text() == "직접입력") {
        $("#custom-delivery-request").removeClass("hidden");
    } else {
        $("#custom-delivery-request").addClass("hidden");
    }
}

function _zoneCode() {
    new daum.Postcode({
        oncomplete: function (data) {
            var mainAddress = "";

            if (data.userSelectedType === "R") {
                mainAddress = data.roadAddress;
            } else {
                mainAddress = data.jibunAddress;
            }

            $(".zoneCode").val(data.zonecode);
            $(".mainAddress").val(mainAddress);
            $(".subAddress").focus();
        }
    }).open();
}