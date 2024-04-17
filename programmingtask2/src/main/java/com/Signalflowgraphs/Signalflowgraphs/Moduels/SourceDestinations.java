package com.Signalflowgraphs.Signalflowgraphs.Moduels;
import java.util.List;

public class SourceDestinations {
    private int source;
    private List<Pair> destinations;

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public List<Pair> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Pair> destinations) {
        this.destinations = destinations;
    }

    @Override
    public String toString() {
        return "SourceDestinations{" +
                "source=" + source +
                ", destinations=" + destinations +
                '}';
    }
}
