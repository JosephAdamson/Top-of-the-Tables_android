package com.joeadamson.topofthetables.trainers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Data model for all trainers used in the app.
 */
public abstract class TrainerModel {

    private final ArrayList<Integer> optionNumbers =
            new ArrayList<>(Arrays.asList(0, 0, 0, 0));
    protected final Random numberGenerator = new Random();
    protected int operandA;
    protected int operandB;
    protected int answer;
    protected int score;

    public TrainerModel() {
        loadTrainer();
    }

    /**
     * Generates new expression and loads options array accordingly.
     */
    public void loadTrainer() {
        generateExpression();
        loadOptions();
    }

    /**
     * Generates expression, constants and answer and loads them
     * into options array. This operation will be the same across all
     * sub-classes.
     */
    protected void loadOptions() {

        // Make sure wrong answers are in a reasonable range relative
        // to the actual answer whilst excluding the actual answer.
        for (int i = 0; i < optionNumbers.size(); i++) {
            int temp = numberGenerator.nextInt((answer + 50) - 1 + 1) + 1;

            // In the case were we get a duplicate of the answer we offset
            // the duplicate with another random number in the range [0,20]
            int val =
                    answer == temp ? temp + numberGenerator.nextInt(20 - 1 + 1) + 1 : temp;
            optionNumbers.set(i, val);
        }

        int answerPos = numberGenerator.nextInt(4);
        optionNumbers.set(answerPos, answer);
    }

    /**
     * Implemented by sub classes; instantiates operandA and operandB
     * and solves with stated operator to get answer.
     */
    protected abstract void generateExpression();

    /**
     * To be overridden by sub classes with appropriate expression.
     *
     * @return "operandA operator operandB"
     */
    public String getExpression() {
        return "";
    }

    /**
     * @return option numbers.
     */
    public ArrayList<Integer> getOptionNumbers() {
        return optionNumbers;
    }

    /**
     * @return left operand in the expression (A * B).
     */
    public int getOperandA() {
        return operandA;
    }

    /**
     * @return right operand in the expression (A * B).
     */
    public int getOperandB() {
        return operandB;
    }

    /**
     * @return answer for the expression A * B.
     */
    public int getAnswer() {
        return answer;
    }

    /**
     * @return current user score for a session.
     */
    public int getScore() {
        return score;
    }

    /**
     * Update current score
     *
     * @param score new score
     */
    public void setScore(int score) {
        this.score = score;
    }
}
