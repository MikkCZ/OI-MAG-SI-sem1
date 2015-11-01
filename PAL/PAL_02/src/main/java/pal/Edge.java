/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pal;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class Edge {

    private final Node start, target;

    public Edge(Node start, Node target) {
        this.start = start;
        this.target = target;
    }

    public Node getStart() {
        return start;
    }

    public Node getTarget() {
        return target;
    }

}
