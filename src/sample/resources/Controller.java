package sample.resources;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private static TextArea textArea;
    @FXML
    private Button startServerButton;

    @FXML
    public void onButtonClick(ActionEvent event){
        String content= null;
        try {
            content = InetAddress.getLocalHost().toString();
            textArea.setText(content);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public static void setTextArea(String content){
        textArea.setText(content);
    }
}
