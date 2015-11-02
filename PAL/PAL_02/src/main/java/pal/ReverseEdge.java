package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ReverseEdge implements Task {

    private static final int FRESH = 0, OPENED = 1, CLOSED = 2;
    private int N, M;
    private Node[] nodes;
    private Edge[] edges;
    private int topolSortedCounter = 0;
    private boolean[][] hasNasl, hasPred;

    @Override
    public String eval(final InputStream is) throws IOException {
        Main.printTimeDuration("Start");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = br.readLine();
        N = Integer.parseInt(line.split(" ")[0]);
        M = Integer.parseInt(line.split(" ")[1]);
        hasNasl = new boolean[N][N];
        hasPred = new boolean[N][N];

        nodes = new Node[N];
        for (int i = 0; i < N; i++) {
            line = br.readLine();
            nodes[i] = new Node(i, Integer.parseInt(line.trim()), N, hasNasl);
        }

        edges = new Edge[M];
        for (int i = 0; i < M; i++) {
            line = br.readLine();
            edges[i] = new Edge(
                    nodes[Integer.parseInt(line.split(" ")[0])],
                    nodes[Integer.parseInt(line.split(" ")[1])]
            );
        }
        br = null;
        line = null;
        Main.printTimeDuration("Reading input + graph init");

        Node[] tmpNodeArray = new Node[N];
        topolSort(tmpNodeArray);
        Main.printTimeDuration("Topol order");

        for (int i = 0; i < N; i++) {
            nodes[i].initNasl(tmpNodeArray);
        }
//        for (int i = N - 1; i >= 0; i--) {
//            nodes[i].initPred(tmpNodeArray);
//        }
        nodes = null;
        Main.printTimeDuration("Init NASL and PRED");

        int weight;
        int max = -1;
        Edge[] maxEdges = new Edge[M];
        int maxEdgesCounter = 0;
        for (Edge e : edges) {
            weight = getCWeight(e, tmpNodeArray);
            if (weight == max) {
                maxEdges[maxEdgesCounter++] = e;
            } else if (weight > max) {
                max = weight;
                maxEdgesCounter = 0;
                maxEdges[maxEdgesCounter++] = e;
            }
        }
        edges = null;
        tmpNodeArray = null;
        Main.printTimeDuration("C sets");
        Arrays.sort(maxEdges, 0, maxEdgesCounter - 1, Edge.getLexicographicalComparator());
        Main.printTimeDuration("C sets sort");

        return maxEdges[0].toString() + " " + max;
    }

    private void topolSort(Node[] tmpArray) {
        int rootsCounter = 0;
        int[] nodeState = new int[N];
//        for (int i = 0; i < N; i++) {
//            nodeState[i] = FRESH;
//        }
        for (Node n : nodes) {
            if (n.predIsEmpty()) {
                tmpArray[rootsCounter++] = n;
            }
        }
        for (int i = 0; i < rootsCounter; i++) {
            visitNode(tmpArray[i], nodeState);
        }
    }

    private void visitNode(Node node, int[] nodeState) {
//        nodeState[node.getName()] = OPENED;
        int nodeNaslCount = node.getNaslCount();
        Node[] nodeNasl = node.getNasl();
        for (int i = 0; i < nodeNaslCount; i++) {
            if (nodeState[nodeNasl[i].getName()] == FRESH) {
                visitNode(nodeNasl[i], nodeState);
//            } else if (nodeState[n.getName()] == OPENED) {
//                throw new RuntimeException("This graph is not acyclic.");
            }
        }
        nodes[topolSortedCounter++] = node;
        nodeState[node.getName()] = CLOSED;
    }

    private int getCWeight(Edge e, Node[] tmpArray) {
        int weight = 0;
        int intersectionCount = getIntersection(e.getStart(), e.getTarget(), tmpArray);
        for (int i = 0; i < intersectionCount; i++) {
            weight += tmpArray[i].getWeight();
        }
        if (weight > 0) {
            weight += e.getStart().getWeight() + e.getTarget().getWeight();
        }
        return weight;
    }

    private int getIntersection(Node parent, Node child, Node[] target) {
        int intersectionCounter = 0;
        int parentNaslCount = parent.getNaslCount();
        int childPredCount = child.getPredCount();
//        if (parentNaslCount < childPredCount) {
            Node[] parentNasl = parent.getNasl();
            for (int i = 0; i < parentNaslCount; i++) {
                if (child.hasPred(parentNasl[i])) {
                    target[intersectionCounter++] = parentNasl[i];
                }
            }

//        } else {
//            Node[] parentNasl = parent.getNasl();
//            for (int i = 0; i < parentNaslCount; i++) {
//                if (child.hasPred(parentNasl[i])) {
//                    target[intersectionCounter++] = parentNasl[i];
//                }
//            }
//        }
//        } else {
//            Node[] childPred = child.getPred();
//            for (int i = 0; i < childPredCount; i++) {
//                if (child.hasNasl(childPred[i])) {
//                    target[intersectionCounter++] = childPred[i];
//                }
//            }
//        }
        return intersectionCounter;
    }

}
