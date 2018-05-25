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

import static jdk.nashorn.internal.objects.NativeMath.max;
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
        ArrayList<Label> labels = new ArrayList<>(nbNodes);
        for (int i = 0; i < nbNodes; i++) {
            labels.add(null);
            //labels[i] = new Label(null, null, false, Double.POSITIVE_INFINITY);
        }
        Label labi = new Label(data.getOrigin(), null, false, 0.0);
        labels.set(data.getOrigin().getId(), labi);

        BinaryHeap<Label> tas= new BinaryHeap<>();
        tas.insert(labels.get(data.getOrigin().getId()));
        //BinaryHeap<Node> tas = new BinaryHeap<>();
        //tas.insert(data.getOrigin());

        int nodeEvaluated = 0;
        int maxHeapSize = 0;

        while (!done)
        {
            if (tas.isEmpty()) {
                break;
            }
            if (tas.size() > maxHeapSize) {
                maxHeapSize = tas.size();
            }
            Label x = tas.deleteMin();
            nodeEvaluated++;
            if(x.me.equals(data.getOrigin())){notifyOriginProcessed(x.me);}
            x.marked = true;
            notifyNodeMarked(x.me);
            if (x.me.equals(data.getDestination())) {
                done = true;
                notifyDestinationReached(x.me);
            }
            Iterator<Arc> it = graph.get(x.me.getId()).iterator();

            while (it.hasNext())
            {
                Arc arc = it.next();
                Node y = arc.getDestination();
                Label label_y = labels.get(y.getId());
                // Allocation du label
                if (label_y == null) {
                    labels.set(y.getId(), new Label(null, null, false, Double.POSITIVE_INFINITY));
                    label_y = labels.get(y.getId());
                }

                if (!(label_y.marked) && !y.equals(x) && data.isAllowed(arc))
                {
                    double AncienCout = labels.get(y.getId()).cost;
                    double NewCout = labels.get(arc.getOrigin().getId()).cost + data.getCost(arc);
                    notifyNodeReached(y);
                    if (NewCout < AncienCout)
                    {
                        labels.get(y.getId()).me = y ;
                        labels.get(y.getId()).cost = NewCout;
                        labels.get(y.getId()).parent = x.me;
                        try {
                            tas.remove(labels.get(y.getId()));
                        }
                        catch (ElementNotFoundException ignored){}

                        tas.insert(labels.get(y.getId()));
                    }
                }
            }
            if (!done) {
                done = true;
                for (Label lab : labels) {
                    if (lab == null || !lab.marked) {
                        done = false;
                        break;
                    }
                }
            }
        }

        try {

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

            Path sol_path = Path.createShortestPathFromNodes(graph, result);
            solution = new ShortestPathSolution(data, AbstractSolution.Status.FEASIBLE, sol_path, nodeEvaluated, maxHeapSize);
            return solution;
        }
        catch (Exception e) {
            return new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE, null, nodeEvaluated, maxHeapSize);
        }

    }

}
