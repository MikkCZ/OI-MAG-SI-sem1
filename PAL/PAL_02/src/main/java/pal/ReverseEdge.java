package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReverseEdge implements Task {

    private int N, M;
    private Node[] nodes;
    private List<Edge> edges;
    private Map<Edge, Set<Node>> C;

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
            int weight = Integer.parseInt(line.trim());
            nodes[i] = new Node(i, weight, N);
        }

        edges = new ArrayList<>(M);
        for (int i = 0; i < M; i++) {
            line = br.readLine();
            int start = Integer.parseInt(line.split(" ")[0]);
            int target = Integer.parseInt(line.split(" ")[1]);
            edges.add(new Edge(nodes[start], nodes[target]));
        }
        Main.printTimeDuration("Reading input");

        createTopol();
        Main.printTimeDuration("Topol order");

        int max = -1;
        List<Edge> maxEdges = new ArrayList<>(M);
        for (Edge e : edges) {
            int weight = getCWeight(e);
            if (weight == max) {
                maxEdges.add(e);
            } else if (weight > max) {
                max = weight;
                maxEdges.clear();
                maxEdges.add(e);
            }
        }
        Main.printTimeDuration("C sets");

        return maxEdges.get(0).toString() + " " + max;
    }

    private List<Node> createTopol() {
        for (Edge e : edges) {
            Node start = e.getStart();
            Node target = e.getTarget();
            start.addNASL(target);
            target.addPRED(start);
        }
        for (Node n : nodes) {
            if (n.predNum() == 0) {
                dfs(n, new HashSet<Node>(1));
            }
        }
        List<Node> topolNodes = new ArrayList<>(N);
        for (Node n : nodes) {
            topolNodes.add(n);
        }
        Collections.sort(topolNodes, Node.getTopolComparator());
        return topolNodes;
    }

    private Set<Node> dfs(Node n, Set<Node> parentPred) {
        Set<Node> pred = n.getPred();
        pred.addAll(parentPred);

        Set<Node> nasl = n.getNasl();
        Set<Node> childNasl = new HashSet<>(2 * N);
        for (Node child : nasl) {
            childNasl.addAll(dfs(child, pred));
        }
        nasl.addAll(childNasl);
        return nasl;
    }

    private int getCWeight(Edge e) {
        Set<Node> startNasl = e.getStart().getNasl();
        Set<Node> targetPred = e.getTarget().getPred();
        Set<Node> c = new HashSet<>(2 * N);
        c.addAll(startNasl);
        c.retainAll(targetPred);
        int weight = 0;
        for (Node n : c) {
            weight += n.getWeight();
        }
        if (weight > 0) {
            weight += e.getStart().getWeight() + e.getTarget().getWeight();
        }
        return weight;
    }

}
