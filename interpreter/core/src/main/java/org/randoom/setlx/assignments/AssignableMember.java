package org.randoom.setlx.assignments;

import org.randoom.setlx.exceptions.IncompatibleTypeException;
import org.randoom.setlx.exceptions.SetlException;
import org.randoom.setlx.exceptions.UndefinedOperationException;
import org.randoom.setlx.operators.Variable;
import org.randoom.setlx.types.Term;
import org.randoom.setlx.types.Value;
import org.randoom.setlx.utilities.CodeFragment;
import org.randoom.setlx.utilities.Expression;
import org.randoom.setlx.utilities.State;
import org.randoom.setlx.utilities.VariableScope;

import java.util.List;

/**
 * Assignment to the member of an object.
 */
public class AssignableMember extends AAssignableExpression {
    private final static String FUNCTIONAL_CHARACTER = generateFunctionalCharacter(AssignableMember.class);

    private final Expression lhs;      // left hand side (Variable, Expr, CollectionAccess, etc)
    private final Variable   member;   // member to access
    private final String     memberID; // member to access

    /**
     * Create new AssignableMember expression.
     *
     * @param lhs    Left hand side (Variable, Expr, CollectionAccess, etc)
     * @param member Member to access.
     */
    public AssignableMember(final Expression lhs, final Variable member) {
        this.lhs      = unify(lhs);
        this.member   = unify(member);
        this.memberID = this.member.getId();
    }

    @Override
    public boolean collectVariablesAndOptimize(State state, List<String> boundVariables, List<String> unboundVariables, List<String> usedVariables) {
        return lhs.collectVariablesAndOptimize(state, boundVariables, unboundVariables, usedVariables);
    }

    @Override
    public boolean collectVariablesWhenAssigned(State state, List<String> boundVariables, List<String> unboundVariables, List<String> usedVariables) {
        // lhs is read, not bound, so use collectVariablesAndOptimize()
        return lhs.collectVariablesAndOptimize(state, boundVariables, unboundVariables, usedVariables);
    }

    @Override
    public Value evaluate(State state) throws SetlException {
        final Value lhs = this.lhs.evaluate(state);
        try {
            return lhs.getObjectMemberUnCloned(state, memberID);
        } catch (final SetlException se) {
            final StringBuilder error = new StringBuilder();
            error.append("Error in \"");
            lhs.appendString(state, error, 0);
            error.append(".");
            error.append(memberID);
            error.append("\":");
            se.addToTrace(error.toString());
            throw se;
        }
    }

    @Override
    public void assignUncloned(State state, Value value, String context) throws SetlException {
        if (this.lhs instanceof AAssignableExpression) {
            final Value lhs = this.lhs.evaluate(state);
            lhs.setObjectMember(state, memberID, value, context);
        } else {
            throw new IncompatibleTypeException(
                    "Left-hand-side of \"" + this.toString(state) + " := " + value.toString(state) + "\" is unusable for member assignment."
            );
        }
    }

    @Override
    public boolean assignUnclonedCheckUpTo(State state, Value value, VariableScope outerScope, boolean checkObjects, String context) throws SetlException {
        throw new UndefinedOperationException(
                "Error in \"" + this + "\":" + state.getEndl() +
                        "This expression can not be used as target for this kind of assignments."
        );
    }

    @Override
    public void appendString(State state, StringBuilder sb, int tabs) {
        lhs.appendString(state, sb, tabs);
        sb.append(".");
        sb.append(memberID);
    }

    @Override
    public Value toTerm(State state) throws SetlException {
        final Term result = new Term(FUNCTIONAL_CHARACTER, 2);
        result.addMember(state, lhs.toTerm(state));
        result.addMember(state, member.toTerm(state));
        return result;
    }

    private final static long COMPARE_TO_ORDER_CONSTANT = generateCompareToOrderConstant(AssignableMember.class);

    @Override
    public int compareTo(CodeFragment other) {
        if (this == other) {
            return 0;
        } else if (other.getClass() == AssignableMember.class) {
            AssignableMember otr = (AssignableMember) other;
            int cmp = memberID.compareTo(otr.memberID);
            if (cmp != 0) {
                return cmp;
            }
            return lhs.compareTo(otr.lhs);
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
        if (this == obj) {
            return true;
        } else if (obj.getClass() == AssignableMember.class) {
            AssignableMember other = (AssignableMember) obj;
            return memberID.equals(other.memberID) && lhs.equals(other.lhs);
        }
        return false;
    }

    @Override
    public int computeHashCode() {
        int hash = ((int) COMPARE_TO_ORDER_CONSTANT) + lhs.hashCode();
        return hash * 31 + memberID.hashCode();
    }
}
