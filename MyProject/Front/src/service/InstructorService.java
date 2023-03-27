/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import application.Main;
import dto.Account;
import dto.Question;
import dto.SubmittedTest;
import dto.TakenBy;
import dto.Task;
import dto.Test;
import facade.InstructorTestFacade;
import gui.InstructorGUI;
import java.awt.CardLayout;
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
import validator.IsParsableValidation;

/**
 *
 * @author Milos
 */
public class InstructorService {

    private InstructorGUI instructorGUI;
    private InstructorTestFacade instructorTestFacade;
    private IsParsableValidation parsable;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ArrayList<Test> tList;
    private ArrayList<SubmittedTest> sTList;
    private Test test;
    private SubmittedTest submittedTest;
    private final CardLayout cardLayout;

    public InstructorService(InstructorGUI instructorGUI, Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        this.instructorGUI = instructorGUI;
        this.instructorTestFacade = new InstructorTestFacade();
        this.parsable = new IsParsableValidation();
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.cardLayout = (CardLayout) (instructorGUI.getMain_Panel_().getLayout());
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
        instructorGUI.getDelete_Test_Button_().addActionListener(e -> deleteTest());
        instructorGUI.getBack_To_Instructor_Button_1_().addActionListener(e -> backToInstructor1());
        instructorGUI.getLook_Up_Submitted_Test_Button_().addActionListener(e -> lookUpSubmittedTest());
        instructorGUI.getBack_To_Instructor_Button_2_().addActionListener(e -> backToInstructor2());
        instructorGUI.getGrade_Submitted_Test__Button_().addActionListener(e -> gradeSubmittedTest());
    }

    private void createTest() {
        if (test == null) {
            test = new Test();
            test.setQuestionList(new ArrayList<>());
            test.setTaskList(new ArrayList<>());
        }
        instructorGUI.getQuestion_Number_Label_().setText("0");
        instructorGUI.getTask_Number_Label_().setText("0");
        cardLayout.show(instructorGUI.getMain_Panel_(), "create_Test_Card_");
    }

    private void addQuestion() {
        String correctAnswer = instructorGUI.getCorrect_Answer_ComboBox_().getSelectedItem().toString();
        if (instructorGUI.getQuestion_Field_().getText().trim().isEmpty()
                || instructorGUI.getOption1_Field_().getText().trim().isEmpty()
                || instructorGUI.getOption2_Field_().getText().trim().isEmpty()
                || instructorGUI.getOption3_Field_().getText().trim().isEmpty()
                || instructorGUI.getOption4_Field_().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        switch (correctAnswer) {
            case "Option 1" ->
                correctAnswer = instructorGUI.getOption1_Field_().getText();
            case "Option 2" ->
                correctAnswer = instructorGUI.getOption2_Field_().getText();
            case "Option 3" ->
                correctAnswer = instructorGUI.getOption3_Field_().getText();
            case "Option 4" ->
                correctAnswer = instructorGUI.getOption4_Field_().getText();
        }
        Question q = new Question(instructorGUI.getQuestion_Field_().getText(),
                instructorGUI.getOption1_Field_().getText(),
                instructorGUI.getOption2_Field_().getText(),
                instructorGUI.getOption3_Field_().getText(),
                instructorGUI.getOption4_Field_().getText(),
                correctAnswer);
        test.getQuestionList().add(q);
        instructorGUI.getQuestion_Field_().setText(null);
        instructorGUI.getOption1_Field_().setText(null);
        instructorGUI.getOption2_Field_().setText(null);
        instructorGUI.getOption3_Field_().setText(null);
        instructorGUI.getOption4_Field_().setText(null);
        instructorGUI.getCorrect_Answer_ComboBox_().setSelectedIndex(0);
        instructorGUI.getQuestion_Number_Label_().setText("" + test.getQuestionList().size());
        JOptionPane.showMessageDialog(null, "Question was added to the test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addTask() {
        if (instructorGUI.getTask_Description_Field_().getText().trim().isEmpty() || instructorGUI.getCode_TextArea_().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Task t = new Task(instructorGUI.getTask_Description_Field_().getText(), instructorGUI.getCode_TextArea_().getText());
        test.getTaskList().add(t);
        instructorGUI.getTask_Description_Field_().setText(null);
        instructorGUI.getCode_TextArea_().setText(null);
        instructorGUI.getTask_Number_Label_().setText("" + test.getTaskList().size());
        JOptionPane.showMessageDialog(null, "Task was added to the test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
    }

    private void finishTest() {
        try {
            if (instructorGUI.getTest_Name_Field_().getText().trim().isEmpty() || instructorGUI.getTest_Description_Field_().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (test.getQuestionList().isEmpty() || test.getTaskList().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Make sure you have at least one question and one task!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            test.setTestName(instructorGUI.getTest_Name_Field_().getText());
            test.setTestDescription(instructorGUI.getTest_Description_Field_().getText());
            out.writeObject("Finish and add new test!");
            out.writeObject(test);
        } catch (IOException ex) {
            System.out.println("An error occurred while creating test!");
            ex.printStackTrace();
        }
    }

    private void resetTestFrom(boolean check) {
        try {
            if (check) {
                out.writeObject("Change in database!");
                test.getQuestionList().removeAll(test.getQuestionList());
                test.getTaskList().removeAll(test.getTaskList());
                instructorGUI.getTest_Name_Field_().setText(null);
                instructorGUI.getTest_Description_Field_().setText(null);
                instructorGUI.getQuestion_Number_Label_().setText("0");
                instructorGUI.getTask_Number_Label_().setText("0");
                JOptionPane.showMessageDialog(null, "Test created!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while finishing test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while reseting test from!");
            ex.printStackTrace();
        }
    }

    private void deleteTest() {
        try {
            if (tList.isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are no tests!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int index = instructorGUI.getTest_List_().getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(null, "You must have a select test first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Check!", JOptionPane.YES_NO_OPTION);
            if (choice == 1 || choice == -1) {
                return;
            }
            out.writeObject("Delete instructor test!");
            out.writeObject(tList.get(index).getTestId());
        } catch (IOException ex) {
            System.out.println("An error occurred while trying to delete instructor test!");
            ex.printStackTrace();
        }
    }

    private void finishDeletingTest(boolean check) {
        try {
            if (check) {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "You have successfully deleted this test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "Something went wrong while deleting this test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error has occurred while deleting this test!");
            ex.printStackTrace();
        }
    }

    private void backToInstructor1() {
        if (discardCheck()) {
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
        try {
            if (instructorGUI.getSubmitted_Test_Table_().getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "You must have a selected submitted test first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int index = instructorGUI.getSubmitted_Test_Table_().getSelectedRow();
            out.writeObject("Get submitted test for instructor!");
            out.writeObject(sTList.get(index).getTestId());
        } catch (IOException ex) {
            System.out.println("An error occurred while trying to look up submitted test!");
            ex.printStackTrace();
        }
    }

    private void prepareLookUp() {
        if (submittedTest != null) {
            instructorTestFacade.composeSubmittedTest(submittedTest, instructorGUI.getSubmitted_Question_Panel_(), instructorGUI.getSubmitted_Task_Panel_());
            cardLayout.show(instructorGUI.getMain_Panel_(), "look_Up_Submitted_Test_Card_");
        } else {
            JOptionPane.showMessageDialog(null, "Something went wrong while trying to look up submitted test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void backToInstructor2() {
        instructorGUI.getSubmitted_Question_Panel_().removeAll();
        instructorGUI.getSubmitted_Task_Panel_().removeAll();
        cardLayout.show(instructorGUI.getMain_Panel_(), "instructor_Card_");
    }

    private void gradeSubmittedTest() {
        try {
            if (instructorGUI.getSubmitted_Test_Table_().getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "You must have a selected submitted test first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int index = instructorGUI.getSubmitted_Test_Table_().getSelectedRow();
            if (!parsable.isStringParsable(instructorGUI.getSubmitted_Test_Table_().getModel().getValueAt(index, 5).toString())) {
                JOptionPane.showMessageDialog(null, "Change column score to a wanted number!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                out.writeObject("Change in database!");
                return;
            }
            int score = Integer.parseInt(instructorGUI.getSubmitted_Test_Table_().getModel().getValueAt(index, 5).toString());
            int studentId = Integer.parseInt(instructorGUI.getSubmitted_Test_Table_().getModel().getValueAt(index, 3).toString());
            out.writeObject("Grade submitted test!");
            out.writeObject(score);
            out.writeObject(sTList.get(index).getTestId());
            out.writeObject(studentId);
        } catch (IOException ex) {
            System.out.println("An error occurred while trying to grade submitted test!");
            ex.printStackTrace();
        }
    }

    private void refreshScore(boolean check) {
        try {
            if (check) {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "You have successfully graded submitted test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "Something went wrong while grading submitted test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while grading submitted test!");
            ex.printStackTrace();
        }
    }

    private void testListModel(ArrayList<Test> tList) {
        new SwingWorker<DefaultListModel, Void>() {
            @Override
            protected DefaultListModel doInBackground() throws Exception {
                DefaultListModel testListModel = new DefaultListModel();
                int j = 0;
                for (Test t : tList) {
                    ++j;
                    testListModel.addElement("No: " + j + "  ||  Test ID: " + t.getTestId() + "  ||  Test Name: " + t.getTestName());
                }
                return testListModel;
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

    private void takenByListModel(ArrayList<TakenBy> takenByList) {
        new SwingWorker<DefaultListModel, Void>() {
            @Override
            protected DefaultListModel doInBackground() throws Exception {
                DefaultListModel testListModel = new DefaultListModel();
                int j = 0;
                for (TakenBy tb : takenByList) {
                    ++j;
                    testListModel.addElement(
                            "No: "
                            + j
                            + "  ||  Test ID: "
                            + tb.getTestId()
                            + "  ||  Test Name: "
                            + tb.getTestName()
                            + "  ||  Date Take: "
                            + tb.getDateTaken()
                            + "  ||  Student ID: "
                            + tb.getStudentId()
                            + "  ||  Student Name: "
                            + tb.getStudentName());
                }
                return testListModel;
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

    private void studentTableModel(ArrayList<Account> sList) {
        new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                DefaultTableModel refreshModel = (DefaultTableModel) instructorGUI.getStudent_Table_().getModel();
                refreshModel.setRowCount(0);
                Object[] row = new Object[9];
                int i = 0;
                for (Account s : sList) {
                    row[0] = ++i;
                    row[1] = s.getAccountId();
                    row[2] = s.getUsernameEmail();
                    row[3] = s.getFirstName() + " " + s.getLastName();
                    row[4] = s.getCity();
                    row[5] = s.getStreet();
                    row[6] = s.getPostalCode();
                    row[7] = s.getBirthDate();
                    row[8] = s.getGender();
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
                    System.out.println("An error occurred while refreshing student table!");
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void submittedTableModel(ArrayList<SubmittedTest> sTList) {
        new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                DefaultTableModel refreshModel = (DefaultTableModel) instructorGUI.getSubmitted_Test_Table_().getModel();
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

    public void listen() {
        new Thread(
                () -> {
                    String operation;
                    while (socket.isConnected()) {
                        try {
                            operation = (String) in.readObject();
                            if (operation.equals("Finish and add new test!")) {
                                boolean check = (boolean) in.readObject();
                                resetTestFrom(check);
                            }
                            if (operation.equals("Delete instructor test!")) {
                                boolean check = (boolean) in.readObject();
                                finishDeletingTest(check);
                            }
                            if (operation.equals("Get submitted test for instructor!")) {
                                submittedTest = (SubmittedTest) in.readObject();
                                prepareLookUp();
                            }
                            if (operation.equals("Grade submitted test!")) {
                                boolean check = (boolean) in.readObject();
                                refreshScore(check);
                            }
                            if (operation.equals("Change in database!")) {
                                tList = (ArrayList<Test>) in.readObject();
                                testListModel(tList);
                                ArrayList<Account> sList = (ArrayList<Account>) in.readObject();
                                studentTableModel(sList);
                                sTList = (ArrayList<SubmittedTest>) in.readObject();
                                submittedTableModel(sTList);
                                ArrayList<TakenBy> takenByList = (ArrayList<TakenBy>) in.readObject();
                                takenByListModel(takenByList);
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
            System.out.println("An error occurred while closing socket and streams on a client side!");
            ex.printStackTrace();
        }
    }

    private boolean discardCheck() {
        if (!test.getQuestionList().isEmpty() || !test.getTaskList().isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(null, "The test will be discarded!\nAre you sure you want to leave?", "Check!", JOptionPane.YES_NO_OPTION);
            if (choice == 1 || choice == -1) {
                return false;
            }
        }
        test.setTaskList(null);
        test.setQuestionList(null);
        test = null;
        return true;
    }
}
