package dev.lopez.servlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lopez.models.Form;
import dev.lopez.services.FormDaoImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GetApprovableFormsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        ObjectMapper om = new ObjectMapper();
        HttpSession s = req.getSession(false);
        if(s != null) {
            FormDaoImpl fdi = new FormDaoImpl();
            ArrayList<Form> approvableForms = null;
            try {
                approvableForms = fdi.getApprovableFormsByUserId((int)s.getAttribute("userId"));
                ArrayList<FormWithDeptName> allFormsWithDeptName = new ArrayList<FormWithDeptName>();
                for (Form form : approvableForms) {
                    allFormsWithDeptName.add(new FormWithDeptName(form));
                }
                om.writeValue(out, allFormsWithDeptName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            resp.sendRedirect("/TRMS/login");
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}