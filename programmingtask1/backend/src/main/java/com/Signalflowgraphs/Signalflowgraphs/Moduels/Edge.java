package com.Signalflowgraphs.Signalflowgraphs.Moduels;

import java.util.Objects;

public class Edge<T> {
    private boolean isDirected;
    private final Vertex<T> vertex1;
    private final Vertex<T> vertex2;
    private int weight;

    public Edge(Vertex<T> vertex1, Vertex<T> vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }
    public Edge(Vertex<T> vertex1, Vertex<T> vertex2, boolean isDirected, int weight) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
        this.isDirected = isDirected;
    }
    Edge(Vertex<T> vertex1, Vertex<T> vertex2, boolean isDirected) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.isDirected = isDirected;
    }

    public Vertex<T> getVertex1() {
        return vertex1;
    }

    public Vertex<T> getVertex2() {
        return vertex2;
    }

    public int getWeight() {
        return weight;
    }
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Edge<?> edge = (Edge<?>) o;
    return Objects.equals(vertex1, edge.vertex1) && Objects.equals(vertex2, edge.vertex2);
}
    @Override
    public int hashCode() {
        return Objects.hash(vertex1, vertex2);
    }
}
