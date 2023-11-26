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
    private mainMyTunesViewController parentController;
    private Stage stage;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public void setParentController(mainMyTunesViewController parentController)
    {
        this.parentController = parentController;
    }

    public void clickFileBrowser(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");

        // Set the initial directory, if needed
        // fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Set the selected file path to the TextField
            lblNewPath.setText(selectedFile.getPath());
        }
    }

    /*
    Ændringen i bund og grund er at istedet for at gemme hele filepathen så kører den relativepath metoden
    som kun giver filepathen fra ordet "data" ergo ingen computer specific filepath (og play metoden kan faktisk køre ikke fulde paths)
     */
    public void clickCreateSong(ActionEvent actionEvent) {
        try {
            if(lblNewPath.getText().isEmpty())
                return;

            String fullPath = lblNewPath.getText();
            String relativePath = getRelativePath(fullPath);

            MyTunesModel model = MyTunesModel.getInstance();
            Song newSong = new Song(-1, txtSongTitle.getText(), txtSongArtist.getText(),
                                    txtSongCategory.getText(), relativePath);
            model.createNewSong(newSong);
            stage.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getRelativePath(String fullPath)
    {
        int indexOfData = fullPath.indexOf("data");
        if(indexOfData != -1)
        {
            return fullPath.substring(indexOfData);
        }
        else
        {
            return fullPath;
        }
    }
}
