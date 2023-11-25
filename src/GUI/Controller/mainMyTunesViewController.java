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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

public class mainMyTunesViewController implements Initializable {

    private MediaPlayer mediaPlayer;
    private Duration pausedTime;
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

    public void play(Song song) {
        File file = new File(song.getFilePath());
        stop(mediaPlayer);

        if (file != null) {
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnEndOfMedia(() -> stop(mediaPlayer));

            mediaPlayer.play();
        }
    }

    public void stop(MediaPlayer media) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void pause(MediaPlayer media) {
        if (mediaPlayer != null) {
            pausedTime = mediaPlayer.getCurrentTime();
            mediaPlayer.pause();
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
        //selectedSong er markeret fra listen og dens path gemmes til songPath
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
        Path songPath = Paths.get(selectedSong.getFilePath()).normalize();

        if (mediaPlayer == null) {
            play(selectedSong);
        }
        else {
            //Filepath på sangen som i mediaplayer gemems
            String mediaPathString = Objects.requireNonNull(mediaPlayer.getMedia().getSource()).substring(6); //io.file har "file:/" foran filepathen, så her fjerner vi det.
            mediaPathString = mediaPathString.replace("%20", " "); //io.file erstatter spaces med "%20" så her erstattes det med " "
            Path mediaPath = Paths.get(mediaPathString).normalize();

            //Fortsætter paused sang.
            if (mediaPath.equals(songPath)) {
                mediaPlayer.seek(pausedTime);
                mediaPlayer.play();
            } else { //Starter ny selectedSong
                play(selectedSong);
            }
        }
    }

    public void clickPauseSong(ActionEvent actionEvent) {
        pause(mediaPlayer);
    }

    public void clickStopSong(ActionEvent actionEvent) {
        stop(mediaPlayer);
    }
}
