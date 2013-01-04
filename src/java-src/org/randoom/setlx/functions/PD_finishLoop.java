package org.randoom.setlx.functions;

import org.randoom.setlx.types.Om;
import org.randoom.setlx.types.Value;
import org.randoom.setlx.utilities.DebugPrompt;
import org.randoom.setlx.utilities.State;

import java.util.List;

// finishLoop()                  : DEBUG: continue execution of current loop until it finishes

public class PD_finishLoop extends PreDefinedFunction {
    public final static PreDefinedFunction DEFINITION = new PD_finishLoop();

    private PD_finishLoop() {
        super("finishLoop");
    }

    @Override
    public Value execute(final State state, final List<Value> args, final List<Value> writeBackVars) {
        state.setDebugFinishLoop(true);
        state.setDebugModeActive(false);
        DebugPrompt.stopPrompt();
        return Om.OM.hide();
    }
}

