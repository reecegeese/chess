package client;

import client.model.Notification;

public interface NotificationHandler {
    void notify(Notification notification);
}