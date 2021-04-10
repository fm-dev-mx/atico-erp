package com.atico.erp.sales.backend.repositories;

import com.atico.erp.sales.backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT COUNT(p) FROM Customer p")
    public Integer countAllCustomers();

    @Query("SELECT p FROM Customer p WHERE status=:status")
    public List<Customer> getAllCustomersByStatus(@Param("status") Customer.Status status);

    @Query("SELECT p FROM Customer p WHERE is_deleted=False AND status=:status")
    public List<Customer> getNotDeletedCustomersByStatus(@Param("status") Customer.Status status);

    @Modifying
    @Query("UPDATE Customer p SET is_deleted=true WHERE id=:customer_id")
    public void safeDelete(@Param("customer_id") Long customerId);

}
