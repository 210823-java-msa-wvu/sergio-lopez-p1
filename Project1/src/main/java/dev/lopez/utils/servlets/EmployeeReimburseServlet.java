package dev.lopez.utils.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lopez.utils.exceptions.NoPermissionException;
import dev.lopez.utils.exceptions.NotYoursToSignException;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@WebServlet(urlPatterns = {"/employee_manage", "/employee_manage/revoke/*","/employee_manage/upinfo/*",
                           "/employee_manage/upscore/*","/employee_manage/uppresentation/*"})
public class EmployeeReimburseServlet extends HttpServlet {
    private static final ObjectMapper om = new ObjectMapper();
    private static final ReimburseServices reimburseServices = new ReimburseServices();
    private static final UserServices userServices = new UserServices();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        Employee emp = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("employeeinfo")){
                emp = om.readValue(cookie.getValue(), Employee.class);
                break;
            }
        }
        reimburseServices.updateReimbursement();
        List<Reimburse> lor = reimburseServices.getReimburseByApplicant(emp.getId());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(om.writeValueAsString(lor));
        resp.setStatus(200);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //find out type of put request
        StringBuilder servletpath_sb = new StringBuilder(req.getServletPath());
        servletpath_sb.replace(0,1,"");
        System.out.println(servletpath_sb);
        servletpath_sb.replace(0,servletpath_sb.indexOf("/"),"");
        System.out.println(servletpath_sb);
        String servletPath = servletpath_sb.toString();
        //get exmployee info
        Cookie[] cookies = req.getCookies();
        Employee emp = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("employeeinfo")){
                emp = om.readValue(cookie.getValue(), Employee.class);
                break;
            }
        }
        if(servletPath.equals("/revoke")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/employee_manage/revoke/
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            try{
                int id = Integer.parseInt(temp.toString());
                reimburseServices.declineReimburse(emp.getId(),id);
                resp.setStatus(200);
            }
            catch (NumberFormatException|NullPointerException e){
                resp.setStatus(400);
                resp.getWriter().write("bad request, unable to find the reimbursement");
            }
            catch (NotYoursToSignException e){
                resp.setStatus(403);
                resp.getWriter().write("declined, you are not allowed to decline that reimbursement");
            }
        }
        else if(servletPath.equals("/upinfo")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/employee_manage/upinfo/
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            try{
                int id = Integer.parseInt(temp.toString());
                reimburseServices.uploadInfo(emp,id);
                resp.setStatus(200);
            }
            catch (NumberFormatException|NullPointerException e){
                resp.setStatus(400);
            }
            catch (NoPermissionException e){
                resp.setStatus(403);
            }
        }
        else if(servletPath.equals("/upscore")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/employee_manage/upscore/
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            try{
                int id = Integer.parseInt(temp.toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
                String json = br.readLine();
                int score = Integer.parseInt(json);
                reimburseServices.uploadGrade(emp,id,score);
                resp.setStatus(200);
            }
            catch (NumberFormatException|NullPointerException e){
                resp.setStatus(400);
            }
            catch (NoPermissionException e){
                resp.setStatus(403);
            }
        }
        else if(servletPath.equals("/uppresentation")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/employee_manage/uppresentation/
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            try{
                int id = Integer.parseInt(temp.toString());
                reimburseServices.uploadPresentation(emp,id);
                resp.setStatus(200);
            }
            catch (NumberFormatException|NullPointerException e){
                resp.setStatus(400);
            }
            catch (NoPermissionException e){
                resp.setStatus(403);
            }
        }
        else{
            resp.setStatus(404);
        }
        Cookie cookie = new Cookie("employeeinfo",om.writeValueAsString(userServices.getEmployeeById(emp.getId())));
        resp.addCookie(cookie);
    }
}
