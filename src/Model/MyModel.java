package Model;

import Server.Configurations;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public class MyModel extends Observable implements IModel {

    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private MazeGeneratorSolver generator;
    MediaPlayer mediaPlayer;
    MediaPlayer mediaWinner;

    public MyModel() {
        generator = new MazeGeneratorSolver();
    }

    @Override
    public void generateMaze(int rows, int cols) {
        maze = generator.generateRandomMaze(rows, cols);
        if (mediaWinner != null)
            mediaWinner.pause();
        mediaPlayer.play();
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
            mediaPlayer.pause();
            String s = "resources\\music\\winner.mp3";
            Media m = new Media(Paths.get(s).toUri().toString());
            mediaWinner = new MediaPlayer(m);
            mediaWinner.setAutoPlay(true);
            mediaWinner.play();
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
        Configurations x = Configurations.getInstance();
        String gen = x.getGen();
        String thread = x.getPoolSize();
        String search = x.getSearch();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("threadPoolSize: " + thread + "\n"+ "mazeGeneratingAlgorithm: " + gen + "\n" + "mazeSearchingAlgorithm: " + search);
        alert.show();
    }

    public void music() {
        String s = "resources\\music\\background.mp3";
        Media m = new Media(Paths.get(s).toUri().toString());
        mediaPlayer = new MediaPlayer(m);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.play();
    }

    public void Mute() {
        if (mediaWinner != null)
            if (!(mediaWinner.isMute()))
                mediaWinner.pause();
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    public void Unmute() {
        if (mediaPlayer != null)
            mediaPlayer.play();
    }

    public void saveMaze(String chosen) throws IOException {
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        String file = tempDirectoryPath + chosen;
        File f = new File(file);
        ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream(file));
        byte[] data = maze.toByteArray();
        save.write(data);
        save.close();
        String fileMaze = "./resources/mazes/" + chosen;
        File f2 = new File(fileMaze);
        ObjectOutputStream saveM = new ObjectOutputStream(new FileOutputStream(fileMaze));
        saveM.write(data);
        saveM.close();
    }

    public void open(File chosen) throws IOException {
        ObjectInputStream load = new ObjectInputStream(new FileInputStream(chosen));
        byte[] byteArr = load.readAllBytes();
        Maze m = new Maze(byteArr);
        maze = generator.generateLoadedMaze(m);
        setChanged();
        notifyObservers("maze generated");
        movePlayer(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
    }

    public void setProperties(int thread, int gen, int search) {
        Configurations c = Configurations.getInstance();
        Properties p = new Properties();
        p.setProperty("threadPoolSize", String.valueOf(thread));
        p.setProperty("mazeGeneratingAlgorithm", String.valueOf(gen));
        p.setProperty("mazeSearchingAlgorithm", String.valueOf(search));
    }

}
