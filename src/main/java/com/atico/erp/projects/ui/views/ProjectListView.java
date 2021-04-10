package com.atico.erp.projects.ui.views;

import com.atico.erp.core.backend.services.AppUserDetailsService;
import com.atico.erp.core.ui.Notifications;
import com.atico.erp.core.ui.dialogs.ConfirmationDialog;
import com.atico.erp.core.ui.views.MainLayout;
import com.atico.erp.projects.backend.entities.Project;
import com.atico.erp.projects.backend.services.ProjectService;
import com.atico.erp.projects.ui.forms.CreateProjectForm;
import com.atico.erp.projects.ui.forms.CreateSubprojectForm;
import com.atico.erp.sales.backend.services.CustomerService;
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

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Projects")
public class ProjectListView extends VerticalLayout {

    private ProjectService projectService;
    private CustomerService customerService;
    private AppUserDetailsService appUserDetailsService;

    private Project selectedProject = null;

    private TreeGrid<Project> grid = new TreeGrid<>();
    private ComboBox<Project.Status> statusComboBox = new ComboBox<>("Filter by status");
    private Checkbox showDeletedCheckbox = new Checkbox("Show deleted");

    private Dialog createProjectDialog;
    private Dialog createSubprojectDialog;

    private GridMenuItem createSubprojectMenu;
    private GridMenuItem exploreMenu;
    private GridMenuItem deleteMenu;

    public ProjectListView(ProjectService projectService,
                           CustomerService customerService,
                           AppUserDetailsService appUserDetailsService) {

        this.projectService = projectService;
        this.customerService = customerService;
        this.appUserDetailsService = appUserDetailsService;
        setSizeFull();

        configureFilterControls();
        configureTreeGrid();
        configureTreeGridContextMenu();
        add(getToolbar(), statusComboBox, grid, showDeletedCheckbox);
    }

    public Component getToolbar() {
        Button createProjectButton = new Button("Create project");
        createProjectButton.addClickListener(event -> {
            createProjectDialog = new Dialog();
            CreateProjectForm projectForm
                    = new CreateProjectForm(customerService,
                    appUserDetailsService.getLoggedUser()
            );
            projectForm.addListener(CreateProjectForm.CreateEvent.class, this::createProject);
            projectForm.addListener(CreateProjectForm.CancelEvent.class, e -> createProjectDialog.close());
            createProjectDialog.add(projectForm);
            createProjectDialog.open();
        });

        Button refreshButton = new Button("Refresh");
        refreshButton.addClickListener(event -> {
            updateTreeGrid();
        });

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(createProjectButton, refreshButton);
        return toolbar;
    }

    public void configureFilterControls() {
        statusComboBox.setItems(Project.Status.values());
        statusComboBox.addValueChangeListener(event -> {
            updateTreeGrid();
        });
        statusComboBox.setValue(Project.Status.Ongoing);

        showDeletedCheckbox.addValueChangeListener(event -> {
            updateTreeGrid();
        });
    }

    public void configureTreeGrid() {

        grid.setSizeFull();
        grid.addClassName("project-grid");

        // Project number and project name in hierarchy column
        grid.addComponentHierarchyColumn(project -> {

            Span projectNumber = new Span(project.getProjectNumber());
            if(project.isDeleted()) {
                projectNumber.setText(project.getProjectNumber() + " (deleted)");
                projectNumber.getStyle().set("color", "var(--lumo-error-text-color)");
            } else {
                projectNumber.setText(project.getProjectNumber());
            }

            Span projectName = new Span(project.getName());
            projectName.getStyle().set("color", "var(--lumo-secondary-text-color)");
            projectName.getStyle().set("font-size", "var(--lumo-font-size-m)");

            VerticalLayout projectDetails = new VerticalLayout(projectNumber, projectName);
            projectDetails.setPadding(false);
            projectDetails.setSpacing(false);

            Icon projectIcon = new Icon(VaadinIcon.BUILDING);
            projectIcon.setSize("24px");
            projectIcon.setColor("dodgerblue");

            HorizontalLayout layout = new HorizontalLayout(projectIcon, projectDetails);
            layout.setVerticalComponentAlignment(Alignment.START);

            return layout;
        }).setHeader("Project").setAutoWidth(true);

        // Project status
        grid.addColumn(project -> {
            return project.getStatus().toString();
        }).setHeader("Status").setAutoWidth(true);

        // Customer name
        grid.addColumn(project -> {
            return project.getCustomer().getName();
        }).setHeader("Customer").setAutoWidth(true);

        // Key dates
        grid.addComponentColumn(project -> {

            String formattedStartDate = "N/A";
            String formattedDeliveryDate = "N/A";

            if(project.getStartDate() != null) {
                formattedStartDate = project.getStartDate()
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            }

            if(project.getDeliveryDate() != null) {
                formattedDeliveryDate = project.getDeliveryDate()
                                               .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            }

            Span startDate  = new Span("Started on " + formattedStartDate);
            Span deliveryDate = new Span("Delivery by " + formattedDeliveryDate);

            startDate.getStyle().set("color", "var(--lumo-secondary-text-color)");
            startDate.getStyle().set("font-size", "var(--lumo-font-size-s)");

            deliveryDate.getStyle().set("color", "var(--lumo-secondary-text-color)");
            deliveryDate.getStyle().set("font-size", "var(--lumo-font-size-s)");

            VerticalLayout keyDates = new VerticalLayout(startDate, deliveryDate);
            keyDates.setPadding(false);
            keyDates.setSpacing(false);
            return keyDates;
        }).setHeader("Key dates").setAutoWidth(true);

        // Configure project grid event handlers
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedProject = event.getValue();
        });
    }

    public void configureTreeGridContextMenu() {

        GridContextMenu menu = new GridContextMenu(grid);

        exploreMenu = menu.addItem("Explore", e -> {
            UI.getCurrent().navigate("explore-project-view/" + selectedProject.getId().toString());
        });

        createSubprojectMenu = menu.addItem("Create subproject", e -> {
            createSubprojectDialog = new Dialog();
            CreateSubprojectForm subprojectForm =
                    new CreateSubprojectForm(selectedProject, appUserDetailsService.getLoggedUser());
            subprojectForm.addListener(CreateSubprojectForm.CreateEvent.class, this::createSubproject);
            subprojectForm.addListener(CreateSubprojectForm.CancelEvent.class, cancelEvent -> createSubprojectDialog.close());
            createSubprojectDialog.add(subprojectForm);
            createSubprojectDialog.open();
        });

        deleteMenu = menu.addItem("Delete", e -> {
            if(selectedProject == null) return;
            String confirmText = selectedProject.getParentProject() == null
                    ? "Be careful, are you sure to delete the selected project and all its subprojects?"
                    : "Be careful, are you sure to delete the selected subproject?";

            ConfirmationDialog confirm = new ConfirmationDialog(confirmText);
            confirm.addListener(ConfirmationDialog.OkEvent.class, this::deleteProject);
            confirm.open();
        });

        menu.addOpenedChangeListener(e -> {
            exploreMenu.setEnabled(selectedProject != null);
            createSubprojectMenu.setEnabled(selectedProject != null);
            deleteMenu.setEnabled(selectedProject != null);
        });
    }

    public void updateTreeGrid() {
        try {
            if(showDeletedCheckbox.getValue() == false) {
                grid.setItems(projectService.findNotDeletedProjectsByStatus(statusComboBox.getValue()),
                              projectService::findNotDeletedSubprojects);
            } else {
                grid.setItems(projectService.findAllProjectsByStatus(statusComboBox.getValue()),
                        projectService::findAllSubprojects);
            }
            grid.getDataProvider().refreshAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createProject(CreateProjectForm.CreateEvent event) {

        Project insertedProject = projectService.insertProject(event.getProject());

        createProjectDialog.close();
        Notifications.Success("Project successfully created!");
        updateTreeGrid();
    }

    public void createSubproject(CreateSubprojectForm.CreateEvent event) {
        projectService.insertSubproject(event.getSubproject());
        createSubprojectDialog.close();
        Notifications.Success("Subproject successfully created!");
        updateTreeGrid();
    }

    public void deleteProject(ConfirmationDialog.OkEvent event) {
        if(selectedProject != null) {
            projectService.safeDelete(selectedProject);
            Notifications.Success("Project successfully deleted!");
            updateTreeGrid();
        }
    }

}
