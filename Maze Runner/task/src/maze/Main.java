package maze;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter the size of a maze");
        int column = scanner.nextInt();
        int row = scanner.nextInt();
        MazeRunner mazeRunner = new MazeRunner(row, column);
        mazeRunner.init();
    }
}