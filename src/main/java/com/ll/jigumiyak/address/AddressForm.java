package com.ll.jigumiyak.address;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressForm {

    private Integer zoneCode;

    private String mainAddress;

    private String subAddress;
}
