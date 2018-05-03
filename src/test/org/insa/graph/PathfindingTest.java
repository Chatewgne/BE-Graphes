package org.insa.graph;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.insa.algo.AbstractInputData;
import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.*;
import org.insa.graph.RoadInformation.RoadType;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathfindingTest {

    // Small graph use for tests
    private static Graph graph;

    private static Graph realGraph;

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


        // On charge la carte de Toulouse
        String mapName = "../maps/toulouse.mapgr";
        GraphReader reader =  new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        realGraph = reader.read();

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, "");
             //   speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

        // Create nodes
        nodes = new Node[6];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, new Point(0,0));
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

    private void testShortestPathAlgorithm(Node u, Node i, Graph g, int arcInspectorId){
        ArcInspector insp = ArcInspectorFactory.getAllFilters().get(arcInspectorId); // no filters
        ShortestPathData data = new ShortestPathData(g, u, i, insp);
        DijkstraAlgorithm Dijk = new DijkstraAlgorithm(data);
        BellmanFordAlgorithm Bell = new BellmanFordAlgorithm(data);
        AStarAlgorithm Ast = new AStarAlgorithm(data);

        ShortestPathSolution bell_sol = Bell.run();
        ShortestPathSolution djik_sol = Dijk.run();
        ShortestPathSolution ast_sol = Ast.run();

        assertEquals(bell_sol.isFeasible(),djik_sol.isFeasible());

        if (bell_sol.isFeasible()) {
            if (insp.getMode() == AbstractInputData.Mode.LENGTH) {
                assertEquals(bell_sol.getPath().getLength(), djik_sol.getPath().getLength(), 1e-6);
                assertEquals(djik_sol.getPath().getLength(), ast_sol.getPath().getLength(), 1e-6);
            }
            if (insp.getMode() == AbstractInputData.Mode.TIME) {
                assertEquals(bell_sol.getPath().getMinimumTravelTime(), djik_sol.getPath().getMinimumTravelTime(), 1e-6);
                assertEquals(djik_sol.getPath().getMinimumTravelTime(), ast_sol.getPath().getMinimumTravelTime(), 1e-6);
            }
        }

    }

    @Test
    public void testSmallGraph(){
        for(int i = 0; i < nodes.length;i++)
        {
            for(int u = 0; u < nodes.length;u++)
            {
                if (i!=u){
                    testShortestPathAlgorithm(nodes[i],nodes[u], graph, 0);
                }
            }
        }
    }

    @Test
    public void testRealMap() {
        for (int i = 0; i < 6; i++) {
            Node start = realGraph.get(35052);
            Node end = realGraph.get(16597);
            testShortestPathAlgorithm(start, end, realGraph, i);

            start = realGraph.get(7890);
            end = realGraph.get(25890);
            testShortestPathAlgorithm(start, end, realGraph, i);

            start = realGraph.get(25890);
            end = realGraph.get(7890);
            testShortestPathAlgorithm(start, end, realGraph, i);

            start = realGraph.get(33333);
            end = realGraph.get(1);
            testShortestPathAlgorithm(start, end, realGraph, i);
        }
    }

    @Test
    public void testNoPath() {
        Node start = realGraph.get(23683);
        Node end = realGraph.get(1445);

        ArcInspector insp = ArcInspectorFactory.getAllFilters().get(4); // Pedestrian only
        ShortestPathData data = new ShortestPathData(realGraph, start, end, insp);
        DijkstraAlgorithm Dijk = new DijkstraAlgorithm(data);
        BellmanFordAlgorithm Bell = new BellmanFordAlgorithm(data);
        AStarAlgorithm Ast = new AStarAlgorithm(data);

        ShortestPathSolution bell_sol = Bell.run();
        ShortestPathSolution djik_sol = Dijk.run();
        ShortestPathSolution ast_sol = Ast.run();

        assertEquals(bell_sol.isFeasible(),djik_sol.isFeasible());
        assertEquals(bell_sol.isFeasible(),ast_sol.isFeasible());

        if (bell_sol.isFeasible()) {
            assertEquals(bell_sol.getPath().getLength(), djik_sol.getPath().getLength(), 1e-6);
            assertEquals(bell_sol.getPath().getLength(), ast_sol.getPath().getLength(), 1e-6);
        }
    }

    @Test
    public void testSameNode() {
        Node start = realGraph.get(5);
        Node end = realGraph.get(5);

        ArcInspector insp = ArcInspectorFactory.getAllFilters().get(0); // no filters
        ShortestPathData data = new ShortestPathData(realGraph, start, end, insp);
        DijkstraAlgorithm Dijk = new DijkstraAlgorithm(data);
        ShortestPathSolution djik_sol = Dijk.run();

        AStarAlgorithm Ast = new AStarAlgorithm(data);
        ShortestPathSolution ast_sol = Ast.run();

        assertEquals(djik_sol.isFeasible(), false);
        assertEquals(ast_sol.isFeasible(), false);
    }

    @Test
    public void veryLongPath() {
        Node start = realGraph.get(22596);
        Node end = realGraph.get(3030);

        testShortestPathAlgorithm(start, end, realGraph,0);
        testShortestPathAlgorithm(start, end, realGraph,5);
    }

    @Test
    public void testShortestPathLongerThanFastestPath() {
        Node start = realGraph.get(50);
        Node end = realGraph.get(950);
        ArcInspector insp_length = ArcInspectorFactory.getAllFilters().get(1);
        ArcInspector insp_time = ArcInspectorFactory.getAllFilters().get(2);
        ShortestPathData data_length = new ShortestPathData(realGraph, start, end, insp_length);
        ShortestPathData data_time = new ShortestPathData(realGraph, start, end, insp_time);
        DijkstraAlgorithm dijk_length = new DijkstraAlgorithm(data_length);
        DijkstraAlgorithm dijk_time = new DijkstraAlgorithm(data_time);
        AStarAlgorithm ast_length = new AStarAlgorithm(data_length);
        AStarAlgorithm ast_time= new AStarAlgorithm(data_time);

        ShortestPathSolution dijk_sol_length = dijk_length.run();
        ShortestPathSolution dijk_sol_time  = dijk_time.run();
        ShortestPathSolution ast_sol_time = ast_time.run();
        ShortestPathSolution ast_sol_length = ast_length.run();
        assertTrue(dijk_sol_length.getPath().getMinimumTravelTime() >= dijk_sol_time.getPath().getMinimumTravelTime());
        assertTrue(dijk_sol_length.getPath().getLength()<= dijk_sol_time.getPath().getLength());
        assertTrue(ast_sol_length.getPath().getMinimumTravelTime() >= ast_sol_time.getPath().getMinimumTravelTime());
        assertTrue(ast_sol_length.getPath().getLength()<= ast_sol_time.getPath().getLength());

    }

    @Test
    public void testNoBusWay() {
        Node start = realGraph.get(5402);
        Node end = realGraph.get(5932);

        ArcInspector insp = ArcInspectorFactory.getAllFilters().get(1); // Only roads open for cars
        ShortestPathData data = new ShortestPathData(realGraph, start, end, insp);
        DijkstraAlgorithm Dijk = new DijkstraAlgorithm(data);
        BellmanFordAlgorithm Bell = new BellmanFordAlgorithm(data);
        AStarAlgorithm Ast = new AStarAlgorithm(data);

        ShortestPathSolution bell_sol = Bell.run();
        ShortestPathSolution djik_sol = Dijk.run();
        ShortestPathSolution ast_sol = Ast.run();

        assertEquals(bell_sol.isFeasible(),djik_sol.isFeasible());

        for (Arc arc : djik_sol.getPath().getArcs()) {
            assertNotEquals(arc.getDestination(), realGraph.get(15765));
            assertNotEquals(arc.getOrigin(), realGraph.get(15765));
        }

        for (Arc arc : bell_sol.getPath().getArcs()) {
            assertNotEquals(arc.getDestination(), realGraph.get(15765));
            assertNotEquals(arc.getOrigin(), realGraph.get(15765));
        }

        for (Arc arc : ast_sol.getPath().getArcs()) {
            assertNotEquals(arc.getDestination(), realGraph.get(15765));
            assertNotEquals(arc.getOrigin(), realGraph.get(15765));
        }


    }

}
