package com.atico.erp.projects.backend.entities;

import com.atico.erp.core.backend.entities.AbstractEntity;
import com.atico.erp.core.backend.entities.User;
import com.atico.erp.sales.backend.entities.Customer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="projects")
public class Project extends AbstractEntity {

    public enum Status {
        Ongoing, Finished, Delivered
    }

    @NotEmpty(message = "Must enter a project name")
    private String name = "";

    @NotNull
    private String description = "";

    @NotNull
    private Integer projectIndex = 0;

    @NotNull
    private Integer subprojectIndex = 0;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Project.Status status = Status.Ongoing;

    @NotNull(message = "Must enter a project start date")
    private LocalDate startDate;

    @NotNull(message = "Must enter a project delivery date")
    private LocalDate deliveryDate;

    @NotNull(message = "Must select a customer contact")
    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="parent_project_id")
    private Project parentProject;


    @OneToMany(mappedBy = "parentProject", fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Project> subprojects = new LinkedList<>();


    @ManyToOne
    @JoinColumn(name="created_by_id")
    private User createdBy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase().trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProjectIndex() {
        return projectIndex;
    }

    public void setProjectIndex(Integer projectIndex) {
        this.projectIndex = projectIndex;
    }

    public Integer getSubprojectIndex() {
        return subprojectIndex;
    }

    public String getProjectNumber() {
        String projectStr = String.format("%1$3s", projectIndex.toString()).replace(' ', '0');
        String subprojectStr = String.format("%1$2s", subprojectIndex.toString()).replace(' ', '0');

        return "P" + projectStr + "-" + subprojectStr;
    }

    public void setSubprojectIndex(Integer subprojectIndex) {
        this.subprojectIndex = subprojectIndex;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Project getParentProject() {
        return parentProject;
    }

    public void setParentProject(Project parentProject) {
        this.parentProject = parentProject;
    }

    public List<Project> getSubprojects() {
        return subprojects;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
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

}
