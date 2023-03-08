/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import application.Main;
import facade.StudentFacade;
import gui.StudentGUI;
import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author Milos
 */
public class StudentService {
    
    private StudentGUI studentGUI;
    private StudentFacade studentFacade;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private final CardLayout cardLayout;
    
    public StudentService(StudentGUI studentGUI, Socket socket) {
        this.studentGUI = studentGUI;
        this.studentFacade = new StudentFacade();
        try {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        cardLayout = (CardLayout) (studentGUI.getMain_Panel_().getLayout());
        refreshTestList();
        refreshSubmittedTestTable();
        refreshAverageScore();
        studentGUI.setVisible(true);
    }
    
    public void initStudent() {
        studentGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (studentGUI.getMain_Panel_().getComponent(1).isVisible()) {
                    JOptionPane.showMessageDialog(null, "You can't leave now!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                    studentGUI.setDefaultCloseOperation(StudentGUI.DO_NOTHING_ON_CLOSE);
                    return;
                }
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave?\nYou will be logged out!", "Check!", JOptionPane.YES_NO_OPTION);
                if (choice == 1 || choice == -1) {
                    studentGUI.setDefaultCloseOperation(StudentGUI.DO_NOTHING_ON_CLOSE);
                    return;
                }
                CurrentAccount.getInstance(null).logoutAccount();
                close(socket, in, out);
                Main.go();
                studentGUI.dispose();
                studentGUI.setDefaultCloseOperation(StudentGUI.DO_NOTHING_ON_CLOSE);
            }
        });
        studentGUI.getTest_List_().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                testList();
            }
        });
        studentGUI.getTake_Button_().addActionListener(e -> takeTest());
        studentGUI.getSubmit_Button_().addActionListener(e -> submitTest());
    }
    
    private void testList() {
        int index = studentGUI.getTest_List_().getSelectedIndex();
        String des = studentFacade.testListCheck(index);
        if (des != null) {
            studentGUI.getTest_Description_Label_().setText(des);
        }
    }
    
    private void takeTest() {
        int index = studentGUI.getTest_List_().getSelectedIndex();
        if (studentFacade.takeTestCheck(index, studentGUI.getQuestion_Panel_(), studentGUI.getTask_Panel_())) {
            studentGUI.getTest_Description_Label_().setText(null);
            cardLayout.show(studentGUI.getMain_Panel_(), "work_On_The_Test_Card_");
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }
    
    private void submitTest() {
        if (studentFacade.submitTestCheck(studentGUI.getQuestion_Panel_(), studentGUI.getTask_Panel_())) {
            cardLayout.show(studentGUI.getMain_Panel_(), "student_Card_");
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }
    
    private void refreshTestList() {
        new SwingWorker<DefaultListModel, DefaultListModel>() {
            @Override
            protected DefaultListModel doInBackground() throws Exception {
                return studentFacade.testListModel();
            }
            
            @Override
            protected void done() {
                try {
                    DefaultListModel model = get();
                    studentGUI.getTest_List_().setModel(model);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }.execute();
    }
    
    private void refreshSubmittedTestTable() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                studentFacade.submittedTestTableModel(studentGUI.getSubmitted_Test_Table_().getModel());
                return null;
            }
        }.execute();
    }
    
    private void refreshAverageScore() {
        new SwingWorker<String, String>() {
            @Override
            protected String doInBackground() throws Exception {
                return studentFacade.setAverageScore();
            }
            
            @Override
            protected void done() {
                try {
                    String avg = get();
                    studentGUI.getStudent_Average_Score_Label_().setText(avg);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }.execute();
    }
    
    public void listenForChanges() {
        new Thread(
                () -> {
                    String change;
                    while (socket.isConnected()) {
                        try {
                            change = in.readUTF();
                            if (change.equals("Change in database!")) {
                                refreshTestList();
                                refreshSubmittedTestTable();
                                refreshAverageScore();
                            }
                        } catch (IOException ex) {
                            close(socket, in, out);
                        }
                    }
                }, "listenThread"
        ).start();
    }
    
    private void close(Socket socket, DataInputStream in, DataOutputStream out) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
