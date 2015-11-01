package pal;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class Node {

    public static final int BEFORE = -1, EQUAL = 0, AFTER = 1;
    private final int name, weight, N;
    private final Set<Node> NASL, PRED;
    private boolean naslInited = false;

    public Node(int name, int weight, int N) {
        this.name = name;
        this.weight = weight;
        this.N = N;
        this.NASL = new HashSet<>(2 * N);
        this.PRED = new HashSet<>(2 * N);
    }

    public int getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public void initAllNaslPred(Set<Node> parentPred) {
        PRED.addAll(parentPred);
        if (!naslInited && !NASL.isEmpty()) {
            Set<Node> newNasl = new HashSet<>(2 * N);
            for (Node n : NASL) {
                n.initAllNaslPred(PRED);
                newNasl.addAll(n.NASL);
            }
            NASL.addAll(newNasl);
        }
        naslInited = true;
    }

    public Set<Node> getNasl() {
        return NASL;
    }

    public Set<Node> getPred() {
        return PRED;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this.name;
        hash = 83 * hash + this.weight;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (this.name != other.name) {
            return false;
        }
        if (this.weight != other.weight) {
            return false;
        }
        return true;
    }

    public static Comparator<Node> getTopolComparator(int nodes) {
        return new TopolComparator<>(nodes);
    }

    private static class TopolComparator<T extends Node> implements Comparator<T> {

        private final boolean isNasl[][];

        public TopolComparator(int nodes) {
            isNasl = new boolean[nodes][nodes];
        }

        @Override
        public int compare(T arg0, T arg1) {
            if (arg0 == arg1) {
                return EQUAL;
            }
            if (isNasl[arg0.getName()][arg1.getName()]) {
                return BEFORE;
            } else if (isNasl[arg1.getName()][arg0.getName()]) {
                return AFTER;
            }
            if (arg0.getNasl().contains(arg1)) {
                isNasl[arg0.getName()][arg1.getName()] = true;
                return BEFORE;
            } else if (arg1.getNasl().contains(arg0)) {
                isNasl[arg1.getName()][arg0.getName()] = true;
                return AFTER;
            } else {
                return EQUAL;
            }
        }

    }

}
