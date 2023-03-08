/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import dao.AdminAccess;
import dao.contracts.AdminContract;
import dao.dto.Account;
import dao.dto.AccountBuilder;
import dao.dto.Director;
import encryptor.Encryptor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import service.CurrentAccount;
import validator.EmailValidation;
import validator.IsParsableValidation;

/**
 *
 * @author Milos
 */
public class AdminControlFacade {

    private Account admin;
    private AdminContract adminDAO;
    private Director director;
    private AccountBuilder accountBuilder;
    private Account newAccount;
    private Encryptor encryptor;
    private EmailValidation emailValidation;
    private IsParsableValidation parsable;

    public AdminControlFacade() {
        this.admin = CurrentAccount.getInstance(null).getAccount();
        this.adminDAO = new AdminAccess();
        this.director = new Director();
        this.encryptor = new Encryptor();
        this.emailValidation = new EmailValidation();
        this.parsable = new IsParsableValidation();
    }

    public boolean editAdminAccount(int id, String username, String password, String firstName, String lastName, String email, String phoneNumber) {
        if (username.length() < 6 || username.length() > 20) {
            JOptionPane.showMessageDialog(null, "Username minimum size can be 6 and maximum 20 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (checkUser(username)) {
            JOptionPane.showMessageDialog(null, "Account with this username already exists!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (password.length() < 6 || password.length() > 25) {
            JOptionPane.showMessageDialog(null, "Password minimum size can be 6 and maximum 25 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (!emailValidation.checkEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (!parsable.isStringParsable(phoneNumber) || phoneNumber.length() != 10) {
            JOptionPane.showMessageDialog(null, "Phone number must have 10 numbers!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        String pwd = encryptor.getEncryptedString(password);
        accountBuilder = new AccountBuilder();
        director.buildAdminAccount(accountBuilder);
        accountBuilder.accountId(id)
                .usernameEmail(username)
                .password(pwd)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber);
        newAccount = accountBuilder.build();
        return edit(newAccount);
    }

    public boolean deleteAdminAccount(int id) {
        String[] delete = {"SOFT", "FORCE"};
        JComboBox cb = new JComboBox(delete);
        String choice;
        int option = JOptionPane.showConfirmDialog(null, cb, "Select delete option!", JOptionPane.DEFAULT_OPTION);
        if (option == 0) {
            choice = cb.getSelectedItem().toString();
        } else {
            return false;
        }
        return deleteAdminAcc(id, choice);
    }

    public boolean editInstructorAccount(int id, String email, String password, String firstName, String lastName, String initials, String phoneNumber) {
        if (!emailValidation.checkEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (password.length() < 6 || password.length() > 25) {
            JOptionPane.showMessageDialog(null, "Password minimum size can be 6 and maximum 25 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (initials.length() > 3
                || initials.equals(initials.toLowerCase())) {
            JOptionPane.showMessageDialog(null, "Initials must be in uppercase and max 3 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (!parsable.isStringParsable(phoneNumber) || phoneNumber.length() != 10) {
            JOptionPane.showMessageDialog(null, "Phone number must have 10 numbers!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        String pwd = encryptor.getEncryptedString(password);
        accountBuilder = new AccountBuilder();
        director.buildInstructorAccount(accountBuilder);
        accountBuilder.accountId(id)
                .usernameEmail(email)
                .password(pwd)
                .firstName(firstName)
                .lastName(lastName)
                .initials(initials)
                .phoneNumber(phoneNumber);
        newAccount = accountBuilder.build();
        return edit(newAccount);
    }

    public boolean deleteInstructorAccount(int id) {
        return deleteInstructorAcc(id);
    }

    public boolean editStudentAccount(int id, String email, String password, String firstName, String lastName, String city, String street, String postalCode, String instructorId) {
        if (!emailValidation.checkEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (password.length() < 6 || password.length() > 25) {
            JOptionPane.showMessageDialog(null, "Password minimum size can be 6 and maximum 25 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (!parsable.isStringParsable(postalCode) || postalCode.length() != 5) {
            JOptionPane.showMessageDialog(null, "Postal code must be 5 digit number!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        int newPostalCode = Integer.parseInt(postalCode);
        if (!parsable.isStringParsable(instructorId)) {
            JOptionPane.showMessageDialog(null, "Instructor id must be a number!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        int newInstructorId = Integer.parseInt(instructorId);
        if (!checkIdOfInstructor(newInstructorId)) {
            JOptionPane.showMessageDialog(null, "There is no instructor with this id!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        String pwd = encryptor.getEncryptedString(password);
        accountBuilder = new AccountBuilder();
        director.buildStudentAccount(accountBuilder);
        accountBuilder.accountId(id)
                .usernameEmail(email)
                .password(pwd)
                .firstName(firstName)
                .lastName(lastName)
                .city(city)
                .street(street)
                .postalCode(newPostalCode)
                .instructorId(newInstructorId);
        newAccount = accountBuilder.build();
        return edit(newAccount);
    }

    public boolean deleteStudentAccount(int id) {
        return deleteStudentAcc(id);
    }

    public boolean godAdminEdit(int adminId, String newId) {
        if (!parsable.isStringParsable(newId)) {
            JOptionPane.showMessageDialog(null, "Admin id must be a number!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        int newAdminId = Integer.parseInt(newId);
        if (!checkIdOfAdmin(newAdminId)) {
            JOptionPane.showMessageDialog(null, "There is no admin with this id!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return godEdit(adminId, newAdminId);
    }

    public boolean godAdminDelete(int adminId) {
        String[] delete = {"SOFT", "FORCE"};
        JComboBox cb = new JComboBox(delete);
        String choice;
        int option = JOptionPane.showConfirmDialog(null, cb, "Select delete option!", JOptionPane.DEFAULT_OPTION);
        if (option == 0) {
            choice = cb.getSelectedItem().toString();
        } else {
            return false;
        }
        return deleteAdminAcc(adminId, choice);
    }

    private boolean edit(Account acc) {
        return adminDAO.editAccount(acc);
    }

    private boolean godEdit(int adminId, int newAdminId) {
        return adminDAO.godAdminEditAdmin(adminId, newAdminId);
    }

    private boolean deleteAdminAcc(int IdOfAdminToDelete, String choice) {
        return adminDAO.deleteAdmin(admin.getAccountId(), IdOfAdminToDelete, choice);
    }

    private boolean deleteInstructorAcc(int IdOfInstructorToDelete) {
        return adminDAO.deleteInstructor(IdOfInstructorToDelete);
    }

    private boolean deleteStudentAcc(int IdOfStudentToDelete) {
        return adminDAO.deleteStudent(IdOfStudentToDelete);
    }

    private boolean checkUser(String username) {
        return adminDAO.checkUsername(username);
    }

    private boolean checkIdOfInstructor(int idOfInstructor) {
        return adminDAO.checkForInstructorID(idOfInstructor);
    }

    private boolean checkIdOfAdmin(int idOfAdmin) {
        return adminDAO.checkForAdminID(idOfAdmin);
    }
}
