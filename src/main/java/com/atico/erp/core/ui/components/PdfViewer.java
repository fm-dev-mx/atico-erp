package com.atico.erp.core.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamResource;

@Tag("object")
public class PdfViewer extends Component implements HasSize {

    public PdfViewer(StreamResource resource) {
        this();
        getElement().setAttribute("data", resource);
    }

    public PdfViewer(String url) {
        this();
        getElement().setAttribute("data"
                , url);
    }

    public PdfViewer() {
        getElement().setAttribute("type", "application/pdf");
        setSizeFull();
    }

    public void setFile(StreamResource resource) {
        getElement().setAttribute("data", resource);
    }
}
