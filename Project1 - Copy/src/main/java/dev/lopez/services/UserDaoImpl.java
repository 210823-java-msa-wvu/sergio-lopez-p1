package dev.lopez.services;
import dev.lopez.daos.UserDao;
import dev.lopez.models.User;
import dev.lopez.utils.ConnectionUtil;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDaoImpl implements UserDao {

    //ConnectionUtil cf = ConnectionUtil.getInstance();
    private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

    @Override
    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> userList = new ArrayList<User>();
        Connection conn = cu.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM \"TRMS1.2\".employee");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            userList.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                    rs.getDouble(6), rs.getInt(7), rs.getString(8), rs.getString(9)));
        }
        return userList;
    }

    @Override
    public User loginUser(String username, String password) throws SQLException {
        Connection conn = cu.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM \"TRMS1.2\".employee where username = '" + username + "' AND pass_word = '" + password + "'");

        ResultSet rs = ps.executeQuery();
        User u = new User();
        u.setId(-1);
        if (rs.next()) {
            u = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                    rs.getDouble(6), rs.getInt(7), rs.getString(8), rs.getString(9));
        }
        return u;
    }


    public void changeReimbursementAmount(double newAmount, int userId) throws SQLException {
        Connection conn = cu.getConnection();
        CallableStatement call = conn.prepareCall("{ call setReimbursementAmount(?, ?) }");
        call.setDouble(1, newAmount);
        call.setInt(2, userId);
        call.execute();
    }

    public void resetReimbursementAllUsers() throws SQLException{
        Connection conn = cu.getConnection();
        CallableStatement call = conn.prepareCall("{ call resetReimbursementAmount() }");
        call.execute();
    }

   @Override
    public String isBenco(int userid) throws SQLException{
        Connection conn = cu.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM \"TRMS1.2\".benco where bencoid = ?"); //SELECT bencoid FROM benco WHERE bencoid = ?
        ps.setInt(1, userid);
        ResultSet rs = ps.executeQuery();
        return rs.next()?"1":"0";
    }

    public int getUserIdByFormId(int formid) throws SQLException {
        Connection conn = cu.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT employeeid FROM form WHERE formid = ?");
        ps.setInt(1, formid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    @Override
    public User getUserById(int id) throws SQLException {
        Connection conn = cu.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM \"TRMS1.2\".employee WHERE userid = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        User u = null;
        if (rs.next()) {
            u = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                    rs.getDouble(6), rs.getInt(7), rs.getString(8), rs.getString(9));
        }
        return u;
    }

    @Override
    public User getBenCo() throws SQLException {
        Connection conn = cu.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * from \"TRMS1.2\".employee WHERE userid = (SELECT bencoid FROM benco)");
        ResultSet rs = ps.executeQuery();
        User u = null;
        if (rs.next()) {
            u = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                    rs.getDouble(6), rs.getInt(7), rs.getString(8), rs.getString(9));
        }
        return u;
    }
}
