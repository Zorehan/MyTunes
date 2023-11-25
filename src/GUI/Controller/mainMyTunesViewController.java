package GUI.Controller;

import BE.Playlist;
import BE.Song;
import GUI.Model.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class mainMyTunesViewController implements Initializable {

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
    @FXML
    private TableColumn<Song, String> colTitle = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> colArtist = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> colCategory = new TableColumn<>();
    @FXML
    private TableColumn<Song, Integer> colTimeSongs = new TableColumn<>();
    @FXML
    private TextField txtSongSearch;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            MyTunesModel model = new MyTunesModel();
            tblSongs.setItems(model.getObservableSongs());

            colTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
            colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
            colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
            colTimeSongs.setCellValueFactory(new PropertyValueFactory<>("playTime"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void play(String filePathPlay) {
        File file = new File(filePathPlay);
        stop();

        if (file != null) {
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnEndOfMedia(() -> stop());

            mediaPlayer.play();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /*
     * Åbner et ny vindue så man kan lave en Song object fra scratch
     */
    public void clickNewSong(ActionEvent actionEvent) throws Exception {
        try {
            Parent newWindow = FXMLLoader.load(getClass().getResource("/view/newSongView.fxml"));

            Stage stage = new Stage();
            stage.setTitle("New Song");
            stage.setScene(new Scene(newWindow));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickDeleteSong(ActionEvent actionEvent) {

    }

    public void clickPlaySong(ActionEvent actionEvent) {
    }
}
