package com.atico.erp.core.backend.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="addresses")
public class Address extends AbstractEntity {

    @NotNull
    @NotEmpty
    private String country = "";

    @NotNull
    @NotEmpty
    private String state = "";

    @NotNull
    @NotEmpty
    private String city = "";

    @NotNull
    @NotEmpty
    private String neighborhood = "";

    @NotNull
    @NotEmpty
    private String street = "";

    @NotNull
    @NotEmpty
    private String outdoorNumber = "";

    private String interiorNumber = "";

    private String zipCode = "";

    private String notes = "";

    @ManyToOne
    @JoinColumn(name="created_by_id")
    private User createdBy;


    @OneToMany(mappedBy = "address_id", fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getOutdoorNumber() {
        return outdoorNumber;
    }

    public void setOutdoorNumber(String outdoorNumber) {
        this.outdoorNumber = outdoorNumber;
    }

    public String getInteriorNumber() {
        return interiorNumber;
    }

    public void setInteriorNumber(String interiorNumber) {
        this.interiorNumber = interiorNumber;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getStreetNumberNeighborhood() {
        if(street == null)
            return "Unknown Address";

        if(interiorNumber != null)
            return street + " " + outdoorNumber + " - " +interiorNumber+ ", " +neighborhood;
        else
            return street + " " + outdoorNumber + ", " +neighborhood;
    }

    public String getCityStateCountry() {
            return city+ ", " +state+ ", " +country ;
    }
}