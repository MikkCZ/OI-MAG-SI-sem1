package pal;

import java.util.Comparator;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class Edge {

    public static final int BEFORE = -1, EQUAL = 0, AFTER = 1;
    private final Node start, target;

    public Edge(Node start, Node target) {
        this.start = start;
        this.target = target;
        this.start.addNasl(target);
        this.target.addPred(start);
    }

    public Node getStart() {
        return start;
    }

    public Node getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return String.format("%1$d %2$d", start.getName(), target.getName());
    }

    public static Comparator<Edge> getLexicographicalComparator() {
        return new LexicographicalEdgeComparator<>();
    }

    private static class LexicographicalEdgeComparator<T extends Edge> implements Comparator<T> {

        @Override
        public int compare(T arg0, T arg1) {
            if (arg0 == arg1) {
                return EQUAL;
            }
            if (arg0.getStart().getName() < arg1.getStart().getName()) {
                return BEFORE;
            } else if (arg0.getStart().getName() > arg1.getStart().getName()) {
                return AFTER;
            }
            if (arg0.getTarget().getName() < arg1.getTarget().getName()) {
                return BEFORE;
            } else if (arg0.getTarget().getName() > arg1.getTarget().getName()) {
                return AFTER;
            }
            return EQUAL;
        }

    }

}
