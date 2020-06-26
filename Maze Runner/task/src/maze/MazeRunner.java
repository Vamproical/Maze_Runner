package maze;

import java.util.*;

public class MazeRunner {
    private final int width;
    private final int height;
    private final Cell[][] cells;

    public MazeRunner(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[height][width];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.WALL);
        }
    }

    private void generateMaze() {
        Node[][] graph = createGraph();
        Node root = graph[0][0].copy();

        List<Edge> edges = new ArrayList<>(graph[0][0].getEdges());

        List<Node> used = new ArrayList<>();
        used.add(root);

        while (!edges.isEmpty()) {
            Edge optimal = findOptimal(edges);

            Node trunk = root.find(optimal);
            Node subject = optimal.other(trunk);
            trunk.createDirectedEdge(subject.copy(), 1);
            used.add(subject);

            edges.addAll(subject.edges);
            edges.removeIf(edge -> used.stream().anyMatch(edge.to::equals));
        }

        clearAtNode(root);
    }

    private void clearAtNode(Node node) {
        cells[node.getY()][node.getX()] = Cell.PASS;

        for (Edge edge : node.getEdges()) {
            cells[(edge.from.getY() + edge.to.getY()) / 2][(edge.from.getX() + edge.to.getX()) / 2] = Cell.PASS;
            clearAtNode(edge.other(node));
        }
    }

    private Edge findOptimal(List<Edge> adjacent) {
        return adjacent.stream().min(Comparator.comparing(Edge::getWeight)).orElseThrow();
    }

    private Node[][] createGraph() {
        Random random = new Random();

        Node[][] nodes = new Node[(height - 1) / 2][(width - 1) / 2];

        for (int j = 1; j < width - 1; j += 2) {
            for (int i = 1; i < height - 1; i += 2) {
                Node node = new Node(j, i);

                if (j > 1) {
                    node.createEdge(nodes[i / 2][j / 2 - 1], random.nextInt(100));
                }
                if (i > 1) {
                    node.createEdge(nodes[i / 2 - 1][j / 2], random.nextInt(100));
                }

                nodes[i / 2][j / 2] = node;
            }
        }

        return nodes;
    }

    private void pierce() {
        int x = -1;
        int y = height / 2;

        do {
            x++;
            cells[y][x] = Cell.PASS;
        } while (cells[y + 1][x] != Cell.PASS && cells[y - 1][x] != Cell.PASS && cells[y][x + 1] != Cell.PASS);

        x = width;
        y = height / 2;

        do {
            x--;
            cells[y][x] = Cell.PASS;
        } while (cells[y + 1][x] != Cell.PASS && cells[y - 1][x] != Cell.PASS && cells[y][x - 1] != Cell.PASS);
    }

    @Override
    public String toString() {
        StringJoiner rowJoiner = new StringJoiner(System.lineSeparator());

        for (Cell[] row : cells) {
            StringJoiner joiner = new StringJoiner("");

            for (Cell cell : row) {
                joiner.add(cell == Cell.PASS ? "  " : "\u2588\u2588");
            }

            rowJoiner.merge(joiner);
        }

        return rowJoiner.toString();
    }

    public void init() {
        generateMaze();
        pierce();
        System.out.println(toString());
    }

    enum Cell {
        WALL, PASS
    }
}
