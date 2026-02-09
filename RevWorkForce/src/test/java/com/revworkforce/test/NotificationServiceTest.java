package com.revworkforce.test;

import com.revworkforce.service.NotificationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    private final NotificationService service = new NotificationService();

    @Test
    void testSendNotification() {
        assertDoesNotThrow(() ->
                service.notify(1001, "Test notification"));
    }

    @Test
    void testMarkNotificationsAsRead() {
        assertDoesNotThrow(() ->
                service.markAsRead(1001));
    }
}