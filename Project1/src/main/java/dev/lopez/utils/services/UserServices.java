package dev.lopez.utils.services;


import dev.lopez.utils.dao.EmployeeDao;
import dev.lopez.utils.dao.ManagerDao;
import dev.lopez.utils.model.Employee;
import dev.lopez.utils.model.Manager;


public class UserServices {
    private static final EmployeeDao empDao = new EmployeeDao();
    private static final ManagerDao managerDao = new ManagerDao();

    public Employee employeeLogin(String username, String password){
        Employee emp = empDao.getEmployeeByName(username);
        if(emp==null) return null;
        if(password.equals(emp.getPassword())) return emp;
        else return null;
    }

    public Employee getEmployeeById(int id){
        return empDao.getEmployeeById(id);
    }

    public Manager managerLogin(String username, String password){
        Manager m = managerDao.getManagerByName(username);
        if(m==null) return null;
        if(password.equals(m.getPassword())) return m;
        else return null;
    }

    public Manager getManagerById(int id){
        return managerDao.getManagerById(id);
    }
    public Employee addPending(Employee emp,float amount){
        float previous = emp.getPending();
        emp.setPending(previous + amount);
        empDao.updateEmployeePending(emp);
        return emp;
    }

    public Employee addAwarded(Employee emp,float amount){
        float previous = emp.getAwarded();
        emp.setAwarded(previous + amount);
        empDao.updateEmployeeAwarded(emp);
        return emp;
    }
}
