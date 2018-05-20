package org.insa.algo.shortestpath;

import org.insa.graph.Arc;
import org.insa.graph.Node;

public class AstarLabel implements Comparable<AstarLabel> {
    public boolean marked ;
    public Node parent ;
    public double cost ;
    public Node me;
    public double estimatedGoalDistance ;

    public AstarLabel(Node me, Node parent,boolean marked, double cost, double estimation) {
        this.me = me;
        this.marked = marked;
        this.parent = parent;
        this.cost = cost;
        this.estimatedGoalDistance = estimation;
    }

/*
    @Override
    public int compareTo(Label label) {
        Double val = this.cost - label.cost;
        return val.intValue();
    }*/

    @Override
    public int compareTo(AstarLabel label) {
        //Double val = (this.cost + this.estimatedGoalDistance) - (label.cost + label.estimatedGoalDistance);
        //if (val == 0) val = this.cost - label.cost;
        return (int) Math.signum((this.cost + this.estimatedGoalDistance) - (label.cost + label.estimatedGoalDistance));
    }

    public double getCoutTotal(){
        return this.cost + this.estimatedGoalDistance;
    }

}
