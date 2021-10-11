package dev.lopez.daos;
import dev.lopez.models.Form;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface FormDao {


    public void createForm(Date courseStart, String location, String description, double cost, String gradingFormat,
                           String typeOfEvent, String workRelatedJustification, double workTimeMissed, String linkToFiles,
                           int employeeId, int deptId) throws SQLException;


    public ArrayList<Form> getFormsByUserId(int employeeId) throws SQLException;


    public boolean isSupervisor(int formId, int userId) throws SQLException;


    public boolean isDeptHead(int formId, int userId) throws SQLException;


    public ArrayList<Form> getApprovableFormsByUserId(int employeeId) throws SQLException;


    public boolean hasApprovableForms(int userId) throws SQLException;


    public ArrayList<Form> getFormsByStatus(int status) throws SQLException;


    public Form getFormBy(int formid) throws SQLException;


    public void setStatus(int formId, int state) throws SQLException;


    public HashMap<Integer, String> getDepartments() throws SQLException;
}