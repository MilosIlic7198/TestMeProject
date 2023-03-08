/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import dao.contracts.StudentContract;
import dao.StudentAccess;
import dao.dto.Account;
import dao.dto.Test;
import dao.dto.SubmittedTest;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import service.CurrentAccount;

/**
 *
 * @author Milos
 */
public class StudentFacade {

    private Account student;
    private StudentContract studentDAO;
    private StudentTestFacade compose;
    private ArrayList<Test> tList;
    private ArrayList<SubmittedTest> sTList;
    private Test test;
    private SubmittedTest sTest;

    public StudentFacade() {
        this.student = CurrentAccount.getInstance(null).getAccount();
        this.studentDAO = new StudentAccess();
        this.compose = new StudentTestFacade();
    }

    public String testListCheck(int index) {
        if (tList.isEmpty()) {
            return null;
        }
        return tList.get(index).getTestDescription();
    }

    public boolean takeTestCheck(int index, JPanel question_Panel_, JPanel task_Panel_) {
        if (index == -1) {
            JOptionPane.showMessageDialog(null, "You must have a select test first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Check!", JOptionPane.YES_NO_OPTION);
        if (choice == 1 || choice == -1) {
            return false;
        }
        if (taken(tList.get(index).getTestId(), student.getAccountId())
                && populateTest(tList.get(index).getTestId())) {
            callComposeTest(test, question_Panel_, task_Panel_);
            JOptionPane.showMessageDialog(null, "You have successfully taken test!\nGood luck!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean taken(int testId, int studentId) {
        return studentDAO.addTakenBy(testId, studentId);
    }

    private boolean populateTest(int testId) {
        test = studentDAO.getTest(testId);
        return test != null;
    }

    private void callComposeTest(Test test, JPanel question_Panel_, JPanel task_Panel_) {
        compose.composeTest(test, question_Panel_, task_Panel_);
    }

    public boolean submitTestCheck(JPanel question_Panel_, JPanel task_Panel_) {
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Check!", JOptionPane.YES_NO_OPTION);
        if (choice == 1 || choice == -1) {
            return false;
        }
        sTest = new SubmittedTest(test.getTestId(), student.getInstructorId(), student.getAccountId());
        callComposeSubmittedTest(sTest, test, question_Panel_, task_Panel_);
        if (submitTest(sTest)) {
            JOptionPane.showMessageDialog(null, "You have successfully submited test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        JOptionPane.showMessageDialog(null, "Something went wrong while submitting test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
        return false;
    }

    private void callComposeSubmittedTest(SubmittedTest sTest, Test test, JPanel question_Panel_, JPanel task_Panel_) {
        compose.composeSubmittedTest(sTest, test, question_Panel_, task_Panel_);
    }

    private boolean submitTest(SubmittedTest sTest) {
        return studentDAO.submitTest(sTest);
    }

    public void submittedTestTableModel(TableModel tableModel) {
        DefaultTableModel refreshModel = (DefaultTableModel) tableModel;
        populateSubmittedTestList();
        populateSubmittedTableModel(refreshModel);
    }

    private void populateSubmittedTestList() {
        sTList = studentDAO.getAllSubmittedTests(student.getAccountId());
    }

    private void populateSubmittedTableModel(DefaultTableModel submittedTableModel) {
        submittedTableModel.setRowCount(0);
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
            submittedTableModel.addRow(row);
        }
        submittedTableModel.fireTableDataChanged();
    }

    public DefaultListModel testListModel() {
        DefaultListModel listModel = new DefaultListModel();
        populateTestList();
        populateListModel(listModel);
        return listModel;
    }

    private void populateTestList() {
        tList = studentDAO.getStudentTests(student.getInstructorId(), student.getAccountId());
    }

    private void populateListModel(DefaultListModel listModel) {
        for (Test t : tList) {
            listModel.addElement(t.getTestId() + " " + t.getTestName());
        }
    }

    public String setAverageScore() {
        return "Average Score: " + averageScore();
    }

    private double averageScore() {
        return studentDAO.getAverageScore(student.getAccountId());
    }
}
