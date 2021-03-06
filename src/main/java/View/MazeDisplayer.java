package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private Solution solution;
    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    private double cellWidth = 0.0;
    private double cellHieght = 0.0;
    boolean isSolved=false;

    public Maze getMaze() {
        return maze;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public void drawMaze(Maze maze) {
        this.maze = maze;
        if (getCellHieght()==0.0 && getCellWidth()==0.0) {
            setCellHieght(getHeight());
            setCellWidth(getWidth());
            setPlayerPosition(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());
        }
        else {
            setCellHieght(getCellHieght());
            setCellWidth(getCellWidth());
        }
        draw();
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(double cellWidth) {
        this.cellWidth = cellWidth;
    }

    public double getCellHieght() {
        return cellHieght;
    }

    public void setCellHieght(double cellHieght) {
        this.cellHieght = cellHieght;
    }

    public void drawSolution(Solution sol) {
        solution = sol;
        int rows = maze.getRows();
        int cols = maze.getCols();

        double cellHeight = getCellHieght() / rows;
        double cellWidth = getCellWidth() / cols;

        GraphicsContext graphicsContext = getGraphicsContext2D();
        double x ;
        double y ;
        graphicsContext.setFill(Color.BLUE);

        Image pathImage = null;
        try{
            pathImage = new Image(new FileInputStream("resources\\images\\path.png"));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }
        ArrayList<AState> solutionPath = solution.getSolutionPath();
        int r=0;
        int c=0;
        for (int i = 0; i < solutionPath.size(); i++) {
            MazeState MS = (MazeState) solutionPath.get(i);
            r = MS.getRow();
            c = MS.getCol();
            y = r * cellWidth;
            x = c * cellHeight;
            if(pathImage == null)
                graphicsContext.fillRect(x, y, cellWidth, cellHeight);
            else
                graphicsContext.drawImage(pathImage, x, y, cellWidth, cellHeight);
        }
        drawPlayer(graphicsContext, cellHeight, cellWidth);
        drawGoalPosition(graphicsContext, cellHeight, cellWidth);
    }

    private void draw() {
        if(maze != null){
            int rows = maze.getRows();
            int cols = maze.getCols();

            double cellHeight = getCellHieght() / rows;
            double cellWidth = getCellWidth() / cols;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, getWidth(), getHeight());

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            if(isSolved==true){
                     drawSolution(solution);
            }
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawGoalPosition(graphicsContext, cellHeight, cellWidth);
        }
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        try{
            wallImage = new Image(new FileInputStream("resources\\images\\wall.jpg"));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(maze.getArray()[i][j] == 1){
                    //if it is a wall:
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream("resources\\images\\pikachu.png"));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    private void drawGoalPosition(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = maze.getGoalPosition().getColumnIndex() * cellWidth;
        double y = maze.getGoalPosition().getRowIndex() * cellHeight;
        graphicsContext.setFill(Color.YELLOW);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream("resources\\images\\ash.jpg"));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

}
