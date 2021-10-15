package dev.lopez.utils.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lopez.utils.exceptions.NoPermissionException;
import dev.lopez.utils.exceptions.NotYoursToSignException;
import dev.lopez.utils.model.Manager;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/manager_manage", "/manager_manage/requestinfo_e/*","/manager_manage/requestinfo_m/*",
        "/manager_manage/confirm_presentation/*","/manager_manage/sign/*","/manager_manage/decline/*",
        "/manager_manage/upload/*"})
public class ManagerReimburseServlet extends HttpServlet {
    private static final ObjectMapper om = new ObjectMapper();
    private static final ReimburseServices reimburseServices = new ReimburseServices();
    private static final UserServices userServices = new UserServices();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        Manager m = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("managerinfo")){
                m = om.readValue(cookie.getValue(), Manager.class);
                break;
            }
        }
        reimburseServices.updateReimbursement();
        List<Reimburse> lor = new ArrayList<>();
        List<Reimburse> lor1 = reimburseServices.getReimburseForManager(m);
        List<Reimburse> lor2 = reimburseServices.getReimburseForReward(m);
        lor.addAll(lor1);
        lor.addAll(lor2);
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
        if(servletPath.equals("/requestinfo_e")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/manager_manage/requestinfo_e
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            try{
                int id = Integer.parseInt(temp.toString());
                reimburseServices.requestInfo(m,id,0);
                resp.setStatus(200);
            }
            catch (NumberFormatException|NullPointerException e){
                resp.setStatus(400);
                resp.getWriter().write("bad request, unable to find the reimbursement");
            }
            catch (NotYoursToSignException | NoPermissionException e){
                resp.setStatus(403);
                resp.getWriter().write("declined, you are not allowed to request info on this reimbursement");
            }
        }
        else if(servletPath.equals("/confirm_presentation")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/manager_manage/requestinfo_e
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
            catch (NotYoursToSignException|NoPermissionException e){
                resp.setStatus(403);
                resp.getWriter().write("declined, the reimbursement is not eligible to be confirmed");
            }
        }
        else if(servletPath.equals("/requestinfo_m")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/manager_manage/requestinfo_e
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            try{
                int id = Integer.parseInt(temp.toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
                String json = br.readLine();
                int manager_rank = Integer.parseInt(json);
                reimburseServices.requestInfo(m,id,manager_rank);
                resp.setStatus(200);
            }
            catch (NumberFormatException|NullPointerException e){
                resp.setStatus(400);
                resp.getWriter().write("bad request, unable to find the reimbursement");
            }
            catch (NotYoursToSignException|NoPermissionException e){
                resp.setStatus(403);
                resp.getWriter().write("declined, you are not allowed to request info for that reimbursement");
            }
        }
        else if(servletPath.equals("/sign")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/manager_manage/requestinfo_e
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            try{
                int id = Integer.parseInt(temp.toString());
                reimburseServices.signReimburse(m,id);
                resp.setStatus(200);
            }
            catch (NumberFormatException|NullPointerException e){
                resp.setStatus(400);
                resp.getWriter().write("bad request, unable to find the reimbursement");
            }
            catch (NotYoursToSignException|NoPermissionException e){
                resp.setStatus(403);
                resp.getWriter().write("declined, you are not allowed to sign that reimbursement");
            }
        }
        else if(servletPath.equals("/decline")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/manager_manage/requestinfo_e
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            try{
                int id = Integer.parseInt(temp.toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
                String reason = br.readLine();
                reimburseServices.declineReimburse(m,id,reason);
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
        else if(servletPath.equals("/upload")){
            StringBuilder temp = new StringBuilder(req.getRequestURI());
            //clean up /Project1/manager_manage/requestinfo_e
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            temp.replace(0,temp.indexOf("/")+1,"");
            try{
                int id = Integer.parseInt(temp.toString());
                reimburseServices.uploadInfo(m,id);
                resp.setStatus(200);
            }
            catch (NumberFormatException|NullPointerException e){
                resp.setStatus(400);
                resp.getWriter().write("bad request, unable to find the reimbursement");
            }
            catch (NotYoursToSignException|NoPermissionException e){
                resp.setStatus(403);
                resp.getWriter().write("declined, you are not allowed to upload to that reimbursement");
            }
        }
    }
}
