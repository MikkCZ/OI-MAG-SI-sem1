/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class BackupConnection implements Task {

    private static final int representative = 0,
            rank = 1;

    private final StringBuilder result = new StringBuilder();
    private int N, M, C1, C2;
    private Edge[] edges;
    private List<Edge> backupEdges;
    private int[][] nodes;
    private int firstA, firstB;

    @Override
    public String eval(final String stdin) {
        String[] input = stdin.split("\n");
        initGraphInfo(input[0]);

        nodes = new int[N][2];
        for (int i = 0; i < N; i++) {
            nodes[i][representative] = i;
        }

        edges = new Edge[M];
        backupEdges = new ArrayList<>(M);
        String[] tmp;
        for (int i = 0; i < M; i++) {
            tmp = input[i + 1].split(" ");
            edges[i] = new Edge(
                    Integer.parseInt(tmp[0]),
                    Integer.parseInt(tmp[1]),
                    Integer.parseInt(tmp[2])
            );
        }
        Arrays.sort(edges, Edge.getPriceComparator());

        result.append(findMinSpanningTree()).append("\n");
        for (Edge e : getSuitableBackupEdges()) {
            result.append(e.toString()).append("\n");
        }

        return result.toString();
    }

    private void initGraphInfo(String firstLine) {
        String[] tmp = firstLine.split(" ");
        N = Integer.parseInt(tmp[0]);
        M = Integer.parseInt(tmp[1]);
        C1 = Integer.parseInt(tmp[2]);
        C2 = Integer.parseInt(tmp[3]);
    }

    private int findMinSpanningTree() {
        int nodesAdded = 0, price = 0;
        firstA = edges[0].getANode();
        firstB = edges[0].getBNode();
        nodesAdded += 2;
        price += edges[0].getPrice();
        // find spanning tree
        int i = 1;
        for (; nodesAdded < N; i++) {
            Edge e = edges[i];
            int aRep = findRepresentative(e.getANode());
            int bRep = findRepresentative(e.getBNode());

            if (aRep != bRep && union(aRep, bRep)) { // ruzne komponenty
                nodesAdded++;
                price += e.getPrice();
            } else { // uz jsou ve stejne komponente
                addPossibleBackupEdge(e);
            }
        }
        // find more backup egdes
        for (; i < M; i++) {
            Edge e = edges[i];
            if (e.getPrice() > C2) {
                break;
            }
            addPossibleBackupEdge(e);
        }
        return price;
    }

    private boolean union(int a, int b) {
        if ((a == firstA || a == firstB)
                && (b == firstA || b == firstB)) { // jsou z prvni hrany
            return false;
        }

        int newRep, otherRep;
        if (a == firstA || a == firstB) {
            newRep = a;
            otherRep = b;
        } else if (b == firstA || b == firstB) {
            newRep = b;
            otherRep = a;
        } else if (nodes[a][rank] > nodes[b][rank]) {
            newRep = a;
            otherRep = b;
        } else {
            newRep = b;
            otherRep = a;
        }

        int newRank = Math.min(
                nodes[newRep][rank],
                nodes[otherRep][rank] + 1
        );

        nodes[a][representative] = newRep;
        nodes[b][representative] = newRep;
        nodes[newRep][rank] = newRank;

        return true;
    }

    private int findRepresentative(int node) {
        int nodeRepresentative = node;
        do {
            node = nodeRepresentative;
            nodeRepresentative = nodes[nodeRepresentative][representative];
        } while (node != nodeRepresentative);
        return nodeRepresentative;
    }

    private void addPossibleBackupEdge(Edge e) {
        int e_price = e.getPrice();
        if (e_price >= C1 && e_price <= C2) {
            backupEdges.add(e);
        }
    }

    private List<Edge> getSuitableBackupEdges() {
        List<Edge> suitable = new LinkedList<>();
        for (Edge e : backupEdges) {
            int a = findRepresentative(e.getANode());
            int b = findRepresentative(e.getBNode());
            if ((a != b)
                    && (a == firstA || a == firstB)
                    && (b == firstA || b == firstB)) { // jsou z prvni hrany
                suitable.add(e);
            }
        }
        Collections.sort(suitable, Edge.getLexicographicalComparator());
        return suitable;
    }

}
