/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import dao.contracts.BaseContract;
import dao.BaseAccess;
import dao.dto.Admin;
import dao.dto.Instructor;
import dao.dto.Student;
import frames.AdminFrame;
import frames.InstructorFrame;
import frames.StudentFrame;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

/**
 *
 * @author Milos
 */
public class SignInFacade {

    private final JFrame sif;
    private final BaseContract acc;

    public SignInFacade(JFrame sif) {
        this.sif = sif;
        acc = new BaseAccess();
    }

    public void signInCheck(JTextField username_Email_Field_, JPasswordField password_Field_) {
        if (username_Email_Field_.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username / Email field is empty!", "Info!", JOptionPane.INFORMATION_MESSAGE);
        } else if (password_Field_.getPassword().length == 0) {
            JOptionPane.showMessageDialog(null, "Password field is empty!", "Info!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String pwd = new String(password_Field_.getPassword());
            String loginDetails = username_Email_Field_.getText();
            checkForUser(loginDetails, pwd);
        }
    }

    private void checkForUser(String loginDetails, String pwd) {
        try {
            Object obj = acc.getUser(loginDetails, pwd);
            if (obj != null) {
                if (obj instanceof Admin admin) {
                    AdminFrame aframe = new AdminFrame(admin);
                    aframe.setVisible(true);
                    sif.dispose();
                }
                if (obj instanceof Instructor instructor) {
                    InstructorFrame iframe = new InstructorFrame(instructor);
                    iframe.setVisible(true);
                    sif.dispose();
                }
                if (obj instanceof Student student) {
                    StudentFrame sframe = new StudentFrame(student);
                    sframe.setVisible(true);
                    sif.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(null, "The user does not exist!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
