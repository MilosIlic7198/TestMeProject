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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import service.CurrentAccount;
import validator.EmailValidation;
import validator.IsParsableValidation;

/**
 *
 * @author Milos
 */
public class AdminRegisterFacade {

    private Account admin;
    private AdminContract adminDAO;
    private Director director;
    private AccountBuilder accountBuilder;
    private Account newAccount;
    private Encryptor encryptor;
    private EmailValidation emailValidation;
    private IsParsableValidation parsable;

    public AdminRegisterFacade() {
        this.admin = CurrentAccount.getInstance(null).getAccount();
        this.adminDAO = new AdminAccess();
        this.director = new Director();
        this.encryptor = new Encryptor();
        this.emailValidation = new EmailValidation();
        this.parsable = new IsParsableValidation();
    }

    public boolean addAdminCheck(String username, String password, String firstName, String lastName, String email, String phoneNumber) {
        if (!emailValidation.checkEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
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
        String pwd = encryptor.getEncryptedString(password);
        accountBuilder = new AccountBuilder();
        director.buildAdminAccount(accountBuilder);
        accountBuilder.usernameEmail(username)
                .password(pwd)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .godId(admin.getAccountId());
        newAccount = accountBuilder.build();
        return add(newAccount);
    }

    public boolean addInstructorCheck(String email, String password,
            String firstName,
            String lastName,
            String initials,
            String phoneNumber,
            boolean male,
            boolean female,
            Date birthdate) {
        if (initials.length() > 3
                || initials.equals(initials.toLowerCase())) {
            JOptionPane.showMessageDialog(null, "Initials must be in uppercase and max 3 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (!emailValidation.checkEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (password.length() < 6 || password.length() > 25) {
            JOptionPane.showMessageDialog(null, "Password minimum size can be 6 and maximum 25 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        String pwd = encryptor.getEncryptedString(password);
        char gender;
        if (male) {
            gender = 'M';
        } else {
            gender = 'F';
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(birthdate);
        accountBuilder = new AccountBuilder();
        director.buildInstructorAccount(accountBuilder);
        accountBuilder.usernameEmail(email)
                .password(pwd)
                .firstName(firstName)
                .lastName(lastName)
                .initials(initials)
                .birthDate(date)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .adminId(admin.getAccountId());
        newAccount = accountBuilder.build();
        return add(newAccount);
    }

    public boolean addStudentCheck(String email, String password,
            String firstName,
            String lastName,
            String city,
            String street,
            String postalCode,
            boolean male,
            boolean female,
            Date birthdate,
            int id) {
        if (!parsable.isStringParsable(postalCode) || postalCode.length() != 5) {
            JOptionPane.showMessageDialog(null, "Postal code can only be 5 digit numbers!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (!emailValidation.checkEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (password.length() < 6 || password.length() > 25) {
            JOptionPane.showMessageDialog(null, "Password minimum size can be 6 and maximum 25 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        String pwd = encryptor.getEncryptedString(password);
        char gender;
        if (male) {
            gender = 'M';
        } else {
            gender = 'F';
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(birthdate);
        accountBuilder = new AccountBuilder();
        director.buildStudentAccount(accountBuilder);
        accountBuilder.usernameEmail(email)
                .password(pwd)
                .firstName(firstName)
                .lastName(lastName)
                .city(city)
                .street(street)
                .postalCode(Integer.parseInt(postalCode))
                .birthDate(date)
                .gender(gender)
                .instructorId(id)
                .adminId(admin.getAccountId());
        newAccount = accountBuilder.build();
        return add(newAccount);
    }

    private boolean add(Account acc) {
        return adminDAO.addAccount(acc);
    }

    private boolean checkUser(String username) {
        return adminDAO.checkUsername(username);
    }
}
