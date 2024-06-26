package com.Signalflowgraphs.Signalflowgraphs.Moduels;

public class Pair {
    private int destination, weight;

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    public Pair(int destination, int weight){
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "{" +
                destination + ", " +
                weight +
                '}';
    }
}
