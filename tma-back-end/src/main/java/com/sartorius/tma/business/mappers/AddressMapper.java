package com.sartorius.tma.business.mappers;

import com.sartorius.tma.dtos.AddressDto;
import com.sartorius.tma.persistence.entities.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {


  public Address toAddress(AddressDto addressRequest) {
    return addressRequest != null ? new Address(addressRequest.getStreet(), addressRequest.getStreetNumber(),
        addressRequest.getPostalCode(),
        addressRequest.getCity(), addressRequest.getCountry()) : null;
  }

  public AddressDto toAddressDto(Address address) {
    return  new AddressDto(address.getAddressStreet(), address.getAddressStreetNumber(),
        address.getAddressPostalCode(),
        address.getAddressCity(), address.getAddressCountry());
  }


}
