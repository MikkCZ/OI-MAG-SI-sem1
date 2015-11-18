/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pal;

import java.util.ArrayList;
import java.util.Collection;
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
    private final List<Node> children;
    private String cert = null;

    public Node(int ID, int total) {
        this.ID = ID;
        this.neighbors = new ArrayList<>(total);
        this.children = new ArrayList<>(total);
    }

    public void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }

    public void removeNeighbor(Node neighbor) {
        neighbors.remove(neighbor);
        children.add(neighbor);
    }
    
    public List<Node> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return neighbors.size() == 1;
    }

    public Collection<Node> stripFromGraph() {
        Collection<Node> leaves = new ArrayList<>(neighbors.size());
        for (Node neighbor : neighbors) {
            neighbor.removeNeighbor(this);
            if (neighbor.isLeaf()) {
                leaves.add(neighbor);
            }
        }
        return leaves;
    }

    public String getCert() {
        if (cert != null) {
            return cert;
        }
        Collections.sort(children, Node.comparator);
        StringBuilder sb = new StringBuilder();
        sb.append("0");
        for (Node child : children) {
            sb.append(child.getCert());
        }
        sb.append("1");
        cert = sb.toString();
        return cert;
    }
    
    public static final Comparator<Node> comparator = new LexicographicalCertComparator();

    private static class LexicographicalCertComparator<T extends Node> implements Comparator<T> {

        @Override
        public int compare(T arg0, T arg1) {
            return arg0.getCert().compareTo(arg1.getCert());
        }
    }

}
