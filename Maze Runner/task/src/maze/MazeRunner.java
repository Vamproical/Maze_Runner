package maze;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MazeRunner {
    private final int width;
    private final int height;
    private final Cell[][] cells;


    public MazeRunner(int width, boolean isValid) {
        this.width = width;
        this.height = width;
        this.cells = new Cell[width][height];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.WALL);
        }
        if (isValid) {
            generateMaze();
            pierce();
        }
    }

    public MazeRunner(int height, int width, Cell[][] grid) {
        this.width = width;
        this.height = height;
        this.cells = grid;
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
        int y = width / 2;

        do {
            x++;
            cells[y][x] = Cell.PASS;
        } while (cells[y + 1][x] != Cell.PASS && cells[y - 1][x] != Cell.PASS && cells[y][x + 1] != Cell.PASS);

        x = width;
        y = width / 2;

        do {
            x--;
            cells[y][x] = Cell.PASS;
        } while (cells[y + 1][x] != Cell.PASS && cells[y - 1][x] != Cell.PASS && cells[y][x - 1] != Cell.PASS);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                stringBuilder.append(cell == Cell.PASS ? "  " : "\u2588\u2588");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private static MazeRunner read(String str) {
        try {
            String[] whole = str.split("\n");
            String[] size = whole[0].split("");
            int height = size.length / 2;
            int width = size.length / 2;
            Cell[][] grid = new Cell[height][width];
            for (int i = 0; i < height; i++) {
                var row = whole[i].split("");
                int k = 0;
                for (int j = 0; j < 2 * width; j += 2) {
                    if (row[j].equals("█")) {
                        grid[i][k] = Cell.WALL;
                    } else {
                        grid[i][k] = Cell.PASS;
                    }
                    ++k;
                }
            }
            return new MazeRunner(height, width, grid);
        } catch (Exception e) {
            System.out.println(
                    "Cannot load the maze. " +
                            "It has an invalid format" + " " + e.getMessage()
            );
            return new MazeRunner(0, false);
        }
    }

    public static MazeRunner readFromFile(String path) {
        try {
            String content = Files.readString(Paths.get(path));
            return read(content);
        } catch (IOException e) {
            System.out.println("The file " + path + " does not exist");
            return new MazeRunner(0, false);
        }
    }

    public void writeToFile(String path) {
        File file = new File(path);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (Cell[] row : cells) {
                for (Cell cell : row) {
                    printWriter.print(cell == Cell.PASS ? "  " : "\u2588\u2588");
                }
                printWriter.println("");
            }
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }
    }

    enum Cell {
        WALL, PASS
    }
}
