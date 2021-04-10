package com.atico.erp.projects.ui.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.atico.erp.core.backend.entities.User;
import com.atico.erp.projects.backend.entities.Project;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class CreateSubprojectForm extends FormLayout {

    private Project subproject = new Project();

    private TextField name = new TextField("Subproject name");
    private DatePicker startDate = new DatePicker("Subproject start date");
    private DatePicker deliveryDate = new DatePicker("Subproject delivery date");

    Button createButton = new Button("Create");
    Button cancelButton = new Button("Cancel");

    Binder<Project> binder = new BeanValidationBinder<>(Project.class);

    public CreateSubprojectForm(Project parentProject, User loggedUser) {

        subproject.setProjectIndex(parentProject.getProjectIndex());
        subproject.setParentProject(parentProject);
        subproject.setCustomer(parentProject.getCustomer());
        subproject.setCreatedBy(loggedUser);

        addClassName("input-form");
        name.addClassName("uppercase-text");

        binder.bindInstanceFields(this);
        binder.setBean(subproject);

        configureSubprojectStartDatePicker(parentProject);

        add(new H1("Create subproject"));
        add(getParentProjectName(parentProject), 2);
        add(getParentProjectStartDate(parentProject), getParentProjectDeliveryDate(parentProject));
        add(name, 2);
        add(startDate, deliveryDate);
        add(createButtonsLayout(), 2);
    }

    private void configureSubprojectStartDatePicker(Project parentProject) {
        startDate.setValue(parentProject.getStartDate());
        startDate.setMin(parentProject.getStartDate());
        deliveryDate.setValue(parentProject.getDeliveryDate());
        startDate.addValueChangeListener(event -> {
            deliveryDate.setMin(event.getValue());
        });
    }

    private TextField getParentProjectName(Project parentProject) {
        TextField parentProjectName = new TextField("Parent project");
        parentProjectName.setValue(parentProject.getName());
        parentProjectName.setReadOnly(true);
        return parentProjectName;
    }

    private TextField getParentProjectStartDate(Project parentProject) {
        TextField parentProjectStartDate = new TextField("Parent project start date");

        if(parentProject.getStartDate() == null)
            parentProjectStartDate.setValue("N/A");
        else {
            parentProjectStartDate
                    .setValue(
                            parentProject.getStartDate()
                                    .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
                    );
        }
        parentProjectStartDate.setReadOnly(true);
        return parentProjectStartDate;
    }

    private TextField getParentProjectDeliveryDate(Project parentProject) {
        TextField parentProjectDeliveryDate = new TextField("Parent project delivery date");

        if(parentProject.getDeliveryDate() == null)
            parentProjectDeliveryDate.setValue("N/A");
        else
            parentProjectDeliveryDate
                    .setValue(
                            parentProject.getDeliveryDate()
                                         .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
                    );

        parentProjectDeliveryDate.setReadOnly(true);
        return parentProjectDeliveryDate;
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
            binder.writeBean(subproject);
            fireEvent(new CreateEvent(this, subproject));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class SubprojectFormEvent extends ComponentEvent<CreateSubprojectForm> {

        private Project subproject;

        public SubprojectFormEvent(CreateSubprojectForm source, Project subproject) {
            super(source, false);
            this.subproject = subproject;
        }

        public Project getSubproject() {
            return subproject;
        }
    }

    public static class CreateEvent extends SubprojectFormEvent {
        public CreateEvent(CreateSubprojectForm source, Project project) {
            super(source, project);
        }
    }

    public static class CancelEvent extends SubprojectFormEvent {
        public CancelEvent(CreateSubprojectForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
