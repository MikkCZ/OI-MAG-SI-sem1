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

    @Override
    public String eval(final InputStream is) throws IOException {
        Main.printTimeDuration("Start");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = br.readLine();
        N = Integer.parseInt(line.split(" ")[0]);
        M = Integer.parseInt(line.split(" ")[1]);

        nodes = new Node[N];
        for (int i = 0; i < N; i++) {
            line = br.readLine();
            nodes[i] = new Node(i, Integer.parseInt(line.trim()), N);
        }

        edges = new Edge[M];
        for (int i = 0; i < M; i++) {
            line = br.readLine();
            edges[i] = new Edge(
                    nodes[Integer.parseInt(line.split(" ")[0])],
                    nodes[Integer.parseInt(line.split(" ")[1])]
            );
        }
        Main.printTimeDuration("Reading input + graph init");

        topolSort();
        Main.printTimeDuration("Topol order");

        for (int i = 0; i < N; i++) {
            nodes[i].initNasl();
        }
        for (int i = N - 1; i >= 0; i--) {
            nodes[i].initPred();
        }
        Main.printTimeDuration("Init NASL and PRED");

        int weight;
        int max = -1;
        Edge[] maxEdges = new Edge[N];
        int maxEdgesCounter = 0;
        for (Edge e : edges) {
            weight = getCWeight(e);
            if (weight == max) {
                maxEdges[maxEdgesCounter++] = e;
            } else if (weight > max) {
                max = weight;
                maxEdgesCounter = 0;
                maxEdges[maxEdgesCounter++] = e;
            }
        }
        Main.printTimeDuration("C sets");
        Arrays.sort(maxEdges, 0, maxEdgesCounter - 1, Edge.getLexicographicalComparator());
        Main.printTimeDuration("C sets sort");

        return maxEdges[0].toString() + " " + max;
    }

    private void topolSort() {
        Node[] roots = new Node[N];
        int rootsCounter = 0;
        int[] nodeState = new int[N];
//        for (int i = 0; i < N; i++) {
//            nodeState[i] = FRESH;
//        }
        for (Node n : nodes) {
            if (n.emptyPred()) {
                roots[rootsCounter++] = n;
            }
        }
        for (int i = 0; i < rootsCounter; i++) {
            visitNode(roots[i], nodeState);
        }
    }

    private void visitNode(Node node, int[] nodeState) {
//        nodeState[node.getName()] = OPENED;
        for (Node n : node.getNasl()) {
            if (nodeState[n.getName()] == FRESH) {
                visitNode(n, nodeState);
//            } else if (nodeState[n.getName()] == OPENED) {
//                throw new RuntimeException("This graph is not acyclic.");
            }
        }
        nodes[topolSortedCounter++] = node;
        nodeState[node.getName()] = CLOSED;
    }

    private int getCWeight(Edge e) {
        int weight = 0;
        for (Node n : getIntersection(e.getStart(), e.getTarget())) {
            if (n == null) {
                break;
            }
            weight += n.getWeight();
        }
        if (weight > 0) {
            weight += e.getStart().getWeight() + e.getTarget().getWeight();
        }
        return weight;
    }

    private Node[] getIntersection(Node parent, Node child) {
        Node[] intersection = new Node[N];
        int intersectionCounter = 0;
        for (Node n : parent.getNasl()) {
            if (child.hasPred(n)) {
                intersection[intersectionCounter++] = n;
            }
        }
        return intersection;
    }

}
