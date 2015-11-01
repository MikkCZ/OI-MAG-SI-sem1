package pal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class Node {

    public static final int BEFORE = -1, EQUAL = 0, AFTER = 1;
    private final int name, weight, N;
    private final List<Node> nasl, pred;
    private final boolean[] hasNasl, hasPred;
    private boolean naslInited = false, predInited = false;

    public Node(int name, int weight, int N) {
        this.name = name;
        this.weight = weight;
        this.N = N;
        this.nasl = new ArrayList<>(N);
        this.pred = new ArrayList<>(N);
        this.hasNasl = new boolean[N];
        this.hasPred = new boolean[N];
    }

    public int getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public boolean hasNasl(Node n) {
        return hasNasl[n.getName()];
    }

    public void addNasl(Node n) {
        if (!hasNasl(n)) {
            nasl.add(n);
            hasNasl[n.getName()] = true;
        }
    }

    public void initNasl() {
        if (!naslInited) {
            List<Node> toAdd = new ArrayList<>(N);
            for (Node n : this.nasl) {
                n.initNasl();
                for (Node m : n.getNasl()) {
                    if (!this.hasNasl(m)) {
                        toAdd.add(m);
                        this.hasNasl[m.getName()] = true;
                    }
                }
            }
            this.nasl.addAll(toAdd);
            naslInited = true;
        }
    }

    public List<Node> getNasl() {
        return nasl;
    }

    public boolean hasPred(Node n) {
        return hasPred[n.getName()];
    }

    public void addPred(Node n) {
        if (!hasPred(n)) {
            pred.add(n);
            hasPred[n.getName()] = true;
        }
    }

    public void initPred() {
        if (!predInited) {
            List<Node> toAdd = new ArrayList<>(N);
            for (Node n : this.pred) {
                n.initPred();
                for (Node m : n.getPred()) {
                    if (!this.hasPred(m)) {
                        toAdd.add(m);
                        this.hasPred[m.getName()] = true;
                    }
                }
            }
            this.pred.addAll(toAdd);
            predInited = true;
        }
    }

    public List<Node> getPred() {
        return pred;
    }

    @Override
    public String toString() {
        return "Node{" + "name=" + name + '}';
    }

    public static Comparator<Node> getTopolNaslComparator() {
        return new TopolNaslComparator<>();
    }

    private static class TopolNaslComparator<T extends Node> implements Comparator<T> {

        @Override
        public int compare(T arg0, T arg1) {
            if (arg0.hasNasl(arg1)) {
                return BEFORE;
            } else if (arg1.hasNasl(arg0)) {
                return AFTER;
            } else {
                return EQUAL;
            }
        }

    }
}
