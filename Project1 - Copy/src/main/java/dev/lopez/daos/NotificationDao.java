package dev.lopez.daos;
import dev.lopez.models.Notification;

import java.sql.SQLException;
import java.util.List;

public interface NotificationDao {


    void createNotification(int formID, String content) throws SQLException;


    public List<Notification> getNotifications(int employeeID) throws SQLException;


    public List<Notification> showAllNotifications(int employeeID) throws SQLException;


    public void HideNotification(int notificationID) throws SQLException;


}