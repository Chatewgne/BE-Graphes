package org.insa.algo.shortestpath;

import org.insa.algo.AbstractSolution;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.shortestpath.Label;
import org.insa.algo.utils.ElementNotFoundException;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

import java.util.*;

import static jdk.nashorn.internal.objects.NativeMath.max;
import static jdk.nashorn.internal.objects.NativeMath.min;
import static jdk.nashorn.internal.runtime.ScriptObject.spillAllocationLength;


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
        Map<Node, Label> labels = new HashMap<>(nbNodes);
       // for (int i = 0; i < nbNodes; i++) {
         //   labels.add(null);
            //labels[i] = new Label(null, null, false, Double.POSITIVE_INFINITY);
        //}
        Label labi = new Label(data.getOrigin(), null, false, 0.0);
        labels.put(data.getOrigin(),labi);

        BinaryHeap<Label> tas= new BinaryHeap<>();
        tas.insert(labels.get(data.getOrigin()));
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
                Label label_y;

                if (labels.containsKey(y)) {
                    label_y = labels.get(y);
                }
                else {
                    label_y = new Label(null, null, false, Double.POSITIVE_INFINITY);
                    labels.put(y,label_y);
                }

                if (!(label_y.marked) && !y.equals(x.me) && data.isAllowed(arc))
                {
                    double AncienCout = labels.get(y).cost;
                    double NewCout = labels.get(arc.getOrigin()).cost + data.getCost(arc);
                    notifyNodeReached(y);
                    if (NewCout < AncienCout)
                    {
                        Label y_lab = labels.get(y);
                        y_lab.me = y ;
                        y_lab.cost = NewCout;
                        y_lab.parent = x.me;
                        if (AncienCout != Double.POSITIVE_INFINITY) {
                            try {
                                tas.remove(labels.get(y));
                            } catch (ElementNotFoundException ignored) {}
                        }
                        tas.insert(labels.get(y));
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
                current = labels.get(current).parent;

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
