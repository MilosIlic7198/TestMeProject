/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.contracts;

import dao.dto.Test;
import dao.dto.SubmittedTest;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public interface StudentContract {

    ArrayList<Test> getStudentTests(int instructorId, int studentId);

    Test getTest(int testId);

    boolean addTakenBy(int testId, int studentId);

    boolean submitTest(SubmittedTest t);

    ArrayList<SubmittedTest> getAllSubmittedTests(int studentId);

    Double getAverageScore(int studentId);
}
