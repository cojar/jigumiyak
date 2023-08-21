package com.ll.jigumiyak.address;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public Address create(Integer zoneCode, String mainAddress, String subAddress) {

        Address address = Address.builder()
                .zoneCode(zoneCode)
                .mainAddress(mainAddress)
                .subAddress(subAddress)
                .build();

        this.addressRepository.save(address);

        return address;
    }

    public void modify(Address address, Integer zoneCode, String mainAddress, String subAddress) {

        address = address.toBuilder()
                .zoneCode(zoneCode)
                .mainAddress(mainAddress)
                .subAddress(subAddress)
                .build();

        this.addressRepository.save(address);
    }
}
