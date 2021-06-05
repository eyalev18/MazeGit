package View;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.BestFirstSearch;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public MazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

    public void generateMaze(ActionEvent actionEvent) {
        if(generator == null)
            generator = new MazeGenerator();

        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        mazeDisplayer.setSolved(false);
        Maze maze = generator.generateRandomMaze(rows, cols);
        mazeDisplayer.drawMaze(maze);
        setPlayerPosition(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());
    }

    public void solveMaze(ActionEvent actionEvent) {
        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();*/
        mazeDisplayer.setSolved(true);
        mazeDisplayer.drawSolution();


    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
    }

    public void keyPressed(KeyEvent keyEvent) {
        int row = mazeDisplayer.getPlayerRow();
        int col = mazeDisplayer.getPlayerCol();

        switch (keyEvent.getCode()) {
            case DIGIT9:
                if (row - 1 < 0 || mazeDisplayer.getMaze().getCols() <= col + 1 || mazeDisplayer.getMaze().getArray()[row - 1][col + 1] == 1)
                    break;
                col += 1;
                row -= 1;
                break;
            case DIGIT8:

                if (row - 1 < 0 || mazeDisplayer.getMaze().getArray()[row - 1][col] == 1)
                    break;
                row -= 1;
                break;
            case DIGIT7:
                if (row - 1 < 0 || col - 1 < 0 || mazeDisplayer.getMaze().getArray()[row - 1][col - 1] == 1)
                    break;
                row -= 1;
                col -= 1;
                break;
            case DIGIT6:
                if (mazeDisplayer.getMaze().getCols() <= col + 1 || mazeDisplayer.getMaze().getArray()[row][col + 1] == 1)
                    break;
                col += 1;
                break;
            case DIGIT4:
                if (col - 1 < 0 || mazeDisplayer.getMaze().getArray()[row][col - 1] == 1)
                    break;
                col -= 1;
                break;
            case DIGIT3:
                if (mazeDisplayer.getMaze().getRows() <= row + 1 || mazeDisplayer.getMaze().getCols() <= col + 1 || mazeDisplayer.getMaze().getArray()[row + 1][col + 1] == 1)
                    break;
                row += 1;
                col += 1;
                break;
            case DIGIT2:
                if (mazeDisplayer.getMaze().getRows() <= row + 1 || mazeDisplayer.getMaze().getArray()[row + 1][col] == 1)
                    break;
                row += 1;
                break;
            case DIGIT1:
                if (mazeDisplayer.getMaze().getRows() <= row + 1 || col < 0 || mazeDisplayer.getMaze().getArray()[row + 1][col - 1] == 1)
                    break;
                row += 1;
                col -= 1;
                break;

        }
        setPlayerPosition(row, col);
        if (mazeDisplayer.getMaze().getGoalPosition().getRowIndex() == row && mazeDisplayer.getMaze().getGoalPosition().getColumnIndex() == col){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Maze Solved");
            alert.show();
            setPlayerPosition(mazeDisplayer.getMaze().getStartPosition().getRowIndex(),mazeDisplayer.getMaze().getStartPosition().getColumnIndex());
            mazeDisplayer.setSolved(false);
        }
        keyEvent.consume();

    }

    public void setPlayerPosition(int row, int col){

        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);

    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
}
