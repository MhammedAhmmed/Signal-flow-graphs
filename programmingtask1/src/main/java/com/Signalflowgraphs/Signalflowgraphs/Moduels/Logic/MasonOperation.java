package com.Signalflowgraphs.Signalflowgraphs.Moduels.Logic;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MasonOperation {
    private final AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph;
    private final AllCyclesInDirectedGraphJohnson allCyclesInDirectedGraphJohnson;

    public MasonOperation(AllForwardPathsInDirectedGraph allForwardPathsInDirectedGraph, AllCyclesInDirectedGraphJohnson allCyclesInDirectedGraphJohnson) {
        this.allForwardPathsInDirectedGraph = allForwardPathsInDirectedGraph;
        this.allCyclesInDirectedGraphJohnson = allCyclesInDirectedGraphJohnson;
    }

    private Integer getSumOfIndividualCycles(){
        int sum = 0;
        for(Integer value : allCyclesInDirectedGraphJohnson.getCyclesGains())
            sum += value;
        return sum;
    }

    private Integer getSumOfTwoNonTouchingCycles(){
        int sum = 0;
        for (Integer value: allCyclesInDirectedGraphJohnson.getTwoNonTouchingCyclesGains())
            sum += value;
        return sum;
    }

    private List<Integer> getSumOfIndividualCyclesNonTouchingPaths(){
        List<Integer> values = new ArrayList<>();
        for (List<Integer> cycles : allCyclesInDirectedGraphJohnson.getCyclesNonTouchingPathsGains()){
            int sum = 0;
            for (Integer value : cycles)
                sum += value;
            values.add(sum);
        }
        return values;
    }

    private List<Integer> getSumOfTwoNonTouchingCyclesNonTouchingPaths(){
        List<Integer> values = new ArrayList<>();
        for (List<Integer> cycles : allCyclesInDirectedGraphJohnson.getTwoNonTouchingCyclesNonTouchingPathsGains()){
            int sum = 0;
            for (Integer value : cycles)
                sum += value;
            values.add(sum);
        }
        return values;
    }

    public List<Integer> getDeltasOfPaths (int numberOfPaths){
        List<Integer> individuals = getSumOfIndividualCyclesNonTouchingPaths();
        List<Integer> twos = getSumOfTwoNonTouchingCyclesNonTouchingPaths();
        List<Integer> deltaPaths = new ArrayList<>();
        for (int i = 0; i < numberOfPaths; i++) deltaPaths.add(1);
        for (int i = 0; i < individuals.size(); i++){
            deltaPaths.set(i, deltaPaths.get(i) - individuals.get(i));
            if(i < twos.size())
                 deltaPaths.set(i, deltaPaths.get(i) + twos.get(i));
        }
        return deltaPaths;
    }

    private List<Integer> getSums(){
        List<Integer> sums = new ArrayList<>();
        List<List<Integer>> results = allCyclesInDirectedGraphJohnson.getAllCombinationsOfNNonTouchingCyclesGains();
        for (List<Integer> result : results){
            int sum = 0;
            for (Integer integer : result)
                sum += integer;
            sums.add(sum);
        }
        return sums;
    }

    public double getDelta(){
        double delta = 1 - getSumOfIndividualCycles();
        boolean plusTurn = true;
        for (Integer integer : getSums()){
            if (plusTurn){
                delta += integer;
                plusTurn = false;
            }
            else {
                delta -= integer;
                plusTurn = true;
            }
        }
        return delta;
    }

    public Double getTransferFunctionValue(){
        double delta = getDelta();
        List<Integer> pathsGains = allForwardPathsInDirectedGraph.getAllPathsGain();
        System.out.println("Paths gains: " + pathsGains);
        System.out.println("Delta: " + delta);
        System.out.println("Cycles non touching paths: " + getSumOfIndividualCyclesNonTouchingPaths());
        System.out.println("Two cycles non touching paths: " + getSumOfTwoNonTouchingCyclesNonTouchingPaths());
        System.out.println("Delta of Paths : " + getDeltasOfPaths(pathsGains.size()));
        List<Integer> deltaPaths = getDeltasOfPaths(pathsGains.size());
        double dominator = 0;
        for (int i = 0; i < pathsGains.size(); i++){
            dominator += pathsGains.get(i) * deltaPaths.get(i);
        }
        return (double) (dominator / delta);
    }

}
