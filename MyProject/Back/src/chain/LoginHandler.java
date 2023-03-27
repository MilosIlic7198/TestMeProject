/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chain;

/**
 *
 * @author Milos
 */
public abstract class LoginHandler {

    private LoginHandler next;

    public LoginHandler setNextHandler(LoginHandler loginHandler) {
        this.next = loginHandler;
        return next;
    }

    public abstract int handle(String usernameEmail, String password);

    protected int nextHandler(String usernameEmail, String password) {
        if (next == null) {
            return 1;
        }
        return next.handle(usernameEmail, password);
    }
}
