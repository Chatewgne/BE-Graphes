package org.insa.algo.shortestpath;
import org.insa.algo.AbstractInputData;
import org.insa.algo.AbstractSolution;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.shortestpath.AstarLabel;
import org.insa.algo.utils.ElementNotFoundException;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

import java.util.*;

import static jdk.nashorn.internal.objects.NativeMath.max;
import static jdk.nashorn.internal.objects.NativeMath.min;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
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
        Map<Node,AstarLabel> labels = new HashMap<>(nbNodes);
        AstarLabel labi = new AstarLabel(data.getOrigin(), null, false, 0.0,Double.POSITIVE_INFINITY);
        labels.put(data.getOrigin(), labi);

        BinaryHeap<AstarLabel> tas = new BinaryHeap<>();
        tas.insert(labels.get(data.getOrigin()));

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
            AstarLabel x = tas.deleteMin() ;
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
                // Allocation du label
                AstarLabel label_y;

                if (labels.containsKey(y)) {
                    label_y = labels.get(y);
                }
                else {
                    label_y = new AstarLabel(null, null, false, Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);
                    labels.put(y,label_y);
                }

                if (!(label_y.marked) && !y.equals(x.me) && data.isAllowed(arc))
                {
                    double OldCost = labels.get(y).cost;
                    double NewCostFromOrigin = labels.get(arc.getOrigin()).cost + data.getCost(arc);
                    notifyNodeReached(y);
                    if (NewCostFromOrigin < OldCost)
                    {
                        labels.get(y).me = y ;
                        labels.get(y).cost = NewCostFromOrigin;
                        labels.get(y).parent = x.me;
                        if (data.getMode() == AbstractInputData.Mode.TIME) {
                            if (data.getMaximumSpeed() > 0) {
                                labels.get(y).estimatedGoalDistance = y.getPoint().distanceTo(data.getDestination().getPoint()) / data.getMaximumSpeed() ;
                            }
                            else {
                                labels.get(y).estimatedGoalDistance = y.getPoint().distanceTo(data.getDestination().getPoint()) / 36.111;
                            }
                        }
                        else {
                            labels.get(y).estimatedGoalDistance = y.getPoint().distanceTo(data.getDestination().getPoint());
                        }
                        if (OldCost != Double.POSITIVE_INFINITY) {
                            try {
                                tas.remove(labels.get(y));
                            } catch (ElementNotFoundException ignored) {
                            }
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
