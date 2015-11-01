package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReverseEdge implements Task {

    private int N, M;
    private Node[] nodes;
    private List<Edge> edges;

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

        edges = new ArrayList<>(M);
        for (int i = 0; i < M; i++) {
            line = br.readLine();
            edges.add(new Edge(
                    nodes[Integer.parseInt(line.split(" ")[0])],
                    nodes[Integer.parseInt(line.split(" ")[1])])
            );
        }
        Main.printTimeDuration("Reading input + graph init");

        List<Node> topolSortedNodes = topolSort();
        Main.printTimeDuration("Topol order");

        for (int i = 0; i < N; i++) {
            topolSortedNodes.get(i).initNasl();
        }
        for (int i = N - 1; i >= 0; i--) {
            topolSortedNodes.get(i).initPred();
        }
        Main.printTimeDuration("Init NASL and PRED");

        int weight;
        int max = -1;
        List<Edge> maxEdges = new ArrayList<>(M);
        for (Edge e : edges) {
            weight = getCWeight(e);
            if (weight == max) {
                maxEdges.add(e);
            } else if (weight > max) {
                max = weight;
                maxEdges.clear();
                maxEdges.add(e);
            }
        }
        Main.printTimeDuration("C sets");
        Collections.sort(maxEdges, Edge.getLexicographicalComparator());
        Main.printTimeDuration("C sets sort");

        return maxEdges.get(0).toString() + " " + max;
    }

    private List<Node> topolSort() {
        List<Node> roots = new ArrayList<>(N);
        final int FRESH = 0, OPENED = 1, CLOSED = 2;
        int[] nodeState = new int[N];
        for (int i = 0; i < N; i++) {
            nodeState[i] = FRESH;
        }
        for (Node n : nodes) {
            if (n.getPred().isEmpty()) {
                roots.add(n);
            }
        }
        List<Node> sorted = new ArrayList<>(N);
        for (Node root : roots) {
            visitNode(root, nodeState, sorted);
        }
        return sorted;
        //Arrays.sort(nodes, Node.getTopolNaslComparator());
    }

    private void visitNode(Node node, int[] nodeState, List<Node> sorted) {
        final int FRESH = 0, OPENED = 1, CLOSED = 2;
        nodeState[node.getName()] = OPENED;
        for (Node n : node.getNasl()) {
            if (nodeState[n.getName()] == FRESH) {
                visitNode(n, nodeState, sorted);
            } else if (nodeState[n.getName()] == OPENED) {
                throw new RuntimeException("This graph is not acyclic.");
            }
        }
        sorted.add(node);
        nodeState[node.getName()] = CLOSED;
    }

    private int getCWeight(Edge e) {
        List<Node> c = getIntersection(e.getStart(), e.getTarget());
        int weight = 0;
        for (Node n : c) {
            weight += n.getWeight();
        }
        if (weight > 0) {
            weight += e.getStart().getWeight() + e.getTarget().getWeight();
        }
        return weight;
    }

    private List<Node> getIntersection(Node parent, Node child) {
        List<Node> intersection = new ArrayList<>(N);
        for (Node n : parent.getNasl()) {
            if (child.hasPred(n)) {
                intersection.add(n);
            }
        }
        return intersection;
    }

}
