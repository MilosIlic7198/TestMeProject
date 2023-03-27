/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chain;

/**
 *
 * @author Milos
 */
public class InputCheck extends LoginHandler {

    @Override
    public int handle(String usernameEmail, String password) {
        if (usernameEmail.trim().isEmpty()) {
            return -1;
        }
        if (password.trim().isEmpty()) {
            return -1;
        }
        return nextHandler(usernameEmail, password);
    }
}
