/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.dto.Account;

/**
 *
 * @author Milos
 */
public class CurrentAccount {

    private static CurrentAccount instance;
    private Account account;

    private CurrentAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void logoutAccount() {
        this.account = null;
    }

    public static CurrentAccount getInstance(Account account) {
        if (instance == null) {
            instance = new CurrentAccount(account);
        } else if (instance.getAccount() == null) {
            instance = new CurrentAccount(account);
        }
        return instance;
    }
}
