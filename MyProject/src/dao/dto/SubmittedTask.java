/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.dto;

/**
 *
 * @author Milos
 */
public class SubmittedTask {

    private int testId;
    private String taskDescription;
    private String code;

    public SubmittedTask(String taskDescription, String code) {
        this.taskDescription = taskDescription;
        this.code = code;
    }

    public SubmittedTask(int testId, String taskDescription, String code) {
        this.testId = testId;
        this.taskDescription = taskDescription;
        this.code = code;
    }

    public int getTestId() {
        return testId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getCode() {
        return code;
    }
}
