package com.atico.erp.core.ui.toolbars;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

public class CloseSaveToolbar extends HorizontalLayout {

    private Button closeButton = new Button("Close", new Icon(VaadinIcon.CLOSE_CIRCLE_O));
    private Button saveButton = new Button("Save", new Icon(VaadinIcon.CHECK_CIRCLE_O));

    public CloseSaveToolbar() {

        closeButton.addClickListener(closeButtonClick -> {
            fireEvent(new CloseEvent(this));
        });

        saveButton.addClickListener(saveButtonClick -> {
            fireEvent(new SaveEvent(this));
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(closeButton, saveButton);
        setPadding(true);
        setJustifyContentMode(JustifyContentMode.END);
    }

    public Boolean getSaveEnabled() {
        return saveButton.isEnabled();
    }

    public void setSaveEnabled(Boolean enabled) {
        saveButton.setEnabled(enabled);
    }

    public void setSaveVisible(Boolean visible) { saveButton.setVisible(visible); }

    static public class Event extends ComponentEvent<CloseSaveToolbar> {

        public Event(CloseSaveToolbar source) {
            super(source, false);
        }
    }

    static public class CloseEvent extends Event {

        public CloseEvent(CloseSaveToolbar source) {
            super(source);
        }
    }

    static public class SaveEvent extends Event {
        public SaveEvent(CloseSaveToolbar source) {
            super(source);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
