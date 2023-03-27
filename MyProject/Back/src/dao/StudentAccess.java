/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jdbc.DatabaseConnection;
import dao.contracts.StudentContract;
import dto.Test;
import dto.Question;
import dto.Task;
import dto.SubmittedTest;
import dto.SubmittedQuestion;
import dto.SubmittedTask;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public class StudentAccess implements StudentContract {

    @Override
    public ArrayList<Test> getStudentTests(int instructorId, int studentId) {
        String query = "SELECT t.* FROM test t WHERE t.instructor_id = ? AND t.test_id NOT IN (SELECT tb.test_id FROM taken_by tb WHERE tb.student_id = ?);";
        ArrayList<Test> tList = new ArrayList<>();
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, instructorId);
            pst.setInt(2, studentId);
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
            System.out.println("An error occurred while getting student tests!");
            ex.printStackTrace();
        }
        return tList;
    }

    @Override
    public Test getTest(int testId) {
        String getTestQuery = "SELECT t.* FROM test t WHERE t.test_id = ?;";
        String getQuestionQuery = "SELECT tq.* FROM test_questions tq WHERE tq.test_id = ?;";
        String getTaskQuery = "SELECT tt.* FROM test_tasks tt WHERE tt.test_id = ?;";
        Test test = null;
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement getTest = con.prepareStatement(getTestQuery);  PreparedStatement getQestion = con.prepareStatement(getQuestionQuery);  PreparedStatement getTask = con.prepareStatement(getTaskQuery)) {
            getTest.setInt(1, testId);
            try ( ResultSet testRS = getTest.executeQuery()) {
                if (testRS.next()) {
                    test = new Test(testRS.getInt(1), testRS.getInt(2), testRS.getString(3), testRS.getString(4));
                    getQestion.setInt(1, test.getTestId());
                    try ( ResultSet questionRS = getQestion.executeQuery()) {
                        if (questionRS.isBeforeFirst()) {
                            test.setQuestionList(new ArrayList<>());
                            Question q;
                            while (questionRS.next()) {
                                q = new Question(
                                        questionRS.getString(2),
                                        questionRS.getString(3),
                                        questionRS.getString(4),
                                        questionRS.getString(5),
                                        questionRS.getString(6),
                                        questionRS.getString(7));
                                test.getQuestionList().add(q);
                            }
                        }
                    }
                    getTask.setInt(1, test.getTestId());
                    try ( ResultSet taskRS = getTask.executeQuery()) {
                        if (taskRS.isBeforeFirst()) {
                            test.setTaskList(new ArrayList<>());
                            Task t;
                            while (taskRS.next()) {
                                t = new Task(
                                        taskRS.getString(2),
                                        taskRS.getString(3));
                                test.getTaskList().add(t);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while getting test for student!");
            ex.printStackTrace();
        }
        return test;
    }

    @Override
    public boolean addTakenBy(int testId, int studentId) {
        String query = "INSERT INTO taken_by VALUES(?,?,CURDATE());";
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, testId);
            pst.setInt(2, studentId);
            return pst.executeUpdate() >= 1;
        } catch (SQLException ex) {
            System.out.println("An error occurred while adding taken by details!");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean submitTest(SubmittedTest t) {
        try ( Connection con = DatabaseConnection.getConnection()) {
            String submitTestQuery = "INSERT INTO submitted_test (test_id, instructor_id, student_id) VALUES(?, ?, ?);";
            String submitQuestionQuery = "INSERT INTO submitted_questions (test_id, question, answer, correct_answer) VALUES(?, ?, ?, ?);";
            String submitTaskQuery = "INSERT INTO submitted_tasks (test_id, task_description, code) VALUES(?, ?, ?);";
            con.setAutoCommit(false);
            try ( PreparedStatement submitTest = con.prepareStatement(submitTestQuery);  PreparedStatement submitQuestion = con.prepareStatement(submitQuestionQuery);  PreparedStatement submitTask = con.prepareStatement(submitTaskQuery)) {
                submitTest.setInt(1, t.getTestId());
                submitTest.setInt(2, t.getInstructorId());
                submitTest.setInt(3, t.getStudentId());
                submitTest.executeUpdate();
                for (SubmittedQuestion sq : t.getSqList()) {
                    submitQuestion.setInt(1, t.getTestId());
                    submitQuestion.setString(2, sq.getQuestion());
                    submitQuestion.setString(3, sq.getAnswer());
                    submitQuestion.setString(4, sq.getCorrectAnswer());
                    submitQuestion.executeUpdate();
                }
                for (SubmittedTask st : t.getStList()) {
                    submitTask.setInt(1, t.getTestId());
                    submitTask.setString(2, st.getTaskDescription());
                    submitTask.setString(3, st.getCode());
                    submitTask.executeUpdate();
                }
                con.commit();
            } catch (SQLException ex) {
                System.out.println("Insert is being rolled back!");
                ex.printStackTrace();
                con.rollback();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while submitting student test!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<SubmittedTest> getAllSubmittedTests(int studentId) {
        String query = "SELECT * FROM student_submitted_test WHERE student_id = ?;";
        ArrayList<SubmittedTest> sTList = new ArrayList<>();
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, studentId);
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
            System.out.println("An error occurred while getting all student submitted tests!");
            ex.printStackTrace();
        }
        return sTList;
    }

    @Override
    public Double getAverageScore(int studentId) {
        Double d = null;
        String query = "SELECT AVG(st.score) FROM submitted_test st WHERE st.student_id = ?;";
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, studentId);
            try ( ResultSet rs = pst.executeQuery()) {
                rs.next();
                d = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while getting student average score!");
            ex.printStackTrace();
        }
        return d;
    }
}
