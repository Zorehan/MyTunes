package GUI.Controller;

import BE.Playlist;
import GUI.Model.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class newPlaylistController {

    private Stage stage;
    private mainViewController parentController;

    @FXML
    private TextField txtPlaylistname;

    public void clickCreatePlaylist(ActionEvent actionEvent) {
        try
        {
            if(txtPlaylistname.getText().isEmpty())
            {
                return;
            }

            MyTunesModel model = MyTunesModel.getInstance();
            Playlist newPlaylist = new Playlist(-1, txtPlaylistname.getText());
            model.createNewPlaylist(newPlaylist);
            stage.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public void setParentController(mainViewController parentController)
    {
        this.parentController = parentController;
    }

}
