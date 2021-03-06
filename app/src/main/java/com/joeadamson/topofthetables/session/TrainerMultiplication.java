package com.joeadamson.topofthetables.session;

import java.util.Locale;

/**
 * Computes multiplication problems for user to solve.
 *
 * @author Joseph Adamson
 */
public class TrainerMultiplication extends TrainerModel {

    public TrainerMultiplication() {
        loadTrainer();
    }

    @Override
    protected void generateExpression() {
        operandA = numberGenerator.nextInt(12 - 1 + 1) + 1;
        operandB = numberGenerator.nextInt(12 - 1 + 1) + 1;
        answer = operandA * operandB;
    }

    @Override
    public String getExpression() {
        return String.format(Locale.getDefault(),
                "%d x %d", operandA, operandB);
    }
}
