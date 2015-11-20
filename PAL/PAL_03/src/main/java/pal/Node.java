/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class Node {

    private final int ID;
    private final List<Node> neighbors;
    private final List<String> propagatedCerts;
    private final List<Node> propagatedFrom;
    private Node parent;

    public Node(int ID, int total) {
        this.ID = ID;
        this.neighbors = new ArrayList<>(total);
        this.propagatedCerts = new ArrayList<>(total);
        this.propagatedFrom = new ArrayList<>(total);
    }

    public void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }

    public Node getOnlyParent() {
        return neighbors.get(0);
    }

    public int getDegree() {
        return neighbors.size();
    }

    public String getCert() {
        return propagateCert();
    }

    public String propagateCert() {
        Collections.sort(propagatedCerts);
        StringBuilder sb = new StringBuilder();
        sb.append("0");
        for (String cert : propagatedCerts) {
            sb.append(cert);
        }
        sb.append("1");
        String certToPropagate = sb.toString();
        for (Node n : neighbors) {
            if (!propagatedFrom.contains(n)) {
                n.addPropagatedCert(certToPropagate, this);
                this.parent = n;
                break;
            }
        }
        return certToPropagate;
    }

    public void addPropagatedCert(String propagatedCert, Node child) {
        propagatedCerts.add(propagatedCert);
        propagatedFrom.add(child);
    }

    public Node getParent() {
        return this.parent;
    }

    public void clearPropagated() {
        this.propagatedCerts.clear();
        this.propagatedFrom.clear();
        this.parent = null;
    }

    @Override
    public String toString() {
        return Integer.toString(ID);
    }

    public static final Comparator<Node> IDComparator = new NodeIDComparator();

    private static class NodeIDComparator<T extends Node> implements Comparator<T> {

        @Override
        public int compare(T arg0, T arg1) {
            return arg0.toString().compareTo(arg1.toString());
        }
    }

}
