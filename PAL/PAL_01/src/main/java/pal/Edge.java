/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pal;

import java.util.Comparator;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class Edge {

    public static final int BEFORE = -1, EQUAL = 0, AFTER = 1;
    private final int a_node, b_node, price;

    public Edge(int a_node, int b_node, int price) {
        if (a_node < b_node) {
            this.a_node = a_node;
            this.b_node = b_node;
        } else {
            this.a_node = b_node;
            this.b_node = a_node;
        }
        this.price = price;
    }

    public int getANode() {
        return a_node;
    }

    public int getBNode() {
        return b_node;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%1$d %2$d %3$d", a_node, b_node, price);
    }

    public static Comparator<Edge> getPriceComparator() {
        return new PriceEdgeComparator<>();
    }

    public static Comparator<Edge> getLexicographicalComparator() {
        return new LexicographicalEdgeComparator<>();
    }

    private static class PriceEdgeComparator<T extends Edge> implements Comparator<T> {

        @Override
        public int compare(T arg0, T arg1) {
            if (arg0 == arg1) {
                return EQUAL;
            }
            if (arg0.getPrice() == arg1.getPrice()) {
                return EQUAL;
            } else if (arg0.getPrice() < arg1.getPrice()) {
                return BEFORE;
            } else {
                return AFTER;
            }
        }

    }

    private static class LexicographicalEdgeComparator<T extends Edge> implements Comparator<T> {

        @Override
        public int compare(T arg0, T arg1) {
            if (arg0 == arg1) {
                return EQUAL;
            }
            if (arg0.getANode() < arg1.getANode()) {
                return BEFORE;
            } else if (arg0.getANode() > arg1.getANode()) {
                return AFTER;
            }
            if (arg0.getBNode() < arg1.getBNode()) {
                return BEFORE;
            } else if (arg0.getBNode() > arg1.getBNode()) {
                return AFTER;
            }
            if (arg0.getPrice() < arg1.getPrice()) {
                return BEFORE;
            } else if (arg0.getPrice() > arg1.getPrice()) {
                return AFTER;
            }
            return EQUAL;
        }

    }

}
