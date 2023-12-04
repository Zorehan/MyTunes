package GUI.Controller;

import BE.Playlist;
import BE.PlaylistSong;
import BE.Song;
import GUI.Model.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class songToPlaylistController implements Initializable {
    @FXML
    private TableView<Playlist> tblPlaylists;
    @FXML
    public TableColumn<Playlist, String> colName = new TableColumn<>();
    MyTunesModel model;
    private browseViewController parentController;
    private Stage stage;

    public songToPlaylistController() {
        try {
            model = MyTunesModel.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblPlaylists.setItems(model.getObservablePlaylists());

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public void setParentController(browseViewController parentController)
    {
        this.parentController = parentController;
    }

    public void clickAddSong (ActionEvent actionEvent){
        try {
            Song selectedSong = model.getSong();
            Playlist playlist = tblPlaylists.getSelectionModel().getSelectedItem();
            PlaylistSong ps = new PlaylistSong(playlist.getId(), selectedSong.getId());

            model.createNewPlaylistSong(ps);
            stage.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clickCancel (ActionEvent actionEvent){
        stage.close();
    }

        /*
        }
    try {
        if (mainController.getSelectedPlaylist() == null && tblSongs.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Song s = tblSongs.getSelectionModel().getSelectedItem();
        Playlist p = mainController.getSelectedPlaylist();

        PlaylistSong ps = new PlaylistSong(p.getId(), s.getId());
        model.createNewPlaylistSong(ps);
    } catch (Exception e) {
        throw new RuntimeException();
    }
         */
}
