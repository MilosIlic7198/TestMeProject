/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Milos
 */
public class AdminFacade {

    private AdminRegisterFacade register;
    private AdminModelFacade model;
    private AdminControlFacade control;

    public AdminFacade() {
        this.register = new AdminRegisterFacade();
        this.model = new AdminModelFacade();
        this.control = new AdminControlFacade();
    }

    public boolean adminCheck(String username, String password, String firstName, String lastName, String email, String phoneNumber) {
        if (firstName.trim().isEmpty()
                || lastName.trim().isEmpty()
                || phoneNumber.trim().isEmpty()
                || email.trim().isEmpty()
                || username.trim().isEmpty()
                || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return register.addAdminCheck(username, password, firstName, lastName, email, phoneNumber);
    }

    public boolean instructorCheck(String email, String password,
            String firstName,
            String lastName,
            String initials,
            String phoneNumber,
            boolean male,
            boolean female,
            Date birthdate) {
        if (firstName.trim().isEmpty()
                || lastName.trim().isEmpty()
                || initials.trim().isEmpty()
                || phoneNumber.trim().isEmpty()
                || (male == false && female == false)
                || birthdate == null
                || email.trim().isEmpty()
                || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return register.addInstructorCheck(email, password, firstName, lastName, initials, phoneNumber, male, female, birthdate);
    }

    public boolean studentCheck(String email, String password,
            String firstName,
            String lastName,
            String city,
            String street,
            String postalCode,
            boolean male,
            boolean female,
            Date birthdate,
            int index) {
        if (firstName.trim().isEmpty()
                || lastName.trim().isEmpty()
                || city.trim().isEmpty()
                || street.trim().isEmpty()
                || postalCode.trim().isEmpty()
                || (male == false && female == false)
                || birthdate == null
                || email.trim().isEmpty()
                || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        int id = model.getIdFromComboboxList(index);
        return register.addStudentCheck(email, password, firstName, lastName, city, street, postalCode, male, female, birthdate, id);
    }

    public boolean editAdminCheck(int id, String username, String password, String firstName, String lastName, String email, String phoneNumber) {
        if (username.trim().isEmpty()
                || password.trim().isEmpty()
                || firstName.trim().isEmpty()
                || lastName.trim().isEmpty()
                || email.trim().isEmpty()
                || phoneNumber.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return control.editAdminAccount(id, username, password, firstName, lastName, email, phoneNumber);
    }

    public boolean deleteAdminCheck(int id) {
        int choice;
        choice = JOptionPane.showConfirmDialog(null, """
                                                     Are you sure you want to delete this admin account?
                                                     You will be presented with a option of FORCE and SOFT delete if you want to continue.
                                                     With FORCE delete you will delete all instructors and students that this admin created!
                                                     It will also delete created test and submitted tests from instuctors and students!
                                                     With SOFT delete you will inherit all instructor and student that this admin created!
                                                     In both cases, created admins will go to god admin to be managed!
                                                     Do you want to continue?""", "Check!", JOptionPane.YES_NO_OPTION);
        if (choice == 1 || choice == -1) {
            return false;
        }
        return control.deleteAdminAccount(id);
    }

    public boolean editInstructorCheck(int id, String email, String password, String firstName, String lastName, String initials, String phoneNumber) {
        if (email.trim().isEmpty()
                || password.trim().isEmpty()
                || firstName.trim().isEmpty()
                || lastName.trim().isEmpty()
                || initials.trim().isEmpty()
                || phoneNumber.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return control.editInstructorAccount(id, email, password, firstName, lastName, initials, phoneNumber);
    }

    public boolean deleteInstructorCheck(int id) {
        int choice;
        choice = JOptionPane.showConfirmDialog(null, """
                                                     Are you sure you want to delete this instructor account?
                                                     If this instructor has created tests and assigned students it will delete them too!
                                                     If you don't want to do that go and change all student instructor id to some other instructor!
                                                     Do you want to continue?""", "Check!", JOptionPane.YES_NO_OPTION);
        if (choice == 1 || choice == -1) {
            return false;
        }
        return control.deleteInstructorAccount(id);
    }

    public boolean editStudentCheck(int id, String email, String password, String firstName, String lastName, String city, String street, String postalCode, String instructorId) {
        if (email.trim().isEmpty()
                || password.trim().isEmpty()
                || firstName.trim().isEmpty()
                || lastName.trim().isEmpty()
                || city.trim().isEmpty()
                || street.trim().isEmpty()
                || postalCode.trim().isEmpty()
                || instructorId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return control.editStudentAccount(id, email, password, firstName, lastName, city, street, postalCode, instructorId);
    }

    public boolean deleteStudentCheck(int id) {
        int choice;
        choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this student account?", "Check!", JOptionPane.YES_NO_OPTION);
        if (choice == 1 || choice == -1) {
            return false;
        }
        return control.deleteStudentAccount(id);
    }

    public boolean godAdminEditCheck(int adminId, String newId) {
        return control.godAdminEdit(adminId, newId);
    }

    public boolean godAdminDeleteCheck(int adminId) {
        int choice;
        choice = JOptionPane.showConfirmDialog(null, """
                                                     Are you sure you want to delete this admin account?
                                                     You will be presented with a option of FORCE and SOFT delete if you want to continue.
                                                     With FORCE delete you will delete all instructors and students that this admin created!
                                                     It will also delete created test and submitted tests from instuctors and students!
                                                     With SOFT delete you will inherit all instructor and student that this admin created!
                                                     In both cases, created admins will go to god admin to be managed!
                                                     Do you want to continue?""", "Check!", JOptionPane.YES_NO_OPTION);
        if (choice == 1 || choice == -1) {
            return false;
        }
        return control.godAdminDelete(adminId);
    }

    public DefaultComboBoxModel setStudentComboboxModel() {
        return model.getStudentComboboxModel();
    }

    public DefaultTableModel setAllAdminTableModel() {
        return model.getAllAdminTableModel();
    }

    public DefaultTableModel setAdminTableModel() {
        return model.getAdminTableModel();
    }

    public DefaultTableModel setInstructorTableModel() {
        return model.getInstructorTableModel();
    }

    public DefaultTableModel setStudentTableModel() {
        return model.getStudentTableModel();
    }
}
