/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import dao.dto.SubmittedQuestion;
import dao.dto.SubmittedTask;
import dao.dto.SubmittedTest;
import dao.dto.Test;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 *
 * @author Milos
 */
public class StudentTestFacade {

    private JPanel[] arrayOfGeneratedQuestionJPanels;
    private JPanel[] arrayOfGeneratedTaskJPanels;

    public void composeTest(Test test, JPanel question_Panel_, JPanel task_Panel_) {
        composeQuestion(test, question_Panel_);
        composeTask(test, task_Panel_);
    }

    private void composeQuestion(Test test, JPanel question_Panel_) {
        arrayOfGeneratedQuestionJPanels = new JPanel[test.getQuestionList().size()];

        for (int i = 0; i < arrayOfGeneratedQuestionJPanels.length; i++) {
            arrayOfGeneratedQuestionJPanels[i] = new JPanel();
            arrayOfGeneratedQuestionJPanels[i].setLayout(new BoxLayout(arrayOfGeneratedQuestionJPanels[i], BoxLayout.PAGE_AXIS));

            JLabel question_Label_ = new JLabel("Question " + (i + 1) + ":  " + test.getQuestionList().get(i).getQuestion());
            JRadioButton option1_RadioButton_ = new JRadioButton(test.getQuestionList().get(i).getOption1());
            JRadioButton option2_RadioButton_ = new JRadioButton(test.getQuestionList().get(i).getOption2());
            JRadioButton option3_RadioButton_ = new JRadioButton(test.getQuestionList().get(i).getOption3());
            JRadioButton option4_RadioButton_ = new JRadioButton(test.getQuestionList().get(i).getOption4());

            question_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            option1_RadioButton_.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            option2_RadioButton_.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            option3_RadioButton_.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            option4_RadioButton_.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);

            question_Label_.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            option1_RadioButton_.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            option2_RadioButton_.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            option3_RadioButton_.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            option4_RadioButton_.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

            ButtonGroup question_ButtonGroup = new ButtonGroup();
            question_ButtonGroup.add(option1_RadioButton_);
            question_ButtonGroup.add(option2_RadioButton_);
            question_ButtonGroup.add(option3_RadioButton_);
            question_ButtonGroup.add(option4_RadioButton_);

            arrayOfGeneratedQuestionJPanels[i].add(question_Label_);
            arrayOfGeneratedQuestionJPanels[i].add(option1_RadioButton_);
            arrayOfGeneratedQuestionJPanels[i].add(option2_RadioButton_);
            arrayOfGeneratedQuestionJPanels[i].add(option3_RadioButton_);
            arrayOfGeneratedQuestionJPanels[i].add(option4_RadioButton_);

            Border border = BorderFactory.createMatteBorder(1, 2, 1, 2, Color.gray);
            arrayOfGeneratedQuestionJPanels[i].setBorder(border);

            question_Panel_.add(arrayOfGeneratedQuestionJPanels[i]);
        }
        question_Panel_.setLayout(new GridLayout(arrayOfGeneratedQuestionJPanels.length, 1));
        question_Panel_.revalidate();
        question_Panel_.repaint();
    }

    private void composeTask(Test test, JPanel task_Panel_) {
        arrayOfGeneratedTaskJPanels = new JPanel[test.getTaskList().size()];

        for (int i = 0; i < arrayOfGeneratedTaskJPanels.length; i++) {
            arrayOfGeneratedTaskJPanels[i] = new JPanel();
            arrayOfGeneratedTaskJPanels[i].setLayout(new BoxLayout(arrayOfGeneratedTaskJPanels[i], BoxLayout.PAGE_AXIS));

            JLabel task_Label_ = new JLabel("Task " + (i + 1) + ":  " + test.getTaskList().get(i).getTaskDescription());
            JTextArea code_Area_ = new JTextArea(test.getTaskList().get(i).getCode());

            task_Label_.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            code_Area_.setAlignmentX(JTextArea.LEFT_ALIGNMENT);

            task_Label_.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            code_Area_.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            arrayOfGeneratedTaskJPanels[i].add(task_Label_);
            arrayOfGeneratedTaskJPanels[i].add(code_Area_);

            Border border = BorderFactory.createMatteBorder(1, 2, 1, 2, Color.gray);
            arrayOfGeneratedTaskJPanels[i].setBorder(border);

            task_Panel_.add(arrayOfGeneratedTaskJPanels[i]);
        }
        task_Panel_.setLayout(new GridLayout(arrayOfGeneratedTaskJPanels.length, 1));
        task_Panel_.revalidate();
        task_Panel_.repaint();
    }

    public void composeSubmittedTest(SubmittedTest sTest, Test test, JPanel question_Panel_, JPanel task_Panel_) {
        composeSubmittedQuestion(sTest, test);
        composeSubmittedTask(sTest, test);
        test = null;
        question_Panel_.removeAll();
        task_Panel_.removeAll();
    }

    private void composeSubmittedQuestion(SubmittedTest sTest, Test test) {
        sTest.setSqList(new ArrayList<>());
        SubmittedQuestion sq;
        for (int i = 0; i < arrayOfGeneratedQuestionJPanels.length; i++) {
            JRadioButton jR1 = (JRadioButton) arrayOfGeneratedQuestionJPanels[i].getComponents()[1];
            JRadioButton jR2 = (JRadioButton) arrayOfGeneratedQuestionJPanels[i].getComponents()[2];
            JRadioButton jR3 = (JRadioButton) arrayOfGeneratedQuestionJPanels[i].getComponents()[3];
            JRadioButton jR4 = (JRadioButton) arrayOfGeneratedQuestionJPanels[i].getComponents()[4];
            sq = new SubmittedQuestion(test.getQuestionList().get(i).getQuestion(),
                    jR1.isSelected() ? jR1.getText()
                    : jR2.isSelected() ? jR2.getText()
                    : jR3.isSelected() ? jR3.getText()
                    : jR4.isSelected() ? jR4.getText()
                    : "[did not answer]",
                    test.getQuestionList().get(i).getCorrectAnswer());
            sTest.getSqList().add(sq);
        }
    }

    private void composeSubmittedTask(SubmittedTest sTest, Test test) {
        sTest.setStList(new ArrayList<>());
        SubmittedTask st;
        for (int i = 0; i < arrayOfGeneratedTaskJPanels.length; i++) {
            JTextArea jT = (JTextArea) arrayOfGeneratedTaskJPanels[i].getComponents()[1];
            st = new SubmittedTask(test.getTaskList().get(i).getTaskDescription(), jT.getText());
            sTest.getStList().add(st);
        }
    }
}
