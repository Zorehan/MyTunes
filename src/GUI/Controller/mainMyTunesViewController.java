package GUI.Controller;

import BE.Playlist;
import BE.Song;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class mainMyTunesViewController implements Initializable
{
    private MediaPlayer mediaPlayer;
    @FXML
    private TableView<Playlist> tblPlaylists;
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

    public void play(String filePathPlay)
    {
        File file = new File(filePathPlay);
        stop();

        if(file != null)
        {
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnEndOfMedia(() -> stop());

            mediaPlayer.play();
        }
    }

    public void stop()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
    }
}
