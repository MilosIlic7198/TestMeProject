/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import dao.InstructorAccess;
import dao.contracts.InstructorContract;
import dao.dto.Instructor;
import dao.dto.Question;
import dao.dto.Student;
import dao.dto.SubmittedTest;
import dao.dto.TakenBy;
import dao.dto.Task;
import dao.dto.Test;
import java.awt.Color;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Milos
 */
public class InstructorFacade {

    private Instructor instructor;
    private InstructorContract instructorDAO;
    private DefaultListModel testListModel;
    private DefaultListModel takenByListModel;
    private DefaultTableModel studentTableModel;
    private DefaultTableModel submittedTableModel;
    private Test test;
    private ArrayList<Test> tList;
    private ArrayList<Student> sList;
    private ArrayList<SubmittedTest> sTList;
    private SubmittedTest submittedTest;
    private JPanel[] arrayOfGeneratedSubmittedQuestionJPanels;
    private JPanel[] arrayOfGeneratedSubmittedTaskJPanels;

    public InstructorFacade(Instructor instructor) {
        this.instructor = instructor;
        instructorDAO = new InstructorAccess();
    }

    public void createTestCheck(JLabel question_Number_Label_, JLabel task_Number_Label_) {
        if (test == null) {
            test = new Test();
            test.setQuestionList(new ArrayList<Question>());
            test.setTaskList(new ArrayList<Task>());
            question_Number_Label_.setText(String.valueOf(test.getQuestionList().size()));
            task_Number_Label_.setText(String.valueOf(test.getTaskList().size()));
        }
    }

    public void addQuestionCheck(
            JLabel question_Number_Label_,
            JTextField question_Field_,
            JTextField option1_Field_,
            JTextField option2_Field_,
            JTextField option3_Field_,
            JTextField option4_Field_,
            JComboBox correct_Answer_ComboBox_) {
        //TODO: Add validation!
        if (question_Field_.getText().trim().isEmpty()
                || option1_Field_.getText().trim().isEmpty()
                || option2_Field_.getText().trim().isEmpty()
                || option3_Field_.getText().trim().isEmpty()
                || option4_Field_.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Question q = new Question(
                question_Field_.getText(),
                option1_Field_.getText(),
                option2_Field_.getText(),
                option3_Field_.getText(),
                option4_Field_.getText(),
                correct_Answer_ComboBox_.getSelectedItem().toString());

        test.getQuestionList().add(q);

        JOptionPane.showMessageDialog(null, "Question was added to the test!", "Info!", JOptionPane.INFORMATION_MESSAGE);

        question_Field_.setText(null);
        option1_Field_.setText(null);
        option2_Field_.setText(null);
        option3_Field_.setText(null);
        option4_Field_.setText(null);
        correct_Answer_ComboBox_.setSelectedIndex(0);

        question_Number_Label_.setText(String.valueOf(test.getQuestionList().size()));
    }

    public void addTaskCheck(JLabel task_Number_Label_, JTextField task_Description_Field_, JTextArea code_TextArea_) {
        //TODO: Add validation!
        if (task_Description_Field_.getText().trim().isEmpty() || code_TextArea_.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Task t = new Task(task_Description_Field_.getText(), code_TextArea_.getText());

        test.getTaskList().add(t);

        JOptionPane.showMessageDialog(null, "Task was added to the test!", "Info!", JOptionPane.INFORMATION_MESSAGE);

        task_Description_Field_.setText(null);
        code_TextArea_.setText(null);

        task_Number_Label_.setText(String.valueOf(test.getTaskList().size()));
    }

    public void finishTestCheck(
            JLabel question_Number_Label_,
            JLabel task_Number_Label_,
            JTextField test_Name_Field_,
            JTextField test_Description_Field_,
            JList test_List_) {
        //TODO: Add validation!
        if (test_Name_Field_.getText().trim().isEmpty() || test_Description_Field_.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (test.getQuestionList().isEmpty() || test.getTaskList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Make sure you have at least one question and one task!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        test.setInstructorId(3);//TODO: Change it to instructor.getInstructorId() later!
        test.setTestName(test_Name_Field_.getText());
        test.setTestDescription(test_Description_Field_.getText());

        createTest(test);
        setTestListModel(test_List_);

        JOptionPane.showMessageDialog(null, "Test created!", "Info!", JOptionPane.INFORMATION_MESSAGE);

        test_Name_Field_.setText(null);
        test_Description_Field_.setText(null);
        test.getQuestionList().removeAll(test.getQuestionList());
        test.getTaskList().removeAll(test.getTaskList());
        question_Number_Label_.setText(String.valueOf(test.getQuestionList().size()));
        task_Number_Label_.setText(String.valueOf(test.getTaskList().size()));
    }

    public void lookUpCheck(JTable submitted_Test_Table_, JPanel submitted_Question_Panel_, JPanel submitted_Task_Panel_) {
        if (submitted_Test_Table_.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected submitted test first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        } else {
            populateSubmittedTest(sTList.get(submitted_Test_Table_.getSelectedRow()).getTestId());
            composeSubmittedTest(submitted_Question_Panel_, submitted_Task_Panel_);
        }
    }

    public void backToInstructorCheck(JPanel submitted_Question_Panel_, JPanel submitted_Task_Panel_) {
        submittedTest = null;
        arrayOfGeneratedSubmittedQuestionJPanels = null;
        arrayOfGeneratedSubmittedTaskJPanels = null;
        submitted_Question_Panel_.removeAll();
        submitted_Task_Panel_.removeAll();
    }

    public void gradeSubmittedTestCheck(JTable submitted_Test_Table_) {
        if (submitted_Test_Table_.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected submitted test first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!isParsable(submitted_Test_Table_.getModel().getValueAt(submitted_Test_Table_.getSelectedRow(), 4).toString())) {
            JOptionPane.showMessageDialog(null, "Change column score to a wanted number!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            setSubmittedTableModel(submitted_Test_Table_);
            return;
        }
        grade(Integer.parseInt(submitted_Test_Table_.getModel().getValueAt(submitted_Test_Table_.getSelectedRow(), 4).toString()),
                sTList.get(submitted_Test_Table_.getSelectedRow()).getTestId(),
                Integer.parseInt(submitted_Test_Table_.getModel().getValueAt(submitted_Test_Table_.getSelectedRow(), 2).toString()));
    }

    private void createTest(Test test) {
        try {
            instructorDAO.addTest(test);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setTestListModel(JList test_List_) {
        testListModel = new DefaultListModel();
        populateTestList();
        populateTestListModel();
        test_List_.setModel(testListModel);
    }

    private void populateTestListModel() {
        for (Test test : tList) {
            testListModel.addElement(test.getTestId() + " " + test.getTestName());
        }
    }

    private void populateTestList() {
        try {
            tList = instructorDAO.getAllTests(3);//TODO: Change it to instructor.getInstructorId() later!
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setTakenByListModel(JList taken_By_List_) {
        try {
            takenByListModel = new DefaultListModel();
            ArrayList<TakenBy> takenByList = instructorDAO.getAllTakenByTest(3);//TODO: Change it to instructor.getInstructorId() later!
            for (TakenBy tb : takenByList) {
                takenByListModel.addElement(tb.getTestName() + " " + tb.getStudentName() + " " + tb.getDateTaken());
            }
            taken_By_List_.setModel(takenByListModel);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setStudentTableModel(JTable student_Table_) {
        studentTableModel = (DefaultTableModel) student_Table_.getModel();
        populateStudentList();
        populateStudentTableModel();
        student_Table_.setModel(studentTableModel);
    }

    private void populateStudentTableModel() {
        studentTableModel.setRowCount(0);
        Object[] row = new Object[8];
        int i = 0;
        for (Student s : sList) {
            row[0] = ++i;
            row[1] = s.getStudentId();
            row[2] = s.getFirstName() + " " + s.getLastName();
            row[3] = s.getCity();
            row[4] = s.getStreet();
            row[5] = s.getPostalCode();
            row[6] = s.getBirthDate();
            row[7] = s.getGender();
            studentTableModel.addRow(row);
        }
    }

    private void populateStudentList() {
        try {
            sList = instructorDAO.getAllStudents(3);//TODO: Change it to instructor.getInstructorId() later!
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setSubmittedTableModel(JTable submitted_Test_Table_) {
        submittedTableModel = (DefaultTableModel) submitted_Test_Table_.getModel();
        populateSubmittedTestList();
        populateSubmittedTableModel();
        submitted_Test_Table_.setModel(submittedTableModel);
    }

    public void populateSubmittedTableModel() {
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
    }

    private void populateSubmittedTestList() {
        try {
            sTList = instructorDAO.getAllSubmittedTests(3);//TODO: Change it to instructor.getInstructorId() later!
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void grade(int score, int testId, int studentId) {
        try {
            int n = instructorDAO.gradeSubmittedTest(score, testId, studentId);
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "You have successfully graded submitted test!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void populateSubmittedTest(int submittedTestId) {
        try {
            submittedTest = instructorDAO.getSubmittedTest(submittedTestId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void composeSubmittedTest(JPanel submitted_Question_Panel_, JPanel submitted_Task_Panel_) {
        composeSubmittedQuestion(submitted_Question_Panel_);
        composeSubmittedTask(submitted_Task_Panel_);
    }

    public void composeSubmittedQuestion(JPanel submitted_Question_Panel_) {
        arrayOfGeneratedSubmittedQuestionJPanels = new JPanel[submittedTest.getSqList().size()];

        for (int i = 0; i < arrayOfGeneratedSubmittedQuestionJPanels.length; i++) {
            arrayOfGeneratedSubmittedQuestionJPanels[i] = new JPanel();
            arrayOfGeneratedSubmittedQuestionJPanels[i].setLayout(new BoxLayout(arrayOfGeneratedSubmittedQuestionJPanels[i], BoxLayout.PAGE_AXIS));

            JLabel question_Label_ = new JLabel("Question " + (i + 1) + ": " + submittedTest.getSqList().get(i).getQuestion());
            JLabel answer_Label_ = new JLabel("Answer: " + submittedTest.getSqList().get(i).getAnswer());
            JLabel correct_answer_Label_ = new JLabel("Correct answer: " + submittedTest.getSqList().get(i).getCorrectAnswer());

            question_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            answer_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            correct_answer_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);

            question_Label_.setForeground(Color.black);
            answer_Label_.setForeground(Color.black);
            correct_answer_Label_.setForeground(Color.black);

            if (submittedTest.getSqList().get(i).getAnswer().equalsIgnoreCase(submittedTest.getSqList().get(i).getCorrectAnswer())) {
                arrayOfGeneratedSubmittedQuestionJPanels[i].setBackground(new Color(212, 255, 148));
            } else {
                arrayOfGeneratedSubmittedQuestionJPanels[i].setBackground(new Color(255, 159, 148));
            }
            arrayOfGeneratedSubmittedQuestionJPanels[i].add(question_Label_);
            arrayOfGeneratedSubmittedQuestionJPanels[i].add(answer_Label_);
            arrayOfGeneratedSubmittedQuestionJPanels[i].add(correct_answer_Label_);

            submitted_Question_Panel_.add(arrayOfGeneratedSubmittedQuestionJPanels[i]);
        }
        submitted_Question_Panel_.setLayout(new GridLayout(arrayOfGeneratedSubmittedQuestionJPanels.length, 1));
        submitted_Question_Panel_.revalidate();
        submitted_Question_Panel_.repaint();
    }

    public void composeSubmittedTask(JPanel submitted_Task_Panel_) {
        arrayOfGeneratedSubmittedTaskJPanels = new JPanel[submittedTest.getStList().size()];

        for (int i = 0; i < arrayOfGeneratedSubmittedTaskJPanels.length; i++) {
            arrayOfGeneratedSubmittedTaskJPanels[i] = new JPanel();
            arrayOfGeneratedSubmittedTaskJPanels[i].setLayout(new BoxLayout(arrayOfGeneratedSubmittedTaskJPanels[i], BoxLayout.PAGE_AXIS));

            JLabel task_Label_ = new JLabel("Task " + (i + 1) + ": " + submittedTest.getStList().get(i).getTaskDescription());
            JTextArea code_Area_ = new JTextArea(submittedTest.getStList().get(i).getCode());

            task_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            code_Area_.setAlignmentX(JTextArea.LEFT_ALIGNMENT);

            code_Area_.setEditable(false);

            arrayOfGeneratedSubmittedTaskJPanels[i].add(task_Label_);
            arrayOfGeneratedSubmittedTaskJPanels[i].add(code_Area_);

            submitted_Task_Panel_.add(arrayOfGeneratedSubmittedTaskJPanels[i]);
        }
        submitted_Task_Panel_.setLayout(new GridLayout(arrayOfGeneratedSubmittedTaskJPanels.length, 1));
        submitted_Task_Panel_.revalidate();
        submitted_Task_Panel_.repaint();
    }

    public static boolean isParsable(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    public boolean discardCheck(
            JTextField test_Name_Field_,
            JTextField test_Description_Field_,
            JTextField question_Field_,
            JTextField option1_Field_,
            JTextField option2_Field_,
            JTextField option3_Field_,
            JTextField option4_Field_,
            JComboBox correct_Answer_ComboBox_,
            JTextField task_Description_Field_,
            JTextArea code_TextArea_) {
        if (!test.getQuestionList().isEmpty() || !test.getTaskList().isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(null, "The test will be discarded!\nAre you sure you want to leave?", "Check!", JOptionPane.YES_NO_OPTION);
            if (choice == 1) {
                return false;
            }
        }
        test_Name_Field_.setText(null);
        test_Description_Field_.setText(null);
        question_Field_.setText(null);
        option1_Field_.setText(null);
        option2_Field_.setText(null);
        option3_Field_.setText(null);
        option4_Field_.setText(null);
        correct_Answer_ComboBox_.setSelectedIndex(0);
        task_Description_Field_.setText(null);
        code_TextArea_.setText(null);
        test.setTaskList(null);
        test.setQuestionList(null);
        test = null;
        return true;
    }
}
