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
public class Test {

    private int testId;
    private int instructorId;
    private String testName;
    private String testDescription;

    private ArrayList<Question> qList;
    private ArrayList<Task> tList;

    public Test(int testId, int instructorId, String testName, String testDescription) {
        this.testId = testId;
        this.instructorId = instructorId;
        this.testName = testName;
        this.testDescription = testDescription;
    }

    public Test() {
    }

    public ArrayList<Question> getQuestionList() {
        return qList;
    }

    public void setQuestionList(ArrayList<Question> qList) {
        this.qList = qList;
    }

    public ArrayList<Task> getTaskList() {
        return tList;
    }

    public void setTaskList(ArrayList<Task> tList) {
        this.tList = tList;
    }

    public int getTestId() {
        return testId;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }
}
