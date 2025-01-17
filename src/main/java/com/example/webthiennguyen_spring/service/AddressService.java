package com.example.webthiennguyen_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.webthiennguyen_spring.entities.Address;
import com.example.webthiennguyen_spring.repository.AddressRepository;

@Service
public class AddressService {
	@Autowired
    private AddressRepository addressRepository;
	
	public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }
    
    public List<Address> getAllAddresses() {  
        return addressRepository.findAll();  
    }  
    
 
 
}
