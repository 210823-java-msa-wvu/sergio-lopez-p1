package dev.lopez.utils.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lopez.utils.exceptions.InsufficientBalanceException;
import dev.lopez.utils.exceptions.StartDatePassedException;
import dev.lopez.utils.model.Employee;
import dev.lopez.utils.model.Reimburse;
import dev.lopez.utils.services.ReimburseServices;
import dev.lopez.utils.services.UserServices;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/submit_reimbursement")
public class SubmitServlet extends HttpServlet {
    private static final ObjectMapper om = new ObjectMapper();
    private static final ReimburseServices reimburseServices = new ReimburseServices();
    private static final UserServices userServices = new UserServices();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        Employee emp = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("employeeinfo")){
                emp = om.readValue(cookie.getValue(),Employee.class);
                break;
            }
        }
        System.out.println(req.getParameter("type"));
        try {
            Reimburse reimb = reimburseServices.submitReimburse(emp,
                    LocalDate.parse(req.getParameter("startdate")),
                    Integer.parseInt(req.getParameter("type")),
                    Float.parseFloat(req.getParameter("cost")),
                    Integer.parseInt(req.getParameter("grade_format")),
                    Integer.parseInt(req.getParameter("pass_grade")),
                    req.getParameter("note"),
                    req.getParameter("location"));
            resp.setStatus(200);
            Cookie cookie = new Cookie("employeeinfo",om.writeValueAsString(userServices.getEmployeeById(emp.getId())));
            resp.addCookie(cookie);
            resp.sendRedirect("http://localhost:8083/Project1/employee.html");
        }
        catch (InsufficientBalanceException e){
            resp.setStatus(403);
            resp.sendRedirect("http://localhost:8083/Project1/insufficient_amount.html");
        }
        catch (StartDatePassedException e){
            resp.setStatus(403);
            resp.sendRedirect("http://localhost:8083/Project1/start_date_passed.html");
        }
    }
}
