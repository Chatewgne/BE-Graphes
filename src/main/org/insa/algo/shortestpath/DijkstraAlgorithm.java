package org.insa.algo.shortestpath;

import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;

import java.util.Arrays;
import java.util.Iterator;

import static jdk.nashorn.internal.objects.NativeMath.min;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        // Retrieve the graph.
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        boolean done = false ;
        // Initialize array of distances.
        Label[] labels = new Label[nbNodes];
        Arrays.fill(labels, new Label(false, null,Double.POSITIVE_INFINITY));
        labels[data.getOrigin().getId()] = new Label(false,null,0.0);

        BinaryHeap<Node> tas = new BinaryHeap<>();
        tas.insert(data.getOrigin());

        while (!done)
        {
            Node x = tas.deleteMin() ;
            labels[x.getId()].marked = true;
            Iterator<Arc> it = x.iterator();
            while (it.hasNext())
            {
                Arc arc = it.next();
                if (!(labels[arc.getDestination().getId()].marked))
                {
                    double AncienCout = labels[arc.getDestination().getId()].cost;
                    double NewCout =labels[arc.getOrigin().getId()].cost + arc.getLength();
                    if (AncienCout > NewCout)
                    {
                        labels[arc.getDestination().getId()].cost = NewCout;
                        tas.insert(x);
                        labels[arc.getDestination().getId()].parent = arc.getOrigin();
                    }
                }
            }
            done = true;
            for (Label lab : labels)
            {
                if (lab.marked) {
                    done = false;
                }
            }
        }



        return solution;
    }

}
