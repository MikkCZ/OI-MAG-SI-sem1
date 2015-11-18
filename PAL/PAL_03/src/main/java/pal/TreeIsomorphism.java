package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    @Override
    public String eval(InputStream is) throws IOException {
        Main.printTimeDuration("Start");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = br.readLine();
        N = Integer.parseInt(line);

        nodesA = new Node[N];
        leavesA = new HashSet<>(N);
        initGraph(nodesA, leavesA, br);

        nodesB = new Node[N + 1];
        leavesB = new HashSet<>(N + 1);
        initGraph(nodesB, leavesB, br);

        br = null;
        line = null;
        Main.printTimeDuration("Reading input + graphs init");

        List<Node> rootsA = getRoot(leavesA);
        Collections.sort(rootsA, Node.comparator);
        List<Node> rootsB = getRoot(leavesB);
        Collections.sort(rootsB, Node.comparator);
        Main.printTimeDuration("Certificates");

        String certA;
        StringBuilder sb = new StringBuilder();
        for (Node root : rootsA) {
            sb.append(root.getCert());
        }
        System.out.println(certA = sb.toString());

        String certB;
        sb = new StringBuilder();
        for (Node root : rootsB) {
            sb.append(root.getCert());
        }
        System.out.println(certB = sb.toString());

        Node rootA = rootsA.get(0);
        Node rootB = rootsB.get(0);

        diffTrees(rootA, rootB);

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<Node> getRoot(Set<Node> leaves) {
        Set<Node> newLeaves = new HashSet<>(leaves.size());
        for (Node n : leaves) {
            newLeaves.addAll(n.stripFromGraph());
        }
        if (newLeaves.isEmpty()) {
            return new ArrayList<>(leaves);
        }
        return getRoot(newLeaves);
    }

    private void initGraph(Node[] nodes, Set<Node> leaves, BufferedReader br) throws IOException {
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
            addConnection(leaves, nodes[nodeA], nodes[nodeB]);
        }
    }

    private void addConnection(Set<Node> leavesSet, Node A, Node B) {
        A.addNeighbor(B);
        B.addNeighbor(A);
        if (A.isLeaf()) {
            leavesSet.add(A);
        } else {
            leavesSet.remove(A);
        }
        if (B.isLeaf()) {
            leavesSet.add(B);
        } else {
            leavesSet.remove(B);
        }
    }

}
