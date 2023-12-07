package GUI.Controller;

import BE.Song;
import GUI.Model.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class browseViewController implements Initializable{
    private MyTunesModel model;
    private mainViewController mainController;

    @FXML
    private TableView<Song> tblSongs;
    @FXML
    private TableColumn<Song, String> colTitle = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> colArtist = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> colCategory = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> colTimeSongs = new TableColumn<>();

    public browseViewController()
    {
        try {
            model = MyTunesModel.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblSongs.setItems(model.getObservableSongs());
        model.setPlaylistSongs(model.getObservableSongs());
        colTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colTimeSongs.setCellValueFactory(new PropertyValueFactory<>("playTime"));

        tblSongs.setEditable(true);
        colTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        colArtist.setCellFactory(TextFieldTableCell.forTableColumn());
        colCategory.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void setSelectedSong(){
        model.setSong(tblSongs.getSelectionModel().getSelectedItem());
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

    public void clickAddToPlaylist(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addSongToPlaylistView.fxml"));
            Parent newWindow = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Song");
            stage.setScene(new Scene(newWindow));

            songToPlaylistController controller = loader.getController();
            controller.setParentController(this);
            controller.setStage(stage);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}