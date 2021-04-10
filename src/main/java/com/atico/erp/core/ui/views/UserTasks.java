package com.atico.erp.core.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UserTasks extends VerticalLayout {

    public UserTasks() {
        setSizeFull();
        add(new Button("User tasks"));
    }
}
