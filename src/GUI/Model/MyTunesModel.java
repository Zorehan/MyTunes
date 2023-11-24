package GUI.Model;

import BE.Playlist;
import BE.Song;
import BLL.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MyTunesModel {

    private ObservableList<Song> allSongs;

    private ObservableList<Playlist> allPlaylists;

    private SongManager songManager;

    public MyTunesModel() throws Exception
    {
        songManager = new SongManager();
        allSongs = FXCollections.observableArrayList();
        allSongs.addAll(songManager.getAllSongs());

        allPlaylists = FXCollections.observableArrayList();
        allPlaylists.addAll(songManager.getAllPlaylists());
    }

    public ObservableList<Song> getObservableSongs()
    {
        return allSongs;
    }

    public ObservableList<Playlist> getObservablePlaylists()
    {
        return allPlaylists;
    }

    public void searchSong(String query) throws Exception
    {
        List<Song> searchResults = songManager.searchSongs(query);
        allSongs.clear();
        allSongs.addAll(searchResults);
    }

    public Song createNewSong(Song song) throws Exception
    {
        Song s = songManager.createNewSong(song);
        allSongs.add(s);
        return s;
    }

    public Playlist createNewPlaylist(Playlist playlist) throws Exception
    {
        Playlist p = songManager.createNewPlaylist(playlist);
        allPlaylists.add(p);
        return p;
    }

    public void deleteSong(Song song) throws Exception
    {
        songManager.deleteSong(song);
        allSongs.remove(song);
    }

    public void deletePlaylist(Playlist playlist) throws Exception
    {
        songManager.deletePlaylist(playlist);
        allPlaylists.remove(playlist);
    }

    public void updateSong(Song song) throws Exception
    {
        songManager.updateSong(song);
    }

    public void updatePlaylist(Playlist playlist) throws Exception
    {
        songManager.updatePlaylist(playlist);
    }
}
