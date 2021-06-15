package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable, Observer {

    public MyViewModel viewModel;
    private Scene scene;
    private Stage stage;

    public javafx.scene.control.TextField textField_thread;
    public javafx.scene.control.TextField textField_mazeGen;
    public javafx.scene.control.TextField textField_mazeSearch;
    public javafx.scene.control.Button prop;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "properties change":
//                setProperties();
                break;
            default:
                System.out.println("Not implemented change: " + change);
                break;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void back(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MyView.fxml"));
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
    }

    public void setProperties(ActionEvent actionEvent) throws IOException {
        String thread = textField_thread.getText();
        String gen = textField_mazeGen.getText();
        String search = textField_mazeSearch.getText();
        viewModel.setProperties(thread, gen, search);
        back(actionEvent);
    }

    public void BackToMain(ActionEvent actionEvent) throws IOException {
        back(actionEvent);
    }
}
