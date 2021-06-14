package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements Initializable, Observer {

    public MyViewModel viewModel;
    public Pane pane;
    public Button solveButton;
    public Button hideButton;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
        this.viewModel.music();
    }

    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;

    public javafx.scene.control.TextField textField_thread;
    public javafx.scene.control.TextField textField_mazeGen;
    public javafx.scene.control.TextField textField_mazeSearch;

    static Stage primaryStage;
    private Scene scene;
    private Stage stage;
    private Parent root;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    private double startDragX;
    private double stopDragX;
    private double startDragY;
    private double stopDragY;

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
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        mazeDisplayer.setSolved(false);
        solveButton.setDisable(false);
        viewModel.generateMaze(rows, cols);
    }

    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
        hideButton.setDisable(false);
        viewModel.solveMaze();
    }

    public void openFile(ActionEvent actionEvent) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources/mazes"));
        File chosen = fc.showOpenDialog(null);
        viewModel.open(chosen);
        mazeDisplayer.setSolved(false);
        solveButton.setDisable(false);
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent);
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

    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "maze generated":
                mazeGenerated();
                break;
            case "player moved":
                playerMoved();
                break;
            case "maze solved":
                mazeSolved();
                break;
            default:
                System.out.println("Not implemented change: " + change);
                break;
        }
    }

    private void mazeSolved() {
        mazeDisplayer.setSolved(true);
        mazeDisplayer.drawSolution(viewModel.getSolution());
    }

    private void playerMoved() {
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    private void mazeGenerated() {
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }

    public void propertiesDisplay(ActionEvent actionEvent) {
        viewModel.propertiesDisplay();
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void scroll(ScrollEvent scrollEvent) {
        double delta = scrollEvent.getDeltaY();
        mazeDisplayer.setCellWidth(mazeDisplayer.getCellWidth()+delta);
        mazeDisplayer.setCellHieght(mazeDisplayer.getCellHieght()+delta);
        mazeDisplayer.drawMaze(mazeDisplayer.getMaze());
    }

    public void saveMaze(ActionEvent actionEvent) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources/mazes"));
        try {
            File chosen = fc.showSaveDialog(null);
            viewModel.saveMaze(chosen.getName());
        } catch (Exception ex) {

        }
    }

    public void Unmute(ActionEvent actionEvent) {
        viewModel.Unmute();
    }

    public void Mute(ActionEvent actionEvent) {
        viewModel.Mute();
    }

    public void drag(MouseEvent mouseEvent) {
        startDragX = mouseEvent.getX();
        startDragY = mouseEvent.getY();
    }

    public void dragOver(MouseEvent mouseEvent) {
        stopDragX = mouseEvent.getX();
        stopDragY = mouseEvent.getY();
        dragMove();
    }

    public void dragMove() {
        double rightLeft = startDragX - stopDragX;
        double upDown = startDragY - stopDragY;
        if (rightLeft > 0 && Math.abs(rightLeft) > Math.abs(upDown)) {
            viewModel.movePlayerByDrag("LEFT");
        }
        else if (rightLeft < 0 && Math.abs(rightLeft) > Math.abs(upDown)) {
            viewModel.movePlayerByDrag("RIGHT");
        }
        else if (upDown < 0 && Math.abs(rightLeft) < Math.abs(upDown)) {
            viewModel.movePlayerByDrag("DOWN");
        }
        else if (upDown > 0 && Math.abs(rightLeft) < Math.abs(upDown)) {
            viewModel.movePlayerByDrag("UP");
        }
    }

    public void propertiesScene(ActionEvent actionEvent) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Properties.fxml"));
        root = fxmlLoader.load();
        stage.setTitle("Properties");
        stage.setScene(new Scene(root, 1000, 700));
        stage.show();
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        PropertiesController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
    }

    public void getStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void HelpScene(ActionEvent actionEvent) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Help.fxml"));
        root = fxmlLoader.load();
        stage.setTitle("About The Game");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        PropertiesController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
    }

    public void AboutScene(ActionEvent actionEvent) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About.fxml"));
        root = fxmlLoader.load();
        stage.setTitle("About The Game");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        PropertiesController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
    }

    public void HideSolution(ActionEvent actionEvent) {
        mazeDisplayer.setSolved(false);
        hideButton.setDisable(true);
        mazeDisplayer.drawMaze(mazeDisplayer.getMaze());
    }
}
