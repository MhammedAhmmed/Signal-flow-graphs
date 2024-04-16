package com.Signalflowgraphs.Signalflowgraphs.Moduels;

import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.AllCyclesInDirectedGraphJohnson;
<<<<<<< HEAD
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.AllForwardPathsInDirectedGraph;
import com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic.MasonOperation;
=======
>>>>>>> 4a2071dec2f7676c05ce37b279ba630a8b4ab4fe

import java.util.*;

public class Main {
    public static void main(String[] args) {
<<<<<<< HEAD
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
=======
//        AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph = new AllForwardPathsInDirectedGraph();
//        Pair pair0 = new Pair(1, 1);
//        Pair pair1 = new Pair(2, 2);
//        Pair pair2 = new Pair(3, 2);
//        Pair pair3 = new Pair(3, 2);
//        Pair pair4 = new Pair(1, 2);
//        Pair pair5 = new Pair(4, 1);
//        Pair pair6 = new Pair(1, -2);
//        Pair pair7 = new Pair(5, 1);
//
//        List<Pair>list0 = new ArrayList<>();
//        list0.add(pair0);
//
//        List<Pair>list1 = new ArrayList<>();
//        list1.add(pair1);
//
//        List<Pair>list2 = new ArrayList<>();
//        list2.add(pair2);
//        list2.add(pair3);
//        list2.add(pair4);
//
//        List<Pair>list3 = new ArrayList<>();
//        list3.add(pair5);
//
//        List<Pair>list4 = new ArrayList<>();
//        list4.add(pair6);
//        list4.add(pair7);
>>>>>>> 4a2071dec2f7676c05ce37b279ba630a8b4ab4fe

//        get all cycles
        johnson.findAllCycles(cycleGraphInitialization);
//        get all two non-touching cycles
        johnson.findAllTwoNonTouchingCycles();
//        get all cycles non touching paths
        johnson.findAllCyclesNonTouchingPaths(paths);
//        get all two non-touching cycles paths
        johnson.findAllTwoNonTouchingCyclesPaths();

        MasonOperation masonOperation = new MasonOperation(paths, johnson);

<<<<<<< HEAD
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
=======
//        Map<Integer, List<Pair>>mp = new HashMap<>();
//
//        mp.put(0, list0);
//        mp.put(1, list1);
//        mp.put(2, list2);
//        mp.put(3, list3);
//        mp.put(4, list4);
//        mp.put(5, new ArrayList<>());

//        Pair pair0 = new Pair(0, 1);
//        Pair pair1 = new Pair(1, 1);
//        Pair pair2 = new Pair(2, 1);
//        Pair pair3 = new Pair(3, 1);
//        Pair pair4 = new Pair(4, 1);
>>>>>>> 4a2071dec2f7676c05ce37b279ba630a8b4ab4fe
//
//
<<<<<<< HEAD
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
=======
//        List<Pair>list1 = new ArrayList<>();
//        list1.add(pair2);
//
//        List<Pair>list2 = new ArrayList<>();
//        list2.add(pair3);
//        list2.add(pair4);
//
//        List<Pair>list3 = new ArrayList<>();
//        list3.add(pair0);
//
//        List<Pair>list4 = new ArrayList<>();
//        list4.add(pair2);
//
//        Map<Integer, List<Pair>> mp = new HashMap<>();
//        mp.put(0, list0);
//        mp.put(1, list1);
//        mp.put(2, list2);
//        mp.put(3, list3);
//        mp.put(4, list4);
//
//        allForwardPathsInDirectedGraph.findAllPaths(mp, 0, 5);
//        List<List<Integer>> result = graphOperations.findAllCycles(mp);
//
//        for(List<Integer> ll: allForwardPathsInDirectedGraph.getAllPaths())
//            System.out.println(ll);
//        int[] array = new int[100];
//        Random random = new Random();
//        for(int i = 0;i < 100; i++)
//            array[i] = random.nextInt(10001);
//        for(int num: array){
//            System.out.print(num+",");
//        }

//        --------------------------------------------------------------------------------------------------

        SourceDestinations e1 = new SourceDestinations();
        SourceDestinations e2 = new SourceDestinations();
        SourceDestinations e3 = new SourceDestinations();
        SourceDestinations e4 = new SourceDestinations();
        SourceDestinations e5 = new SourceDestinations();
        SourceDestinations e6 = new SourceDestinations();

        e1.setSource(1); e1.setDestinations(List.of(new Pair(2, 1)));
        e2.setSource(2); e2.setDestinations(List.of(new Pair(3, 2)));
        e3.setSource(3); e3.setDestinations(List.of(new Pair(2, 3), new Pair(4, 4)));
        e4.setSource(4); e4.setDestinations(List.of(new Pair(5, 5)));
        e5.setSource(5); e5.setDestinations(List.of(new Pair(4, 6), new Pair(6, 7), new Pair(6, 8)));
        e6.setSource(6); e6.setDestinations(List.of(new Pair(4, 9), new Pair(7, 10)));

        List<SourceDestinations> list = new ArrayList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);

        AllCyclesInDirectedGraphJohnson johnson = new AllCyclesInDirectedGraphJohnson();
        johnson.graphInitialize(list);
        johnson.findAllCycles();

        System.out.println("Gains:");
        for (Integer weight: johnson.cyclesGains)
            System.out.print(weight + " ");
        System.out.println();

        System.out.println("All cycles:");
        for (List<Vertex<Integer>> vv : johnson.allCycles){
            for (Vertex<Integer> v : vv)
                System.out.print(v + " ");
            System.out.println();
        }

        System.out.println("Distinct cycles:");
        for(List<Vertex<Integer>> vv: johnson.distinctCycles){
            for(Vertex<Integer>v: vv)
                System.out.print(v + " ");
            System.out.println();
        }

>>>>>>> 4a2071dec2f7676c05ce37b279ba630a8b4ab4fe
    }
}