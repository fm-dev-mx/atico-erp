package com.atico.erp.core.ui.components;

import com.vaadin.flow.component.html.Span;

public class GridSecondaryText extends Span {

    public GridSecondaryText(String text) {
        super(text);
        getStyle().set("font-size", "var(--lumo-font-size-m)");
        getStyle().set("color", "var(--lumo-secondary-text-color)");
        getStyle().set("margin-top", "0");
        getStyle().set("margin-bottom", "0");
    }
}
