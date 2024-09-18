package com.motlient.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NotificationAppFactoryTest {

    @DisplayName("Should generate NotificationApp from the factory")
    @Test
    void shouldGenerateNotificationApp() {
        NotificationAppFactory factory = new NotificationAppFactory();
        NotificationApp notificationApp = factory.createNotificationApp();
        Assertions.assertNotNull(notificationApp);
    }
}
