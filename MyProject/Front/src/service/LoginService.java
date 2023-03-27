/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import gui.AdminGUI;
import gui.InstructorGUI;
import gui.LoginGUI;
import gui.StudentGUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Milos
 */
public class LoginService implements Observer {

    private LoginGUI loginGUI;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public LoginService(LoginGUI loginGUI) {
        this.loginGUI = loginGUI;
        try {
            this.socket = new Socket("localhost", 7007);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("An error occurred while constructing login service!");
            ex.printStackTrace();
        }
        loginGUI.setVisible(true);
    }

    @Override
    public void performAction(String usernameEmail, String password) {
        login(usernameEmail, password);
    }

    private void login(String usernameEmail, String password) {
        try {
            out.writeObject("Login!");
            out.writeObject(usernameEmail);
            out.writeObject(password);
            int check = (int) in.readObject();
            switch (check) {
                case 1 -> {
                    char type = (char) in.readObject();
                    switch (type) {
                        case 'A' -> {
                            int id = (int) in.readObject();
                            AdminGUI adminGUI = new AdminGUI();
                            if (id != 1) {
                                adminGUI.getControl_Tabbed_().setEnabledAt(3, false);
                            }
                            AdminService adminService = new AdminService(adminGUI, socket, out, in);
                            adminService.initAdmin();
                            adminService.listen();
                            loginGUI.dispose();
                        }
                        case 'I' -> {
                            InstructorGUI instructorGUI = new InstructorGUI();
                            InstructorService instructorService = new InstructorService(instructorGUI, socket, out, in);
                            instructorService.initInstructor();
                            instructorService.listen();
                            loginGUI.dispose();
                        }
                        case 'S' -> {
                            StudentGUI studentGUI = new StudentGUI();
                            StudentService studentService = new StudentService(studentGUI, socket, out, in);
                            studentService.initStudent();
                            studentService.listen();
                            loginGUI.dispose();
                        }
                    }
                    JOptionPane.showMessageDialog(null, "You have successfully logged in!", "Login successful!", JOptionPane.INFORMATION_MESSAGE);
                }
                case -1 -> {
                    JOptionPane.showMessageDialog(null, "Fields are empty!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                }
                case -2 -> {
                    JOptionPane.showMessageDialog(null, "Incorrect login details!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("An error occurred while logging in!");
            ex.printStackTrace();
        }
    }
}
