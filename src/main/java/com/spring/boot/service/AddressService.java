package com.spring.boot.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.spring.boot.entities.Address;

@Service
public interface AddressService extends JpaRepository<Address, Integer> {

}
