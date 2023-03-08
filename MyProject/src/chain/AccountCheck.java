/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chain;

import dao.AccountAccess;
import dao.contracts.AccountContract;
import encryptor.Encryptor;
import javax.swing.JOptionPane;

/**
 *
 * @author Milos
 */
public class AccountCheck extends LoginHandler {

    private final AccountContract acc = new AccountAccess();
    private final Encryptor encryptor = new Encryptor();

    @Override
    public boolean handle(String usernameEmail, String password) {
        String pwd = encryptor.getEncryptedString(password);
        if (!acc.checkAccount(usernameEmail, pwd)) {
            JOptionPane.showMessageDialog(null, "Incorrect login details!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return nextHandler(usernameEmail, password);
    }
}
