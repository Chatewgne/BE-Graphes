package org.insa.algo.shortestpath;

import org.insa.graph.Arc;
import org.insa.graph.Node;

public class Label implements Comparable<Label> {
    public boolean marked ;
    public Node parent ;
    public double cost ;
    public Node me;

    public Label(Node me, Node parent,boolean marked, double cost) {
        this.marked = marked;
        this.parent = parent;
        this.cost = cost;
    }


    @Override
    public int compareTo(Label label) {
        Double val = this.cost - label.cost;
        return val.intValue();
    }
}
