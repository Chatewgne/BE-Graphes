package org.insa.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.algo.utils.PriorityQueueTest;
import org.insa.graph.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

public class DijkstraTest {

    // Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc f1to2, f1to3, f2to4, f2to6, f2to5, f3to1, f3to6, f3to2, f5to3, f5to6, f5to4, f6to5;

    // Some paths...
    private static Path emptyPath, singleNodePath, shortPath, longPath, loopPath, longLoopPath,
            invalidPath;

    @BeforeClass
    public static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, "");
             //   speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

        // Create nodes
        nodes = new Node[6];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        // Add arcs...
        f1to2 = Node.linkNodes(nodes[0], nodes[1], 7, speed10, null);
        f1to3 = Node.linkNodes(nodes[0], nodes[2], 8, speed10, null);
        f2to4 = Node.linkNodes(nodes[1], nodes[3], 4, speed10, null);
        f2to6 = Node.linkNodes(nodes[1], nodes[5], 5, speed10, null);
        f2to5 = Node.linkNodes(nodes[1], nodes[4], 1, speed10, null);
        f3to1 = Node.linkNodes(nodes[2], nodes[0], 7, speed10, null);
        f3to6 = Node.linkNodes(nodes[2], nodes[5], 2, speed10, null);
        f3to2 = Node.linkNodes(nodes[2], nodes[1], 2, speed10, null);
        f5to3 = Node.linkNodes(nodes[4], nodes[2], 2, speed10, null);
        f5to6 = Node.linkNodes(nodes[4], nodes[5], 3, speed10, null);
        f5to4 = Node.linkNodes(nodes[4], nodes[3], 2, speed10, null);
        f6to5 = Node.linkNodes(nodes[5], nodes[4], 3, speed10, null);

        graph = new Graph("ID", "", Arrays.asList(nodes), null);

        emptyPath = new Path(graph, new ArrayList<Arc>());
        singleNodePath = new Path(graph, nodes[1]);
        shortPath = new Path(graph, Arrays.asList(new Arc[] { f1to2, f2to5}));
        longPath = new Path(graph, Arrays.asList(new Arc[] { f1to2, f2to6, f6to5, f5to3, f3to1 }));
        loopPath = new Path(graph, Arrays.asList(new Arc[] { f2to5, f5to3, f3to2}));
        longLoopPath = new Path(graph,
                Arrays.asList(new Arc[] { f1to2, f2to5, f5to3, f3to1  }));
        invalidPath = new Path(graph, Arrays.asList(new Arc[] { f2to5, f3to1, f6to5 }));

    }

    private void testDijkstraAlgorithm(Node u, Node i){
        ArcInspector insp = ArcInspectorFactory.getAllFilters().get(0); // no filters
        ShortestPathData data = new ShortestPathData(graph, u, i, insp);
        DijkstraAlgorithm Dijk = new DijkstraAlgorithm(data);
        BellmanFordAlgorithm Bell = new BellmanFordAlgorithm(data);

        ShortestPathSolution bell_sol = Bell.run();
        ShortestPathSolution djik_sol = Dijk.run();

        assertEquals(bell_sol.isFeasible(),djik_sol.isFeasible());

        if (bell_sol.isFeasible()) {
            assertEquals(bell_sol.getPath().getLength(), djik_sol.getPath().getLength(), 1e-6);
        }

    }

    @Test
    public void testAll(){
        for(int i = 0; i < nodes.length;i++)
        {
            for(int u = 0; u < nodes.length;u++)
            {
                if (i!=u){
                    testDijkstraAlgorithm(nodes[i],nodes[u]);
                }
            }
        }
    }

}
