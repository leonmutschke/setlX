package org.randoom.setlx.operators;

import org.randoom.setlx.exceptions.SetlException;
import org.randoom.setlx.operatorUtilities.Stack;
import org.randoom.setlx.types.Om;
import org.randoom.setlx.types.Value;
import org.randoom.setlx.utilities.CodeFragment;
import org.randoom.setlx.utilities.State;

/**
 * Operator that computes the product of the members of the next value from the stack.
 */
public class ProductOfMembers extends AUnaryPrefixOperator {

    @Override
    public Value evaluate(State state, Stack<Value> values) throws SetlException {
        return values.poll().productOfMembers(state, Om.OM);
    }

    @Override
    public void appendOperatorSign(State state, StringBuilder sb) {
        sb.append("*/");
    }

    @Override
    public boolean isLeftAssociative() {
        return false;
    }

    @Override
    public boolean isRightAssociative() {
        return false;
    }

    @Override
    public int precedence() {
        return 1900;
    }

    private final static long COMPARE_TO_ORDER_CONSTANT = generateCompareToOrderConstant(ProductOfMembers.class);

    @Override
    public int compareTo(CodeFragment other) {
        if (this == other || other.getClass() == ProductOfMembers.class) {
            return 0;
        } else {
            return (this.compareToOrdering() < other.compareToOrdering())? -1 : 1;
        }
    }

    @Override
    public long compareToOrdering() {
        return COMPARE_TO_ORDER_CONSTANT;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj.getClass() == ProductOfMembers.class;
    }

    @Override
    public int computeHashCode() {
        return (int) COMPARE_TO_ORDER_CONSTANT;
    }
}
