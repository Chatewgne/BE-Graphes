package org.insa.graph;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.io.*;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.*;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;

public class DataGenerator {

    private String mapPath ;
    private int size ;
    private String outputPath ;
    private int arcInspector;

    public DataGenerator(String mapPath, int size, String outputPath, int arcInspector) {
        this.mapPath = mapPath;
        this.size = size;
        this.outputPath = outputPath;
        this.arcInspector = arcInspector;
    }

    public void createFile() throws java.io.IOException {
        GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") +mapPath))));

        Graph graph = reader.read();
        File file = new File(outputPath);
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            PrintWriter pw = new PrintWriter(file);
            pw.println(mapPath);

            int n = 0 ;
            while (n<size) {
                Random rand = new Random();
                int or = rand.nextInt(graph.size()) + 1;
                int des = rand.nextInt(graph.size()) + 1;
                ArcInspector insp = ArcInspectorFactory.getAllFilters().get(arcInspector);
                ShortestPathData data = new ShortestPathData(graph, graph.get(or), graph.get(des), insp);
                AStarAlgorithm Ast = new AStarAlgorithm(data);
                ShortestPathSolution bell_sol = Ast.run();
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

