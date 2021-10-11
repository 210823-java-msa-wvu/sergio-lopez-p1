package dev.lopez.servlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lopez.services.FormDaoImpl;
import dev.lopez.services.NotificationDaoImpl;
import dev.lopez.services.UserDaoImpl;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class ShowFormsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s != null) {
            while (req.getInputStream().read() != -1) {
            }
            req.getRequestDispatcher("/showforms.html").include(req, resp); // TODO homepage
        } else {
            resp.sendRedirect("/TRMS/login");
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession(false);
        FormDaoImpl fdi = new FormDaoImpl();
        UserDaoImpl udi = new UserDaoImpl();
        ObjectMapper mapper = new ObjectMapper();
        if (s != null) {
            try {
                XhrGrading grade = mapper.readValue(request.getInputStream(), XhrGrading.class);
                if (grade.getPassed() == 1) {
                    try {
                        if(udi.getUserById((int)s.getAttribute("userId")).getRmnReimbursement() >= 0) {
                            fdi.setStatus(grade.getFormId(), 4);
                        } else {
                            //notifications for user benco
                            NotificationDaoImpl ndi = new NotificationDaoImpl();
                            ndi.createNotification(grade.getFormId(), "Your grade for form " + grade.getFormId() + " could not be accepted, as no one agreed to you overdrawing your maximum amount of reimbursement per year. Please contact Benefit Coordinator");

                        }
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // TODO Process the money being withdrawn, don't overdraw blablabla - how can
                    // the benco approve of overdrawing? if string == null, don't overdraw?
                } else if (grade.getPassed() == 0) {
                    fdi.setStatus(grade.getFormId(), -1);
                }
            } catch (Exception e) {
            }
        } else {
            response.sendRedirect("login.html");
        }
    }
}



//This class is set up to store data that can be accessed and
// * manipulated through its setter and getter methods.
class XhrGrading {

    //Private data variables.
    private int formId, passed;


    public XhrGrading() {
        super();
    }


    public XhrGrading(int id, int passed) {
        super();
        this.formId = id;
        this.passed = passed;
    }

    //Getter and setter methods.
    public int getFormId() {
        return formId;
    }

    public void setFormId(int id) {
        this.formId = id;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }
}