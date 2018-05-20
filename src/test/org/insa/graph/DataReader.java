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

import javax.xml.crypto.Data;

public class DataReader {

    public static String path ;

    public DataReader(String path){
        this.path = path;
    }

    public String outputLine(int lineNb){

    String line = null ;
    try {
        // FileReader reads text files in the default encoding.
        FileReader fileReader =
                new FileReader(this.path);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader =
                new BufferedReader(fileReader);

        for(int x = 0; x < lineNb; x++) {
           bufferedReader.readLine();
        }
        line = bufferedReader.readLine();
        bufferedReader.close();
    }
        catch(FileNotFoundException ex) {
        System.out.println(
                "Unable to open file '" +
                        this.path + "'");
    }
        catch(IOException ex) {
        System.out.println(
                "Error reading file '"
                        + this.path + "'");
    }

    return line;
}
    public int nbLines(){
        int lines = 0 ;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(this.path);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while(bufferedReader.readLine() != null) lines++;

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            this.path + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + this.path + "'");
        }

        return lines;
    }

    }