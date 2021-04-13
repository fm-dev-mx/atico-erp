package com.atico.erp.core.backend.repositories;

import com.atico.erp.core.backend.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT COUNT(p) FROM Address p")
    public Integer countAllAddresses();

    @Query("SELECT p FROM Address p WHERE is_deleted=False")
    public List<Address> getNotDeletedAddresses();

    @Modifying
    @Query("UPDATE Address p SET is_deleted=true WHERE id=:address_id")
    public void safeDelete(@Param("address_id") Long addressId);

}
