package com.revworkforce.service;

import com.revworkforce.dao.AnnouncementDAO;
import com.revworkforce.dao.AnnouncementDAOImpl;
import com.revworkforce.model.Announcement;

import java.util.List;

public class AnnouncementService {

    private final AnnouncementDAO dao = new AnnouncementDAOImpl();
    private final NotificationService notificationService = new NotificationService();
    private final AuditService auditService = new AuditService();
    public List<Announcement> viewAnnouncements() {
        return dao.getAllAnnouncements();
    }


    public boolean addAnnouncement(Announcement a) {

        boolean saved = dao.addAnnouncement(a);

        // 🔥 THIS WAS THE MISSING PART
        if (saved) {
            notificationService.notifyAllEmployees(
                    "📢 New company announcement posted"
            );
            auditService.log(a.getCreatedBy(), "Announcement posted");
        }

        return saved;
    }


    }
