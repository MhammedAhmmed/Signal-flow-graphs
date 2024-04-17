package com.Signalflowgraphs.Signalflowgraphs.Moduels;

import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.AllCyclesInDirectedGraphJohnson;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.AllForwardPathsInDirectedGraph;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.MasonOperation;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        SourceDestinations e1 = new SourceDestinations();
        SourceDestinations e2 = new SourceDestinations();
        SourceDestinations e3 = new SourceDestinations();
        SourceDestinations e4 = new SourceDestinations();
        SourceDestinations e5 = new SourceDestinations();
        SourceDestinations e6 = new SourceDestinations();
        SourceDestinations e7 = new SourceDestinations();
        SourceDestinations e8 = new SourceDestinations();

        e1.setSource(1); e1.setDestinations(List.of(new Pair(2, 1), new Pair(5, 1)));
        e2.setSource(2); e2.setDestinations(List.of(new Pair(3, 1)));
        e3.setSource(3); e3.setDestinations(List.of(new Pair(2, 2), new Pair(4, 1)));
        e4.setSource(4); e4.setDestinations(List.of(new Pair(3, 2), new Pair(8, 1)));
        e5.setSource(5); e5.setDestinations(List.of(new Pair(6, 1)));
        e6.setSource(6); e6.setDestinations(List.of(new Pair(5, 2), new Pair(7, 1)));
        e7.setSource(7); e7.setDestinations(List.of(new Pair(6, 2), new Pair(8, 1)));
        e8.setSource(8); e8.setDestinations(new ArrayList<>());

        List<SourceDestinations> list = new ArrayList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        list.add(e7);
        list.add(e8);

        AllForwardPathsInDirectedGraph paths = new AllForwardPathsInDirectedGraph();
        AllCyclesInDirectedGraphJohnson johnson = new AllCyclesInDirectedGraphJohnson();
//        initialize graph to get forward paths
        PathGraphInitialization pathGraphInitialization = new PathGraphInitialization();
        pathGraphInitialization.graphInitialize(list);

//        initialize graph to get cycles
        CycleGraphInitialization cycleGraphInitialization = new CycleGraphInitialization();
        cycleGraphInitialization.graphInitialize(list);

//        get all paths
        paths.findAllPaths(pathGraphInitialization, 1, 8);

//        get all cycles
        johnson.findAllCycles(cycleGraphInitialization);
//        get all two non-touching cycles
        johnson.findAllTwoNonTouchingCycles();
//        get all cycles non touching paths
        johnson.findAllCyclesNonTouchingPaths(paths);
//        get all two non-touching cycles paths
        johnson.findAllTwoNonTouchingCyclesPaths();

        MasonOperation masonOperation = new MasonOperation(paths, johnson);

        List<List<Integer>> allPaths = paths.getAllPaths();
        List<Integer> allPathsGains = paths.getAllPathsGain();
        System.out.println("Paths:");
        for(int i = 0; i < allPaths.size(); i++){
            System.out.println(allPaths.get(i) + " " + allPathsGains.get(i));
        }
        System.out.println("------------------------------------------------------------------------------");
        List<List<Integer>> allCycles = johnson.getAllCycles();
        List<Integer> allCyclesGains = johnson.getCyclesGains();
        System.out.println("Cycles:");
        for(int i = 0; i < allCycles.size(); i++){
            System.out.println(allCycles.get(i) + " " + allCyclesGains.get(i));
        }
//
//
//        System.out.println("Distinct cycles:");
//        for(List<Vertex<Integer>> vv: johnson.getDistinctCycles()){
//            for(Vertex<Integer>v: vv)
//                System.out.print(v + " ");
//            System.out.println();
//        }
//
        System.out.println("------------------------------------------------------------------------------");
        List<List<Integer>> allTwoNonTouchingCycles = johnson.getTwoNonTouchingCycles();
        List<Integer> allTwoNonTouchingCyclesGains = johnson.getTwoNonTouchingCyclesGains();
        System.out.println("Two non touching cycles:");
        for(int i = 0; i < allTwoNonTouchingCyclesGains.size(); i++){
            System.out.println(allTwoNonTouchingCycles.get(i * 2) + ", " + allTwoNonTouchingCycles.get(i * 2 + 1) + " " + allTwoNonTouchingCyclesGains.get(i));
        }
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Cycles non touching paths:");
        List<List<List<Integer>>> allCyclesNonTouchingPaths = johnson.getAllCyclesNonTouchingPaths();
        List<List<Integer>> allCyclesNonTouchingPathsGains = johnson.getCyclesNonTouchingPathsGains();
        for(int i = 0; i < allCyclesNonTouchingPaths.size(); i++){
            System.out.println("p" + i + ":");
            for (int j = 0; j < allCyclesNonTouchingPaths.get(i).size(); j++){
                System.out.println(allCyclesNonTouchingPaths.get(i).get(j) + " " + allCyclesNonTouchingPathsGains.get(i).get(j));
            }
        }
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Two non touching cycles paths:");
        List<List<List<Integer>>> allTwoNonTouchingCyclesPaths = johnson.getTwoNonTouchingCyclesNonTouchingPaths();
        List<List<Integer>> allTwoNonTouchingCyclesPathsGains = johnson.getTwoNonTouchingCyclesNonTouchingPathsGains();
        for(int i = 0; i < allTwoNonTouchingCyclesPaths.size(); i++){
            System.out.println("p" + i + ":");
            for (int j = 0; j < allTwoNonTouchingCyclesPathsGains.get(i).size(); j++){
                System.out.println(allTwoNonTouchingCyclesPaths.get(i).get(j * 2) + ", " + allTwoNonTouchingCyclesPaths.get(i).get(j * 2 + 1) + " " + allTwoNonTouchingCyclesPathsGains.get(i).get(j));
            }
        }
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Mason Data:");
        System.out.println(masonOperation.getTransferFunctionValue());
//        masonOperation.getTransferFunctionValue();
    }
}