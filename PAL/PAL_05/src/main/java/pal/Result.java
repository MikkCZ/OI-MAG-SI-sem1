package pal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class Result {

    private final State start;
    private final Set<State> outputs;
    private boolean ko;

    public Result(State start) {
        this.start = start;
        this.outputs = new HashSet<>(300);
        ko = false;
    }

    public boolean isOK(int F) {
        return !ko && (outputs.size() == F);
    }

    public int size() {
        return outputs.size();
    }

    public void ko() {
        ko = true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(start.toString());
        List<State> outputsList = new ArrayList<>(outputs);
        Collections.sort(outputsList);
        for (State output : outputsList) {
            sb.append(" ");
            sb.append(output.toString());
        }
        return sb.toString().trim();
    }

    public void addOutput(State output) {
        outputs.add(output);
    }

}
