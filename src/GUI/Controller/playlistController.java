package GUI.Controller;

import BE.Playlist;
import BE.PlaylistSong;
import BE.Song;
import GUI.Model.MyTunesModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class playlistController implements Initializable {
    private MyTunesModel model;
    private Playlist playlist;
    private ObservableList<Song> observableSongsOnPlaylist;
    private List<PlaylistSong> playlistSongs;

    @FXML
    private TableView<Song> tblSongsOnPlaylist;
    @FXML
    private TableColumn<Song, String> colTitle = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> colArtist = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> colCategory = new TableColumn<>();
    @FXML
    private TableColumn<Song, Double> colTimeSongs = new TableColumn<>();

    public playlistController() {
        try {
            model = MyTunesModel.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.clearPlaylistSongs();
        playlist = model.getPlaylist();
        try {
            update();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(){
        try {
            playlistSongs = model.getPlaylistSongById(playlist.getId());
            model.getObservablePlaylistSongs().addAll(playlistSongs);
            List<Song> songsOnPlaylist = model.getSongsByPlaylistSong(playlistSongs);
            observableSongsOnPlaylist = FXCollections.observableArrayList(songsOnPlaylist);
            tblSongsOnPlaylist.setItems(observableSongsOnPlaylist);
            colTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
            colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
            colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
            colTimeSongs.setCellValueFactory(new PropertyValueFactory<>("playTime"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setSelectedSong(){
        System.out.println(tblSongsOnPlaylist.getSelectionModel().getSelectedItem());
        model.setSong(tblSongsOnPlaylist.getSelectionModel().getSelectedItem());
    }

    public void clickRemove(ActionEvent actionEvent) {
        try {
            Song song = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
            Playlist playlist = model.getPlaylist();
            PlaylistSong ps = new PlaylistSong(playlist.getId(), song.getId());

            model.deletePlaylistSong(ps);
            update();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

