package com.atico.erp.sales.ui.views;

import com.atico.erp.core.backend.entities.Address;
import com.atico.erp.core.backend.services.AddressService;
import com.atico.erp.core.backend.services.AppUserDetailsService;
import com.atico.erp.core.ui.Notifications;
import com.atico.erp.core.ui.dialogs.ConfirmationDialog;
import com.atico.erp.core.ui.views.MainLayout;
import com.atico.erp.sales.backend.entities.Customer;
import com.atico.erp.sales.backend.services.CustomerService;
import com.atico.erp.sales.ui.forms.CreateCustomerForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "customer-list-view", layout = MainLayout.class)
@PageTitle("Customers")
public class CustomerListView extends VerticalLayout {

    private CustomerService customerService;
    private AddressService addressService;
    private AppUserDetailsService appUserDetailsService;

    private Customer selectedCustomer = null;

    private TreeGrid<Customer> grid = new TreeGrid<>();
    private ComboBox<Customer.Status> statusComboBox = new ComboBox<>("Filter by status");
    private Checkbox showDeletedCheckbox = new Checkbox("Show deleted");

    private Dialog createCustomerDialog;

    private GridMenuItem exploreMenu;
    private GridMenuItem deleteMenu;

    public CustomerListView(CustomerService customerService, AddressService addressService,
                           AppUserDetailsService appUserDetailsService) {

        this.customerService = customerService;
        this.addressService = addressService;
        this.appUserDetailsService = appUserDetailsService;
        setSizeFull();

        configureFilterControls();
        configureTreeGrid();
        configureTreeGridContextMenu();
        add(getToolbar(), statusComboBox, grid, showDeletedCheckbox);
    }

    public Component getToolbar() {
        Button createCustomerButton = new Button("Create customer");
        createCustomerButton.addClickListener(event -> {
            createCustomerDialog = new Dialog();
            CreateCustomerForm customerForm
                    = new CreateCustomerForm(customerService,addressService,
                    appUserDetailsService.getLoggedUser()
            );
            customerForm.addListener(CreateCustomerForm.CreateEvent.class, this::createCustomer);
            customerForm.addListener(CreateCustomerForm.CancelEvent.class, e -> createCustomerDialog.close());
            createCustomerDialog.add(customerForm);
            createCustomerDialog.open();
        });

        Button refreshButton = new Button("Refresh");
        refreshButton.addClickListener(event -> {
            updateTreeGrid();
        });

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(createCustomerButton, refreshButton);
        return toolbar;
    }

    public void configureFilterControls() {
        statusComboBox.setItems(Customer.Status.values());
        statusComboBox.addValueChangeListener(event -> {
            updateTreeGrid();
        });
        statusComboBox.setValue(Customer.Status.Active);

        showDeletedCheckbox.addValueChangeListener(event -> {
            updateTreeGrid();
        });
    }

    public void configureTreeGrid() {

        grid.setSizeFull();
        grid.addClassName("customer-grid");

        // Customer number
        grid.addComponentColumn(customer -> {

            Span customerNumber = new Span(customer.getCustomerNumber());
            if(customer.isDeleted()) {
                customerNumber.setText(customer.getCustomerNumber() + " (deleted)");
                customerNumber.getStyle().set("color", "var(--lumo-error-text-color)");
                customerNumber.getStyle().set("font-size", "var(--lumo-font-size-s)");
            } else {
                customerNumber.setText(customer.getCustomerNumber());
                customerNumber.getStyle().set("color", "var(--lumo-secondary-text-color)");
                customerNumber.getStyle().set("font-size", "var(--lumo-font-size-s)");
            }

            Icon customerIcon = new Icon(VaadinIcon.USER);
            customerIcon.setSize("20px");
            customerIcon.setColor("dodgerblue");

            HorizontalLayout layout = new HorizontalLayout(customerIcon, customerNumber);
            layout.setVerticalComponentAlignment(Alignment.START);

            return layout;
        }).setHeader("Number").setAutoWidth(true);

        // Customer and legal name
        grid.addComponentColumn(customer -> {

            Span customerName = new Span(customer.getName());
            customerName.getStyle().set("color", "var(--lumo-secondary-text-color)");
            customerName.getStyle().set("font-size", "var(--lumo-font-size-s)");

            Span legalName = new Span(customer.getLegalName());
            legalName.getStyle().set("color", "var(--lumo-secondary-text-color)");
            legalName.getStyle().set("font-size", "var(--lumo-font-size-s)");

            VerticalLayout customerDetails = new VerticalLayout(customerName, legalName);
            customerDetails.setPadding(false);
            customerDetails.setSpacing(false);

            return customerDetails;
        }).setHeader("Customer").setAutoWidth(true);

        // Customer RFC
        grid.addComponentColumn(customer -> {
            Span rfc = new Span("");

            if (customer.getRfc() != null ) {
                rfc = new Span(customer.getRfc());
                rfc.getStyle().set("color", "var(--lumo-secondary-text-color)");
                rfc.getStyle().set("font-size", "var(--lumo-font-size-s)");
            }
            VerticalLayout rfcDetail = new VerticalLayout(rfc);

            rfcDetail.setPadding(false);
            rfcDetail.setSpacing(false);
            return rfcDetail;
        }).setHeader("R.F.C.").setAutoWidth(true);

        // Address
        grid.addComponentColumn(customer -> {

            Span streetNumberNeighbor = new Span(customer.getAddress().getStreetNumberNeighborhood());
            streetNumberNeighbor.getStyle().set("color", "var(--lumo-secondary-text-color)");
            streetNumberNeighbor.getStyle().set("font-size", "var(--lumo-font-size-s)");

            Span cityStateCountry = new Span(customer.getAddress().getCityStateCountry());
            cityStateCountry.getStyle().set("color", "var(--lumo-secondary-text-color)");
            cityStateCountry.getStyle().set("font-size", "var(--lumo-font-size-s)");

            VerticalLayout addressDetails = new VerticalLayout(streetNumberNeighbor, cityStateCountry);

            addressDetails.setPadding(false);
            addressDetails.setSpacing(false);

            return addressDetails;
        }).setHeader("Address").setAutoWidth(true);

        // Phone and movil number
        grid.addComponentColumn(customer -> {

            Span movilNumber = new Span("Phone: " + customer.getMovilNumber());
            movilNumber.getStyle().set("color", "var(--lumo-secondary-text-color)");
            movilNumber.getStyle().set("font-size", "var(--lumo-font-size-s)");

            Span phoneNumber = new Span("Movil: " + customer.getPhoneNumber());
            phoneNumber.getStyle().set("color", "var(--lumo-secondary-text-color)");
            phoneNumber.getStyle().set("font-size", "var(--lumo-font-size-s)");

            VerticalLayout addressDetails = new VerticalLayout(movilNumber, phoneNumber);

            addressDetails.setPadding(false);
            addressDetails.setSpacing(false);

            return addressDetails;
        }).setHeader("Phone/Movil Number").setAutoWidth(true);

        // Customer EMail
        grid.addComponentColumn(customer -> {

                Span eMail = new Span(customer.geteMail());
                eMail.getStyle().set("color", "var(--lumo-secondary-text-color)");
                eMail.getStyle().set("font-size", "var(--lumo-font-size-s)");
                VerticalLayout eMailDetail = new VerticalLayout(eMail);

                eMailDetail.setPadding(false);
                eMailDetail.setSpacing(false);

                return eMailDetail;

        }).setHeader("E-Mail").setAutoWidth(true);

        // Configure project grid event handlers
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedCustomer = event.getValue();
        });
    }

    public void configureTreeGridContextMenu() {

        GridContextMenu menu = new GridContextMenu(grid);

        exploreMenu = menu.addItem("Explore", e -> {
            UI.getCurrent().navigate("explore-customer-view/" + selectedCustomer.getId().toString());
        });

        deleteMenu = menu.addItem("Delete", e -> {
            if(selectedCustomer == null) return;
            String confirmText = "Be careful, are you sure to delete the selected customer?";

            ConfirmationDialog confirm = new ConfirmationDialog(confirmText);
            confirm.addListener(ConfirmationDialog.OkEvent.class, this::deleteCustomer);
            confirm.open();
        });

        menu.addOpenedChangeListener(e -> {
            exploreMenu.setEnabled(selectedCustomer != null);
            deleteMenu.setEnabled(selectedCustomer != null);
        });
    }

    public void updateTreeGrid() {
        try {
            if(showDeletedCheckbox.getValue() == false) {
                grid.setItems(customerService.findNotDeletedCustomersByStatus(statusComboBox.getValue()));
            } else {
                grid.setItems(customerService.findAllCustomersByStatus(statusComboBox.getValue()));
            }
            grid.getDataProvider().refreshAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createCustomer(CreateCustomerForm.CreateEvent EventCustomer) {

        Customer insertedCustomer = customerService.insertCustomer(EventCustomer.getCustomer());
        Address insertedAddress = addressService.insertAddress(EventCustomer.getAddress());

        createCustomerDialog.close();
        Notifications.Success("Customer successfully created!");
        updateTreeGrid();
    }

    public void deleteCustomer(ConfirmationDialog.OkEvent event) {
        if(selectedCustomer != null) {
            customerService.safeDelete(selectedCustomer);
            Notifications.Success("Customer successfully deleted!");
            updateTreeGrid();
        }
    }

}
