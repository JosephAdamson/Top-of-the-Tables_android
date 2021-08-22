package com.joeadamson.topofthetables.trainers;

import java.util.Locale;

/**
 * Computes subtraction problems for user to solve.
 *
 * @author Joseph Adamson
 */
public class TrainerSubtraction extends TrainerModel {

    @Override
    protected void generateExpression() {
        operandA = numberGenerator.nextInt(100 - 1 + 1) + 1;
        operandB = numberGenerator.nextInt(operandA - 1 + 1) + 1;
        answer = operandA - operandB;
    }

    @Override
    public String getExpression() {
        return String.format(Locale.getDefault(),
                "%d - %d", operandA, operandB);
    }
}
