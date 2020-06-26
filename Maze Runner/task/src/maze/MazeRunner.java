package maze;

import java.util.Random;

public class MazeRunner {
    private final int row;
    private final int column;
    private final int[][] mazeMatrix;
    private final Random random = new Random();

    public MazeRunner(int row, int column) {
        this.row = row;
        this.column = column;
        this.mazeMatrix = new int[row][column];
    }

    private void createAdjacencyMatrix() {
        for(int i = 0; i < this.row; ++i) {
            for(int j = 0; j < this.column; ++j) {
            if (i == j) {
                this.mazeMatrix[i][j] = 0;
            } else {
                this.mazeMatrix[i][j] = this.random.nextInt(30);
            }
        }
        }

    }

    private void printMaze(int[][] matrix) {
        for(int i = 0; i < this.row; ++i) {
            for(int j = 0; j < this.column; ++j) {
            if (matrix[i][j] == 1) {
                System.out.print("██");
            } else {
                System.out.print("  ");
            }
        }

            System.out.println();
        }

    }

    public void init() {
        this.createAdjacencyMatrix();
        PrimMST primMST = new PrimMST(this.column * this.row);
        primMST.primMST();
    }
}
