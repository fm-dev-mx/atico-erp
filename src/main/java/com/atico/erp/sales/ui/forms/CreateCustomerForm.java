package com.atico.erp.sales.ui.forms;

import com.atico.erp.core.backend.entities.User;
import com.atico.erp.core.ui.forms.FormWithEvents;
import com.atico.erp.sales.backend.entities.Customer;
import com.atico.erp.sales.backend.services.CustomerService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class CreateCustomerForm extends FormWithEvents {

    private Customer customer = new Customer();

    private TextField name = new TextField("Customer name");
    private TextField rfc = new TextField("R.F.C.");

    private CustomerService customerService;

    Button createButton = new Button("Create");
    Button cancelButton = new Button("Cancel");

    //test

    Binder<Customer> binder = new BeanValidationBinder<>(Customer.class);

    public CreateCustomerForm(CustomerService customerService,
                              User loggedUser) {

        this.customerService = customerService;
        customer.setCreatedBy(loggedUser);


        addClassName("input-form");
        name.addClassName("uppercase-text");
        rfc.addClassName("uppercase-text");

        binder.bindInstanceFields(this);
        binder.setBean(customer);

        add(new H1("Create customer"));
        add(name, 2);
        add(rfc, 1);
        add(createButtonsLayout(), 2);
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
            binder.writeBean(customer);
            fireEvent(new CreateEvent(this, customer));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class Event extends ComponentEvent<CreateCustomerForm> {

        private Customer customer;

        public Event(CreateCustomerForm source, Customer customer) {
            super(source, false);
            this.customer = customer;
        }

        public Customer getCustomer() {
            return customer;
        }
    }

    public static class CreateEvent extends CreateCustomerForm.Event {
        public CreateEvent(CreateCustomerForm source, Customer customer) {
            super(source, customer);
        }
    }

    public static class CancelEvent extends CreateCustomerForm.Event {
        public CancelEvent(CreateCustomerForm source) {
            super(source, null);
        }
    }

}
