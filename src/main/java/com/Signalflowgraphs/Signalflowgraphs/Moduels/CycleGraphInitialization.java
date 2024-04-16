package com.Signalflowgraphs.Signalflowgraphs.Moduels;


import java.util.ArrayList;
import java.util.List;

public class CycleGraphInitialization {
    private Graph<Integer> graph;

    private List<Edge<Integer>> edgeList;

    private int numberOfEdges = 0;

    public void graphInitialize (List<SourceDestinations> list){
        graph = new Graph<>(true);
        edgeList = new ArrayList<>();
        for (SourceDestinations sourceDestinations : list){
            for (Pair pair : sourceDestinations.getDestinations()){
                edgeList.add(new Edge<Integer>(
                        new Vertex<>(sourceDestinations.getSource()),
                        new Vertex<>(pair.getDestination()),
                        true,
                        pair.getWeight()
                ));
                graph.addEdge(sourceDestinations.getSource(), pair.getDestination());
                numberOfEdges++;
            }
        }
    }

    public Graph<Integer> getGraph() {
        return graph;
    }

    public List<Edge<Integer>> getEdgeList() {
        return edgeList;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }
}
