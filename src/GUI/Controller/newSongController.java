package GUI.Controller;

import BE.Song;
import GUI.Model.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class newSongController implements Initializable {

    @FXML
    private TextField txtSongCategory, txtSongArtist, txtSongTitle;
    @FXML
    private Label lblNewPath;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void clickFileBrowser(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");

        // Set the initial directory, if needed
        // fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Set the selected file path to the TextField
            lblNewPath.setText(selectedFile.getAbsolutePath());
        }
    }

    public void clickCreateSong(ActionEvent actionEvent) {
        try {
            if(lblNewPath.getText().isEmpty())
                return;

            MyTunesModel model = new MyTunesModel();
            Song newSong = new Song(-1, txtSongTitle.getText(), txtSongArtist.getText(),
                                    txtSongCategory.getText(), lblNewPath.getText());
            model.createNewSong(newSong);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
