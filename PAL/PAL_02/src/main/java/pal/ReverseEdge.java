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

        Set<Node> emptySet = new HashSet<>(1);
        for (Node n : nodes) {
            n.initAllNaslPred(emptySet);
        }
        Main.printTimeDuration("Topol order");

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

    private int getCWeight(Edge e) {
        Set<Node> startNasl = e.getStart().getNasl();
        Set<Node> targetPred = e.getTarget().getPred();
        Set<Node> c = new HashSet<>(2 * N);
        c.addAll(startNasl);
        c.retainAll(targetPred);
        int weight = 0;
        if (!c.isEmpty()) {
            for (Node n : c) {
                weight += n.getWeight();
            }
            weight += e.getStart().getWeight() + e.getTarget().getWeight();
        }
        return weight;
    }

}
