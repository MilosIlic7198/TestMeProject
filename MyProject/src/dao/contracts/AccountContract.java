/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.contracts;

import dao.dto.Account;

/**
 *
 * @author Milos
 */
public interface AccountContract {

    boolean checkAccount(String usernameEmail, String password);

    Account getAccount(String usernameEmail);
}
