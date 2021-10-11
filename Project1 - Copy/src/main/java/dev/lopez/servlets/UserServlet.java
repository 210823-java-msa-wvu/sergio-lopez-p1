package dev.lopez.servlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lopez.models.User;
import dev.lopez.services.UserDaoImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession(false);
        PrintWriter out = response.getWriter();
        if ( s != null) {
            User user = null;
            try {
                user = new UserDaoImpl().getUserById((int)s.getAttribute("userId"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ObjectMapper om = new ObjectMapper();
            om.writeValue(out, user);
        }else {
            response.sendRedirect("/login.html");
        }
        out.close();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}