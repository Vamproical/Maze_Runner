package maze;

public class Edge {
    protected Node from;
    protected Node to;
    private final int weight;

    public Edge(Node from, Node to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public Node other(Node trunk) {
        if (from.equals(trunk)) return to;
        if (to.equals(trunk)) return from;
        return null;
    }

    public boolean has(Node node) {
        return from.equals(node) || to.equals(node);
    }

    @Override
    public String toString() {
        return String.format("%s -> %s (%d)", from, to, weight);
    }
}
