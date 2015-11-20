package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class TreeIsomorphism implements Task {

    private int N;
    private Node[] nodesA, nodesB;
    private Set<Node> leavesA, leavesB;
    private int[] degreeCounterA, degreeCounterB;

    @Override
    public String eval(InputStream is) throws IOException {
        Main.printTimeDuration("Start");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = br.readLine();
        N = Integer.parseInt(line);

        nodesA = new Node[N];
        degreeCounterA = new int[N];
        leavesA = new HashSet<>(N);
        initGraph(nodesA, br);

        nodesB = new Node[N + 1];
        degreeCounterB = new int[N + 1];
        leavesB = new HashSet<>(N + 1);
        initGraph(nodesB, br);

        br = null;
        line = null;
        Main.printTimeDuration("Reading input + graphs init");

        int dg;
        for (Node n : nodesA) {
            dg = n.getDegree();
            if (dg == 1) {
                leavesA.add(n);
            }
            degreeCounterA[dg] = degreeCounterA[dg] + 1;
        }
        for (Node n : nodesB) {
            dg = n.getDegree();
            if (dg == 1) {
                leavesB.add(n);
            }
            degreeCounterB[dg] = degreeCounterB[dg] + 1;
        }
        int degreeOfBParent = countDegreeOfBParent();
        Main.printTimeDuration("Degrees");

        String certA = getCert(leavesA);
        Main.printTimeDuration("Certificate of A");

        List<Integer> solution = new ArrayList<>(N);
        for (Node n : leavesB) {
            if (n.getOnlyParent().getDegree() == degreeOfBParent) {
                boolean canBeCut = tryCut(n, certA);
                if (canBeCut) {
                    solution.add(n.getID());
                }
            }
        }
        Collections.sort(solution);
        Main.printTimeDuration("Try B cuts");

        StringBuilder sb = new StringBuilder();
        for (int id : solution) {
            sb.append(id).append(" ");
        }
        return sb.toString().trim();
    }

    private boolean tryCut(Node leafB, String certA) {
        Set<Node> otherLeaves = new HashSet<>(N);
        for (Node n : leavesB) {
            otherLeaves.add(n);
        }
        otherLeaves.remove(leafB);
        leafB.getOnlyParent().addPropagatedCert("", leafB);
        return certA.equals(getCert(otherLeaves));
    }

    private int countDegreeOfBParent() {
        for (int i = 0; i < N; i++) {
            if (degreeCounterA[i] > degreeCounterB[i]) {
                return i + 1;
            }
        }
        return N + 1;
    }

    private String getCert(Collection<Node> leaves) {
        for (Node n : leaves) {
            n.propagateCert();
        }
        Set<Node> parents = new HashSet<>(N);
        for (Node n : leaves) {
            Node parent = n.getParent();
            if (parent != null && parent.isLeaf()) {
                parents.add(parent);
            }
        }
        if (parents.isEmpty()) {
            List<String> certs = new ArrayList<>(2);
            for (Node n : leaves) {
                certs.add(n.getCert());
                n.clearPropagated();
            }
            Collections.sort(certs);
            StringBuilder sb = new StringBuilder();
            for (String cert : certs) {
                sb.append(cert);
            }
            return sb.toString();
        }
        for (Node n : leaves) {
            n.clearPropagated();
        }
        return getCert(parents);
    }

    private void initGraph(Node[] nodes, BufferedReader br) throws IOException {
        String line;
        int bound = nodes.length - 1;
        for (int i = 0; i < bound; i++) {
            line = br.readLine();
            int nodeA = Integer.parseInt(line.split(" ")[0]);
            int nodeB = Integer.parseInt(line.split(" ")[1]);
            if (nodes[nodeA] == null) {
                nodes[nodeA] = new Node(nodeA, N);
            }
            if (nodes[nodeB] == null) {
                nodes[nodeB] = new Node(nodeB, N);
            }
            addConnection(nodes[nodeA], nodes[nodeB]);
        }
    }

    private void addConnection(Node A, Node B) {
        A.addNeighbor(B);
        B.addNeighbor(A);
    }

}
