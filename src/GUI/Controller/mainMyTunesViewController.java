package GUI.Controller;

import BE.Playlist;
import BE.PlaylistSong;
import BE.Song;
import GUI.Model.MyTunesModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class mainMyTunesViewController implements Initializable {

    public ProgressBar songBar;
    private MediaPlayer mediaPlayer;
    private Duration pausedTime;
    private Timer timer;
    private TimerTask timerTask;
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
    private Slider volumeSlider;
    @FXML
    private TableView<Song> tblSongsOnPlaylist;
    @FXML
    private TableView<Song> tblSongs;
    @FXML
    private TableColumn<Song, String> colTitle = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> colArtist = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> colCategory = new TableColumn<>();
    @FXML
    private TableColumn<Song, Double> colTimeSongs = new TableColumn<>();
    @FXML
    private TableColumn<Playlist, String> colName = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> colSongsOnPlaylistTitle = new TableColumn<>();
    @FXML
    private TableColumn<Song, Double> colTimeSongsOnPlaylist = new TableColumn<>();
    @FXML
    private TextField txtSongSearch;

    private MyTunesModel model;

    public mainMyTunesViewController()
    {
        try {
            model = MyTunesModel.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            tblSongs.setItems(model.getObservableSongs());
            tblPlaylists.setItems(model.getObservablePlaylists());


            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
            colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
            colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
            colTimeSongs.setCellValueFactory(new PropertyValueFactory<>("playTime"));
            colSongsOnPlaylistTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
            colTimeSongsOnPlaylist.setCellValueFactory(new PropertyValueFactory<>("playTime"));

            //Tillad edits i columns
            tblSongs.setEditable(true);
            tblPlaylists.setEditable(true);
            colTitle.setCellFactory(TextFieldTableCell.forTableColumn());
            colArtist.setCellFactory(TextFieldTableCell.forTableColumn());
            colCategory.setCellFactory(TextFieldTableCell.forTableColumn());
            colName.setCellFactory((TextFieldTableCell.forTableColumn()));

            txtSongSearch.textProperty().addListener(((observable, oldValue, newValue) ->
            {
                try
                {
                    model.searchSong(newValue);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }));

            tblPlaylists.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->
            {

                model.clearPlaylistSongs();

                if(newValue != null)
                {
                   Playlist selectedPlaylist = newValue;

                   try
                   {
                       List<PlaylistSong> playlistSongs = model.getPlaylistSongById(newValue.getId());
                       model.getObservablePlaylistSongs().addAll(playlistSongs);

                       List<Song> songsOnPlaylist = model.getSongsByPlaylistSong(playlistSongs);

                       ObservableList<Song> observableSongsOnPlaylist = FXCollections.observableArrayList(songsOnPlaylist);
                       tblSongsOnPlaylist.setItems(observableSongsOnPlaylist);
                   } catch (Exception e) {
                       throw new RuntimeException(e);
                   }
                }
            }));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void beginTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = mediaPlayer.getMedia().getDuration().toSeconds();
                songBar.setProgress(current/end);

                if(current/end == 1){
                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 500, 500);
    }

    public void cancelTimer(){
        timer.cancel();
    }

    public void play(Song song) {
        File file = new File(song.getFilePath());
        stop(mediaPlayer);

        if (file != null) {
            beginTimer();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/newSongView.fxml"));
            Parent newWindow = loader.load();

            Stage stage = new Stage();
            stage.setTitle("New Song");
            stage.setScene(new Scene(newWindow));

            newSongController controller = loader.getController();
            controller.setParentController(this);
            controller.setStage(stage);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickDeleteSong(ActionEvent actionEvent) throws Exception {
    Song song = tblSongs.getSelectionModel().getSelectedItem();
    model.deleteSong(song);
    }

    public void clickPlaySong(ActionEvent actionEvent) {
       if(tblSongsOnPlaylist.getSelectionModel().getSelectedItem() != null)
       {
           Song selectedSong = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
           Path songPath = Paths.get(selectedSong.getFilePath()).normalize();

           if(mediaPlayer == null)
           {
               play(selectedSong);
           }
           else
           {
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
                   play(selectedSong);
               }
           }
       }

        //selectedSong er markeret fra listen og dens path gemmes til songPath
        else if(tblSongs.getSelectionModel().getSelectedItem() != null)
        {
            Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
            Path songPath = Paths.get(selectedSong.getFilePath()).normalize();

            if (mediaPlayer == null) {
                play(selectedSong);
            }
            else {
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
                    play(selectedSong);
                }
            }
        }

    }

    public void clickPauseSong(ActionEvent actionEvent) {
        cancelTimer();
        pause(mediaPlayer);
    }

    public void clickStopSong(ActionEvent actionEvent) {
        stop(mediaPlayer);
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Dobbelt klik for at redigere playlist navn
     */
    public void editPlaylistName(TableColumn.CellEditEvent editedCell){
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

    /**
     * Dobbelt klik for at redigere sangnavne
     */
    public void editSongName(TableColumn.CellEditEvent editedCell){
        try {
            Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
            selectedSong.setName(editedCell.getNewValue().toString());
            model.updateSong(selectedSong);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Dobbelt klik for at redigere artisten
     */
    public void editArtistName(TableColumn.CellEditEvent editedCell){
        try {
            Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
            selectedSong.setArtist(editedCell.getNewValue().toString());
            model.updateSong(selectedSong);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Dobbelt klik for at redigere category
     */
    public void editCategoryName(TableColumn.CellEditEvent editedCell){
        try {
            Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
            selectedSong.setCategory(editedCell.getNewValue().toString());
            model.updateSong(selectedSong);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clickSongToPlaylist(ActionEvent actionEvent) throws Exception {
        try
        {
            if(tblPlaylists.getSelectionModel().getSelectedItem() == null && tblSongs.getSelectionModel().getSelectedItem() == null)
            {
                return;
            }

            Song s = tblSongs.getSelectionModel().getSelectedItem();
            Playlist p = tblPlaylists.getSelectionModel().getSelectedItem();

            PlaylistSong ps = new PlaylistSong(p.getId(), s.getId());
            model.createNewPlaylistSong(ps);
        }
        catch(Exception e)
        {
            throw new RuntimeException();
        }
    }

    public void changeVolume(MouseEvent mouseEvent) {
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue)
                -> mediaPlayer.setVolume(volumeSlider.getValue() * 0.01));
    }
}


