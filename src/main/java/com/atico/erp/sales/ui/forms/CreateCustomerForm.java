package com.atico.erp.sales.ui.forms;

import com.atico.erp.core.backend.entities.Address;
import com.atico.erp.core.backend.entities.User;
import com.atico.erp.core.backend.services.AddressService;
import com.atico.erp.core.ui.forms.FormWithEvents;
import com.atico.erp.sales.backend.entities.Customer;
import com.atico.erp.sales.backend.services.CustomerService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class CreateCustomerForm extends FormWithEvents {

    private Customer customer = new Customer();
    private Address address = new Address();

    private TextField name = new TextField("Customer name");
    private TextField legalName = new TextField("Legal name");
    private TextField rfc = new TextField("R.F.C.");
    private TextField movilNumber = new TextField("Movil number");
    private TextField phoneNumber = new TextField("Phone number");
    private TextField e_mail = new TextField("E-Mail");

    //billing address
    private TextField country = new TextField("Country");
    private TextField state= new TextField("State");
    private TextField city = new TextField("City");
    private TextField street = new TextField("Street");
    private TextField outdorNumber = new TextField("Outdoor number");
    private TextField interiorNumber = new TextField("Interior number");
    private TextField neighborhood = new TextField("Neighborhood");
    private TextField zipCode = new TextField("Zip code");

    private CustomerService customerService;
    private AddressService addressService;

    Button createButton = new Button("Create");
    Button cancelButton = new Button("Cancel");

    Binder<Customer> customerBinder = new BeanValidationBinder<>(Customer.class);
    Binder<Address> addressBinder = new BeanValidationBinder<>(Address.class);

    public CreateCustomerForm(CustomerService customerService, AddressService addressService,
                              User loggedUser) {

        this.customerService = customerService;
        this.addressService = addressService;
        customer.setCreatedBy(loggedUser);
        customer.setAddress(address);

        VerticalLayout customerForm = new VerticalLayout();

        addClassName("input-form");
        name.addClassName("uppercase-text");
        rfc.addClassName("uppercase-text");

        customerBinder.bindInstanceFields(this);
        customerBinder.setBean(customer);

        addressBinder.bindInstanceFields(this);
        addressBinder.setBean(address);

        // BEGIN ACCORDION
        Accordion accordion = new Accordion();

        // PERSONAL INFORMATION
        FormLayout personalForm = new FormLayout();
        personalForm.add(name);
        personalForm.add(legalName);
        HorizontalLayout rfcMovil = new HorizontalLayout(rfc,movilNumber);
        personalForm.add(rfcMovil);
        HorizontalLayout phoneEmail = new HorizontalLayout(phoneNumber,e_mail);
        personalForm.add(phoneEmail);
        accordion.add("Personal information", personalForm);

        // BILLING ADDRESS
        FormLayout billingAddressForm = new FormLayout();
        HorizontalLayout countryState = new HorizontalLayout(country,state);
        billingAddressForm.add(countryState);
        HorizontalLayout cityStreet = new HorizontalLayout(city,street);
        billingAddressForm.add(cityStreet);
        HorizontalLayout outdoorInteriorNumber = new HorizontalLayout(outdorNumber,interiorNumber);
        billingAddressForm.add(outdoorInteriorNumber);
        HorizontalLayout neighborZipCode = new HorizontalLayout(neighborhood,zipCode);
        billingAddressForm.add(neighborZipCode);

        accordion.add("Billing Address", billingAddressForm);

        customerForm.add(
                new H1("Register customer"),
                accordion,
                createButtonsLayout()
        );
        add(customerForm,2);
    }


    private HorizontalLayout createButtonsLayout() {

        customerBinder.addStatusChangeListener(e -> createButton.setEnabled(customerBinder.isValid()));
        addressBinder.addStatusChangeListener(e -> createButton.setEnabled(addressBinder.isValid()));

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
            customerBinder.writeBean(customer);
            addressBinder.writeBean(address);
            fireEvent(new CreateEvent(this, customer, address));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class Event extends ComponentEvent<CreateCustomerForm> {

        private Customer customer;
        private Address address;

        public Event(CreateCustomerForm source, Customer customer, Address address) {
            super(source, false);
            this.customer = customer;
            this.address = address;
        }

        public Customer getCustomer() {
            return customer;
        }
        public Address getAddress(){ return  address;}
    }

    public static class CreateEvent extends CreateCustomerForm.Event {
        public CreateEvent(CreateCustomerForm source, Customer customer, Address address) {
            super(source, customer, address);
        }
    }

    public static class CancelEvent extends CreateCustomerForm.Event {
        public CancelEvent(CreateCustomerForm source) {
            super(source, null, null);
        }
    }

}
