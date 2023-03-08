/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import chain.LoginHandler;
import dao.AccountAccess;
import dao.contracts.AccountContract;
import dao.dto.Account;
import gui.AdminGUI;
import gui.InstructorGUI;
import gui.LoginGUI;
import gui.StudentGUI;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Milos
 */
public class LoginService implements Observer {

    private LoginGUI loginGUI;
    private LoginHandler loginHandler;
    private final AccountContract acc;

    public LoginService(LoginGUI loginGUI, LoginHandler loginHandler) {
        this.loginGUI = loginGUI;
        this.loginHandler = loginHandler;
        this.acc = new AccountAccess();
        loginGUI.setVisible(true);
    }

    @Override
    public void performAction(String usernameEmail, String password) {
        login(usernameEmail, password);
    }

    private void login(String usernameEmail, String password) {
        if (loginHandler.handle(usernameEmail, password)) {
            Account account = acc.getAccount(usernameEmail);
            try {
                char type = CurrentAccount.getInstance(account).getAccount().getAccountType();
                Socket socket = new Socket("localhost", 7007);
                switch (type) {
                    case 'A' -> {
                        AdminGUI adminGUI = new AdminGUI();
                        if (account.getAccountId() != 1) {
                            adminGUI.getControl_Tabbed_().setEnabledAt(3, false);
                        }
                        AdminService adminService = new AdminService(adminGUI, socket);
                        adminService.initAdmin();
                        adminService.listenForChanges();
                        loginGUI.dispose();
                    }
                    case 'I' -> {
                        InstructorGUI instructorGUI = new InstructorGUI();
                        InstructorService instructorService = new InstructorService(instructorGUI, socket);
                        instructorService.initInstructor();
                        instructorService.listenForChanges();
                        loginGUI.dispose();
                    }
                    case 'S' -> {
                        StudentGUI studentGUI = new StudentGUI();
                        StudentService studentService = new StudentService(studentGUI, socket);
                        studentService.initStudent();
                        studentService.listenForChanges();
                        loginGUI.dispose();
                    }
                }
                JOptionPane.showMessageDialog(null, "You have successfully logged in!", "Login successful!", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }
}
