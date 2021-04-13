package com.atico.erp.sales.backend.services;

import com.atico.erp.core.backend.entities.Address;
import com.atico.erp.core.backend.repositories.AddressRepository;
import com.atico.erp.sales.backend.entities.Customer;
import com.atico.erp.sales.backend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CustomerService {
    private CustomerRepository customerRepository;
    private AddressRepository addressRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAll() {
        return this.customerRepository.findAll();
    }

    public List<Customer> findAllCustomersByStatus(Customer.Status status) {
        return customerRepository.getAllCustomersByStatus(status);
    }

    public List<Customer> findNotDeletedCustomersByStatus(Customer.Status status) {
        return customerRepository.getNotDeletedCustomersByStatus(status);
    }

    public Customer insertCustomer(Customer customer) {

        // force customer name to uppercase only before saving into database
        customer.setName(customer.getName().toUpperCase());

        // insert customer
        customerRepository.save(customer);

        // will assign a unique ID (customer_index) for the new customer, which will be equal to the count of all existing
        // customers in database (deleted or not)
        customer.setCustomerIndex(customerRepository.countAllCustomers());

        // update customer
        return customerRepository.save(customer);
    }

    public Address insertAddress(Address address) {

        // insert address
        addressRepository.save(address);

        // update address
        return addressRepository.save(address);
    }

    public void safeDelete(Customer customer) {

        customerRepository.safeDelete(customer.getId());
    }
}
