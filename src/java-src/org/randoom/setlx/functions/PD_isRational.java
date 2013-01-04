package org.randoom.setlx.functions;

import org.randoom.setlx.types.Value;
import org.randoom.setlx.utilities.State;

import java.util.List;

// isRational(value)       : test if value-type is rational

public class PD_isRational extends PreDefinedFunction {
    public final static PreDefinedFunction DEFINITION = new PD_isRational();

    private PD_isRational() {
        super("isRational");
        addParameter("value");
    }

    @Override
    public Value execute(final State state, final List<Value> args, final List<Value> writeBackVars) {
        return args.get(0).isRational();
    }
}

