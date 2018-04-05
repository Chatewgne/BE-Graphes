package org.insa.algo.shortestpath;

import org.insa.graph.Arc;
import org.insa.graph.Node;

public class Label {
    public boolean marked ;
    public Arc parent ;
    public double cost ;

    public Label(boolean marked, Arc parent, double cost) {
        this.marked = marked;
        this.parent = parent;
        this.cost = cost;
    }
}
