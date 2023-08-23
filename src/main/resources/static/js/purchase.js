let amount = 50000;

let tossPayments = TossPayments("test_ck_GePWvyJnrKbJWJDe6DqVgLzN97Eo");

function pay(method, requestJson) {

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
            $("input[id='receiverAddress.zoneCode']").prop("disabled", true);
            $("input[id='receiverAddress.mainAddress']").prop("disabled", true);

            alert(res.message);
        },
        error: function(res) {
            let errorMsg = "";
            $.each(res.responseJSON, function(key, value){
                errorMsg += value + "\n";
            });
            alert(errorMsg);
            $("input[id='receiverAddress.zoneCode']").prop("disabled", true);
            $("input[id='receiverAddress.mainAddress']").prop("disabled", true);
        }
    })

//    console.log(requestJson);
//
//    // 결제 진행
//    tossPayments.requestPayment(method, requestJson)
//    .catch(function (error) {
//
////        // 에러 발생 시 주문 엔티티 삭제
////        $.ajax {
////
////        }
//
//
//    });
}

let path = "/purchase/payment/";
let successUrl = window.location.origin + path + "success";
let failUrl = window.location.origin + path + "fail";
let callbackUrl = window.location.origin + path + "va_callback";
let orderId = new Date().getTime();

let requestJson = {
    "card": {
        "amount": amount,
        "orderId": "sample-" + orderId,
        "orderName": "토스 티셔츠 외 2건",
        "successUrl": successUrl,
        "failUrl": failUrl,
        "cardCompany": null,
        "cardInstallmentPlan": null,
        "maxCardInstallmentPlan": null,
        "useCardPoint": false,
        "customerName": "박토스",
        "customerEmail": null,
        "customerMobilePhone": null,
        "taxFreeAmount": null,
        "useInternationalCardOnly": false,
        "flowMode": "DEFAULT",
        "discountCode": null,
        "appScheme": null
    }
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