/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.dto;

/**
 *
 * @author Milos
 */
public class Account {

    private int accountId;
    private String usernameEmail;
    private String password;
    private char accountType;

    public Account(int accountId, String usernameEmail, String password, char accountType) {
        this.accountId = accountId;
        this.usernameEmail = usernameEmail;
        this.password = password;
        this.accountType = accountType;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getUsernameEmail() {
        return usernameEmail;
    }

    public String getPassword() {
        return password;
    }

    public char getAccountType() {
        return accountType;
    }
}
