package org.randoom.setlx.operators;

import org.randoom.setlx.exceptions.SetlException;
import org.randoom.setlx.operatorUtilities.OperatorExpression.OptimizerData;
import org.randoom.setlx.operatorUtilities.Stack;
import org.randoom.setlx.types.Term;
import org.randoom.setlx.types.Value;
import org.randoom.setlx.utilities.State;

import java.util.List;

/**
 * Base class for operators that take no arguments.
 * They are in fact not really operators.
 */
public abstract class AZeroOperator extends AOperator {

    @Override
    public OptimizerData collectVariables(State state, List<String> boundVariables, List<String> unboundVariables, List<String> usedVariables, Stack<OptimizerData> optimizerData) {
        boolean allowOptimization = collectVariablesAndOptimize(state, boundVariables, unboundVariables, usedVariables);
        return new OptimizerData(allowOptimization);
    }

    @Override
    public boolean hasArgumentBeforeOperator() {
        return false;
    }

    @Override
    public boolean hasArgumentAfterOperator() {
        return false;
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
        return 9999;
    }

    @Override
    public final Value buildTerm(State state, Stack<Value> termFragments) throws SetlException {
        Term term = new Term(generateFunctionalCharacter(this.getClass()));
        return modifyTerm(state, term);
    }

    /**
     * Modify default term for this operator.
     *
     * @param state          Current state of the running setlX program.
     * @param term           Term to work with.
     * @return               Resulting term.
     * @throws SetlException Thrown in case of some (user-) error.
     */
    public Value modifyTerm(State state, Term term) throws SetlException {
        return term;
    }
}