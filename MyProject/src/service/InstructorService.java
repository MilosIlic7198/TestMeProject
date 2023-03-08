/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import application.Main;
import facade.InstructorFacade;
import gui.InstructorGUI;
import java.awt.CardLayout;
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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Milos
 */
public class InstructorService {

    private InstructorGUI instructorGUI;
    private InstructorFacade instructorFacade;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private final CardLayout cardLayout;

    public InstructorService(InstructorGUI instructorGUI, Socket socket) {
        this.instructorGUI = instructorGUI;
        this.instructorFacade = new InstructorFacade();
        try {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        this.cardLayout = (CardLayout) (instructorGUI.getMain_Panel_().getLayout());
        testListModel();
        takenByListModel();
        studentTableModel();
        submittedTableModel();
        instructorGUI.setVisible(true);
    }

    public void initInstructor() {
        instructorGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave?\nYou will be logged out!", "Check!", JOptionPane.YES_NO_OPTION);
                if (choice == 1 || choice == -1) {
                    instructorGUI.setDefaultCloseOperation(InstructorGUI.DO_NOTHING_ON_CLOSE);
                    return;
                }
                CurrentAccount.getInstance(null).logoutAccount();
                close(socket, in, out);
                Main.go();
                instructorGUI.dispose();
                instructorGUI.setDefaultCloseOperation(InstructorGUI.DO_NOTHING_ON_CLOSE);
            }
        });
        instructorGUI.getCreate_Test_Button_().addActionListener(e -> createTest());
        instructorGUI.getAdd_Question_Button_().addActionListener(e -> addQuestion());
        instructorGUI.getAdd_Task_Button_().addActionListener(e -> addTask());
        instructorGUI.getFinish_Test_Button_().addActionListener(e -> finishTest());
        instructorGUI.getBack_To_Instructor_Button_1_().addActionListener(e -> backToInstructor1());
        instructorGUI.getLook_Up_Submitted_Test_Button_().addActionListener(e -> lookUpSubmittedTest());
        instructorGUI.getBack_To_Instructor_Button_2_().addActionListener(e -> backToInstructor2());
        instructorGUI.getGrade_Submitted_Test__Button_().addActionListener(e -> gradeSubmittedTest());
    }

    private void createTest() {
        instructorFacade.createTestCheck();
        instructorGUI.getQuestion_Number_Label_().setText("0");
        instructorGUI.getTask_Number_Label_().setText("0");
        cardLayout.show(instructorGUI.getMain_Panel_(), "create_Test_Card_");
    }

    private void addQuestion() {
        String question = instructorGUI.getQuestion_Field_().getText();
        String option1 = instructorGUI.getOption1_Field_().getText();
        String option2 = instructorGUI.getOption2_Field_().getText();
        String option3 = instructorGUI.getOption3_Field_().getText();
        String option4 = instructorGUI.getOption4_Field_().getText();
        String correctAnswer = instructorGUI.getCorrect_Answer_ComboBox_().getSelectedItem().toString();
        int i = instructorFacade.addQuestionCheck(question, option1, option2, option3, option4, correctAnswer);
        if (i != -1) {
            instructorGUI.getQuestion_Field_().setText(null);
            instructorGUI.getOption1_Field_().setText(null);
            instructorGUI.getOption2_Field_().setText(null);
            instructorGUI.getOption3_Field_().setText(null);
            instructorGUI.getOption4_Field_().setText(null);
            instructorGUI.getCorrect_Answer_ComboBox_().setSelectedIndex(0);
            instructorGUI.getQuestion_Number_Label_().setText("" + i);
            JOptionPane.showMessageDialog(null, "Question was added to the test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addTask() {
        String taskDescription = instructorGUI.getTask_Description_Field_().getText();
        String code = instructorGUI.getCode_TextArea_().getText();
        int i = instructorFacade.addTaskCheck(taskDescription, code);
        if (i != -1) {
            instructorGUI.getTask_Description_Field_().setText(null);
            instructorGUI.getCode_TextArea_().setText(null);
            instructorGUI.getTask_Number_Label_().setText("" + i);
            JOptionPane.showMessageDialog(null, "Task was added to the test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void finishTest() {
        String testName = instructorGUI.getTest_Name_Field_().getText();
        String testDescription = instructorGUI.getTest_Description_Field_().getText();
        if (instructorFacade.finishTestCheck(testName, testDescription)) {
            JOptionPane.showMessageDialog(null, "Test created!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            instructorGUI.getTest_Name_Field_().setText(null);
            instructorGUI.getTest_Description_Field_().setText(null);
            instructorGUI.getQuestion_Number_Label_().setText("0");
            instructorGUI.getTask_Number_Label_().setText("0");
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private void backToInstructor1() {
        if (instructorFacade.discardCheck()) {
            instructorGUI.getTest_Name_Field_().setText(null);
            instructorGUI.getTest_Description_Field_().setText(null);
            instructorGUI.getQuestion_Field_().setText(null);
            instructorGUI.getOption1_Field_().setText(null);
            instructorGUI.getOption2_Field_().setText(null);
            instructorGUI.getOption3_Field_().setText(null);
            instructorGUI.getOption4_Field_().setText(null);
            instructorGUI.getCorrect_Answer_ComboBox_().setSelectedIndex(0);
            instructorGUI.getTask_Description_Field_().setText(null);
            instructorGUI.getCode_TextArea_().setText(null);
            cardLayout.show(instructorGUI.getMain_Panel_(), "instructor_Card_");
        }
    }

    private void lookUpSubmittedTest() {
        if (instructorFacade.lookUpCheck(
                instructorGUI.getSubmitted_Test_Table_(),
                instructorGUI.getSubmitted_Question_Panel_(),
                instructorGUI.getSubmitted_Task_Panel_())) {
            cardLayout.show(instructorGUI.getMain_Panel_(), "look_Up_Submitted_Test_Card_");
        }
    }

    private void backToInstructor2() {
        instructorGUI.getSubmitted_Question_Panel_().removeAll();
        instructorGUI.getSubmitted_Task_Panel_().removeAll();
        cardLayout.show(instructorGUI.getMain_Panel_(), "instructor_Card_");
    }

    private void gradeSubmittedTest() {
        if (!instructorFacade.gradeSubmittedTestCheck(instructorGUI.getSubmitted_Test_Table_())) {
            submittedTableModel();
            return;
        }
        JOptionPane.showMessageDialog(null, "You have successfully graded submitted test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
        try {
            out.writeUTF("Change in database!");
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private void testListModel() {
        new SwingWorker<DefaultListModel, DefaultListModel>() {
            @Override
            protected DefaultListModel doInBackground() throws Exception {
                return instructorFacade.getTestListModel();
            }

            @Override
            protected void done() {
                try {
                    DefaultListModel model = get();
                    instructorGUI.getTest_List_().setModel(model);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }.execute();
    }

    private void takenByListModel() {
        new SwingWorker<DefaultListModel, DefaultListModel>() {
            @Override
            protected DefaultListModel doInBackground() throws Exception {
                return instructorFacade.getTakenByListModel();
            }

            @Override
            protected void done() {
                try {
                    DefaultListModel model = get();
                    instructorGUI.getTaken_By_List_().setModel(model);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }.execute();
    }

    private void studentTableModel() {
        new SwingWorker<DefaultTableModel, DefaultTableModel>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                return instructorFacade.getStudentTableModel();
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel model = get();
                    instructorGUI.getStudent_Table_().setModel(model);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }.execute();
    }

    private void submittedTableModel() {
        new SwingWorker<DefaultTableModel, DefaultTableModel>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                return instructorFacade.getSubmittedTableModel();
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel model = get();
                    instructorGUI.getSubmitted_Test_Table_().setModel(model);
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
                                testListModel();
                                takenByListModel();
                                studentTableModel();
                                submittedTableModel();
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
