package dev.lopez.utils.dao;

import dev.lopez.utils.model.Manager;
import dev.lopez.utils.utils.ConnectionUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagerDao {
    private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

    //Read
    public Manager getManagerById(Integer id){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            String sql = "select * from \"TRMS1.3\".manager where id=?";

            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1,id);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                Manager m = new Manager();
                m.setId(id);
                m.setUsername(result.getString("username"));
                m.setPassword(result.getString("password"));
                m.setFirst(result.getString("first"));
                m.setLast(result.getString("last"));
                m.setReport_to(result.getInt("report_to"));
                m.setRank(result.getInt("rank"));
                return m;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Manager getManagerByName(String name){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            String sql = "select * from \"TRMS1.3\".manager where username=?";

            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1,name);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                Manager m = new Manager();
                m.setId(result.getInt("id"));
                m.setUsername(result.getString("username"));
                m.setPassword(result.getString("password"));
                m.setFirst(result.getString("first"));
                m.setLast(result.getString("last"));
                m.setReport_to(result.getInt("report_to"));
                m.setRank(result.getInt("rank"));
                return m;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Manager> getAllManagers(){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            String sql = "select * from \"TRMS1.3\".manager";
            List<Manager> managers = new ArrayList<Manager>();

            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet result = ps.executeQuery();

            while(result.next()){
                Manager m = new Manager();
                m.setId(result.getInt("id"));
                m.setUsername(result.getString("username"));
                m.setPassword(result.getString("password"));
                m.setFirst(result.getString("first"));
                m.setLast(result.getString("last"));
                m.setReport_to(result.getInt("report_to"));
                m.setRank(result.getInt("rank"));
                managers.add(m);
            }
            return managers;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //Create, not allowed in app

    //Update, not allowed in app

    //Delete, not allowed in app
}
