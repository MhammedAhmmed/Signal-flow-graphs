package com.Signalflowgraphs.Signalflowgraphs.Controllers;

import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.AllForwardPathsInDirectedGraph;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Pair;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.SignalFlowGraph;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.SourceDestinations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping
public class Controller {
    private final AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph;
    private final SignalFlowGraph signalFlowGraph;

    @Autowired
    public Controller(AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph, SignalFlowGraph signalFlowGraph) {
        this.allForwardPathsInDirectedGraph = allForwardPathsInDirectedGraph;
        this.signalFlowGraph = signalFlowGraph;
    }

    @GetMapping ("/graph/{source}/{destination}")
    public void getGraph(@RequestBody List<SourceDestinations> graph, @PathVariable int source, @PathVariable int destination){
        signalFlowGraph.graphInitialize(graph);
        Map<Integer, List<Pair>> initialized_graph = signalFlowGraph.getGraph();
    }
}
