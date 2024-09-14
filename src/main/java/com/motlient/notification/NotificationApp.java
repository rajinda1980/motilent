package com.motlient.notification;

import java.util.logging.Logger;

public class NotificationApp {

    public static final Logger LOGGER = Logger.getLogger(NotificationApp.class.getName());

    public static void main(String[] args) {
        if (args.length != 1) {
            LOGGER.severe("Notification Application : File path is not provided");
            System.exit(1);
        }
    }
}
