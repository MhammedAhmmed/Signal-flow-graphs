package com.Signalflowgraphs.Signalflowgraphs.Controllers;

import com.Signalflowgraphs.Signalflowgraphs.Moduels.CycleGraphInitialization;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.AllCyclesInDirectedGraphJohnson;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.AllForwardPathsInDirectedGraph;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.MasonOperation;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.PathGraphInitialization;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.SourceDestinations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping
public class Controller {
    private final PathGraphInitialization pathGraphInitialization;

    private final CycleGraphInitialization cycleGraphInitialization;

    private final AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph;

    private final AllCyclesInDirectedGraphJohnson allCyclesInDirectedGraphJohnson;

    private final MasonOperation mason;

    @Autowired
    public Controller(PathGraphInitialization pathGraphInitialization, CycleGraphInitialization cycleGraphInitialization, AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph, AllCyclesInDirectedGraphJohnson allCyclesInDirectedGraphJohnson) {
        this.pathGraphInitialization = pathGraphInitialization;
        this.cycleGraphInitialization = cycleGraphInitialization;
        this.allForwardPathsInDirectedGraph = allForwardPathsInDirectedGraph;
        this.allCyclesInDirectedGraphJohnson = allCyclesInDirectedGraphJohnson;
        this.mason = new MasonOperation(this.allForwardPathsInDirectedGraph, this.allCyclesInDirectedGraphJohnson);
    }

    @PostMapping ("/graph")
    public void initializeGraph(@RequestBody List<SourceDestinations> list){
        pathGraphInitialization.graphInitialize(list);
        cycleGraphInitialization.graphInitialize(list);
    }

    @GetMapping ("/graph/paths/{source}/{destination}")
    public List<List<Integer>> getPaths(@PathVariable int source, @PathVariable int destination){
        allForwardPathsInDirectedGraph.findAllPaths(pathGraphInitialization, source, destination);
        return allForwardPathsInDirectedGraph.getAllPaths();
    }

    @GetMapping ("/graph/individual/cycles")
    public List<List<Integer>> getIndividualCycles(){
        return allCyclesInDirectedGraphJohnson.getAllCycles();
    }

    @GetMapping ("/graph/non/touching/cycles")
    public List<List<Integer>> getNonTouchingCycles(){
        return allCyclesInDirectedGraphJohnson.getTwoNonTouchingCycles();
    }

    @GetMapping ("/graph/deltas")
    public List<Integer> getDeltas(){
        List<Integer> deltas = mason.getDeltasOfPaths(allForwardPathsInDirectedGraph.getAllPaths().size());
        deltas.add((int) mason.getDelta());
        return deltas;
    }

    @GetMapping ("/graph/transfer/function")
    public double getTransferFunction(){
        return mason.getTransferFunctionValue();
    }
}
