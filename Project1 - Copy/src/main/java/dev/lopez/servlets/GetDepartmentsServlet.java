package dev.lopez.servlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lopez.services.FormDaoImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class GetDepartmentsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession(false);
        if(s != null) {
            try {
                mapper.writeValue(out, new FormDaoImpl().getDepartments());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            response.sendRedirect("login.html");
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}