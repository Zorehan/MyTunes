package GUI.Controller;

import BE.PlayList;
import BE.Song;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class mainMyTunesViewController implements Initializable
{
    @FXML
    private TableView<PlayList> tblPlaylists;
    /*
    Den her vil jeg helst først skrive når vi har databasen så jeg ved
    hvad fuck det skal være med variabler, men umiddelbart:
    @FXML
    private TableColumn<PlayList, String> colName;
    @FXML
    private TableColumn<PlayList, Integer> colSongs;
    @FXML
    private TableColumn<PlayList, Integer> colTimePlayList;
     */
    @FXML
    private ListView<Song> lstSongsOnPlaylist;
    @FXML
    private TableView<Song> tblSongs;
    /*
    Det samme som i kommentaren ovenover
    @FXML
    private TableColumn<Song, String> colTitle;
    @FXML
    private TableColumn<Song, String> colArtist;
    @FXML
    private TableColumn<Song, String> colCategory;
    @FXML
    private TableColumn<Song, Integer> colTimeSongs;
     */
    @FXML
    private TextField txtSongSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}