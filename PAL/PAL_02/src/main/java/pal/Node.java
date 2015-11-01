package pal;

import java.util.Arrays;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class Node {

    public static final int BEFORE = -1, EQUAL = 0, AFTER = 1;
    private final int name, weight, N;
    private final Node[] nasl;
    private final Node[] pred;
    private int naslCounter = 0, predCounter = 0;
    private final boolean[] hasNasl, hasPred;
    private boolean naslInited = false, predInited = false;

    public Node(int name, int weight, int N) {
        this.name = name;
        this.weight = weight;
        this.N = N;
        this.nasl = new Node[N];
        this.pred = new Node[N];
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
            nasl[naslCounter++] = n;
            hasNasl[n.getName()] = true;
        }
    }

    public void initNasl() {
        if (!naslInited) {
            Node[] toAdd = new Node[N];
            int toAddCounter = 0;
            for (Node n : this.nasl) {
                if (n == null) {
                    break;
                }
                for (Node m : n.nasl) {
                    if (m == null) {
                        break;
                    }
                    if (!this.hasNasl(m)) {
                        toAdd[toAddCounter++] = m;
                        this.hasNasl[m.getName()] = true;
                    }
                }
            }
            for (int i = 0; i < toAddCounter; i++) {
                nasl[naslCounter++] = toAdd[i];
            }
            naslInited = true;
        }
    }

    public Node[] getNasl() {
        return Arrays.copyOfRange(nasl, 0, naslCounter);
    }

    public boolean hasPred(Node n) {
        return hasPred[n.getName()];
    }

    public void addPred(Node n) {
        if (!hasPred(n)) {
            pred[predCounter++] = n;
            hasPred[n.getName()] = true;
        }
    }

    public void initPred() {
        if (!predInited) {
            Node[] toAdd = new Node[N];
            int toAddCounter = 0;
            for (Node n : this.pred) {
                if (n == null) {
                    break;
                }
                n.initPred();
                for (Node m : n.pred) {
                    if (m == null) {
                        break;
                    }
                    if (!this.hasPred(m)) {
                        toAdd[toAddCounter++] = m;
                        this.hasPred[m.getName()] = true;
                    }
                }
            }
            for (int i = 0; i < toAddCounter; i++) {
                pred[predCounter++] = toAdd[i];
            }
            predInited = true;
        }
    }

    public boolean emptyPred() {
        return this.predCounter == 0;
    }
}
