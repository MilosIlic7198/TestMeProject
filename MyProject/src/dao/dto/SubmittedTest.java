/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.dto;

import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public class SubmittedTest {

    private int testId;
    private int instructorId;
    private int studentId;
    private String score;
    private String studentName;
    private String testDescription;

    private ArrayList<SubmittedQuestion> sqList;
    private ArrayList<SubmittedTask> stList;

    public SubmittedTest(int testId, int instructorId, int studentId, String score, String studentName, String testDescription) {
        this.testId = testId;
        this.instructorId = instructorId;
        this.studentId = studentId;
        this.score = score;
        this.studentName = studentName;
        this.testDescription = testDescription;
    }

    public SubmittedTest(int testId, int instructorId, int studentId) {
        this.testId = testId;
        this.instructorId = instructorId;
        this.studentId = studentId;
    }

    public int getTestId() {
        return testId;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getScore() {
        return score;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public ArrayList<SubmittedQuestion> getSqList() {
        return sqList;
    }

    public void setSqList(ArrayList<SubmittedQuestion> sqList) {
        this.sqList = sqList;
    }

    public ArrayList<SubmittedTask> getStList() {
        return stList;
    }

    public void setStList(ArrayList<SubmittedTask> stList) {
        this.stList = stList;
    }
}
