package com.atico.erp.core.ui;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class Notifications {

    private static void Notify(String message, NotificationVariant variant) {
        Notification notification = new Notification(message);
        notification.addThemeVariants(variant);
        notification.setPosition(Notification.Position.TOP_END);
        notification.setDuration(3000);
        notification.open();
    }

    public static void Error(String message) {
        Notify(message, NotificationVariant.LUMO_ERROR);
    }

    public static void Success(String message) {
        Notify(message, NotificationVariant.LUMO_SUCCESS);
    }

    public static void Information(String message) {
        Notify(message, NotificationVariant.LUMO_PRIMARY);
    }

}