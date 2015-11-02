package pal;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class Node {

    public static final int BEFORE = -1, EQUAL = 0, AFTER = 1;
    private final int name, weight;
    private final Node[] nasl;
    private final Node[] pred;
    private int naslCounter = 0, predCounter = 0;
    private final boolean[][] hasNasl;
    private boolean naslInited = false, predInited = false;

    public Node(int name, int weight, int N, boolean[][] hasNasl) {
        this.name = name;
        this.weight = weight;
        this.nasl = new Node[N];
        this.pred = new Node[N];
        this.hasNasl = hasNasl;
        //this.hasNasl = new boolean[N][N];
        //this.hasPred = new boolean[N][N];
    }

    public int getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public boolean hasNasl(Node n) {
        return hasNasl[this.getName()][n.getName()];
    }

    public void addNasl(Node n) {
        nasl[naslCounter++] = n;
        hasNasl[this.getName()][n.getName()] = true;
        n.pred[n.predCounter++] = this;
    }

    public void initNasl(Node[] tmpArray) {
        if (!naslInited) {
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
                        tmpArray[toAddCounter++] = m;
                        this.hasNasl[this.getName()][m.getName()] = true;
                    }
                }
            }
            for (int i = 0; i < toAddCounter; i++) {
                nasl[naslCounter++] = tmpArray[i];
                tmpArray[i].pred[tmpArray[i].predCounter++] = this;
            }
            naslInited = true;
        }
    }

    public int getNaslCount() {
        return naslCounter;
    }

    public Node[] getNasl() {
        return nasl;
    }

    public boolean hasPred(Node n) {
        return n.hasNasl(this);
    }

//    public void addPred(Node n) {
//        pred[predCounter++] = n;
//        hasPred[n.getName()][this.getName()] = true;
//    }
//
//    public void initPred(Node[] tmpArray) {
//        if (!predInited) {
//            int toAddCounter = 0;
//            for (Node n : this.pred) {
//                if (n == null) {
//                    break;
//                }
//                for (Node m : n.pred) {
//                    if (m == null) {
//                        break;
//                    }
//                    if (!this.hasPred(m)) {
//                        tmpArray[toAddCounter++] = m;
//                        this.hasPred[m.getName()][this.getName()] = true;
//                    }
//                }
//            }
//            for (int i = 0; i < toAddCounter; i++) {
//                pred[predCounter++] = tmpArray[i];
//            }
//            predInited = true;
//        }
//    }
    public int getPredCount() {
        return predCounter;
    }
    
    public Node[] getPred() {
        return pred;
    }

    public boolean predIsEmpty() {
        return this.predCounter == 0;
    }
}
