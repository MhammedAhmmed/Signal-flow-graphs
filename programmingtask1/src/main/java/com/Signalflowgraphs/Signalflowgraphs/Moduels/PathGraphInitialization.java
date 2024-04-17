package com.Signalflowgraphs.Signalflowgraphs.Moduels;

import org.springframework.context.annotation.Bean;
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
