/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import dao.StudentAccess;
import dao.contracts.StudentContract;
import dao.dto.Student;
import dao.dto.SubmittedQuestion;
import dao.dto.SubmittedTask;
import dao.dto.SubmittedTest;
import dao.dto.Test;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Milos
 */
public class StudentFacade {

    private Student student;
    private StudentContract studentDAO;
    private DefaultListModel listModel;
    private DefaultTableModel submittedTableModel;
    private ArrayList<Test> tList;
    private Test test;
    private JPanel[] arrayOfGeneratedQuestionJPanels;
    private JPanel[] arrayOfGeneratedTaskJPanels;

    public StudentFacade(Student student) {
        this.student = student;
        this.studentDAO = new StudentAccess();
    }

    public void testListCheck(JList test_List_, JLabel test_Description_Label_) {
        if (tList.isEmpty()) {
            return;
        }
        test_Description_Label_.setText(tList.get(test_List_.getSelectedIndex()).getTestDescription());
    }

    public void takeTestCheck(JList test_List_, JLabel test_Description_Label_, JPanel question_Panel_, JPanel task_Panel_) {
        if (test_List_.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a select test first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        } else {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Check!", JOptionPane.YES_NO_OPTION);
            if (choice == 1) {
                return;
            }
            taken(tList.get(test_List_.getSelectedIndex()).getTestId(), 5); //TODO: Change it to student.getStudentId() later!
            populateTest(tList.get(test_List_.getSelectedIndex()).getTestId());
            composeTest(question_Panel_, task_Panel_);
            test_Description_Label_.setText(null);
        }
    }

    private void taken(int testId, int studentId) {
        try {
            int n = studentDAO.addTakenBy(testId, studentId);
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "You have successfully taken test!\nGood luck!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void populateTest(int testId) {
        try {
            test = (Test) studentDAO.getTest(testId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void composeTest(JPanel question_Panel_, JPanel task_Panel_) {
        composeQuestion(question_Panel_);
        composeTask(task_Panel_);
    }

    public void composeQuestion(JPanel question_Panel_) {
        arrayOfGeneratedQuestionJPanels = new JPanel[test.getQuestionList().size()];

        for (int i = 0; i < arrayOfGeneratedQuestionJPanels.length; i++) {
            arrayOfGeneratedQuestionJPanels[i] = new JPanel();
            arrayOfGeneratedQuestionJPanels[i].setLayout(new BoxLayout(arrayOfGeneratedQuestionJPanels[i], BoxLayout.PAGE_AXIS));

            JLabel question_Label_ = new JLabel("Question " + (i + 1) + ": " + test.getQuestionList().get(i).getQuestion());
            JRadioButton option1_RadioButton_ = new JRadioButton(test.getQuestionList().get(i).getOption1());
            JRadioButton option2_RadioButton_ = new JRadioButton(test.getQuestionList().get(i).getOption2());
            JRadioButton option3_RadioButton_ = new JRadioButton(test.getQuestionList().get(i).getOption3());
            JRadioButton option4_RadioButton_ = new JRadioButton(test.getQuestionList().get(i).getOption4());

            question_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            option1_RadioButton_.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            option2_RadioButton_.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            option3_RadioButton_.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            option4_RadioButton_.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);

            ButtonGroup question_ButtonGroup = new ButtonGroup();
            question_ButtonGroup.add(option1_RadioButton_);
            question_ButtonGroup.add(option2_RadioButton_);
            question_ButtonGroup.add(option3_RadioButton_);
            question_ButtonGroup.add(option4_RadioButton_);

            arrayOfGeneratedQuestionJPanels[i].add(question_Label_);
            arrayOfGeneratedQuestionJPanels[i].add(option1_RadioButton_);
            arrayOfGeneratedQuestionJPanels[i].add(option2_RadioButton_);
            arrayOfGeneratedQuestionJPanels[i].add(option3_RadioButton_);
            arrayOfGeneratedQuestionJPanels[i].add(option4_RadioButton_);

            question_Panel_.add(arrayOfGeneratedQuestionJPanels[i]);
        }
        question_Panel_.setLayout(new GridLayout(arrayOfGeneratedQuestionJPanels.length, 1));
        question_Panel_.revalidate();
        question_Panel_.repaint();
    }

    public void composeTask(JPanel task_Panel_) {
        arrayOfGeneratedTaskJPanels = new JPanel[test.getTaskList().size()];

        for (int i = 0; i < arrayOfGeneratedTaskJPanels.length; i++) {
            arrayOfGeneratedTaskJPanels[i] = new JPanel();
            arrayOfGeneratedTaskJPanels[i].setLayout(new BoxLayout(arrayOfGeneratedTaskJPanels[i], BoxLayout.PAGE_AXIS));

            JLabel task_Label_ = new JLabel("Task " + (i + 1) + ": " + test.getTaskList().get(i).getTaskDescription());
            JTextArea code_Area_ = new JTextArea(test.getTaskList().get(i).getCode());

            task_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            code_Area_.setAlignmentX(JTextArea.LEFT_ALIGNMENT);

            arrayOfGeneratedTaskJPanels[i].add(task_Label_);
            arrayOfGeneratedTaskJPanels[i].add(code_Area_);

            task_Panel_.add(arrayOfGeneratedTaskJPanels[i]);
        }
        task_Panel_.setLayout(new GridLayout(arrayOfGeneratedTaskJPanels.length, 1));
        task_Panel_.revalidate();
        task_Panel_.repaint();
    }

    public void submitTestCheck(JList test_List_, JTable submitted_Test_Table_, JPanel question_Panel_, JPanel task_Panel_) {
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Check!", JOptionPane.YES_NO_OPTION);
        if (choice == 1) {
            return;
        }
        composeSubmittedTest(test.getTestId(), question_Panel_, task_Panel_);
        setSubmittedTestTableModel(submitted_Test_Table_);
        setModel(test_List_);
    }

    public void setSubmittedTestTableModel(JTable submitted_Test_Table_) {
        try {
            submittedTableModel = (DefaultTableModel) submitted_Test_Table_.getModel();
            ArrayList<SubmittedTest> sTList = studentDAO.getAllSubmittedTests(5);//TODO: Change it to student.getStudentId() later!
            submittedTableModel.setRowCount(0);
            Object[] row = new Object[5];
            for (SubmittedTest st : sTList) {
                row[0] = st.getTestId();
                row[1] = st.getTestDescription();
                row[2] = st.getStudentId();
                row[3] = st.getStudentName();
                if (Integer.parseInt(st.getScore()) == 0) {
                    row[4] = "Not graded yet!";
                } else {
                    row[4] = st.getScore();
                }
                submittedTableModel.addRow(row);
            }
            submitted_Test_Table_.setModel(submittedTableModel);

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(submitted_Test_Table_.getModel());
            submitted_Test_Table_.setRowSorter(sorter);
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
            sorter.sort();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void composeSubmittedTest(int testId, JPanel question_Panel_, JPanel task_Panel_) {
        SubmittedTest sTest = new SubmittedTest(testId, 3, 5);//TODO: Change it to student.getInstructorId() and student.getStudentId() later!
        composeSubmittedQuestion(sTest);
        composeSubmittedTask(sTest);
        submitTest(sTest);
        test = null;
        arrayOfGeneratedQuestionJPanels = null;
        arrayOfGeneratedTaskJPanels = null;
        question_Panel_.removeAll();
        task_Panel_.removeAll();
    }

    public void composeSubmittedQuestion(SubmittedTest sTest) {
        sTest.setSqList(new ArrayList<>());
        SubmittedQuestion sq;
        for (int i = 0; i < arrayOfGeneratedQuestionJPanels.length; i++) {
            JRadioButton jR1 = (JRadioButton) arrayOfGeneratedQuestionJPanels[i].getComponents()[1];
            JRadioButton jR2 = (JRadioButton) arrayOfGeneratedQuestionJPanels[i].getComponents()[2];
            JRadioButton jR3 = (JRadioButton) arrayOfGeneratedQuestionJPanels[i].getComponents()[3];
            JRadioButton jR4 = (JRadioButton) arrayOfGeneratedQuestionJPanels[i].getComponents()[4];
            sq = new SubmittedQuestion(test.getQuestionList().get(i).getQuestion(),
                    jR1.isSelected() ? jR1.getText()
                    : jR2.isSelected() ? jR2.getText()
                    : jR3.isSelected() ? jR3.getText()
                    : jR4.isSelected() ? jR4.getText()
                    : null,
                    test.getQuestionList().get(i).getCorrectAnswer());
            sTest.getSqList().add(sq);
        }
    }

    public void composeSubmittedTask(SubmittedTest sTest) {
        sTest.setStList(new ArrayList<>());
        SubmittedTask st;
        for (int i = 0; i < arrayOfGeneratedTaskJPanels.length; i++) {
            JTextArea jT = (JTextArea) arrayOfGeneratedTaskJPanels[i].getComponents()[1];
            st = new SubmittedTask(test.getTaskList().get(i).getTaskDescription(), jT.getText());
            sTest.getStList().add(st);
        }
    }

    public void submitTest(SubmittedTest sTest) {
        try {
            int n = studentDAO.submitTest(sTest);
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "You have successfully submited test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setModel(JList test_List_) {
        listModel = new DefaultListModel();
        populateTestList();
        populateListModel();
        test_List_.setModel(listModel);
    }

    private void populateListModel() {
        for (Test test : tList) {
            listModel.addElement(test.getTestId() + " " + test.getTestName());
        }
    }

    private void populateTestList() {
        try {
            tList = studentDAO.getAllTests(3, 5);//TODO: Change it to student.getInstructorId() and student.getStudentId() later!
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
