package com.atico.erp.core.ui.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.StreamResource;
import com.atico.erp.core.ui.components.PdfViewer;

import java.io.ByteArrayInputStream;

public class PdfViewerDialog extends DialogWithEvents {

    public PdfViewerDialog(String fileName, byte[] pdfBytes) {
        setSizeFull();
        StreamResource resource = new StreamResource(fileName, () -> new ByteArrayInputStream(pdfBytes));
        PdfViewer pdfViewer = new PdfViewer(resource);
        pdfViewer.setSizeFull();
        add(pdfViewer);
        add(getToolbar());
    }

    public HorizontalLayout getToolbar() {

        Button closeButton = new Button("Close", closeEvent -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout layout = new HorizontalLayout(closeButton);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        return layout;
    }

}
