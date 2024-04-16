package com.Signalflowgraphs.Signalflowgraphs.Controllers;

<<<<<<< HEAD
import com.Signalflowgraphs.Signalflowgraphs.Moduels.CycleGraphInitialization;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.PathGraphInitialization;
=======
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.AllForwardPathsInDirectedGraph;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Pair;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.SignalFlowGraph;
>>>>>>> 4a2071dec2f7676c05ce37b279ba630a8b4ab4fe
import com.Signalflowgraphs.Signalflowgraphs.Moduels.SourceDestinations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping
public class Controller {
<<<<<<< HEAD
    private final PathGraphInitialization pathGraphInitialization;

    private final CycleGraphInitialization cycleGraphInitialization;

    @Autowired
    public Controller(PathGraphInitialization pathGraphInitialization, CycleGraphInitialization cycleGraphInitialization) {
        this.pathGraphInitialization = pathGraphInitialization;
        this.cycleGraphInitialization = cycleGraphInitialization;
=======
    private final AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph;
    private final SignalFlowGraph signalFlowGraph;

    @Autowired
    public Controller(AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph, SignalFlowGraph signalFlowGraph) {
        this.allForwardPathsInDirectedGraph = allForwardPathsInDirectedGraph;
        this.signalFlowGraph = signalFlowGraph;
>>>>>>> 4a2071dec2f7676c05ce37b279ba630a8b4ab4fe
    }

    @PostMapping ("/graph")
    public void initializeGraph(@RequestBody List<SourceDestinations> list){
        pathGraphInitialization.graphInitialize(list);
        cycleGraphInitialization.graphInitialize(list);
    }
}
