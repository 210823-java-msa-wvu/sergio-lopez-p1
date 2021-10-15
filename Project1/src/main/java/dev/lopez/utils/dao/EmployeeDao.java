package dev.lopez.utils.dao;

import dev.lopez.utils.model.Employee;
import dev.lopez.utils.utils.ConnectionUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

    //Read
    public Employee getEmployeeById(Integer id){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            String sql = "select * from \"TRMS1.3\".employees where id=?";

            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1,id);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                Employee emp = new Employee();
                emp.setId(id);
                emp.setUsername(result.getString("username"));
                emp.setPassword(result.getString("password"));
                emp.setFirst(result.getString("first"));
                emp.setLast(result.getString("last"));
                emp.setReport_to(result.getInt("reports_to"));
                emp.setPending(result.getFloat("pending"));
                emp.setAwarded(result.getFloat("awarded"));
                return emp;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Employee getEmployeeByName(String name){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            String sql = "select * from \"TRMS1.3\".employees where username=?";

            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1,name);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                Employee emp = new Employee();
                emp.setId(result.getInt("id"));
                emp.setUsername(result.getString("username"));
                emp.setPassword(result.getString("password"));
                emp.setFirst(result.getString("first"));
                emp.setLast(result.getString("last"));
                emp.setReport_to(result.getInt("reports_to"));
                emp.setPending(result.getFloat("pending"));
                emp.setAwarded(result.getFloat("awarded"));
                return emp;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> getAllEmployees(){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            String sql = "select * from \"TRMS1.3\".employees";
            List<Employee> employees = new ArrayList<Employee>();

            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet result = ps.executeQuery();

            while(result.next()){
                Employee emp = new Employee();
                emp.setId(result.getInt("id"));
                emp.setUsername(result.getString("username"));
                emp.setPassword(result.getString("password"));
                emp.setFirst(result.getString("first"));
                emp.setLast(result.getString("last"));
                emp.setReport_to(result.getInt("reports_to"));
                emp.setPending(result.getFloat("pending"));
                emp.setAwarded(result.getFloat("awarded"));
                employees.add(emp);
            }
            return employees;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //Create, not allowed in app

    //Update
    public boolean updateEmployeePending(Employee emp){
        try(Connection con=cu.getConnection()){
            String sql = "update \"TRMS1.3\".employees set pending=? where id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setFloat(1,emp.getPending());
            ps.setInt(2,emp.getId());

            ps.executeUpdate();

            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEmployeeAwarded(Employee emp){
        try(Connection con=cu.getConnection()){
            String sql = "update \"TRMS1.3\".employees set awarded=? where id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setFloat(1,emp.getAwarded());
            ps.setInt(2,emp.getId());

            ps.executeUpdate();

            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Delete, not allowed in app
}
