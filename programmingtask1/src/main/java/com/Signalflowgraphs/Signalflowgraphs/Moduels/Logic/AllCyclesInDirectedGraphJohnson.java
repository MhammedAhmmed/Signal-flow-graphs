package com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic;

import com.Signalflowgraphs.Signalflowgraphs.Moduels.*;

import java.util.*;
import java.util.List;

public class AllCyclesInDirectedGraphJohnson {
    Set<Vertex<Integer>> blockedSet;
    Map<Vertex<Integer>, Set<Vertex<Integer>>> blockedMap;
    Deque<Vertex<Integer>> stack;
    public List<List<Vertex<Integer>>> allCycles;

    Graph<Integer> graph;

    List<Edge<Integer>> edgeList;

    public List<Integer>cyclesGains;

    public List<List<Vertex<Integer>>>distinctCycles;

    public List<List<Vertex<Integer>>> simpleCycles(Graph<Integer> graph) {

        blockedSet = new HashSet<>();
        blockedMap = new HashMap<>();
        stack = new LinkedList<>();
        allCycles = new ArrayList<>();
        long startIndex = 1;
        TarjanStronglyConnectedComponent tarjan = new TarjanStronglyConnectedComponent();
        while(startIndex <= graph.getAllVertex().size()) {
            Graph<Integer> subGraph = createSubGraph(startIndex, graph);
            List<Set<Vertex<Integer>>> sccs = tarjan.scc(subGraph);

            Optional<Vertex<Integer>> maybeLeastVertex = leastIndexSCC(sccs, subGraph);
            if(maybeLeastVertex.isPresent()) {
                Vertex<Integer> leastVertex = maybeLeastVertex.get();
                blockedSet.clear();
                blockedMap.clear();
                findCyclesInSCG(leastVertex, leastVertex);
                startIndex = leastVertex.getId() + 1;
            } else {
                break;
            }
        }
        return allCycles;
    }

    private Optional<Vertex<Integer>> leastIndexSCC(List<Set<Vertex<Integer>>> sccs, Graph<Integer> subGraph) {
        long min = Integer.MAX_VALUE;
        Vertex<Integer> minVertex = null;
        Set<Vertex<Integer>> minScc = null;
        for(Set<Vertex<Integer>> scc : sccs) {
            if(scc.size() == 1) {
                continue;
            }
            for(Vertex<Integer> vertex : scc) {
                if(vertex.getId() < min) {
                    min = vertex.getId();
                    minVertex = vertex;
                    minScc = scc;
                }
            }
        }

        if(minVertex == null) {
            return Optional.empty();
        }
        Graph<Integer> graphScc = new Graph<>(true);
        for(Edge<Integer> edge : subGraph.getAllEdges()) {
            if(minScc.contains(edge.getVertex1()) && minScc.contains(edge.getVertex2())) {
                graphScc.addEdge(edge.getVertex1().getId(), edge.getVertex2().getId());
            }
        }
        return Optional.of(graphScc.getVertex(minVertex.getId()));
    }

    private void unblock(Vertex<Integer> u) {
        blockedSet.remove(u);
        if(blockedMap.get(u) != null) {
            blockedMap.get(u).forEach( v -> {
                if(blockedSet.contains(v)) {
                    unblock(v);
                }
            });
            blockedMap.remove(u);
        }
    }
    private boolean findCyclesInSCG(
            Vertex<Integer> startVertex,
            Vertex<Integer> currentVertex) {
        boolean foundCycle = false;
        stack.push(currentVertex);
        blockedSet.add(currentVertex);

        for (Edge<Integer> e : currentVertex.getEdges()) {
            Vertex<Integer> neighbor = e.getVertex2();

            if (neighbor == startVertex) {
                List<Vertex<Integer>> cycle = new ArrayList<>();
                stack.push(startVertex);
                cycle.addAll(stack);
                Collections.reverse(cycle);
                stack.pop();
                allCycles.add(cycle);
                foundCycle = true;
            } //explore this neighbor only if it is not in blockedSet.
            else if (!blockedSet.contains(neighbor)) {
                boolean gotCycle =
                        findCyclesInSCG(startVertex, neighbor);
                foundCycle = foundCycle || gotCycle;
            }
        }
        //if cycle is found with current vertex then recursively unblock vertex and all vertices which are dependent on this vertex.
        if (foundCycle) {
            //remove from blockedSet  and then remove all the other vertices dependent on this vertex from blockedSet
            unblock(currentVertex);
        } else {
            //if no cycle is found with current vertex then don't unblock it. But find all its neighbors and add this
            //vertex to their blockedMap. If any of those neighbors ever get unblocked then unblock current vertex as well.
            for (Edge<Integer> e : currentVertex.getEdges()) {
                Vertex<Integer> w = e.getVertex2();
                Set<Vertex<Integer>> bSet = getBSet(w);
                bSet.add(currentVertex);
            }
        }
        //remove vertex from the stack.
        stack.pop();
        return foundCycle;
    }

    private Set<Vertex<Integer>> getBSet(Vertex<Integer> v) {
        return blockedMap.computeIfAbsent(v, (key) ->
                new HashSet<>() );
    }

    private Graph createSubGraph(long startVertex, Graph<Integer> graph) {
        Graph<Integer> subGraph = new Graph<>(true);
        for(Edge<Integer> edge : graph.getAllEdges()) {
            if(edge.getVertex1().getId() >= startVertex && edge.getVertex2().getId() >= startVertex) {
                subGraph.addEdge(edge.getVertex1().getId(), edge.getVertex2().getId());
            }
        }
        return subGraph;
    }

//    Graph initialization in cycles
    public void graphInitialize (List<SourceDestinations> list){
        graph = new Graph<>(true);
        edgeList = new ArrayList<>();
        for (SourceDestinations sourceDestinations : list){
            for (Pair pair : sourceDestinations.getDestinations()){
                edgeList.add(new Edge<>(
                        new Vertex<>(sourceDestinations.getSource()),
                        new Vertex<>(pair.getDestination()),
                        true,
                        pair.getWeight()
                ));
                graph.addEdge(sourceDestinations.getSource(), pair.getDestination());
            }
        }
    }

    public void findAllCycles (){
        Map<Edge<Integer>, List<Integer>>map = new HashMap<>();
        for(int i = 0; i < 10; i++){
            List<Integer> list;
            if(!map.containsKey(edgeList.get(i))) {
                list = new ArrayList<>();
            }else{
                list = map.get(edgeList.get(i));
            }
            list.add(edgeList.get(i).getWeight());
            map.put(edgeList.get(i), list);
        }
        allCycles = simpleCycles(graph);

//        Set all distinct cycles using comparison
        distinctCycles = new ArrayList<>();
        for(int i = 0; i < allCycles.size() - 1; i++){
            boolean equal = false;
            for(int j = i + 1; j < allCycles.size(); j++){
                if(allCycles.get(i) == allCycles.get(j)){
                    equal = true;
                    break;
                }
            }
            if(!equal)
                distinctCycles.add(allCycles.get(i));
        }

//        Get gains of all cycles
        cyclesGains = new ArrayList<>();
        for(List<Vertex<Integer>> vertexList : distinctCycles){
            int n = vertexList.size();
            List<Integer>tempList = new ArrayList<>();
            for(int i = 0; i < n - 1; i++){
                Edge<Integer> edge = new Edge<>(vertexList.get(i), vertexList.get(i+1));
                List<Integer>edgeWeights = map.get(edge);
                if(tempList.size() == 0)
                    tempList = edgeWeights;
                else {
                    List<Integer> temp2List = new ArrayList<>();
                    for (Integer weight : tempList) {
                        for (Integer weight2 : edgeWeights) {
                            temp2List.add(weight * weight2);
                        }
                    }
                    tempList = temp2List;
                }
            }
            cyclesGains.addAll(tempList);
        }
    }
}