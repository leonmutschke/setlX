package org.randoom.setlx.statements;

import org.randoom.setlx.exceptions.BacktrackException;
import org.randoom.setlx.exceptions.SetlException;
import org.randoom.setlx.exceptions.TermConversionException;
import org.randoom.setlx.expressions.Variable;
import org.randoom.setlx.types.SetlString;
import org.randoom.setlx.types.Term;
import org.randoom.setlx.types.Value;
import org.randoom.setlx.utilities.State;
import org.randoom.setlx.utilities.TermConverter;

import java.util.List;

/*
grammar rule:
statement
    : [...]
    | 'check' '{' block '}' ('afterBacktrack' '{' block '}')?
    ;

implemented here as:
                  =====                           =====
               mStatements                      mRecovery
*/

public class Check extends Statement {
    // functional character used in terms (MUST be class name starting with lower case letter!)
    private final static String  FUNCTIONAL_CHARACTER   = "^check";

    private final Block mStatements;
    private final Block mRecovery;

    public Check(final Block statements, final Block recovery) {
        mStatements = statements;
        mRecovery   = recovery;
    }

    @Override
    protected Value exec(final State state) throws SetlException {
        try {
            return mStatements.execute(state);
        } catch (final BacktrackException bte) {
            if (mRecovery != null) {
                return mRecovery.execute(state);
            } else {
                return null;
            }
        }
    }

    /* Gather all bound and unbound variables in this statement and its siblings
          - bound   means "assigned" in this expression
          - unbound means "not present in bound set when used"
          - used    means "present in bound set when used"
       Optimize sub-expressions during this process by calling optimizeAndCollectVariables()
       when adding variables from them.
    */
    @Override
    public void collectVariablesAndOptimize (
        final List<Variable> boundVariables,
        final List<Variable> unboundVariables,
        final List<Variable> usedVariables
    ) {
        mStatements.collectVariablesAndOptimize(boundVariables, unboundVariables, usedVariables);

        // bindings inside the recovery block are not always valid --- ignore them
        final int preBound = boundVariables.size();
        if (mRecovery != null) {
            mRecovery.collectVariablesAndOptimize(boundVariables, unboundVariables, usedVariables);
        }
        while (boundVariables.size() > preBound) {
            boundVariables.remove(boundVariables.size() - 1);
        }
    }

    /* string operations */

    @Override
    public void appendString(final State state, final StringBuilder sb, final int tabs) {
        state.getLineStart(sb, tabs);
        sb.append("check ");
        mStatements.appendString(state,sb, tabs, true);
        if (mRecovery != null) {
            sb.append(" afterBacktrack ");
            mRecovery.appendString(state, sb, tabs, true);
        }
    }

    /* term operations */

    @Override
    public Term toTerm(final State state) {
        final Term result = new Term(FUNCTIONAL_CHARACTER, 2);
        result.addMember(state, mStatements.toTerm(state));
        if (mRecovery != null) {
            result.addMember(state, mRecovery.toTerm(state));
        } else {
            result.addMember(state, new SetlString("nil"));
        }
        return result;
    }

    public static Check termToStatement(final Term term) throws TermConversionException {
        if (term.size() != 2) {
            throw new TermConversionException("malformed " + FUNCTIONAL_CHARACTER);
        } else {
            final Block block    = TermConverter.valueToBlock(term.firstMember());
                  Block recovery = null;
            if ( ! term.lastMember().equals(new SetlString("nil"))) {
                recovery = TermConverter.valueToBlock(term.lastMember());
            }
            return new Check(block, recovery);
        }
    }
}

