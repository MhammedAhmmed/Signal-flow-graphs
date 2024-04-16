package com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic;

import com.Signalflowgraphs.Signalflowgraphs.Moduels.Pair;
<<<<<<< HEAD
import com.Signalflowgraphs.Signalflowgraphs.Moduels.PathGraphInitialization;
=======
>>>>>>> 4a2071dec2f7676c05ce37b279ba630a8b4ab4fe

import java.util.*;

public class AllForwardPathsInDirectedGraph {
    private List<List<Integer>>allPaths;
    private List<Integer> allPathsGain;
    private void pathsDFS(int V, int destination, Map<Integer, List<Pair>> adj, boolean[] visited, List<Integer> path, List<List<Integer>> allPaths, Integer gain, List<Integer> allPathsGain){
        visited[V] = true;
        path.add(V);

        if(V == destination){
            allPaths.add(new ArrayList<>(path));
            allPathsGain.add(gain);
            visited[V] = false;
            path.remove(path.size()-1);
            return;
        }

        for(Pair v: adj.get(V)) {
            if (!visited[v.getDestination()]) {
                gain *= v.getWeight();
                pathsDFS(v.getDestination(), destination, adj, visited, path, allPaths, gain, allPathsGain);
                gain /= v.getWeight();
            }
        }

        visited[V] =false;
        path.remove(path.size()-1);
    }
<<<<<<< HEAD
    public void findAllPaths(PathGraphInitialization graphInitialization, int source, int destination){
        Map<Integer, List<Pair>> adj = graphInitialization.getGraph();
        allPaths = new ArrayList<>();
        allPathsGain = new ArrayList<>();

        int N = adj.size() + 1;
=======
    public void findAllPaths(Map<Integer, List<Pair>> adj, int source, int destination){
        allPaths = new ArrayList<>();
        allPathsGain = new ArrayList<>();

        int N = adj.size();
>>>>>>> 4a2071dec2f7676c05ce37b279ba630a8b4ab4fe
        boolean[] visited = new boolean[N];
        Arrays.fill(visited, false);

        List<Integer>path = new ArrayList<>();
        Integer gain = 1;
        pathsDFS(source, destination, adj, visited, path, allPaths, gain, allPathsGain);
    }

    public List<List<Integer>> getAllPaths() {
        return allPaths;
    }

    public List<Integer> getAllPathsGain() {
        return allPathsGain;
    }
}
