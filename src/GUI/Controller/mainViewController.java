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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class mainViewController implements Initializable {
    public ProgressBar songBar;
    public TableColumn<Playlist, String> colName = new TableColumn<>();
    private MediaPlayer mediaPlayer;
    private Duration pausedTime;
    private Timer timer;
    private boolean playing;
    private TimerTask timerTask;
    @FXML
    private Button btnPlayPause;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TableView<Playlist> tblPlaylists;
    @FXML
    private Slider volumeSlider;
    @FXML
    private TextField txtSongSearch;
    @FXML
    private Label lblCurrentSong, lblCurrentArtist, lblSongCurrent, lblSongEnd;
    private MyTunesModel model;
    private browseViewController browseController;
    private playlistController playlistController;
    private List<Song> songList;
    private Song song;

    public mainViewController() {
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
        //Tillad edits i columns
        tblPlaylists.setEditable(true);
    }

    public void beginTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = mediaPlayer.getMedia().getDuration().toSeconds();
                songBar.setProgress(current / end);
                if (current / end == 1) {
                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 500, 500);
    }

    public void cancelTimer() {
        timer.cancel();
    }

    /**
     * Begynder at få label til at tælle op så man kan se hvor langt i sangen man er.
     */
    public void beginLblTimer(){
        mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
            lblSongCurrent.setText(getCurrentTime());
        });
    }

    public String getCurrentTime(){
        double seconds = mediaPlayer.getCurrentTime().toSeconds();

        double sec = seconds % 60;
        double minutes = (seconds/60) % 60;

        return String.format("%d:%02d",(int)minutes, (int)sec);
    }

    public void play(Song song) {
        File file = new File(song.getFilePath());
        stop(mediaPlayer);

        if (file != null) {
            setSongName(song);
            beginTimer();
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            mediaPlayer.setOnEndOfMedia(this::nextSong);
            mediaPlayer.play();
            lblSongEnd.setText(model.getSong().getPlayTime());
            beginLblTimer();
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

    public void nextSong(){
        songList = model.getAllSongsOnPlaylist();

        int curIndex = songList.indexOf(song);
        Song nextSong;

        if(curIndex == songList.size() - 1) {
            curIndex = 0;
            nextSong = songList.get(curIndex);
        }
        else
            nextSong = songList.get(curIndex + 1);

        song = nextSong;
        play(song);
    }

    public void prevSong(){
        songList = model.getAllSongsOnPlaylist();

        int curIndex = songList.indexOf(song);
        Song nextSong;

        if(curIndex == 0) {
            curIndex = songList.size() - 1;
            nextSong = songList.get(curIndex);
        }
        else
            nextSong = songList.get(curIndex - 1);

        song = nextSong;
        play(song);
    }

    public void clickNextSong(ActionEvent actionEvent) throws Exception {
        nextSong();
    }

    public void clickPrevSong(ActionEvent actionEvent) {
        prevSong();
    }

    public void clickPlaySong(ActionEvent actionEvent) {
        if(!playing){
            song = model.getSong();
            songPlay(song);
            playing = true;
            btnPlayPause.setText("Pause");
        }
        else if(playing){
            cancelTimer();
            pause(mediaPlayer);
            playing = false;
            btnPlayPause.setText("Play");
        }
    }

    public void songPlay(Song song) {
        //selectedSong er markeret fra listen og dens path gemmes til songPath
        if (song != null) {
            Path songPath = Paths.get(song.getFilePath()).normalize();
            System.out.println("Song path : " + songPath);
            if (mediaPlayer == null) {
                play(song);
            } else {
                //Filepath på sangen som i mediaplayer gemems
                String mediaPathString = mediaPlayer.getMedia().getSource();
                mediaPathString = mediaPathString.replace("%20", " "); //io.file erstatter spaces med "%20" så her erstattes det med " "
                mediaPathString = mediaPathString.substring(mediaPathString.indexOf("data")); //Skør måde at lave relative path.
                Path mediaPath = Paths.get(mediaPathString).normalize();
                //Fortsætter paused sang.
                if (mediaPath.equals(songPath)) {
                    mediaPlayer.seek(pausedTime);
                    mediaPlayer.play();
                } else { //Starter ny selectedSong
                    play(song);
                }
            }
        }
    }

    public void clickNewPlaylist(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/newPlaylistView.fxml"));
            Parent newWindow = loader.load();

            Stage stage = new Stage();
            stage.setTitle("New Playlist");
            stage.setScene(new Scene(newWindow));

            newPlaylistController controller = loader.getController();
            controller.setParentController(this);
            controller.setStage(stage);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dobbelt klik for at redigere playlist navn
     */
    public void editPlaylistName(TableColumn.CellEditEvent editedCell) {
        try {
            Playlist selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
            selectedPlaylist.setName(editedCell.getNewValue().toString());
            model.updatePlaylist(selectedPlaylist);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sletter en playliste på "delete" knappen
     */
    public void clickDeletePlaylist(ActionEvent actionEvent) {
        try {
            Playlist playlist = tblPlaylists.getSelectionModel().getSelectedItem();
            model.deletePlaylist(playlist);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changeVolume(MouseEvent mouseEvent) {
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue)
                -> mediaPlayer.setVolume(volumeSlider.getValue() * 0.01));
    }

    public void setSongName(Song song) {
        lblCurrentSong.setText(song.getName());
        lblCurrentArtist.setText(song.getArtist());
    }

    public void clickBrowse(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/browseView.fxml"));
        Parent p = loader.load();
        browseController = loader.getController();
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/browseView.fxml")));
        borderPane.setCenter(view);
    }

    public void clickOpenPlaylist(MouseEvent mouseEvent) throws IOException {
        model.setPlaylist(tblPlaylists.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/playlistView.fxml"));
        Parent p = loader.load();
        playlistController = loader.getController();
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/playlistView.fxml")));
        borderPane.setCenter(view);
    }
}
