package Model;

import Server.Configurations;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.control.Alert;

import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {

    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private MazeGeneratorSolver generator;

    public MyModel() {
        generator = new MazeGeneratorSolver();
    }

    @Override
    public void generateMaze(int rows, int cols) {
        maze = generator.generateRandomMaze(rows, cols);
        setChanged();
        notifyObservers("maze generated");
        // start position:
        movePlayer(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public void updatePlayerLocation(MovementDirection direction) {
        int row = playerRow;
        int col = playerCol;
        switch (direction) {
            case NUMPAD9:
                if (row - 1 < 0 || maze.getCols() <= col + 1 || maze.getArray()[row - 1][col + 1] == 1)
                    break;
                movePlayer(playerRow - 1, playerCol + 1);
                break;
            case NUMPAD8:
            case UP:
                if (row - 1 < 0 || maze.getArray()[row - 1][col] == 1)
                    break;
                movePlayer(playerRow - 1, playerCol);
                break;
            case NUMPAD7:
                if (row - 1 < 0 || col - 1 < 0 || maze.getArray()[row - 1][col - 1] == 1)
                    break;
                movePlayer(playerRow - 1, playerCol - 1);
                break;
            case NUMPAD6:
            case RIGHT:
                if (maze.getCols() <= col + 1 || maze.getArray()[row][col + 1] == 1)
                    break;
                movePlayer(playerRow, playerCol + 1);
                break;
            case NUMPAD4:
            case LEFT:
                if (col - 1 < 0 || maze.getArray()[row][col - 1] == 1)
                    break;
                movePlayer(playerRow, playerCol - 1);
                break;
            case NUMPAD3:
                if (maze.getRows() <= row + 1 || maze.getCols() <= col + 1 || maze.getArray()[row + 1][col + 1] == 1)
                    break;
                movePlayer(playerRow + 1, playerCol + 1);
                break;
            case NUMPAD2:
            case DOWN:
                if (maze.getRows() <= row + 1 || maze.getArray()[row + 1][col] == 1)
                    break;
                movePlayer(playerRow + 1, playerCol);
                break;
            case NUMPAD1:
                if (maze.getRows() <= row + 1 || col < 0 || maze.getArray()[row + 1][col - 1] == 1)
                    break;
                movePlayer(playerRow + 1, playerCol - 1);
                break;
        }
        if (maze.getGoalPosition().getRowIndex() == row && maze.getGoalPosition().getColumnIndex() == col){
            movePlayer(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Maze Solved");
            alert.show();
//            generator.setSolved(false);
        }

    }

    private void movePlayer(int row, int col){
        this.playerRow = row;
        this.playerCol = col;
        if (maze.getGoalPosition().getRowIndex() == row && maze.getGoalPosition().getColumnIndex() == col){
            movePlayer(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Maze Solved");
            alert.show();
        }
        setChanged();
        notifyObservers("player moved");
    }

    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze() {
        //solve the maze
        solution = generator.solveRandomMaze();
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public void propertiesDisplay() {
        Configurations x = new Configurations();
        String gen = x.getGen();
        String thread = x.getPoolSize();
        String search = x.getSearch();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("threadPoolSize: " + thread + "\n"+ "mazeGeneratingAlgorithm: " + gen + "\n" + "mazeSearchingAlgorithm: " + search);
        alert.show();
    }

}
