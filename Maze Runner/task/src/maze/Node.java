package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Node {
    private final int x;
    private final int y;
    protected final List<Edge> edges;

    public Node(int x, int y) {
        this.x  = x;
        this.y = y;
        edges = new ArrayList<>();
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
    public void createEdge(Node node, int weight) {
        this.createDirectedEdge(node, weight);
        node.createDirectedEdge(this, weight);
    }

    public void createDirectedEdge(Node node, int weight) {
        edges.add(new Edge(this, node, weight));
    }

    public Node copy() {
        return new Node(x, y);
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("%d %d", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    public Node find(Edge edge) {
        if (edge.has(this)) {
            return this;
        }

        for (Edge e : edges) {
            Node node = e.to.find(edge);
            if (node != null) {
                return node;
            }
        }
        return null;
    }
}
