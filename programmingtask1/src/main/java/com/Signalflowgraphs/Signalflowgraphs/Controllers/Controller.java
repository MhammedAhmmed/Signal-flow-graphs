package com.Signalflowgraphs.Signalflowgraphs.Controllers;

import com.Signalflowgraphs.Signalflowgraphs.Moduels.CycleGraphInitialization;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.AllCyclesInDirectedGraphJohnson;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.AllForwardPathsInDirectedGraph;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.MasonOperation;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.PathGraphInitialization;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.SourceDestinations;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping
public class Controller {
    private PathGraphInitialization pathGraphInitialization;

    private CycleGraphInitialization cycleGraphInitialization;

    private final AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph;

    private final AllCyclesInDirectedGraphJohnson allCyclesInDirectedGraphJohnson;

    private final MasonOperation mason;

    public Controller(AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph, AllCyclesInDirectedGraphJohnson allCyclesInDirectedGraphJohnson, MasonOperation mason) {
        this.allForwardPathsInDirectedGraph = allForwardPathsInDirectedGraph;
        this.allCyclesInDirectedGraphJohnson = allCyclesInDirectedGraphJohnson;
        this.mason = mason;
    }

    @PostMapping ("/graph")
    public void initializeGraph(@RequestBody List<SourceDestinations> list){
        pathGraphInitialization = new PathGraphInitialization();
        cycleGraphInitialization = new CycleGraphInitialization();
        pathGraphInitialization.graphInitialize(list);
        cycleGraphInitialization.graphInitialize(list);
//        allForwardPathsInDirectedGraph = new AllForwardPathsInDirectedGraph();
//        allCyclesInDirectedGraphJohnson = new AllCyclesInDirectedGraphJohnson();
//        mason = new MasonOperation(allForwardPathsInDirectedGraph, allCyclesInDirectedGraphJohnson);

    }


    @GetMapping ("/graph/paths/{source}/{destination}")
    public List<List<Integer>> getPaths(@PathVariable int source, @PathVariable int destination){
        allForwardPathsInDirectedGraph.findAllPaths(pathGraphInitialization, source, destination);
        return allForwardPathsInDirectedGraph.getAllPaths();
    }

    @GetMapping ("/graph/individual/cycles")
    public List<List<Integer>> getIndividualCycles(){
        allCyclesInDirectedGraphJohnson.findAllCycles(cycleGraphInitialization);
        System.out.println("------------------------------------------------------------------------------");
        List<List<Integer>> allCycles = allCyclesInDirectedGraphJohnson.getAllCycles();
        List<Integer> allCyclesGains = allCyclesInDirectedGraphJohnson.getCyclesGains();
        System.out.println("Cycles:");
        for(int i = 0; i < allCycles.size(); i++){
            System.out.println(allCycles.get(i) + " " + allCyclesGains.get(i));
        }
        return allCyclesInDirectedGraphJohnson.getAllCycles();
    }

    @GetMapping ("/graph/non/touching/cycles")
    public List<List<String>> getNonTouchingCycles(){
        System.out.println("------------------------------------------------------------------------------");
        List<List<String>> list1 = allCyclesInDirectedGraphJohnson.getAllCombinationsOfNNonTouchingCycles();
        int index = 2;
        for (List<String> stringList : list1) {
            System.out.println("n = " + index);
            index++;
            for (String s : stringList)
                System.out.println(s);
        }
        return allCyclesInDirectedGraphJohnson.getAllCombinationsOfNNonTouchingCycles();
    }

    @GetMapping ("/graph/deltas")
    public List<Integer> getDeltas(){
        allCyclesInDirectedGraphJohnson.findAllCyclesNonTouchingPaths(allForwardPathsInDirectedGraph);
        allCyclesInDirectedGraphJohnson.findAllTwoNonTouchingCyclesPaths();
        List<Integer> deltas = mason.getDeltasOfPaths(allForwardPathsInDirectedGraph.getAllPaths().size());
        deltas.add((int) mason.getDelta());
        return deltas;
    }

    @GetMapping ("/graph/transfer/function")
    public double getTransferFunction(){
        return mason.getTransferFunctionValue();
    }
}
