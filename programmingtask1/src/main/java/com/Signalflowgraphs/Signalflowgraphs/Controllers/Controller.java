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
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping
public class Controller {
    private final PathGraphInitialization pathGraphInitialization;

    private final CycleGraphInitialization cycleGraphInitialization;

    @Autowired
    public Controller(PathGraphInitialization pathGraphInitialization, CycleGraphInitialization cycleGraphInitialization) {
        this.pathGraphInitialization = pathGraphInitialization;
        this.cycleGraphInitialization = cycleGraphInitialization;
    }

    @PostMapping ("/graph")
    public void initializeGraph(@RequestBody List<SourceDestinations> list){
        pathGraphInitialization.graphInitialize(list);
        cycleGraphInitialization.graphInitialize(list);
//        AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph = new AllForwardPathsInDirectedGraph();
//        allForwardPathsInDirectedGraph.findAllPaths(pathGraphInitialization, );
        AllCyclesInDirectedGraphJohnson allCyclesInDirectedGraphJohnson = new AllCyclesInDirectedGraphJohnson();
        allCyclesInDirectedGraphJohnson.findAllCycles(cycleGraphInitialization);
        System.out.println("------------------------------------------------------------------------------");
        List<List<Integer>> allCycles = allCyclesInDirectedGraphJohnson.getAllCycles();
        List<Integer> allCyclesGains = allCyclesInDirectedGraphJohnson.getCyclesGains();
        System.out.println("Cycles:");
        for(int i = 0; i < allCycles.size(); i++){
            System.out.println(allCycles.get(i) + " " + allCyclesGains.get(i));
        }
    }
//    @GetMapping ("/getAnswer")
//    public double getAnswers() {
////        MasonOperation
////        return null;
//    }

}
