package com.kasperovich.laelectronics.repository;

import com.kasperovich.laelectronics.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query(value = "select a from Address a inner join User u on a.id=u.address.id where u.email=?1")
    Address findAddressByUserEmail(String email);

}
