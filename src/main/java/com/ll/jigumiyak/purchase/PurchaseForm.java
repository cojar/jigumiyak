package com.ll.jigumiyak.purchase;

import com.ll.jigumiyak.address.AddressForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseForm {

    private String purchaserLoginId;

    private String purchaserEmail;

    private String purchaserPhoneNumber;

    private String purchaserName;

    private String receiverName;

    private String receiverPhoneNumber;

    private AddressForm receiverAddress;

    private boolean isBasicAddress;

    private String deliveryRequest;
}
