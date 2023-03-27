/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import application.Main;
import dto.SubmittedTest;
import dto.Test;
import facade.StudentTestFacade;
import gui.StudentGUI;
import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Milos
 */
public class StudentService {

    private StudentGUI studentGUI;
    private StudentTestFacade studentTestFacade;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ArrayList<Test> tList;
    private ArrayList<SubmittedTest> sTList;
    private Test test;
    private final CardLayout cardLayout;

    public StudentService(StudentGUI studentGUI, Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        this.studentGUI = studentGUI;
        this.studentTestFacade = new StudentTestFacade();
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.cardLayout = (CardLayout) (studentGUI.getMain_Panel_().getLayout());
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
                close(socket, in, out);
                Main.go();
                studentGUI.dispose();
                studentGUI.setDefaultCloseOperation(StudentGUI.DO_NOTHING_ON_CLOSE);
            }
        });
        studentGUI.getTest_List_().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                testListCheck();
            }
        });
        studentGUI.getTake_Button_().addActionListener(e -> takeTest());
        studentGUI.getSubmit_Button_().addActionListener(e -> submitTest());
    }

    private void testListCheck() {
        if (tList.isEmpty()) {
            return;
        }
        int index = studentGUI.getTest_List_().getSelectedIndex();
        String des = tList.get(index).getTestDescription();
        studentGUI.getTest_Description_Label_().setText("<html><p align=\"center\">" + des + "</p></html>");
    }

    private void takeTest() {
        try {
            int index = studentGUI.getTest_List_().getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(null, "You must have a select test first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Check!", JOptionPane.YES_NO_OPTION);
            if (choice == 1 || choice == -1) {
                return;
            }
            out.writeObject("Student took the test!");
            out.writeObject(tList.get(index).getTestId());
        } catch (IOException ex) {
            System.out.println("An error has occurred while taking test!");
            ex.printStackTrace();
        }
    }

    private void getTest(boolean check) {
        try {
            if (check) {
                int index = studentGUI.getTest_List_().getSelectedIndex();
                out.writeObject("Get test for student!");
                out.writeObject(tList.get(index).getTestId());
                JOptionPane.showMessageDialog(null, "You have successfully taken test!\nGood luck!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while trying to get test!\nUnlucky!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error has occurred while getting test!");
            ex.printStackTrace();
        }
    }

    private void prepareWorkspace() {
        try {
            if (test != null) {
                studentTestFacade.composeTest(test, studentGUI.getQuestion_Panel_(), studentGUI.getTask_Panel_());
                studentGUI.getTest_Description_Label_().setText(null);
                cardLayout.show(studentGUI.getMain_Panel_(), "work_On_The_Test_Card_");
                out.writeObject("Change in database!");
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while trying to prepare workspace!\nUnlucky!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error has occurred while preparing workspace!");
            ex.printStackTrace();
        }
    }

    private void submitTest() {
        try {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Check!", JOptionPane.YES_NO_OPTION);
            if (choice == 1 || choice == -1) {
                return;
            }
            out.writeObject("Student submitted test!");
            out.writeObject(test.getTestId());
            out.writeObject(studentTestFacade.returnStudentQuestions(test, studentGUI.getQuestion_Panel_()));
            out.writeObject(studentTestFacade.returnStudentTasks(test, studentGUI.getTask_Panel_()));
        } catch (IOException ex) {
            System.out.println("An error has occurred while submitting test!");
            ex.printStackTrace();
        }
    }

    private void leaveWorkspace(boolean check) {
        try {
            if (check) {
                JOptionPane.showMessageDialog(null, "You have successfully submited test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                out.writeObject("Change in database!");
                studentGUI.getQuestion_Panel_().removeAll();
                studentGUI.getTask_Panel_().removeAll();
                test = null;
                cardLayout.show(studentGUI.getMain_Panel_(), "student_Card_");
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while trying to leave workspace!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error has occurred while leaving workspace!");
            ex.printStackTrace();
        }
    }

    private void refreshTestList(ArrayList<Test> tList) {
        new SwingWorker<DefaultListModel, DefaultListModel>() {
            @Override
            protected DefaultListModel doInBackground() throws Exception {
                DefaultListModel listModel = new DefaultListModel();
                int j = 0;
                for (Test t : tList) {
                    ++j;
                    listModel.addElement("No: " + j + "  ||  Test Name: " + t.getTestName());
                }
                return listModel;
            }

            @Override
            protected void done() {
                try {
                    DefaultListModel model = get();
                    studentGUI.getTest_List_().setModel(model);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("An error has occurred while refreshing test list!");
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void refreshSubmittedTestTable(ArrayList<SubmittedTest> sTList) {
        new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                DefaultTableModel refreshModel = (DefaultTableModel) studentGUI.getSubmitted_Test_Table_().getModel();
                refreshModel.setRowCount(0);
                Object[] row = new Object[6];
                int i = 0;
                for (SubmittedTest st : sTList) {
                    row[0] = ++i;
                    row[1] = st.getTestId();
                    row[2] = st.getTestDescription();
                    row[3] = st.getStudentId();
                    row[4] = st.getStudentName();
                    if (Integer.parseInt(st.getScore()) == 0) {
                        row[5] = "Not graded yet!";
                    } else {
                        row[5] = st.getScore();
                    }
                    refreshModel.addRow(row);
                }
                return refreshModel;
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel refreshModel = get();
                    refreshModel.fireTableDataChanged();
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("An error occurred while refreshing submitted test table!");
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void refreshAverageScore(Double avg) {
        new SwingWorker<Double, Void>() {
            @Override
            protected Double doInBackground() throws Exception {
                return avg;
            }

            @Override
            protected void done() {
                try {
                    studentGUI.getStudent_Average_Score_Label_().setText("Average score: " + get());
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("An error has occurred while refreshing average score!");
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    public void listen() {
        new Thread(
                () -> {
                    String operation;
                    while (socket.isConnected()) {
                        try {
                            operation = (String) in.readObject();
                            if (operation.equals("Student took the test!")) {
                                boolean check = (boolean) in.readObject();
                                getTest(check);
                            }
                            if (operation.equals("Get test for student!")) {
                                test = (Test) in.readObject();
                                prepareWorkspace();
                            }
                            if (operation.equals("Student submitted test!")) {
                                boolean check = (boolean) in.readObject();
                                leaveWorkspace(check);
                            }
                            if (operation.equals("Change in database!")) {
                                tList = (ArrayList<Test>) in.readObject();
                                refreshTestList(tList);
                                sTList = (ArrayList<SubmittedTest>) in.readObject();
                                refreshSubmittedTestTable(sTList);
                                Double avg = (Double) in.readObject();
                                refreshAverageScore(avg);
                            }
                        } catch (IOException | ClassNotFoundException ex) {
                            close(socket, in, out);
                            break;
                        }
                    }
                }, "listenThread"
        ).start();
    }

    private void close(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
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
            System.out.println("An error has occurred while closing sockets and streams on client side!");
            ex.printStackTrace();
        }
    }
}
