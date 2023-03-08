/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import dao.InstructorAccess;
import dao.contracts.InstructorContract;
import dao.dto.Account;
import dao.dto.SubmittedTest;
import dao.dto.TakenBy;
import dao.dto.Test;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import service.CurrentAccount;

/**
 *
 * @author Milos
 */
public class InstructorModelFacade {

    private Account instructor;
    private InstructorContract instructorDAO;
    private ArrayList<Test> tList;
    private ArrayList<Account> sList;
    private ArrayList<SubmittedTest> sTList;
    private ArrayList<TakenBy> takenByList;

    public InstructorModelFacade() {
        this.instructor = CurrentAccount.getInstance(null).getAccount();
        this.instructorDAO = new InstructorAccess();
    }

    public int getTestId(int index) {
        return sTList.get(index).getTestId();
    }

    public DefaultListModel setTestListModel() {
        DefaultListModel testListModel = new DefaultListModel();
        populateTestList();
        populateTestListModel(testListModel);
        return testListModel;
    }

    private void populateTestList() {
        tList = instructorDAO.getInstructorTests(instructor.getAccountId());
    }

    private void populateTestListModel(DefaultListModel testListModel) {
        for (Test t : tList) {
            testListModel.addElement(t.getTestId() + " " + t.getTestName());
        }
    }

    public DefaultListModel setTakenByListModel() {
        DefaultListModel takenByListModel = new DefaultListModel();
        populateTakenByList();
        populateTakenByListModel(takenByListModel);
        return takenByListModel;
    }

    private void populateTakenByList() {
        takenByList = instructorDAO.getAllTakenByTest(instructor.getAccountId());
    }

    private void populateTakenByListModel(DefaultListModel takenByListModel) {
        for (TakenBy tb : takenByList) {
            takenByListModel.addElement(tb.getTestName() + " " + tb.getStudentName() + " " + tb.getDateTaken());
        }
    }

    public DefaultTableModel setStudentTableModel() {
        DefaultTableModel studentTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTableModel.addColumn("No.");
        studentTableModel.addColumn("Student ID");
        studentTableModel.addColumn("Student Email");
        studentTableModel.addColumn("Student Name");
        studentTableModel.addColumn("City");
        studentTableModel.addColumn("Street");
        studentTableModel.addColumn("Postal Code");
        studentTableModel.addColumn("Birthdate");
        studentTableModel.addColumn("Gender");
        populateStudentList();
        populateStudentTableModel(studentTableModel);
        return studentTableModel;
    }

    private void populateStudentList() {
        sList = instructorDAO.getInstructorStudents(instructor.getAccountId());
    }

    private void populateStudentTableModel(DefaultTableModel studentTableModel) {
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
            studentTableModel.addRow(row);
        }
    }

    public DefaultTableModel setSubmittedTableModel() {
        DefaultTableModel submittedTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return switch (column) {
                    case 0, 1, 2, 3, 4 ->
                        false;
                    default ->
                        true;
                };
            }
        };
        submittedTableModel.addColumn("No.");
        submittedTableModel.addColumn("Test ID");
        submittedTableModel.addColumn("Test Description");
        submittedTableModel.addColumn("Student ID");
        submittedTableModel.addColumn("Student Name");
        submittedTableModel.addColumn("Score");
        populateSubmittedTestList();
        populateSubmittedTableModel(submittedTableModel);
        return submittedTableModel;
    }

    private void populateSubmittedTestList() {
        sTList = instructorDAO.getAllSubmittedTests(instructor.getAccountId());
    }

    private void populateSubmittedTableModel(DefaultTableModel submittedTableModel) {
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
    }
}
