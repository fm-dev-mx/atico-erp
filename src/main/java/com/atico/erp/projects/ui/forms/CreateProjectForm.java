package com.atico.erp.projects.ui.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.atico.erp.core.backend.entities.User;
import com.atico.erp.core.ui.forms.FormWithEvents;
import com.atico.erp.projects.backend.entities.Project;
import com.atico.erp.sales.backend.entities.Customer;
import com.atico.erp.sales.backend.services.CustomerService;
import java.time.LocalDate;

public class CreateProjectForm extends FormWithEvents {

    private Project project = new Project();

    private TextField name = new TextField("Project name");

    ComboBox<Customer> customer = new ComboBox<>("Customer");

    DatePicker startDate = new DatePicker("Start date");
    DatePicker deliveryDate = new DatePicker("Delivery date");

    private CustomerService customerService;

    Button createButton = new Button("Create");
    Button cancelButton = new Button("Cancel");

    Binder<Project> binder = new BeanValidationBinder<>(Project.class);

    public CreateProjectForm(CustomerService customerService,
                             User loggedUser) {

        this.customerService = customerService;
        project.setCreatedBy(loggedUser);


        addClassName("input-form");
        name.addClassName("uppercase-text");

        binder.bindInstanceFields(this);
        binder.setBean(project);

        configureStartDatePicker();
        configureDeliveryDatePicker();
        configureCustomerComboBox();

        add(new H1("Create project"));
        add(name, 2);
        add(customer, customer);
        add(startDate, deliveryDate);
        add(createButtonsLayout(), 2);
    }

    private void configureStartDatePicker() {
        startDate.addValueChangeListener(event -> {
            deliveryDate.setValue(null);
            deliveryDate.setMin(event.getValue());
        });
        startDate.setValue(LocalDate.now());
    }

    private void configureDeliveryDatePicker() {
        deliveryDate.addValueChangeListener(e -> {
            // do nothing yet
        });
    }

    private void configureCustomerComboBox() {
        customer.setItems(customerService.getAll());
        customer.setItemLabelGenerator(Customer::getLegalName);
    }

    private HorizontalLayout createButtonsLayout() {

        binder.addStatusChangeListener(e -> createButton.setEnabled(binder.isValid()));

        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createButton.addClickShortcut(Key.ENTER);
        createButton.addClickListener(event -> validateAndCreate());
        createButton.setEnabled(false);

        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickShortcut(Key.ESCAPE);
        cancelButton.addClickListener(event -> fireEvent(new CancelEvent(this)));

        HorizontalLayout buttonsLayout = new HorizontalLayout(cancelButton, createButton);
        buttonsLayout.addClassName("dialog-buttons");
        buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        return buttonsLayout;
    }

    private void validateAndCreate() {
        try {
            binder.writeBean(project);
            fireEvent(new CreateEvent(this, project));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class Event extends ComponentEvent<CreateProjectForm> {

        private Project project;

        public Event(CreateProjectForm source, Project project) {
            super(source, false);
            this.project = project;
        }

        public Project getProject() {
            return project;
        }
    }

    public static class CreateEvent extends Event {
        public CreateEvent(CreateProjectForm source, Project project) {
            super(source, project);
        }
    }

    public static class CancelEvent extends Event {
        public CancelEvent(CreateProjectForm source) {
            super(source, null);
        }
    }

}
