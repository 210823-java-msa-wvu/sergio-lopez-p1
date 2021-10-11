package dev.lopez.services;
import dev.lopez.daos.NotificationDao;
import dev.lopez.models.Notification;
import dev.lopez.utils.ConnectionUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDaoImpl implements NotificationDao {

   // public static ConnFactory cf = ConnFactory.getInstance();
   private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

    @Override
    public void createNotification(int formID, String content) throws SQLException {
        Connection conn = cu.getConnection();
        CallableStatement call = conn.prepareCall("{call CREATE_NOTIFICATION(?, ?)");
        call.setInt(1, formID);
        call.setString(2, content);
        call.execute();
    }

    @Override
    public List<Notification> getNotifications(int employeeID) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Notification> n = new ArrayList<Notification>();
        Connection conn = cu.getConnection();
        ps = conn.prepareStatement("SELECT * FROM \"TRMS1.2\".notification WHERE formid IN (SELECT formid FROM \"TRMS1.2\".form where employeeid = ?) AND CHECKED = 0");
        ps.setInt(1, employeeID);
        rs = ps.executeQuery();
        while (rs.next()) {
            n.add(new Notification(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getDate(4), rs.getInt(5)));
        }
        return n;
    }

    @Override
    public List<Notification> showAllNotifications(int employeeID) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Notification> n = new ArrayList<Notification>();
        Connection conn = cu.getConnection();
        ps = conn.prepareStatement("SELECT * FROM \"NOTIFICATION\" WHERE FORMID IN (SELECT FORMID FROM \"FORM\" WHERE EMPLOYEEID = ?)");
        ps.setInt(1, employeeID);
        rs = ps.executeQuery();
        while (rs.next()) {
            n.add(new Notification(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getDate(4), rs.getInt(5)));
        }
        return n;
    }

    @Override
    public void HideNotification(int notificationID) throws SQLException {
        Connection conn = cu.getConnection();
        CallableStatement call = conn.prepareCall("{call HIDE_NOTIFICATION(?)");
        call.setInt(1, notificationID);
        call.execute();
    }
}