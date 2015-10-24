/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class BackupConnection implements Task {

    private static final int representative = 0,
            rank = 1;

    private int N, M, C1, C2;
    private List<Edge> edges;
    private List<Edge> backupEdges;
    private int[][] nodes;
    private int firstA, firstB;

    @Override
    public String eval(final InputStream is) throws IOException {
        Main.printTimeDuration("Start");

        int[] input = new int[3 * 1300000];
        StreamTokenizer st = new StreamTokenizer(new BufferedReader(new InputStreamReader(is)));
        //st.resetSyntax();
        st.parseNumbers();
        st.eolIsSignificant(false);
        int j = 0;
        while (st.nextToken() == StreamTokenizer.TT_NUMBER) {
            input[j++] = (int) st.nval;
        }
        Main.printTimeDuration("Reading input");

        N = input[0];
        M = input[1];
        C1 = input[2];
        C2 = input[3];

        nodes = new int[N][2];
        for (int i = 0; i < N; i++) {
            nodes[i][representative] = i;
        }

        Main.printTimeDuration("Nodes allocation");
        edges = new ArrayList<>(M);
        backupEdges = new ArrayList<>(M - N + 1);
        j = 4;
        for (int i = 0; i < M; i++) {
            if (input[j + 2] > C2) {
                j += 3;
                continue;
            }
            edges.add(new Edge(
                    input[j++],
                    input[j++],
                    input[j++]
            ));
        }
        Main.printTimeDuration("Edges allocation");

        Collections.sort(edges, Edge.getPriceComparator());
        Main.printTimeDuration("Edges sort");

        StringBuilder result = new StringBuilder();
        result.append(findMinSpanningTree());
        Main.printTimeDuration("Spanning Tree");

        for (Edge e : getSuitableBackupEdges()) {
            result.append("\n").append(e.toString());
        }
        Main.printTimeDuration("Backup Edges");

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
        firstA = edges.get(0).getANode();
        firstB = edges.get(0).getBNode();
        nodesAdded += 2;
        price += edges.get(0).getPrice();
        // find spanning tree
        Edge e;
        int aRep, bRep;
        int i = 1;
        for (; nodesAdded < N; i++) {
            e = edges.get(i);
            aRep = findRepresentative(e.getANode());
            bRep = findRepresentative(e.getBNode());

            if (aRep != bRep && union(aRep, bRep)) { // ruzne komponenty
                nodesAdded++;
                price += e.getPrice();
            } else { // uz jsou ve stejne komponente
                addPossibleBackupEdge(e);
            }
        }
        // find more backup egdes
        for (; i < edges.size(); i++) {
            e = edges.get(i);
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
        List<Edge> suitable = new ArrayList<>(backupEdges.size());
        int a, b;
        for (Edge e : backupEdges) {
            a = findRepresentative(e.getANode());
            b = findRepresentative(e.getBNode());
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
