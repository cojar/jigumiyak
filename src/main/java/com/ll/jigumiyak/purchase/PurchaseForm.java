package com.ll.jigumiyak.purchase;

import com.ll.jigumiyak.address.AddressForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseForm {

    private String purchaserName;

    private String purchaserPhoneNumber;

    private String receiverName;

    private String receiverPhoneNumber;

    private AddressForm receiverAddress;

    private String deliveryRequest;

    private String customDeliveryRequest;
}
