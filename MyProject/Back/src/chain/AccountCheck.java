/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chain;

import dao.AccountAccess;
import dao.contracts.AccountContract;

/**
 *
 * @author Milos
 */
public class AccountCheck extends LoginHandler {

    private AccountContract accountDAO = new AccountAccess();

    @Override
    public int handle(String usernameEmail, String password) {
        if (!accountDAO.checkAccount(usernameEmail, password)) {
            return -2;
        }
        return nextHandler(usernameEmail, password);
    }
}
