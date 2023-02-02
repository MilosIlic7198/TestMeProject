/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import com.toedter.calendar.JDateChooser;
import dao.contracts.AdminContract;
import dao.AdminAccess;
import dao.dto.Admin;
import dao.dto.Instructor;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Milos
 */
public class AdminFacade {

    private Admin admin;
    private AdminContract adminDAO;
    private Instructor instructor;
    private Pattern emailPattern;
    private Matcher matcher;
    private String emailRegex;

    public AdminFacade(Admin admin) {
        this.admin = admin;
        adminDAO = new AdminAccess();
    }

    public void addInstructorCheck(
            JTextField instructor_First_Name_Field_,
            JTextField instructor_Last_Name_Field_,
            JTextField instructor_Initials_Field_,
            JTextField instructor_Phone_Number_Field_,
            ButtonGroup instructor_Gender_ButtonGroup_,
            JRadioButton instructor_Gender_Male_,
            JRadioButton instructor_Gender_Female_,
            JDateChooser instructor_Birthdate_DateChooser_,
            JTextField instructor_Email_Field_,
            JTextField instructor_Password_Field_) {
        //TODO: Add validation!
        if (instructor_First_Name_Field_.getText().trim().isEmpty()
                || instructor_Last_Name_Field_.getText().trim().isEmpty()
                || instructor_Initials_Field_.getText().trim().isEmpty()
                || instructor_Phone_Number_Field_.getText().trim().isEmpty()
                || (instructor_Gender_Male_.isSelected() == false && instructor_Gender_Female_.isSelected() == false)
                || instructor_Birthdate_DateChooser_.getDate() == null
                || instructor_Email_Field_.getText().trim().isEmpty()
                || instructor_Password_Field_.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (instructor_Initials_Field_.getText().length() > 3
                || instructor_Initials_Field_.getText().equals(instructor_Initials_Field_.getText().toLowerCase())) {
            JOptionPane.showMessageDialog(null, "Initials must be in uppercase and max 3 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!emailValidation(instructor_Email_Field_.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid email!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String gender = "";
        if (instructor_Gender_Male_.isSelected()) {
            gender = "M";
        } else if (instructor_Gender_Female_.isSelected()) {
            gender = "F";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(instructor_Birthdate_DateChooser_.getDate());
        instructor = new Instructor(
                instructor_Email_Field_.getText(),
                instructor_Password_Field_.getText(),
                instructor_First_Name_Field_.getText(),
                instructor_Last_Name_Field_.getText(),
                instructor_Initials_Field_.getText(),
                date,
                gender.charAt(0),
                instructor_Phone_Number_Field_.getText(),
                1
        );//TODO: Change it to admin.getAdminId() later!

        addInstructor(instructor);

        JOptionPane.showMessageDialog(null, "New instructor was added!", "Info!", JOptionPane.INFORMATION_MESSAGE);

        instructor_Email_Field_.setText(null);
        instructor_Password_Field_.setText(null);
        instructor_First_Name_Field_.setText(null);
        instructor_Last_Name_Field_.setText(null);
        instructor_Initials_Field_.setText(null);
        instructor_Phone_Number_Field_.setText(null);
        instructor_Gender_ButtonGroup_.clearSelection();
        instructor_Birthdate_DateChooser_.setCalendar(null);
        instructor = null;
    }

    private boolean emailValidation(String s) {
        emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        matcher = emailPattern.matcher(s);
        return matcher.find();
    }

    private void addInstructor(Instructor instructor) {
        try {
            adminDAO.addInstructor(instructor);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
