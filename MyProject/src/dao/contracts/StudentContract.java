/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.contracts;

import dao.dto.Test;
import dao.dto.SubmittedTest;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public interface StudentContract {

    ArrayList<Test> getAllTests(int instructorId, int studentId) throws SQLException;

    Test getTest(int testId) throws SQLException;

    int addTakenBy(int testId, int studentId) throws SQLException;

    int submitTest(SubmittedTest t) throws SQLException;

    ArrayList<SubmittedTest> getAllSubmittedTests(int studentId) throws SQLException;
}
