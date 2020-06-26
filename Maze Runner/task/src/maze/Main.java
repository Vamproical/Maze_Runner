package maze;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter the size of a maze");
        int row = scanner.nextInt();
        int column = scanner.nextInt();
        MazeRunner mazeRunner = new MazeRunner(row, column);
        mazeRunner.init();
    }
}