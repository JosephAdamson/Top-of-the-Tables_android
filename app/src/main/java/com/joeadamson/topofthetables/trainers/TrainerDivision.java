package com.joeadamson.topofthetables.trainers;

import java.util.Locale;

/**
 * Computes division problems for user to solve.
 *
 * @author Joseph Adamson
 */
public class TrainerDivision extends TrainerModel {

    @Override
    protected void generateExpression() {
        answer = numberGenerator.nextInt(12 - 1 + 1) + 1;
        operandB = numberGenerator.nextInt(12 - 1 + 1) + 1;
        operandA = answer * operandB;
    }

    @Override
    public String getExpression() {
        return String.format(Locale.getDefault(), "%d %s %d",
                operandA, Character.toString((char) 247), operandB);
    }
}
