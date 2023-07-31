package com.ll.jigumiyak.address;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public Address create(Integer zoneCode, String mainAddress, String subAddress) {

        Address address = new Address();
        address.setZoneCode(zoneCode);
        address.setMainAddress(mainAddress);
        address.setSubAddress(subAddress);

        this.addressRepository.save(address);

        return address;
    }
}
