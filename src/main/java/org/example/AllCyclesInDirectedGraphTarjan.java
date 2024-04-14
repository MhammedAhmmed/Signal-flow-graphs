package org.example;

import java.util.*;

public class AllCyclesInDirectedGraphTarjan {

    private Set<Vertex<Integer>> visited;
    private Deque<Vertex<Integer>> pointStack;
    private Deque<Vertex<Integer>> markedStack;
    private Set<Vertex<Integer>> markedSet;

    public AllCyclesInDirectedGraphTarjan() {
        reset();
    }

    private void reset() {
        visited = new HashSet<>();
        pointStack = new LinkedList<>();
        markedStack = new LinkedList<>();
        markedSet = new HashSet<>();
    }

    public List<List<Vertex<Integer>>> findAllSimpleCycles(Graph<Integer> graph) {
        reset();
        List<List<Vertex<Integer>>> result = new ArrayList<>();
        for (Vertex<Integer> vertex : graph.getAllVertex()) {
            findAllSimpleCycles(vertex, vertex, result);
            visited.add(vertex);
            while (!markedStack.isEmpty()) {
                markedSet.remove(markedStack.pollFirst());
            }
        }
        return result;
    }

    private boolean findAllSimpleCycles(Vertex start, Vertex<Integer> current, List<List<Vertex<Integer>>> result) {
        boolean hasCycle = false;
        pointStack.offerFirst(current);
        markedSet.add(current);
        markedStack.offerFirst(current);

        for (Vertex<Integer> w : current.getAdjacentVertexes()) {
            if (visited.contains(w)) {
                continue;
            } else if (w.equals(start)) {
                hasCycle = true;
                pointStack.offerFirst(w);
                List<Vertex<Integer>> cycle = new ArrayList<>();
                Iterator<Vertex<Integer>> itr = pointStack.descendingIterator();
                while (itr.hasNext()) {
                    cycle.add(itr.next());
                }
                pointStack.pollFirst();
                result.add(cycle);
            } else if (!markedSet.contains(w)) {
                hasCycle = findAllSimpleCycles(start, w, result) || hasCycle;
            }
        }

        if (hasCycle) {
            while (!markedStack.peekFirst().equals(current)) {
                markedSet.remove(markedStack.pollFirst());
            }
            markedSet.remove(markedStack.pollFirst());
        }

        pointStack.pollFirst();
        return hasCycle;
    }
}
