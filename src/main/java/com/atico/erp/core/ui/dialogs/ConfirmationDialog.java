package com.atico.erp.core.ui.dialogs;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

public class ConfirmationDialog extends Dialog {

    public ConfirmationDialog(String text) {
        Div dialogText = new Div();
        dialogText.add(new Text(text));
        dialogText.addClassName("dialog-text");

        Button confirmButton = new Button("Ok");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickShortcut(Key.ENTER);
        confirmButton.addClickListener(event -> {
            fireEvent(new OkEvent(this));
            close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickShortcut(Key.ESCAPE);
        cancelButton.addClickListener(event -> {
            fireEvent(new CancelEvent(this));
            close();
        });

        HorizontalLayout buttonsLayout =
                new HorizontalLayout(cancelButton, confirmButton);
        buttonsLayout.addClassName("dialog-buttons");
        buttonsLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        add(dialogText, buttonsLayout);
    }

    public static abstract class ConfirmDialogEvent extends ComponentEvent<ConfirmationDialog> {
        public ConfirmDialogEvent(ConfirmationDialog source) {
            super(source, false);
        }
    }

    public static class OkEvent extends ConfirmDialogEvent {
        public OkEvent(ConfirmationDialog source) {
            super(source);
        }
    }

    public static class CancelEvent extends ConfirmDialogEvent {
        public CancelEvent(ConfirmationDialog source) {
            super(source);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
