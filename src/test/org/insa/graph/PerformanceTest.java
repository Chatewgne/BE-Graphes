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

    private static void generateInputFile(String mapPath, int size, String outputPath, int arcInsp) throws IOException{
    DataGenerator da = new DataGenerator(mapPath, size, outputPath, arcInsp);
        da.createFile();
    DataReader d = new DataReader(outputPath);
        System.out.println(d.outputLine(0));
        System.out.println(d.nbLines());
}

    private static void runTestOnFile(String inputFile, int arcInspectorId, String outputFile) throws IOException {
        ArcInspector insp = ArcInspectorFactory.getAllFilters().get(arcInspectorId);
        DataReader dataReader = new DataReader(inputFile);
        String mapName =System.getProperty("user.dir") + dataReader.outputLine(0); // renvoie ligne 0 -> nom de la map
        GraphReader reader =  new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        Graph graph = reader.read();
        File file1= new File(outputFile+".txt");
        File file2= new File(outputFile+".csv");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            PrintWriter pwtxt = new PrintWriter(file1);
            PrintWriter pwcsv = new PrintWriter(file2);
            pwtxt.println("Carte de tests :"+dataReader.outputLine(0));
            pwcsv.println("Bellman temps,Bellman nodes évalués,Bell taille max du tas,Dijkstra temps,Dijkstra nodes évalués,Dijkstra taille max du tas,Astar temps,Astar nodes évalués,Astar taille max du tas");

        for(int i =1;i< dataReader.nbLines();i++){
            String[] nodes = dataReader.outputLine(i).split(";");
            int origin = Integer.parseInt(nodes[0]);
            int destination= Integer.parseInt(nodes[1]);
            pwtxt.println("=====================================================================");
            pwtxt.println("Test " + i + ": node d'origine "+origin+" -> node de destination "+destination);
            ShortestPathData data = new ShortestPathData(graph, graph.get(origin), graph.get(destination), insp);
            BellmanFordAlgorithm Bell = new BellmanFordAlgorithm(data);
            ShortestPathSolution bell_sol = Bell.run();
            DijkstraAlgorithm Dijk = new DijkstraAlgorithm(data);
            ShortestPathSolution dijk_sol = Dijk.run();
            AStarAlgorithm Asta = new AStarAlgorithm(data);
            ShortestPathSolution asta_sol = Asta.run();
            pwtxt.println("BellmanFord : éxecution en "+ bell_sol.getSolvingTime().toMillis() + " ms");
            pwtxt.println("Dijkstra : " + dijk_sol.nodeEvaluated + " nodes évalués en " + dijk_sol.getSolvingTime().toMillis() + " ms avec un tas de "+ dijk_sol.maxHeapSize +" noeuds max");
            pwtxt.println("A* : " + asta_sol.nodeEvaluated+ " nodes évalués en "+ asta_sol.getSolvingTime().toMillis() + " ms avec un tas de "+ asta_sol.maxHeapSize +" noeuds max");
            pwcsv.println(bell_sol.getSolvingTime().toMillis()+",0,0,"+dijk_sol.getSolvingTime().toMillis()+","+dijk_sol.nodeEvaluated+","+dijk_sol.maxHeapSize+","+asta_sol.getSolvingTime().toMillis()+","+asta_sol.nodeEvaluated+","+asta_sol.maxHeapSize);
        }
            pwtxt.close();
            pwcsv.close();

        } catch(FileNotFoundException e){
        e.printStackTrace();
        }

    }

    public static void main(String args[]) throws IOException{

        generateInputFile("/maps/ivory-coast.mapgr", 20, "ivorycoast20.txt", 0);
        runTestOnFile("ivorycoast20.txt",0,"ivorycoast20results");
    }

}
