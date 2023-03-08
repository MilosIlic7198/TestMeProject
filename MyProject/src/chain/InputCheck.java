/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chain;

import javax.swing.JOptionPane;

/**
 *
 * @author Milos
 */
public class InputCheck extends LoginHandler {

    @Override
    public boolean handle(String usernameEmail, String password) {
        if (usernameEmail.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username / Email field is empty!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password field is empty!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return nextHandler(usernameEmail, password);
    }
}
