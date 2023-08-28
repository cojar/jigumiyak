function _showDetail(id) {
    let prefix = "#modal_";
    $.ajax({
        url: "/purchase/detail",
        type: "GET",
        data: {
            "id": id,
        },
        success: function(res) {
            console.log(res.code + ": " + res.message);

            $(prefix + "purchaseName").text(res.data.purchaseName);

            // 주문고객 정보
            $(prefix + "loginId").text(res.data.loginId);
            $(prefix + "email").text(res.data.email);
            $(prefix + "purchaserName").text(res.data.purchaserName);
            $(prefix + "purchaserPhoneNumber").text(res.data.purchaserPhoneNumber);

            // 배송지 정보
            $(prefix + "receiverName").text(res.data.receiverName);
            $(prefix + "receiverPhoneNumber").text(res.data.receiverPhoneNumber);
            $(prefix + "receiverAddress").text(res.data.receiverAddress);
            $(prefix + "deliveryRequest").text(res.data.deliveryRequest);

            // 결제 정보
            $(prefix + "totalAmount").text(res.data.totalAmount.toLocaleString() + "원");
            $(prefix + "method").text(res.data.method);
            $(prefix + "paymentDetail").text(res.data.paymentDetail);
            $(prefix + "payDate").text(res.data.payDate);
            $(prefix + "purchaseState").text(res.data.purchaseState);

            // 주문상품 정보
            $("#item-anchor").children().remove();
            let pre_template = `
                <h6 class="border-b-2 border-gray-300 mx-2">주문상품 정보</h6>
            `;
            $("#item-anchor").append(pre_template);
            $.each(res.data.purchaseItem, function(key, item){
                let imgPath = item.imgPath;
                let name = item.name;
                let amount = (item.price * item.count).toLocaleString();
                let count = item.count;

                let template = `
                    <div class="mt-[10px] flex justify-between items-center">
                        <div class="flex-[0_0_40%] flex items-center gap-5 ml-5">
                            <img src="${imgPath}" style="max-width: 100px; max-height: 75px;">
                            <span class="overflow-auto text-sm">${name}</span>
                        </div>
                        <div class="mr-5">
                            <span class="text-sm">${amount}원/${count}개</span>
                        </div>
                    </div>
                `;

                $("#item-anchor").append(template);
            });

            $("#purchaseDetailModal").prop("checked", true);
        },
        error: function(res) {
        }
    })
}