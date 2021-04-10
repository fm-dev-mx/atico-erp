package com.atico.erp.core.backend.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="users")
public class User extends AbstractEntity {

    private String username;
    private String firstName;
    private String lastName;
    private String secondLastName;
    private String alias;
    private String password;
    private Boolean enabled;
    private Boolean useAlias;


    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Address> createdAddresses = new LinkedList<>();


    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFormalName() {
        if(firstName == null || lastName == null)
            return "Unknown user";

        return firstName + " " + lastName;
    }

    public String getDisplayName() {
        if(useAlias && alias != null)
            return alias;
        return getFormalName();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isUseAlias() {
        return useAlias;
    }

    public void setUseAlias(boolean useAlias) {
        this.useAlias = useAlias;
    }

    public String getPassword() {
        return password.trim();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Address> getCreatedAddresses() {
        return createdAddresses;
    }
}