package org.insa.algo.shortestpath;

import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

import java.util.ArrayList;
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
        System.out.println("NbNodes : " + nbNodes);
        Label[] labels = new Label[nbNodes];
        Arrays.fill(labels, new Label(false, null,Double.POSITIVE_INFINITY));
        labels[data.getOrigin().getId()] = new Label(false,null,0.0);

        BinaryHeap<Node> tas = new BinaryHeap<>();
        tas.insert(data.getOrigin());

        while (!done)
        {
            Node x = tas.deleteMin() ;
            System.out.println("ID : " + x.getId());
            labels[x.getId()].marked = true;
            Iterator<Arc> it = x.iterator();
            while (it.hasNext())
            {
                Arc arc = it.next();
                Node y = arc.getDestination();
                if (!(labels[y.getId()].marked) && !y.equals(x))
                {
                    double AncienCout = labels[y.getId()].cost;
                    double NewCout = labels[arc.getOrigin().getId()].cost + arc.getLength();
                    if (AncienCout > NewCout)
                    {
                        labels[y.getId()].cost = NewCout;
                        tas.insert(y);
                        labels[y.getId()].parent = arc;
                        labels[y.getId()].marked = false;
                    }
                }
            }
            done = true;
            for (Label lab : labels)
            {
                if (!lab.marked) {
                    done = false;
                }
            }
        }


        ArrayList<Arc> result = new ArrayList<Arc>();

        int debut = data.getDestination().getId();
        boolean done_rebuilding = false;
        while (! done_rebuilding) {
            Label current = labels[debut];
            Arc path_chunk = current.parent;
            result.add(path_chunk);
            debut = path_chunk.getOrigin().getId();

            if (path_chunk.getOrigin().equals(data.getOrigin())) {
                done_rebuilding = true;
            }

        }


        return solution;
    }

}
