/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.contracts;

import dao.dto.Student;
import dao.dto.Test;
import dao.dto.SubmittedTest;
import dao.dto.TakenBy;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public interface InstructorContract {

    void addTest(Test t) throws SQLException;

    ArrayList<Test> getAllTests(int instructorId) throws SQLException;

    ArrayList<SubmittedTest> getAllSubmittedTests(int instructorId) throws SQLException;

    SubmittedTest getSubmittedTest(int testId) throws SQLException;

    int gradeSubmittedTest(int score, int testId, int studentId) throws SQLException;

    ArrayList<TakenBy> getAllTakenByTest(int instructorId) throws SQLException;

    ArrayList<Student> getAllStudents(int instructorId) throws SQLException;
}
