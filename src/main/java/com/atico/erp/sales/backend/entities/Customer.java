package com.atico.erp.sales.backend.entities;

import com.atico.erp.core.backend.entities.AbstractEntity;
import com.atico.erp.core.backend.entities.Address;
import com.atico.erp.core.backend.entities.User;
import com.atico.erp.projects.backend.entities.Project;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="customers")
public class Customer extends AbstractEntity {

    public enum Status {
        Active, Inactive
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    private Customer.Status status = Customer.Status.Active;

    private String legalName = "";

    @NotNull
    private Integer customerIndex = 0;

    @NotNull
    @NotEmpty
    private String name = "";

    private String rfc = "";

    @ManyToOne
    @JoinColumn(name = "billing_address")
    private Address billingAddress;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private String phoneNumber = "";

    @NotNull
    @NotEmpty
    private String movilNumber = "";

    @NotNull
    @NotEmpty
    private String eMail = "";

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Project> projects = new LinkedList<>();

    @ManyToOne
    @JoinColumn(name="created_by_id")
    private User createdBy;

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRfc() { return rfc; }

    public void setRfc(String rfc) { this.rfc = rfc; }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getCustomerIndex() {
        return customerIndex;
    }

    public void setCustomerIndex(Integer customerIndex) {
        this.customerIndex = customerIndex;
    }

    public String getCustomerNumber() {
        String customerStr = String.format("%1$3s", customerIndex.toString()).replace(' ', '0');

        return "C" + customerStr;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMovilNumber() {
        return movilNumber;
    }

    public void setMovilNumber(String movilNumber) {
        this.movilNumber = movilNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

}
