/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import javax.swing.JOptionPane;
import validator.EmailValidation;
import validator.IsParsableValidation;

/**
 *
 * @author Milos
 */
public class ValidatorFacade {

    private EmailValidation emailValidation;
    private IsParsableValidation parsable;

    public ValidatorFacade() {
        this.parsable = new IsParsableValidation();
        this.emailValidation = new EmailValidation();
    }

    public boolean adminValidation(String newId) {
        if (!parsable.isStringParsable(newId)) {
            JOptionPane.showMessageDialog(null, "Admin id must be a number!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean adminValidation(String username, String password, String email, String phoneNumber) {
        if (!emailValidation.checkEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (username.length() < 6 || username.length() > 20) {
            JOptionPane.showMessageDialog(null, "Username minimum size can be 6 and maximum 20 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (password.length() < 6 || password.length() > 25) {
            JOptionPane.showMessageDialog(null, "Password minimum size can be 6 and maximum 25 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (!parsable.isStringParsable(phoneNumber) || phoneNumber.length() != 10) {
            JOptionPane.showMessageDialog(null, "Phone number must have 10 numbers!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean instructorValidation(String email, String password, String initials, String phoneNumber) {
        if (initials.length() > 3
                || initials.equals(initials.toLowerCase())) {
            JOptionPane.showMessageDialog(null, "Initials must be in uppercase and max 3 characters long!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (!parsable.isStringParsable(phoneNumber) || phoneNumber.length() != 10) {
            JOptionPane.showMessageDialog(null, "Phone number must have 10 numbers!", "Info!", JOptionPane.INFORMATION_MESSAGE);
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
        return true;
    }

    public boolean studentValidation(String email, String password, String postalCode) {
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
        return true;
    }

    public boolean studentValidation(String email, String password, String postalCode, String instructorId) {
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
        if (!parsable.isStringParsable(instructorId)) {
            JOptionPane.showMessageDialog(null, "Instructor id must be a number!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }
}
