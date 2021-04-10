package com.atico.erp.core.ui.components;

import com.vaadin.flow.component.html.H2;

public class GridHeader extends H2 {

    public GridHeader(String text) {
        super(text);
        getStyle().set("font-size", "var(--lumo-font-size-m)");
        getStyle().set("margin-top", "0");
        getStyle().set("margin-bottom", "0");
    }
}
