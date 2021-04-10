package com.atico.erp.core.ui.components;

import com.vaadin.flow.component.html.H1;

public class DialogHeader extends H1 {

    public DialogHeader(String text) {
        super(text);
        getStyle().set("font-size", "var(--lumo-font-size-xl)");
        getStyle().set("margin-bottom", "0");
    }

}
