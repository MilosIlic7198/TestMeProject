/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application;

import chain.AccountCheck;
import chain.InputCheck;
import chain.LoginHandler;
import gui.LoginGUI;
import service.LoginService;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Milos
 */
public class Main {

    public static void main(String[] args) {
        lookAndFeel();
        go();
    }

    private static void lookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public static void go() {
        LoginHandler inputCheck = new InputCheck();
        LoginHandler accountCheck = new AccountCheck();
        inputCheck.setNextHandler(accountCheck);

        LoginGUI loginGUI = new LoginGUI();
        LoginService loginService = new LoginService(loginGUI, inputCheck);
        loginGUI.setObserver(loginService);
    }
}
