package dev.lopez.daos;
import dev.lopez.models.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserDao {


    public ArrayList<User> getUsers() throws SQLException;


    public User loginUser(String username, String password) throws SQLException;


    public void changeReimbursementAmount(double newAmount, int userId) throws SQLException;


    public void resetReimbursementAllUsers() throws SQLException;


    public String isBenco(int userid) throws SQLException;


    public int getUserIdByFormId(int formid) throws SQLException;


    public User getUserById(int id) throws SQLException;


    public User getBenCo() throws SQLException;
}