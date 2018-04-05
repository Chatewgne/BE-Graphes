package org.insa.algo.shortestpath;

import org.insa.algo.AbstractSolution;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.shortestpath.Label;
import org.insa.algo.utils.ElementNotFoundException;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
            labels.add(new Label(null, null, false, Double.POSITIVE_INFINITY));
        }
        Label labi = new Label(data.getOrigin(), null, false, 0.0);
        labels.set(data.getOrigin().getId(), labi);

        BinaryHeap<Label> tas= new BinaryHeap<>();
        tas.insert(labels.get(data.getOrigin().getId()));
        //BinaryHeap<Node> tas = new BinaryHeap<>();
        //tas.insert(data.getOrigin());

        while (!done)
        {
            if (tas.isEmpty()) {
                break;
            }
            Label x = tas.deleteMin() ;
            x.marked = true;
            Iterator<Arc> it = graph.get(x.me.getId()).iterator();
            while (it.hasNext())
            {
                Arc arc = it.next();
                Node y = arc.getDestination();
                if (!(labels.get(y.getId()).marked) && !y.equals(x))
                {
                    double AncienCout = labels.get(y.getId()).cost;
                    double NewCout = labels.get(arc.getOrigin().getId()).cost + arc.getLength();
                    if (NewCout < AncienCout)
                    {
                        labels.get(y.getId()).me = y ;
                        labels.get(y.getId()).cost = NewCout;
                        //tas.insert(labels.get(y.getId()));
                        labels.get(y.getId()).parent = x.me;
                        labels.get(y.getId()).marked = false;
                        try {
                            tas.remove(labels.get(y.getId()));
                        }
                        catch (ElementNotFoundException ignored){}

                        tas.insert(labels.get(y.getId()));
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


        ArrayList<Node> result = new ArrayList<Node>();

        /* Création du chemin */
        Node current = data.getDestination();
        boolean done_rebuilding = false;
        while (! done_rebuilding) {
            result.add(current);
            current = labels.get(current.getId()).parent;

            if (current.equals(data.getOrigin())) {
                done_rebuilding = true;
                result.add(current);
            }
        }
        /* Inversion du chemin */
        for(int i = 0, j = result.size() - 1; i < j; i++) {
            result.add(i, result.remove(j));
        }

        try {
            Path sol_path = Path.createShortestPathFromNodes(graph, result);
            solution = new ShortestPathSolution(data, AbstractSolution.Status.FEASIBLE, sol_path);
            return solution;
        }
        catch (Exception e) {
            return new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE, null);
        }

    }

}
