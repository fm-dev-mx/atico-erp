package com.atico.erp.core.backend.services;

import com.atico.erp.core.backend.entities.Address;
import com.atico.erp.core.backend.repositories.AddressRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AddressService {
    private AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAll() {
        return this.addressRepository.findAll();
    }

    public List<Address> findNotDeletedAddresses() {
        return addressRepository.getNotDeletedAddresses();
    }

    public Address insertAddress(Address address) {

        // insert address
        addressRepository.save(address);

        // update address
        return addressRepository.save(address);
    }

    public void safeDelete(Address address) {

        addressRepository.safeDelete(address.getId());
    }
}
