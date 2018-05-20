package org.insa.graph;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;

import org.insa.algo.AbstractInputData;
import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.*;
import org.insa.graph.RoadInformation.RoadType;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataGenerator {

    private static Graph graph ;

    public static void main(String args[]) throws java.io.IOException {
        String mapName = System.getProperty("user.dir") + "/maps/toulouse.mapgr";
        GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        graph = reader.read();
        File file = new File("input.txt");
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            PrintWriter pw = new PrintWriter(file);
            pw.println("toulouse.mapgr");

            int n = 0 ;
            while (n<100) {
                Random rand = new Random();
                int or = rand.nextInt(graph.size()) + 1;
                int des = rand.nextInt(graph.size()) + 1;
                ArcInspector insp = ArcInspectorFactory.getAllFilters().get(0); // arcInspectorId = 0 ici, changer ??
                ShortestPathData data = new ShortestPathData(graph, graph.get(or), graph.get(des), insp);
                BellmanFordAlgorithm Bell = new BellmanFordAlgorithm(data);
                ShortestPathSolution bell_sol = Bell.run();
                if (bell_sol.isFeasible()) {
                    pw.println(or+";"+des);
                    n++;
                }
            }
                pw.close();
            } catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

