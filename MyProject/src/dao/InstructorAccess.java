/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.jdbc.DatabaseConnection;
import dao.contracts.InstructorContract;
import dao.dto.Test;
import dao.dto.Question;
import dao.dto.Student;
import dao.dto.Task;
import dao.dto.SubmittedTest;
import dao.dto.SubmittedQuestion;
import dao.dto.SubmittedTask;
import dao.dto.TakenBy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public class InstructorAccess implements InstructorContract {

    private String query;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public void addTest(Test t) throws SQLException {
        query = "INSERT INTO test (instructor_id, name, description) VALUES(?,?,?);";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, t.getInstructorId());
        pst.setString(2, t.getTestName());
        pst.setString(3, t.getTestDescription());
        pst.executeUpdate();
        query = "INSERT INTO test_questions (test_id, question, option_1, option_2, option_3, option_4, correct_answer) VALUES(LAST_INSERT_ID(), ?, ?, ?, ?, ?, ?);";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        for (Question question : t.getQuestionList()) {
            pst.setString(1, question.getQuestion());
            pst.setString(2, question.getOption1());
            pst.setString(3, question.getOption2());
            pst.setString(4, question.getOption3());
            pst.setString(5, question.getOption4());
            pst.setString(6, question.getCorrectAnswer());
            pst.executeUpdate();
        }
        query = "INSERT INTO test_tasks (test_id, task_description, code) VALUES(LAST_INSERT_ID(), ?, ?);";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        for (Task task : t.getTaskList()) {
            pst.setString(1, task.getTaskDescription());
            pst.setString(2, task.getCode());
            pst.executeUpdate();
        }
        pst.close();
    }

    @Override
    public ArrayList<Test> getAllTests(int instructorId) throws SQLException {
        ArrayList<Test> tList = null;
        query = "SELECT t.* FROM test t WHERE t.instructor_id = ?;";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, instructorId);
        rs = pst.executeQuery();
        if (rs.isBeforeFirst()) {
            tList = new ArrayList<>();
            Test test;
            while (rs.next()) {
                test = new Test(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4));
                tList.add(test);
            }
        }
        rs.close();
        pst.close();
        return tList;
    }

    @Override
    public ArrayList<SubmittedTest> getAllSubmittedTests(int instructorId) throws SQLException {
        ArrayList<SubmittedTest> sTList = new ArrayList<>();
        query = "SELECT st.*, CONCAT(s.first_name, \" \", s.last_name) AS student_name, t.description FROM submitted_test st JOIN student s ON st.student_id = s.student_id JOIN test t ON st.test_id = t.test_id WHERE st.instructor_id = ?;";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, instructorId);
        rs = pst.executeQuery();
        if (rs.isBeforeFirst()) {
            SubmittedTest sTest;
            while (rs.next()) {
                sTest = new SubmittedTest(rs.getInt(1), rs.getInt(2), rs.getInt(3), String.valueOf(rs.getInt(4)), rs.getString(5), rs.getString(6));
                sTList.add(sTest);
            }
        }
        return sTList;
    }

    @Override
    public SubmittedTest getSubmittedTest(int testId) throws SQLException {
        SubmittedTest submittedTest = null;
        query = "SELECT st.* FROM submitted_test st WHERE test_id = ?;";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, testId);
        rs = pst.executeQuery();
        if (rs.next()) {
            submittedTest = new SubmittedTest(rs.getInt(1), rs.getInt(2), rs.getInt(3));
            query = "SELECT sq.* FROM submitted_questions sq WHERE sq.test_id = ?;";
            pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            pst.setInt(1, submittedTest.getTestId());
            rs = pst.executeQuery();
            if (rs.isBeforeFirst()) {
                submittedTest.setSqList(new ArrayList<SubmittedQuestion>());
                SubmittedQuestion sq;
                while (rs.next()) {
                    sq = new SubmittedQuestion(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4));
                    submittedTest.getSqList().add(sq);
                }
            }
            query = "SELECT st.* FROM submitted_tasks st WHERE st.test_id = ?;";
            pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            pst.setInt(1, submittedTest.getTestId());
            rs = pst.executeQuery();
            if (rs.isBeforeFirst()) {
                submittedTest.setStList(new ArrayList<SubmittedTask>());
                SubmittedTask st;
                while (rs.next()) {
                    st = new SubmittedTask(
                            rs.getString(2),
                            rs.getString(3));
                    submittedTest.getStList().add(st);
                }
            }
        }
        rs.close();
        pst.close();
        return submittedTest;
    }

    @Override
    public int gradeSubmittedTest(int score, int testId, int studentId) throws SQLException {
        int n = -1;
        query = "UPDATE submitted_test SET score = ? WHERE test_id = ? AND student_id = ?;";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, score);
        pst.setInt(2, testId);
        pst.setInt(3, studentId);
        n = pst.executeUpdate();
        pst.close();
        return n;
    }

    @Override
    public ArrayList<TakenBy> getAllTakenByTest(int instructorId) throws SQLException {
        ArrayList<TakenBy> takenByList = new ArrayList<>();
        query = "SELECT tb.*, t.name AS test_name, CONCAT(s.first_name, \" \", s.last_name) AS student_name FROM taken_by tb JOIN test t ON tb.test_id = t.test_id JOIN student s ON tb.student_id = s.student_id WHERE tb.test_id IN (SELECT t2.test_id FROM test t2 WHERE t2.instructor_id = ?);";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, instructorId);
        rs = pst.executeQuery();
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
        return takenByList;
    }

    @Override
    public ArrayList<Student> getAllStudents(int instructorId) throws SQLException {
        ArrayList<Student> sList = new ArrayList<>();
        query = "SELECT s.student_id, s.first_name, s.last_name, s.city, s.street, s.postal_code, s.birthdate, s.gender FROM student s WHERE s.instructor_id = ?;";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, instructorId);
        rs = pst.executeQuery();
        if (rs.isBeforeFirst()) {
            Student s;
            while (rs.next()) {
                s = new Student(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8).charAt(0));
                sList.add(s);
            }
        }
        return sList;
    }
}
