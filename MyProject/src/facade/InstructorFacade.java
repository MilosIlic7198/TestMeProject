/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import dao.InstructorAccess;
import dao.contracts.InstructorContract;
import dao.dto.Account;
import dao.dto.Question;
import dao.dto.SubmittedTest;
import dao.dto.Task;
import dao.dto.Test;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import service.CurrentAccount;
import validator.IsParsableValidation;

/**
 *
 * @author Milos
 */
public class InstructorFacade {

    private Account instructor;
    private InstructorContract instructorDAO;
    private InstructorModelFacade model;
    private InstructorTestFacade compose;
    private IsParsableValidation parsable;
    private Test test;
    private SubmittedTest submittedTest;

    public InstructorFacade() {
        this.instructor = CurrentAccount.getInstance(null).getAccount();
        this.instructorDAO = new InstructorAccess();
        this.model = new InstructorModelFacade();
        this.compose = new InstructorTestFacade();
        this.parsable = new IsParsableValidation();
    }

    public void createTestCheck() {
        if (test == null) {
            test = new Test();
            test.setQuestionList(new ArrayList<>());
            test.setTaskList(new ArrayList<>());
        }
    }

    public int addQuestionCheck(String question, String option1, String option2, String option3, String option4, String correctAnswer) {
        //TODO: Add validation!
        if (question.trim().isEmpty()
                || option1.trim().isEmpty()
                || option2.trim().isEmpty()
                || option3.trim().isEmpty()
                || option4.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return -1;
        }
        Question q = new Question(question, option1, option2, option3, option4, correctAnswer);
        test.getQuestionList().add(q);
        return test.getQuestionList().size();
    }

    public int addTaskCheck(String taskDescription, String code) {
        //TODO: Add validation!
        if (taskDescription.trim().isEmpty() || code.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return -1;
        }
        Task t = new Task(taskDescription, code);
        test.getTaskList().add(t);
        return test.getTaskList().size();
    }

    public boolean finishTestCheck(String testName, String testDescription) {
        //TODO: Add validation!
        if (testName.trim().isEmpty() || testName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (test.getQuestionList().isEmpty() || test.getTaskList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Make sure you have at least one question and one task!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        test.setInstructorId(instructor.getAccountId());
        test.setTestName(testName);
        test.setTestDescription(testDescription);
        if (createTest(test)) {
            test.getQuestionList().removeAll(test.getQuestionList());
            test.getTaskList().removeAll(test.getTaskList());
            return true;
        }
        JOptionPane.showMessageDialog(null, "Something went wrong while creating test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
        return false;
    }

    private boolean createTest(Test test) {
        return instructorDAO.addTest(test);
    }

    public boolean discardCheck() {
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

    public boolean lookUpCheck(JTable submitted_Test_Table_, JPanel submitted_Question_Panel_, JPanel submitted_Task_Panel_) {
        if (submitted_Test_Table_.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected submitted test first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        int index = submitted_Test_Table_.getSelectedRow();
        if (populateSubmittedTest(model.getTestId(index))) {
            callComposeSubmittedTest(submittedTest, submitted_Question_Panel_, submitted_Task_Panel_);
            return true;
        }
        return false;
    }

    private boolean populateSubmittedTest(int submittedTestId) {
        submittedTest = instructorDAO.getSubmittedTest(submittedTestId);
        return submittedTest != null;
    }

    private void callComposeSubmittedTest(SubmittedTest submittedTest, JPanel submitted_Question_Panel_, JPanel submitted_Task_Panel_) {
        compose.composeSubmittedTest(submittedTest, submitted_Question_Panel_, submitted_Task_Panel_);
    }

    public boolean gradeSubmittedTestCheck(JTable submitted_Test_Table_) {
        if (submitted_Test_Table_.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected submitted test first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        int index = submitted_Test_Table_.getSelectedRow();
        if (!parsable.isStringParsable(submitted_Test_Table_.getModel().getValueAt(index, 5).toString())) {
            JOptionPane.showMessageDialog(null, "Change column score to a wanted number!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        int score = Integer.parseInt(submitted_Test_Table_.getModel().getValueAt(index, 5).toString());
        int studentId = Integer.parseInt(submitted_Test_Table_.getModel().getValueAt(index, 3).toString());
        return grade(score, model.getTestId(index), studentId);
    }

    private boolean grade(int score, int testId, int studentId) {
        return instructorDAO.gradeSubmittedTest(score, testId, studentId);
    }

    public DefaultListModel getTestListModel() {
        return model.setTestListModel();
    }

    public DefaultListModel getTakenByListModel() {
        return model.setTakenByListModel();
    }

    public DefaultTableModel getStudentTableModel() {
        return model.setStudentTableModel();
    }

    public DefaultTableModel getSubmittedTableModel() {
        return model.setSubmittedTableModel();
    }
}
