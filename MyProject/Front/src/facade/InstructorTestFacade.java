/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import dto.SubmittedTest;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 *
 * @author Milos
 */
public class InstructorTestFacade {

    private JPanel[] arrayOfGeneratedSubmittedQuestionJPanels;
    private JPanel[] arrayOfGeneratedSubmittedTaskJPanels;

    public void composeSubmittedTest(SubmittedTest submittedTest, JPanel submitted_Question_Panel_, JPanel submitted_Task_Panel_) {
        composeSubmittedQuestion(submittedTest, submitted_Question_Panel_);
        composeSubmittedTask(submittedTest, submitted_Task_Panel_);
    }

    private void composeSubmittedQuestion(SubmittedTest submittedTest, JPanel submitted_Question_Panel_) {
        arrayOfGeneratedSubmittedQuestionJPanels = new JPanel[submittedTest.getSqList().size()];

        for (int i = 0; i < arrayOfGeneratedSubmittedQuestionJPanels.length; i++) {
            arrayOfGeneratedSubmittedQuestionJPanels[i] = new JPanel();
            arrayOfGeneratedSubmittedQuestionJPanels[i].setLayout(new BoxLayout(arrayOfGeneratedSubmittedQuestionJPanels[i], BoxLayout.PAGE_AXIS));

            JLabel question_Label_ = new JLabel("Question " + (i + 1) + ":  " + submittedTest.getSqList().get(i).getQuestion());
            JLabel answer_Label_ = new JLabel("Answer:  " + submittedTest.getSqList().get(i).getAnswer());
            JLabel correct_answer_Label_ = new JLabel("Correct answer:  " + submittedTest.getSqList().get(i).getCorrectAnswer());

            question_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            answer_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            correct_answer_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);

            question_Label_.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            answer_Label_.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            correct_answer_Label_.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

            question_Label_.setForeground(Color.black);
            answer_Label_.setForeground(Color.black);
            correct_answer_Label_.setForeground(Color.black);

            if (submittedTest.getSqList().get(i).getAnswer().equalsIgnoreCase(submittedTest.getSqList().get(i).getCorrectAnswer())) {
                arrayOfGeneratedSubmittedQuestionJPanels[i].setBackground(new Color(212, 255, 148));
            } else {
                arrayOfGeneratedSubmittedQuestionJPanels[i].setBackground(new Color(255, 159, 148));
            }
            arrayOfGeneratedSubmittedQuestionJPanels[i].add(question_Label_);
            arrayOfGeneratedSubmittedQuestionJPanels[i].add(answer_Label_);
            arrayOfGeneratedSubmittedQuestionJPanels[i].add(correct_answer_Label_);

            Border border = BorderFactory.createMatteBorder(1, 2, 1, 2, Color.gray);
            arrayOfGeneratedSubmittedQuestionJPanels[i].setBorder(border);

            submitted_Question_Panel_.add(arrayOfGeneratedSubmittedQuestionJPanels[i]);
        }
        submitted_Question_Panel_.setLayout(new GridLayout(arrayOfGeneratedSubmittedQuestionJPanels.length, 1));
        submitted_Question_Panel_.revalidate();
        submitted_Question_Panel_.repaint();
    }

    private void composeSubmittedTask(SubmittedTest submittedTest, JPanel submitted_Task_Panel_) {
        arrayOfGeneratedSubmittedTaskJPanels = new JPanel[submittedTest.getStList().size()];

        for (int i = 0; i < arrayOfGeneratedSubmittedTaskJPanels.length; i++) {
            arrayOfGeneratedSubmittedTaskJPanels[i] = new JPanel();
            arrayOfGeneratedSubmittedTaskJPanels[i].setLayout(new BoxLayout(arrayOfGeneratedSubmittedTaskJPanels[i], BoxLayout.PAGE_AXIS));

            JLabel task_Label_ = new JLabel("Task " + (i + 1) + ":  " + submittedTest.getStList().get(i).getTaskDescription());
            JTextArea code_Area_ = new JTextArea(submittedTest.getStList().get(i).getCode());

            task_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            code_Area_.setAlignmentX(JTextArea.LEFT_ALIGNMENT);

            task_Label_.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            code_Area_.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            code_Area_.setEditable(false);

            arrayOfGeneratedSubmittedTaskJPanels[i].add(task_Label_);
            arrayOfGeneratedSubmittedTaskJPanels[i].add(code_Area_);

            Border border = BorderFactory.createMatteBorder(1, 2, 1, 2, Color.gray);
            arrayOfGeneratedSubmittedTaskJPanels[i].setBorder(border);

            submitted_Task_Panel_.add(arrayOfGeneratedSubmittedTaskJPanels[i]);
        }
        submitted_Task_Panel_.setLayout(new GridLayout(arrayOfGeneratedSubmittedTaskJPanels.length, 1));
        submitted_Task_Panel_.revalidate();
        submitted_Task_Panel_.repaint();
    }
}
