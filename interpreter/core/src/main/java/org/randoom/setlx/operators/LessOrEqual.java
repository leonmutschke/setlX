package org.randoom.setlx.operators;

import org.randoom.setlx.exceptions.SetlException;
import org.randoom.setlx.operatorUtilities.Stack;
import org.randoom.setlx.types.SetlBoolean;
import org.randoom.setlx.types.Value;
import org.randoom.setlx.utilities.State;

/**
 * Operator that checks if the second values on the stack is less then the first or equal to it.
 */
public class LessOrEqual extends ABinaryInfixOperator {
    @Override
    public Value evaluate(State state, Stack<Value> values) throws SetlException {
        Value rhs = values.poll();
        Value lhs = values.poll();
        try {
            return SetlBoolean.valueOf(lhs.isEqualTo(state, rhs) == SetlBoolean.TRUE || lhs.isLessThan(state, rhs) == SetlBoolean.TRUE);
        } catch (final SetlException se) {
            se.addToTrace("Error in substitute comparison \"(" + lhs.toString(state) + " == " + rhs.toString(state) + ") || (" + lhs.toString(state) + " < " + rhs.toString(state) +  ")\":");
            throw se;
        }
    }

    @Override
    public void appendOperatorSign(State state, StringBuilder sb) {
        sb.append(" <= ");
    }

    @Override
    public boolean isLeftAssociative() {
        return true;
    }

    @Override
    public boolean isRightAssociative() {
        return false;
    }

    @Override
    public int precedence() {
        return 1500;
    }

    private final static long COMPARE_TO_ORDER_CONSTANT = generateCompareToOrderConstant(LessOrEqual.class);

    @Override
    public long compareToOrdering() {
        return COMPARE_TO_ORDER_CONSTANT;
    }

    @Override
    public int computeHashCode() {
        return (int) COMPARE_TO_ORDER_CONSTANT;
    }
}
