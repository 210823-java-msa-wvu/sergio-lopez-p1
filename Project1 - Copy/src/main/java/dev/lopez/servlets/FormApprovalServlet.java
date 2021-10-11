package dev.lopez.servlets;
import java.io.IOException;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lopez.models.Form;
import dev.lopez.models.User;
import dev.lopez.services.FormDaoImpl;
import dev.lopez.services.NotificationDaoImpl;
import dev.lopez.services.UserDaoImpl;

public class FormApprovalServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession s = request.getSession(false);
        if(s != null) {
            request.getRequestDispatcher("/approval.html").forward(request, response);
        }else {
            response.sendRedirect("/TRMS/login");
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession s = request.getSession(false);
        FormDaoImpl fdi = new FormDaoImpl();
        UserDaoImpl udi = new UserDaoImpl();
        NotificationDaoImpl ndi = new NotificationDaoImpl();
        ObjectMapper mapper = new ObjectMapper();
        XhrApprovalObject xhrApproval = mapper.readValue(request.getInputStream(), XhrApprovalObject.class);
        System.out.println(xhrApproval);
        int status = 0;
        if (s != null) {
            status = xhrApproval.getStatus();
            if (xhrApproval.getApproved() == 1) {

                // change
                // If the state is 0 and the user is the supervisor, we set the status to one or
                // two, depending on if he also is the depthead
                try {
                    if (status == 0 && fdi.isSupervisor(xhrApproval.getFormId(), (int) s.getAttribute("userId"))) {
                        status = (fdi.isDeptHead(xhrApproval.getFormId(), (int) s.getAttribute("userId"))) ? 2 : 1;

                    } else if (status == 1 && fdi.isDeptHead(xhrApproval.getFormId(), (int) s.getAttribute("userId"))) {
                        status = 2;
                    } else if (status == 2 && (int) s.getAttribute("isBenco") == 1) {

                        status = 3;
                    }
                    fdi.setStatus(xhrApproval.getFormId(), status);
                } catch (SQLException e1) {

                    e1.printStackTrace();
                }
            } else if ((status = xhrApproval.getApproved()) == -1) { // Denied the Form!
                System.out.println(status + "We're in denial of form." + xhrApproval.getFormId());
                try { // lmao don't even try to understand this mess! jkjk - it get's the userid of
                    // the forms owner and updates his remaining balance to blurg I HATE JAVASCRIPT

                    int userIdOfForm = udi.getUserIdByFormId(xhrApproval.getFormId());
                    ndi.createNotification(xhrApproval.getFormId(),
                            "Your Application was denied. \n\n" + xhrApproval.getAttachedReasoning());
                    double newAmount = calculateNewAmount(fdi.getFormBy(xhrApproval.getFormId()),
                            udi.getUserById(userIdOfForm));
                    udi.changeReimbursementAmount(newAmount, userIdOfForm);
                    System.out.println(newAmount + " This is the new amount that is being set.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println(status + " This is the Status that is being set.");
                    fdi.setStatus(xhrApproval.getFormId(), status);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            } else if (xhrApproval.getApproved() == 0) { // Add a Question! Display them in open forms? where status !=
                // -1
                // && 3 && 4
                System.out.println(xhrApproval);
                try {
                    ndi.createNotification(xhrApproval.getFormId(), "Open Question about your form "
                            + xhrApproval.getFormId() + ": " + xhrApproval.getAttachedReasoning());
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            response.sendRedirect("login.html");
        }
    }

    private double calculateNewAmount(Form f, User u) {
        double newAmount = u.getRmnReimbursement();
        switch (f.getTypeOfEvent()) {
            case "1":
                newAmount = 0.8 * f.getCost();
                break;
            case "2":
                newAmount = 0.6 * f.getCost();
                break;
            case "3":
                newAmount = 0.75 * f.getCost();
                break;// document.getElementById("reimbamtEst").value = parseFloat(
            case "4":
                newAmount = f.getCost();
                break;

            case "5":
                newAmount = 0.9 * f.getCost();
                break;
            case "6":
                newAmount = 0.3 * f.getCost();
                break;
        }
        return newAmount + u.getRmnReimbursement();
    }


}


class XhrApprovalObject {

    // Private data variables.
    private String attachedReasoning;
    private int formId, approved, subsAmt, status;


    public XhrApprovalObject() {
        super();
    }


    public XhrApprovalObject(int formId, int approved, String attachedReasoning, int newReimbursementAmount,
                             int status) {
        super();
        this.formId = formId;
        this.approved = approved;
        this.attachedReasoning = attachedReasoning;
        this.subsAmt = newReimbursementAmount;
        this.status = status;
    }

    // Getter and setter methods.

    public int getSubsAmt() {
        return subsAmt;
    }

    public void setSubsAmt(int subsAmt) {
        this.subsAmt = subsAmt;
    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getAttachedReasoning() {
        return attachedReasoning;
    }

    public void setAttachedReasoning(String attachedReasoning) {
        this.attachedReasoning = attachedReasoning;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    // toString

    @Override
    public String toString() {
        return "XhrApprovalObject [attachedReasoning=" + attachedReasoning + ", formId=" + formId + ", approved="
                + approved + ", subsAmt=" + subsAmt + ", status=" + status + "]";
    }

}