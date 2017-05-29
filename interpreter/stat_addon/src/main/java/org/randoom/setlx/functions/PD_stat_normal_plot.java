package org.randoom.setlx.functions;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.randoom.setlx.exceptions.SetlException;
import org.randoom.setlx.parameters.ParameterDefinition;
import org.randoom.setlx.plot.types.Canvas;
import org.randoom.setlx.plot.utilities.ConnectJFreeChart;
import org.randoom.setlx.types.SetlBoolean;
import org.randoom.setlx.types.SetlList;
import org.randoom.setlx.types.SetlString;
import org.randoom.setlx.types.Value;
import org.randoom.setlx.utilities.Checker;
import org.randoom.setlx.utilities.Defaults;
import org.randoom.setlx.utilities.State;

import java.util.*;

/**
 * stat_normal_plot(mu, sigma, canvas):
 *                  Plots the probability density function for normal distributions with given mean 'mu' and standard deviation 'sigma' on a given canvas.
 */
public class PD_stat_normal_plot extends PreDefinedProcedure {

    private final static ParameterDefinition MU          = createParameter("mu");
    private final static ParameterDefinition SIGMA       = createParameter("sigma");
    private final static ParameterDefinition CANVAS      = createParameter("canvas");
    private final static ParameterDefinition COLOR       = createOptionalParameter("color", new SetlString("DEFAULT_COLOR"));
    private final static ParameterDefinition LOWER_BOUND = createOptionalParameter("lowerBound", Defaults.createSetlDoubleValue(-5.0));
    private final static ParameterDefinition INTERVAL    = createOptionalParameter("interval", Defaults.getDefaultPlotInterval());
    private final static ParameterDefinition UPPER_BOUND = createOptionalParameter("upperBound", Defaults.createSetlDoubleValue(5.0));

    /** Definition of the PreDefinedProcedure 'stat_normal_plot' */
    public final static PreDefinedProcedure DEFINITION = new PD_stat_normal_plot();

    private PD_stat_normal_plot() {
        super();
        addParameter(MU);
        addParameter(SIGMA);
        addParameter(CANVAS);
        addParameter(COLOR);
        addParameter(LOWER_BOUND);
        addParameter(INTERVAL);
        addParameter(UPPER_BOUND);
    }

    @Override
    public Value execute(State state, HashMap<ParameterDefinition, Value> args) throws SetlException {
        final Value mu         = args.get(MU);
        final Value sigma      = args.get(SIGMA);
        final Value canvas     = args.get(CANVAS);
        final Value color      = args.get(COLOR);
        final Value lowerBound = args.get(LOWER_BOUND);
        final Value interval   = args.get(INTERVAL);
        final Value upperBound = args.get(UPPER_BOUND);

        Checker.checkIfNumber(state, mu, lowerBound, upperBound);
        Checker.checkIfUpperBoundGreaterThanLowerBound(state, lowerBound, upperBound);
        Checker.checkIfNumberAndGreaterZero(state, sigma, interval);
        Checker.checkIfCanvas(state, canvas);

        List<Integer> colorScheme = new ArrayList<>();

        if (color.isString() == SetlBoolean.TRUE && color.toString().equals("DEFAULT_COLOR")) {
            colorScheme = Defaults.DEFAULT_COLOR_SCHEME;
        } else {
            for (Iterator<Value> value = ((SetlList) color).iterator(); value.hasNext();) {
                colorScheme.add(value.next().toJIntValue(state));
            }
        }

        NormalDistribution nd = new NormalDistribution(mu.toJDoubleValue(state), sigma.toJDoubleValue(state));

        /** The valueList is the list of every pair of coordinates [x,y] that the graph consists of.
         *  It is filled by iteratively increasing the variable 'counter' (x), and calculating the density for every new value of 'counter' (y).
         */
        List<List<Double>> valueList = new ArrayList<>();
        for (double counter = lowerBound.toJDoubleValue(state); counter < upperBound.toJDoubleValue(state); counter += interval.toJDoubleValue(state)) {
            valueList.add(new ArrayList<Double>(Arrays.asList(counter, nd.density(counter))));
        }

        return ConnectJFreeChart.getInstance().addListGraph((Canvas) canvas, valueList, "Probability Density Function (mean: " + mu.toString() + ", standard deviation: " + sigma.toString(), colorScheme, false);
    }
}
