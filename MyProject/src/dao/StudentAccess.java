/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.jdbc.DatabaseConnection;
import dao.contracts.StudentContract;
import dao.dto.Test;
import dao.dto.Question;
import dao.dto.Task;
import dao.dto.SubmittedTest;
import dao.dto.SubmittedQuestion;
import dao.dto.SubmittedTask;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public class StudentAccess implements StudentContract {

    private String query;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public ArrayList<Test> getAllTests(int instructorId, int studentId) throws SQLException {
        ArrayList<Test> tList = new ArrayList<>();
        query = "SELECT t.* FROM test t WHERE t.instructor_id = ? AND t.test_id NOT IN (SELECT tb.test_id FROM taken_by tb WHERE tb.student_id = ?);";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, instructorId);
        pst.setInt(2, studentId);
        rs = pst.executeQuery();
        if (rs.isBeforeFirst()) {
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
    public Test getTest(int testId) throws SQLException {
        Test test = null;
        query = "SELECT t.* FROM test t WHERE t.test_id = ?;";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, testId);
        rs = pst.executeQuery();
        if (rs.next()) {
            test = new Test(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4));
            query = "SELECT tq.* FROM test_questions tq WHERE tq.test_id = ?;";
            pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            pst.setInt(1, test.getTestId());
            rs = pst.executeQuery();
            if (rs.isBeforeFirst()) {
                test.setQuestionList(new ArrayList<Question>());
                Question q;
                while (rs.next()) {
                    q = new Question(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7));
                    test.getQuestionList().add(q);
                }
            }
            query = "SELECT tt.* FROM test_tasks tt WHERE tt.test_id = ?;";
            pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            pst.setInt(1, test.getTestId());
            rs = pst.executeQuery();
            if (rs.isBeforeFirst()) {
                test.setTaskList(new ArrayList<Task>());
                Task t;
                while (rs.next()) {
                    t = new Task(
                            rs.getString(2),
                            rs.getString(3));
                    test.getTaskList().add(t);
                }
            }
        }
        rs.close();
        pst.close();
        return test;
    }

    @Override
    public int addTakenBy(int testId, int studentId) throws SQLException {
        int n = -1;
        query = "INSERT INTO taken_by VALUES(?,?,CURDATE());";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, testId);
        pst.setInt(2, studentId);
        n = pst.executeUpdate();
        pst.close();
        return n;
    }

    @Override
    public int submitTest(SubmittedTest t) throws SQLException {
        int n = -1;
        query = "INSERT INTO submitted_test (test_id, instructor_id, student_id) VALUES(?, ?, ?);";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, t.getTestId());
        pst.setInt(2, t.getInstructorId());
        pst.setInt(3, t.getStudentId());
        pst.executeUpdate();
        query = "INSERT INTO submitted_questions (test_id, question, answer, correct_answer) VALUES(?, ?, ?, ?);";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        for (SubmittedQuestion sq : t.getSqList()) {
            pst.setInt(1, t.getTestId());
            pst.setString(2, sq.getQuestion());
            pst.setString(3, sq.getAnswer());
            pst.setString(4, sq.getCorrectAnswer());
            pst.executeUpdate();
        }
        query = "INSERT INTO submitted_tasks (test_id, task_description, code) VALUES(?, ?, ?);";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        int i = 0;
        for (SubmittedTask st : t.getStList()) {
            pst.setInt(1, t.getTestId());
            pst.setString(2, st.getTaskDescription());
            pst.setString(3, st.getCode());
            pst.executeUpdate();
            if (i++ == t.getStList().size() - 1) {
                n = 1;
            }
        }
        pst.close();
        return n;
    }

    @Override
    public ArrayList<SubmittedTest> getAllSubmittedTests(int studentId) throws SQLException {
        ArrayList<SubmittedTest> sTList = new ArrayList<>();
        query = "SELECT st.*, CONCAT(s.first_name, \" \", s.last_name) AS student_name, t.description FROM submitted_test st JOIN student s ON st.student_id = s.student_id JOIN test t ON st.test_id = t.test_id WHERE st.student_id  = ?;";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setInt(1, studentId);
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
}
