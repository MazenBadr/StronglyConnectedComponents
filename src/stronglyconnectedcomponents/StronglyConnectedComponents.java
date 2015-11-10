/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stronglyconnectedcomponents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mazenbadr
 */
public class StronglyConnectedComponents {

    private static int time = 0;
    private static HashSet<Integer> isExplored = new HashSet<>();
    private static Hashtable<Integer, Integer> finishingTime = new Hashtable<>();

    public static void main(String[] args) {
        Hashtable<Integer, ArrayList<Integer>> graph = new Hashtable<>();
        Hashtable<Integer, ArrayList<Integer>> reversedGraph = new Hashtable<>();
        InputStream stream = ClassLoader.getSystemResourceAsStream("SCC.txt");
        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
        String line;
        try {
            while ((line = buffer.readLine()) != null) {
                String[] vals = line.trim().split("\\s+");
                ArrayList<Integer> temp = new ArrayList<>();
                ArrayList<Integer> rTemp = new ArrayList<>();
                int key = Integer.parseInt(vals[0]);
                temp.add(Integer.parseInt(vals[1]));
                int rKey = Integer.parseInt(vals[1]);
                rTemp.add(Integer.parseInt(vals[0]));
                if (graph.putIfAbsent(key, temp) != null) {
                    graph.get(key).add(temp.get(0));
                }
                if (reversedGraph.putIfAbsent(rKey, rTemp) != null) {
                    reversedGraph.get(rKey).add(rTemp.get(0));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(StronglyConnectedComponents.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<Integer> sizeOfSCC = new ArrayList<>();
        for (int i = reversedGraph.size(); i > 0; i--) {
            if (!isExplored.contains(i)) {
                int[] tempo = {1};
                stackDFS(reversedGraph, i, tempo);
            }
        }
        isExplored = new HashSet<>();
        for (int i = finishingTime.size(); i > 0; i--) {
            int key = finishingTime.get(i);
            if (!isExplored.contains(key)) {
                int[] tempo = {1};
                sizeOfSCC.add(stackDFS(graph, key, tempo));
            }
        }
        sizeOfSCC.sort(null);
        sizeOfSCC.stream().forEach((size) -> {
            System.out.println(size);
        });
    }

    public static int DFS(Hashtable<Integer, ArrayList<Integer>> graph, Integer node, int[] size) {
        isExplored.add(node);
        ArrayList<Integer> children = new ArrayList<>();
        children = graph.get(node);
        if (children != null) {
            for (int child : children) {
                if (!isExplored.contains(child)) {
                    size[0]++;
                    DFS(graph, child, size);
                }
            }
        }
        time++;
        finishingTime.putIfAbsent(time, node);
        return size[0];
    }

    public static int stackDFS(Hashtable<Integer, ArrayList<Integer>> graph, int node, int[] size) {
        Stack<Integer> stack = new Stack<>();
        stack.push(node);
        int v = 0;
        while (!stack.isEmpty()) {

            v = stack.pop();
            if (!isExplored.contains(v)) {
                isExplored.add(v);
                ArrayList<Integer> children = new ArrayList<>();
                children = graph.get(v);
                if (children != null) {
                    for (int child : children) {
                        if (!isExplored.contains(child)) {
//                            isExplored.add(child);
                            size[0]++;
                            stack.push(child);
                        } else {
                            time++;
                            finishingTime.putIfAbsent(time, v);
                        }
                    }
                }else {
                            time++;
                            finishingTime.putIfAbsent(time, v);
                        }

            }
        }

        return size[0];
    }
}
