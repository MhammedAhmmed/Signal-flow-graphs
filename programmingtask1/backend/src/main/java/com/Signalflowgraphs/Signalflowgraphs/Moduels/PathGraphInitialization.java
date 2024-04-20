package com.Signalflowgraphs.Signalflowgraphs.Moduels;

<<<<<<< HEAD:programmingtask1/backend/src/main/java/com/Signalflowgraphs/Signalflowgraphs/Moduels/PathGraphInitialization.java
=======
import org.springframework.context.annotation.Bean;
>>>>>>> 76b9e46918475c1157e9f4238ba44379a7e35f86:programmingtask1/src/main/java/com/Signalflowgraphs/Signalflowgraphs/Moduels/PathGraphInitialization.java
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PathGraphInitialization {
    private Map<Integer, List<Pair>>adj;

    public void graphInitialize(List<SourceDestinations> graph){
        adj = new HashMap<>();
        for(SourceDestinations entry: graph){
            adj.put(entry.getSource(), entry.getDestinations());
        }
    }

    public Map<Integer, List<Pair>> getGraph() {
        return adj;
    }

    @Override
    public String toString() {
        return "PathGraphInitialization{" +
                "adj=" + adj +
                '}';
    }
}
