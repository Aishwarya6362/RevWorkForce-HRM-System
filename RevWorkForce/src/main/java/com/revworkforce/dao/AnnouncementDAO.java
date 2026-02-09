package com.revworkforce.dao;

import com.revworkforce.model.Announcement;
import java.util.List;

public interface AnnouncementDAO {

    boolean addAnnouncement(Announcement announcement);

    List<Announcement> getAllAnnouncements();
}