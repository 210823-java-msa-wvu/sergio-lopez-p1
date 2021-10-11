package dev.lopez.services;
import dev.lopez.daos.FormDao;
import dev.lopez.models.Form;

import dev.lopez.utils.ConnectionUtil;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class FormDaoImpl implements FormDao {

    //ConnFactory cf = ConnFactory.getInstance();
    private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

    @Override
    public void createForm(Date courseStart, String location, String description, double cost, String gradingFormat,
                           String typeOfEvent, String workRelatedJustification, double workTimeMissed, String linkToFiles,
                           int employeeId, int deptId) throws SQLException {


        Connection conn = cu.getConnection();
        CallableStatement call = conn.prepareCall("{call createform(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        call.setDate(1, courseStart);
        call.setString(2, location);
        call.setString(3, description);
        call.setDouble(4, cost);
        call.setString(5, gradingFormat);
        call.setString(6, typeOfEvent);
        call.setString(7, workRelatedJustification);
        call.setDouble(8, workTimeMissed);
        call.setString(9, linkToFiles);
        call.setInt(10, employeeId);
        call.setInt(11, deptId);
        call.execute();
    }

    @Override
    public ArrayList<Form> getFormsByUserId(int employeeId) throws SQLException {
        ArrayList<Form> formList = new ArrayList<Form>();
        Connection conn = cu.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM form WHERE employeeid = ?");
        ps.setInt(1, employeeId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            formList.add(new Form(rs.getInt(1), rs.getDate(2), rs.getDate(3), rs.getString(4), rs.getString(5), rs.getDouble(6),
                    rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getInt(11),
                    rs.getString(12), rs.getString(13), rs.getDouble(14), rs.getString(15), rs.getInt(16),
                    rs.getInt(17)));
        }
        return formList;
    }

    @Override
    public boolean isSupervisor(int formId, int userId) throws SQLException{
        Connection conn = cu.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE userid = (SELECT employeeid FROM form WHERE formid = ?) AND reportsto = ?");
        ps.setInt(1, formId);
        ps.setInt(2, userId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    @Override
    public boolean isDeptHead(int formId, int userId) throws SQLException{
        Connection conn = cu.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM form WHERE formid = ? AND deptid = (SELECT deptid FROM department WHERE depthead = ?)");
        ps.setInt(1, formId);
        ps.setInt(2, userId);
        ResultSet rs = ps.executeQuery();
        return rs.next();

    }

    @Override
    public ArrayList<Form> getApprovableFormsByUserId(int employeeId) throws SQLException {
        ArrayList<Form> formList = new ArrayList<Form>();
        Connection conn = cu.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        if (new UserDaoImpl().isBenco(employeeId).equals("1")) {
            ps = conn.prepareStatement("SELECT * FROM \"TRMS1.2\".form WHERE status >= 2");
            rs = ps.executeQuery();
            while (rs.next()) {
                formList.add(new Form(rs.getInt(1), rs.getDate(2), rs.getDate(3), rs.getString(4), rs.getString(5),
                        rs.getDouble(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
                        rs.getInt(11), rs.getString(12), rs.getString(13), rs.getDouble(14), rs.getString(15),
                        rs.getInt(16), rs.getInt(17)));
            }
            return formList;
        } else {
            // States: 0 just opened, 1 super approved, 2 dep approved, 3 benco approved, 4 passed
            ps = conn.prepareStatement("SELECT * FROM \"TRMS1.2\".form WHERE employeeid IN (SELECT userId from \"TRMS1.2\".employee WHERE reportsto = ?) AND status >= 0 OR deptid = (SELECT deptId FROM \"TRMS1.2\".department WHERE depthead = ?) AND status >= 1");
            ps.setInt(1, employeeId);
            ps.setInt(2, employeeId);
            rs = ps.executeQuery();
            while (rs.next()) {
                formList.add(new Form(rs.getInt(1), rs.getDate(2), rs.getDate(3), rs.getString(4), rs.getString(5),
                        rs.getDouble(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
                        rs.getInt(11), rs.getString(12), rs.getString(13), rs.getDouble(14), rs.getString(15),
                        rs.getInt(16), rs.getInt(17)));
            }
            return formList;
        }
    }

    @Override
    public boolean hasApprovableForms(int userId) throws SQLException {
        return (getApprovableFormsByUserId(userId) != null);
    }

    @Override
    public ArrayList<Form> getFormsByStatus(int status) throws SQLException {
        ArrayList<Form> formList = new ArrayList<Form>();
        Connection conn = cu.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Form WHERE status = ?");
        ps.setInt(1, status);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            formList.add(new Form(rs.getInt(1), rs.getDate(2), rs.getDate(3), rs.getString(4), rs.getString(5), rs.getDouble(6),
                    rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getInt(11),
                    rs.getString(12), rs.getString(13), rs.getDouble(14), rs.getString(15), rs.getInt(16),
                    rs.getInt(17)));
        }
        return formList;
    }

    @Override
    public Form getFormBy(int formid) throws SQLException {
        Connection conn = cu.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Form WHERE formid = ?");
        ps.setInt(1, formid);
        ResultSet rs = ps.executeQuery();
        Form f = null;
        if (rs.next()) {
            f = new Form(rs.getInt(1), rs.getDate(2), rs.getDate(3), rs.getString(4), rs.getString(5), rs.getDouble(6),
                    rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getInt(11),
                    rs.getString(12), rs.getString(13), rs.getDouble(14), rs.getString(15), rs.getInt(16),
                    rs.getInt(17));
        }
        return f;
    }

    @Override
    public void setStatus(int formId, int state) throws SQLException{
        Connection conn = cu.getConnection();
        CallableStatement call = conn.prepareCall("{ call change_status(?, ?)");
        call.setInt(1, formId);
        call.setInt(2, state);
        call.execute();
    }

    @Override
    public HashMap<Integer, String> getDepartments() throws SQLException {
        HashMap<Integer, String> departments = new HashMap<Integer, String>();
        Connection conn = cu.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT deptid, deptname FROM \"TRMS1.2\".department");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            departments.put(rs.getInt(1), rs.getString(2));
        }
        return departments;
    }
}