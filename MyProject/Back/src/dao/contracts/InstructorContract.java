/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.contracts;

import dto.Account;
import dto.Test;
import dto.SubmittedTest;
import dto.TakenBy;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public interface InstructorContract {

    boolean addTest(Test test);

    boolean deleteTest(int testId);

    ArrayList<Test> getInstructorTests(int instructorId);

    ArrayList<SubmittedTest> getAllSubmittedTests(int instructorId);

    SubmittedTest getSubmittedTest(int testId);

    boolean gradeSubmittedTest(int score, int testId, int studentId);

    ArrayList<TakenBy> getAllTakenByTest(int instructorId);

    ArrayList<Account> getInstructorStudents(int instructorId);
}
