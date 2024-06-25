package com.sartorius.tma.business.services;

import com.sartorius.tma.persistence.entities.Address;
import com.sartorius.tma.persistence.repositories.AddressRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final AddressRepository addressRepository;

  public List<Address> getAllAdresses() {
    return this.addressRepository.findAll();
  }

  public Address saveAddress(Address address) {
    return this.addressRepository.save(address);
  }

  public void deleteAddress(Long id) {
    this.addressRepository.deleteById(id);
  }
}
