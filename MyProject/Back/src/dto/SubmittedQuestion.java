/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author Milos
 */
public class SubmittedQuestion implements Serializable {

    private int testId;
    private String question;
    private String answer;
    private String correctAnswer;

    public SubmittedQuestion(String question, String answer, String correctAnswer) {
        this.question = question;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
    }

    public SubmittedQuestion(int testId, String question, String answer, String correctAnswer) {
        this.testId = testId;
        this.question = question;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
    }

    public int getTestId() {
        return testId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
