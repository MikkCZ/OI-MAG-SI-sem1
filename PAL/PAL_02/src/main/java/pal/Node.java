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
    private final int name, weight;
    private final Set<Node> NASL, PRED;

    public Node(int name, int weight, int total) {
        this.name = name;
        this.weight = weight;
        this.NASL = new HashSet<>(2 * total);
        this.PRED = new HashSet<>(2 * total);
    }
    
    public int getWeight() {
        return weight;
    }

    public boolean addNASL(Node n) {
        return NASL.add(n);
    }

    public Set<Node> getNasl() {
        return NASL;
    }

    public boolean addPRED(Node n) {
        return PRED.add(n);
    }
    
    public Set<Node> getPred() {
        return PRED;
    }

    public int predNum() {
        return PRED.size();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + this.name;
        hash = 47 * hash + this.weight;
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

    public static Comparator<Node> getTopolComparator() {
        return new TopolComparator<>();
    }

    private static class TopolComparator<T extends Node> implements Comparator<T> {

        @Override
        public int compare(T arg0, T arg1) {
            if (arg0 == arg1) {
                return EQUAL;
            }
            if (arg0.getNasl().contains(arg1)) {
                return BEFORE;
            } else if (arg1.getNasl().contains(arg0)) {
                return AFTER;
            } else {
                return EQUAL;
            }
        }

    }

}
