package dev.lopez.driver;

import java.sql.SQLException;

import dev.lopez.services.FormDaoImpl;
import dev.lopez.services.UserDaoImpl;

public class Driver {

    public static void main(String[] args) {

        UserDaoImpl udi = new UserDaoImpl();
        FormDaoImpl fdi = new FormDaoImpl();


        try {
            //System.out.println(udi.getUserById(3));
            System.out.println(udi.loginUser("lopezbraca", "password"));
            //fdi.createForm(Date.valueOf("2020-05-21"), "FSU", "Mediocre course to learn coding!", 150000.00, "LetterGrade", "Coding-Course", "", 0, "", udi.getBenCo().getId(), 3);
            System.out.println(fdi.getFormsByUserId(udi.getBenCo().getId()));
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

}
