package dev.lopez.utils.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lopez.utils.exceptions.NoPermissionException;
import dev.lopez.utils.exceptions.NotYoursToSignException;
import dev.lopez.utils.model.Manager;
import dev.lopez.utils.model.Reimburse;
import dev.lopez.utils.services.ReimburseServices;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/benco","/benco/confirm_grade/*","/benco/change/*","/benco/exceed/*"})
public class BencoSpecialServlet extends HttpServlet {
    private static final ObjectMapper om = new ObjectMapper();
    private static final ReimburseServices reimburseServices = new ReimburseServices();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Reimburse> lor = reimburseServices.getAllReimbursement();
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
        //get manager info
        Cookie[] cookies = req.getCookies();
        Manager m = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("managerinfo")){
                m = om.readValue(cookie.getValue(), Manager.class);
                break;
            }
        }
        if(servletPath.equals("/confirm_grade")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/benco/confirm_grade
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            try{
                int id = Integer.parseInt(temp.toString());
                reimburseServices.confirmGrade(m,id);
                resp.setStatus(200);
            }
            catch (NumberFormatException|NullPointerException e){
                resp.setStatus(400);
                resp.getWriter().write("bad request, unable to find the reimbursement");
            }
            catch (NotYoursToSignException | NoPermissionException e){
                resp.setStatus(403);
                resp.getWriter().write("declined, the reimbursement is not eligible to be confirmed");
            }
        }
        else if(servletPath.equals("/change")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/benco/change
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            try {
                int id = Integer.parseInt(temp.toString());
                Map<String, String> map = om.readValue(req.getReader(), Map.class);
                float amount = Float.parseFloat(map.get("amount"));
                String reason = map.get("reason");
                reimburseServices.changeAmount(m, id, amount, reason);
                resp.setStatus(200);
            }
            catch (NumberFormatException|NullPointerException e){
                resp.setStatus(400);
                resp.getWriter().write("bad request, unable to find the reimbursement");
            }
            catch (NotYoursToSignException|NoPermissionException e){
                resp.setStatus(403);
                resp.getWriter().write("declined, you are not allowed to decline that reimbursement");
            }
        }
        else if(servletPath.equals("/exceed")){
            List<Reimburse> lor = reimburseServices.getReimburseExceed();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().print(om.writeValueAsString(lor));
            resp.setStatus(200);
        }
    }
}
