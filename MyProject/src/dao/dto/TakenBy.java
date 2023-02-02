/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.dto;

/**
 *
 * @author Milos
 */
public class TakenBy {

    private int testId;
    private int studentId;
    private String dateTaken;
    private String testName;
    private String studentName;

    public TakenBy(int testId, int studentId, String dateTaken, String testName, String studentName) {
        this.testId = testId;
        this.studentId = studentId;
        this.dateTaken = dateTaken;
        this.testName = testName;
        this.studentName = studentName;
    }

    public TakenBy(int testId, int studentId) {
        this.testId = testId;
        this.studentId = studentId;
    }

    public int getTestId() {
        return testId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public String getTestName() {
        return testName;
    }

    public String getStudentName() {
        return studentName;
    }
}
