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


public class PerformanceTest {

    private static Graph graph;

    @BeforeClass
    public static void initAll() throws IOException {
        String mapName = "../maps/toulouse.mapgr";
        GraphReader reader =  new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        graph = reader.read();
        /*PrintWriter printw = new PrintWriter("inpuuut.txt","UTF-8");
        printw.println("KOUKOU");
        printw.println("T");
        printw.close();*/
        File file = new File("save.txt");
        try {

            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            PrintWriter pw = new PrintWriter(file);
            pw.println("Hello World");
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void generateInputFile() throws IOException{
       // java.nio.file.Path p = Paths.get("./input.txt");
        System.out.println(("KOUKOU"));


          //  FileWriter filew = new FileWriter("./input.txt");

        /*
        for (int n=0;n<2;n++){

            Random rand = new Random();
            int  or = rand.nextInt(graph.size()) + 1;
            int des = rand.nextInt(graph.size()) + 1;
            ArcInspector insp = ArcInspectorFactory.getAllFilters().get(0); // arcInspectorId = 0 ici, changer ??
            ShortestPathData data = new ShortestPathData(graph, graph.get(or), graph.get(des), insp);
            BellmanFordAlgorithm Bell = new BellmanFordAlgorithm(data);
            ShortestPathSolution bell_sol = Bell.run();
            if(bell_sol.isFeasible()){
               /* try (PrintWriter out = new PrintWriter(
                        Files.newOutputStream(p, CREATE, APPEND))) {
                    out.println(or+";"+des);
                } catch (IOException x) {
                    System.err.println(x);
                }*//*
               printw.println("bite");
            }
        }
        printw.close();*/
    }
/*
    private void generateOutputFile(Node u, Node i, int arcInspectorId){
        ArcInspector insp = ArcInspectorFactory.getAllFilters().get(arcInspectorId);
        ShortestPathData data = new ShortestPathData(graph, u, i, insp);
    }*/




}
