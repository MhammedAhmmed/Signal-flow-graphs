package com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic;

import com.Signalflowgraphs.Signalflowgraphs.Moduels.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;

@Service
public class AllCyclesInDirectedGraphJohnson {
    private Set<Vertex<Integer>> blockedSet;
    private Map<Vertex<Integer>, Set<Vertex<Integer>>> blockedMap;
    private Deque<Vertex<Integer>> stack;
    private List<List<Vertex<Integer>>> allCycles;

    private List<Integer>cyclesGains;

    private List<List<Vertex<Integer>>>distinctCycles;

    private List<List<List<Vertex<Integer>>>> allCyclesNonTouchingPaths;

    private List<List<Integer>> cyclesNonTouchingPathsGains;

    private List<List<List<Vertex<Integer>>>> twoNonTouchingCyclesNonTouchingPaths;

    private List<List<Integer>> twoNonTouchingCyclesNonTouchingPathsGains;
    private List<List<List<List<Vertex<Integer>>>>> allNNonTouchingCycles;
    private List<List<Integer>> allNNonTouchingCyclesGains;
    private final int maxN = 10;

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

    public void findAllCycles (CycleGraphInitialization graphInitialization){
        Graph<Integer> graph = graphInitialization.getGraph();

        List<Edge<Integer>> edgeList = graphInitialization.getEdgeList();
        Map<Edge<Integer>, List<Integer>>map = new HashMap<>();
        for(int i = 0; i < graphInitialization.getNumberOfEdges(); i++){
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
        for(int i = 0; i < allCycles.size(); i++){
            boolean equal = false;
            for(int j = i + 1; j < allCycles.size(); j++){
                if(allCycles.get(i).equals(allCycles.get(j))){
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

    public void findAllCyclesNonTouchingPaths(AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph){
        List<List<Integer>> paths = allForwardPathsInDirectedGraph.getAllPaths();
        allCyclesNonTouchingPaths = new ArrayList<>();
        cyclesNonTouchingPathsGains = new ArrayList<>();
        for(List<Integer> path : paths){
            List<List<Vertex<Integer>>> cycles = new ArrayList<>();
            List<Integer> cyclesGainPath = new ArrayList<>();
            for(int i = 0; i < allCycles.size(); i++){
                boolean isTouching = false;
                for(Integer value : path){
                    if(allCycles.get(i).contains(new Vertex<Integer>(value))){
                        isTouching = true;
                        break;
                    }
                }
                if (!isTouching) {
                    cycles.add(allCycles.get(i));
                    cyclesGainPath.add(cyclesGains.get(i));
                }
            }
            allCyclesNonTouchingPaths.add(cycles);
            cyclesNonTouchingPathsGains.add(cyclesGainPath);
        }
    }

    public void findAllTwoNonTouchingCyclesPaths() {
        twoNonTouchingCyclesNonTouchingPaths = new ArrayList<>();
        twoNonTouchingCyclesNonTouchingPathsGains = new ArrayList<>();
        for (int k = 0; k < allCyclesNonTouchingPaths.size(); k++){
            List<List<Vertex<Integer>>> twoNonTouchingCyclesNonTouchingPath = new ArrayList<>();
            List<Integer> twoNonTouchingCyclesNonTouchingPathGains = new ArrayList<>();
            for (int i = 0; i < allCyclesNonTouchingPaths.get(k).size(); i++){
                for(int j = i + 1; j < allCyclesNonTouchingPaths.get(k).size(); j++){
                    boolean isTouching = false;
                    for(Vertex<Integer> v : allCyclesNonTouchingPaths.get(k).get(i)){
                        if(allCyclesNonTouchingPaths.get(k).get(j).contains(v)){
                            isTouching = true;
                            break;
                        }
                    }
                    if(!isTouching) {
                        twoNonTouchingCyclesNonTouchingPath.add(allCyclesNonTouchingPaths.get(k).get(i));
                        twoNonTouchingCyclesNonTouchingPath.add(allCyclesNonTouchingPaths.get(k).get(j));
                        twoNonTouchingCyclesNonTouchingPathGains.add(cyclesNonTouchingPathsGains.get(k).get(i) * cyclesNonTouchingPathsGains.get(k).get(j));
                    }
                }
            }
            twoNonTouchingCyclesNonTouchingPaths.add(twoNonTouchingCyclesNonTouchingPath);
            twoNonTouchingCyclesNonTouchingPathsGains.add(twoNonTouchingCyclesNonTouchingPathGains);
        }
    }


    private boolean areNonTouching(List<Vertex<Integer>> cycle1, List<Vertex<Integer>> cycle2) {
        // Iterate over vertices of cycle1
        for (Vertex<Integer> vertex1 : cycle1) {
            // Iterate over vertices of cycle2
            for (Vertex<Integer> vertex2 : cycle2) {
                // Check if any vertex of cycle1 is the same as any vertex of cycle2
                if (vertex1.equals(vertex2)) {
                    return false; // Cycles are touching
                }
            }
        }
        return true; // Cycles are non-touching
    }

    private void generateNonTouchingCombinations(int n, int index, List<List<Vertex<Integer>>> currentCombination, List<List<List<Vertex<Integer>>>> result) {
        if (n == 0) {
            result.add(new ArrayList<>(currentCombination));
            return;
        }

        for (int i = index; i < allCycles.size(); i++) {
            boolean isValid = true;
            for (List<Vertex<Integer>> cycle : currentCombination) {
                if (!areNonTouching(cycle, allCycles.get(i))) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                currentCombination.add(allCycles.get(i));
                generateNonTouchingCombinations(n - 1, i + 1, currentCombination, result);
                currentCombination.remove(currentCombination.size() - 1);
            }
        }
    }

    private List<List<List<Vertex<Integer>>>> getNonTouchingCycleCombinations(int n) {
        List<List<List<Vertex<Integer>>>> result= new ArrayList<>();
        generateNonTouchingCombinations(n, 0, new ArrayList<>(), result);
        return result;
    }

    public void findAllCombinationsOfNNonTouchingCycles(){
        allNNonTouchingCycles = new ArrayList<>();
        for (int i = 2; i <= maxN; i++) {
            List<List<List<Vertex<Integer>>>> combinations = getNonTouchingCycleCombinations(i);
            if (combinations.isEmpty()) break;
            allNNonTouchingCycles.add(combinations);
        }
    }

    // Function to calculate the total gain of a combination by multiplying individual gains
    private int calculateCombinationGain(List<List<Vertex<Integer>>> combination, List<Integer> cyclesGains) {
        int totalGain = 1; // Initialize total gain to 1 for multiplication
        for (List<Vertex<Integer>> cycle : combination) {
            int cycleIndex = allCycles.indexOf(cycle);
            totalGain *= cyclesGains.get(cycleIndex); // Multiply cycle gain with total gain
        }
        return totalGain;
    }

    // Function to get combinations of n non-touching cycles with their gains
    private List<Integer> getNonTouchingCycleCombinationsWithGains(List<List<List<Vertex<Integer>>>> combinations) {
        List<Integer> result = new ArrayList<>();
        for (List<List<Vertex<Integer>>> combination : combinations) {
            int gain = calculateCombinationGain(combination, cyclesGains);
            result.add(gain);
        }
        return result;
    }
    public void findAllCombinationsOfNNonTouchingCyclesGains(){
        allNNonTouchingCyclesGains = new ArrayList<>();
        for (List<List<List<Vertex<Integer>>>> combinations : allNNonTouchingCycles){
            List<Integer> result = getNonTouchingCycleCombinationsWithGains(combinations);
            allNNonTouchingCyclesGains.add(result);
        }
    }


    public List<List<Integer>> getAllCycles() {
        List<List<Integer>> allCyclesList = new ArrayList<>();
        for (List<Vertex<Integer>> cycle : allCycles){
            List<Integer> cycleList = new ArrayList<>();
            for(Vertex<Integer> vertex : cycle){
                cycleList.add((int) vertex.getId());
            }
            allCyclesList.add(cycleList);
        }
        return allCyclesList;
    }

    public List<Integer> getCyclesGains() {
        return cyclesGains;
    }

    public List<List<String>> getAllCombinationsOfNNonTouchingCycles(){
        List<List<String>> AllCombinationsOfNNonTouchingCycles = new ArrayList<>();
        for (List<List<List<Vertex<Integer>>>> combinations : allNNonTouchingCycles) {
            List<String> list = new ArrayList<>();
            for (List<List<Vertex<Integer>>> combination : combinations){
                list.add(combination.toString());
            }
            AllCombinationsOfNNonTouchingCycles.add(list);
        }
        return AllCombinationsOfNNonTouchingCycles;
    }


    public List<List<Integer>> getAllNNonTouchingCyclesGains() {
        return allNNonTouchingCyclesGains;
    }

    public List<List<Vertex<Integer>>> getDistinctCycles() {
        return distinctCycles;
    }

    public List<List<List<Integer>>> getAllCyclesNonTouchingPaths() {
        List<List<List<Integer>>> allCyclesNonTouchingPathsList = new ArrayList<>();
        for (List<List<Vertex<Integer>>> path : allCyclesNonTouchingPaths){
            List<List<Integer>> allCyclesNonTouchingPath = new ArrayList<>();
            for (List<Vertex<Integer>> cycle : path){
                List<Integer> cycleNonTouchingPath = new ArrayList<>();
                for(Vertex<Integer> vertex : cycle){
                    cycleNonTouchingPath.add((int) vertex.getId());
                }
                allCyclesNonTouchingPath.add(cycleNonTouchingPath);
            }
            allCyclesNonTouchingPathsList.add(allCyclesNonTouchingPath);
        }
        return allCyclesNonTouchingPathsList;
    }

    public List<List<Integer>> getCyclesNonTouchingPathsGains() {
        return cyclesNonTouchingPathsGains;
    }

    public List<List<List<Integer>>> getTwoNonTouchingCyclesNonTouchingPaths() {
        List<List<List<Integer>>> twoNonTouchingCyclesNonTouchingPathsList = new ArrayList<>();
        for (List<List<Vertex<Integer>>> path : twoNonTouchingCyclesNonTouchingPaths){
            List<List<Integer>> twoNonTouchingCyclesPath = new ArrayList<>();
            for (List<Vertex<Integer>> cycle : path) {
                List<Integer> twoNonTouchingCyclePath = new ArrayList<>();
                for (Vertex<Integer> vertex : cycle){
                    twoNonTouchingCyclePath.add((int) vertex.getId());
                }
                twoNonTouchingCyclesPath.add(twoNonTouchingCyclePath);
            }
            twoNonTouchingCyclesNonTouchingPathsList.add(twoNonTouchingCyclesPath);
        }
        return twoNonTouchingCyclesNonTouchingPathsList;
    }

    public List<List<Integer>> getTwoNonTouchingCyclesNonTouchingPathsGains() {
        return twoNonTouchingCyclesNonTouchingPathsGains;
    }


}