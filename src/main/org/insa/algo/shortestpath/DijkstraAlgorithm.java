package org.insa.algo.shortestpath;

import org.insa.algo.AbstractSolution;
import org.insa.algo.ArcInspector;
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
        ArrayList<Label> labels = new ArrayList<>();
        for (int i = 0; i < nbNodes; i++) {
            labels.add(new Label(false, null, Double.POSITIVE_INFINITY));
        }
        labels.set(data.getOrigin().getId(), new Label(false,null,0.0));

        BinaryHeap<Node> tas = new BinaryHeap<>();
        tas.insert(data.getOrigin());

        while (!done)
        {
            if (tas.isEmpty()) {
                break;
            }
            Node x = tas.deleteMin() ;
            System.out.println("ID : " + x.getId());
            labels.get(x.getId()).marked = true;
            Iterator<Arc> it = x.iterator();
            while (it.hasNext())
            {
                Arc arc = it.next();
                Node y = arc.getDestination();
                if (!(labels.get(y.getId()).marked) && !y.equals(x))
                {
                    double AncienCout = labels.get(y.getId()).cost;
                    double NewCout = labels.get(arc.getOrigin().getId()).cost + arc.getLength();
                    if (AncienCout > NewCout)
                    {
                        labels.get(y.getId()).cost = NewCout;
                        tas.insert(y);
                        labels.get(y.getId()).parent = arc;
                        labels.get(y.getId()).marked = false;
                    }
                }
            }
            done = true;
            for (Label lab : labels)
            {
                if (!lab.marked) {
                    done = false;
                    break;
                }
            }
        }


        ArrayList<Arc> result = new ArrayList<Arc>();

        int debut = data.getDestination().getId();
        boolean done_rebuilding = false;
        while (! done_rebuilding) {
            Label current = labels.get(debut);
            Arc path_chunk = current.parent;
            result.add(path_chunk);
            debut = path_chunk.getOrigin().getId();

            if (path_chunk.getOrigin().equals(data.getOrigin())) {
                done_rebuilding = true;
            }

        }

        Path sol_path = new Path(graph, result);

        solution = new ShortestPathSolution(data, AbstractSolution.Status.FEASIBLE, sol_path);

        return solution;
    }

}
