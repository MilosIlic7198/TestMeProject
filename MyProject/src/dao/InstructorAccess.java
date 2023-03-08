/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.jdbc.DatabaseConnection;
import dao.contracts.InstructorContract;
import dao.dto.Account;
import dao.dto.AccountBuilder;
import dao.dto.Test;
import dao.dto.Question;
import dao.dto.Task;
import dao.dto.SubmittedTest;
import dao.dto.SubmittedQuestion;
import dao.dto.SubmittedTask;
import dao.dto.TakenBy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public class InstructorAccess implements InstructorContract {

    @Override
    public boolean addTest(Test test) {
        try ( Connection con = DatabaseConnection.getConnection()) {
            String addTestQuery = "INSERT INTO test (instructor_id, name, description) VALUES(?, ?, ?);";
            String addQuestionQuery = "INSERT INTO test_questions (test_id, question, option_1, option_2, option_3, option_4, correct_answer) VALUES(?, ?, ?, ?, ?, ?, ?);";
            String addTaskQuery = "INSERT INTO test_tasks (test_id, task_description, code) VALUES(?, ?, ?);";
            con.setAutoCommit(false);
            try ( PreparedStatement addTest = con.prepareStatement(addTestQuery, Statement.RETURN_GENERATED_KEYS);  PreparedStatement addQuestion = con.prepareStatement(addQuestionQuery);  PreparedStatement addTask = con.prepareStatement(addTaskQuery)) {
                addTest.setInt(1, test.getInstructorId());
                addTest.setString(2, test.getTestName());
                addTest.setString(3, test.getTestDescription());
                addTest.executeUpdate();
                try ( ResultSet rs = addTest.getGeneratedKeys()) {
                    rs.next();
                    for (Question question : test.getQuestionList()) {
                        addQuestion.setInt(1, rs.getInt(1));
                        addQuestion.setString(2, question.getQuestion());
                        addQuestion.setString(3, question.getOption1());
                        addQuestion.setString(4, question.getOption2());
                        addQuestion.setString(5, question.getOption3());
                        addQuestion.setString(6, question.getOption4());
                        addQuestion.setString(7, question.getCorrectAnswer());
                        addQuestion.executeUpdate();
                    }
                    for (Task task : test.getTaskList()) {
                        addTask.setInt(1, rs.getInt(1));
                        addTask.setString(2, task.getTaskDescription());
                        addTask.setString(3, task.getCode());
                        addTask.executeUpdate();
                    }
                }
                con.commit();
            } catch (SQLException ex) {
                System.out.println(ex.getLocalizedMessage());
                System.out.println("Insert is being rolled back!");
                con.rollback();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<Test> getInstructorTests(int instructorId) {
        String query = "SELECT t.* FROM test t WHERE t.instructor_id = ?;";
        ArrayList<Test> tList = new ArrayList<>();
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, instructorId);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.isBeforeFirst()) {
                    Test test;
                    while (rs.next()) {
                        test = new Test(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4));
                        tList.add(test);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return tList;
    }

    @Override
    public ArrayList<SubmittedTest> getAllSubmittedTests(int instructorId) {
        String query = "SELECT * FROM student_submitted_test WHERE instructor_id = ?;";
        ArrayList<SubmittedTest> sTList = new ArrayList<>();
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, instructorId);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.isBeforeFirst()) {
                    SubmittedTest sTest;
                    while (rs.next()) {
                        sTest = new SubmittedTest(rs.getInt(1), rs.getInt(2), rs.getInt(3), String.valueOf(rs.getInt(4)), rs.getString(5), rs.getString(6));
                        sTList.add(sTest);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return sTList;
    }

    @Override
    public SubmittedTest getSubmittedTest(int testId) {
        String getSubmitTestQuery = "SELECT st.* FROM submitted_test st WHERE test_id = ?;";
        String getSubmitQuestionQuery = "SELECT sq.* FROM submitted_questions sq WHERE sq.test_id = ?;";
        String getSubmitTaskQuery = "SELECT st.* FROM submitted_tasks st WHERE st.test_id = ?;";
        SubmittedTest submittedTest = null;
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement getSubmittedTest = con.prepareStatement(getSubmitTestQuery);  PreparedStatement getSubmittedQuestion = con.prepareStatement(getSubmitQuestionQuery);  PreparedStatement getSubmittedTask = con.prepareStatement(getSubmitTaskQuery)) {
            getSubmittedTest.setInt(1, testId);
            try ( ResultSet submittedTestRS = getSubmittedTest.executeQuery()) {
                if (submittedTestRS.next()) {
                    submittedTest = new SubmittedTest(submittedTestRS.getInt(1), submittedTestRS.getInt(2), submittedTestRS.getInt(3));
                    getSubmittedQuestion.setInt(1, submittedTest.getTestId());
                    try ( ResultSet submittedQuestionRS = getSubmittedQuestion.executeQuery()) {
                        if (submittedQuestionRS.isBeforeFirst()) {
                            submittedTest.setSqList(new ArrayList<>());
                            SubmittedQuestion sq;
                            while (submittedQuestionRS.next()) {
                                sq = new SubmittedQuestion(
                                        submittedQuestionRS.getString(2),
                                        submittedQuestionRS.getString(3),
                                        submittedQuestionRS.getString(4));
                                submittedTest.getSqList().add(sq);
                            }
                        }
                    }
                    getSubmittedTask.setInt(1, submittedTest.getTestId());
                    try ( ResultSet submittedTaskRS = getSubmittedTask.executeQuery()) {
                        if (submittedTaskRS.isBeforeFirst()) {
                            submittedTest.setStList(new ArrayList<>());
                            SubmittedTask st;
                            while (submittedTaskRS.next()) {
                                st = new SubmittedTask(
                                        submittedTaskRS.getString(2),
                                        submittedTaskRS.getString(3));
                                submittedTest.getStList().add(st);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return submittedTest;
    }

    @Override
    public boolean gradeSubmittedTest(int score, int testId, int studentId) {
        String query = "UPDATE submitted_test SET score = ? WHERE test_id = ? AND student_id = ?;";
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, score);
            pst.setInt(2, testId);
            pst.setInt(3, studentId);
            return pst.executeUpdate() >= 1;
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public ArrayList<TakenBy> getAllTakenByTest(int instructorId) {
        String query = "SELECT * FROM test_taken_by_student WHERE test_id IN (SELECT t.test_id FROM test t WHERE t.instructor_id = ?);";
        ArrayList<TakenBy> takenByList = new ArrayList<>();
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, instructorId);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.isBeforeFirst()) {
                    TakenBy tb;
                    while (rs.next()) {
                        tb = new TakenBy(
                                rs.getInt(1),
                                rs.getInt(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5));
                        takenByList.add(tb);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return takenByList;
    }

    @Override
    public ArrayList<Account> getInstructorStudents(int instructorId) {
        String query = "SELECT * FROM instructor_student WHERE instructor_id = ?;";
        ArrayList<Account> sList = new ArrayList<>();
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, instructorId);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.isBeforeFirst()) {
                    AccountBuilder accountBuilder = new AccountBuilder();
                    Account stud;
                    while (rs.next()) {
                        accountBuilder.accountId(rs.getInt(1))
                                .usernameEmail(rs.getString(2))
                                .firstName(rs.getString(3))
                                .lastName(rs.getString(4))
                                .city(rs.getString(5))
                                .street(rs.getString(6))
                                .postalCode(rs.getInt(7))
                                .birthDate(rs.getString(8))
                                .gender(rs.getString(9).charAt(0));
                        stud = accountBuilder.build();
                        sList.add(stud);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return sList;
    }
}
