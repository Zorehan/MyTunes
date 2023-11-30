package GUI.Model;

import BE.Playlist;
import BE.PlaylistSong;
import BE.Song;
import BLL.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class MyTunesModel {
    private static MyTunesModel instance;

    private ObservableList<Song> allSongs;

    private ObservableList<Playlist> allPlaylists;

    private ObservableList<PlaylistSong> allPlaylistSongs;

    private ObservableList<Song> allSongsOnPlaylist;

    private SongManager songManager;

    private Song song;
    private Playlist playlist;


    public MyTunesModel() throws Exception
    {
        songManager = new SongManager();
        allSongs = FXCollections.observableArrayList();
        allSongs.addAll(songManager.getAllSongs());

        allPlaylists = FXCollections.observableArrayList();
        allPlaylists.addAll(songManager.getAllPlaylists());

        allPlaylistSongs = FXCollections.observableArrayList();

        allSongsOnPlaylist = FXCollections.observableArrayList();
    }

    public static MyTunesModel getInstance() throws Exception
    {
        if(instance == null)
        {
            instance = new MyTunesModel();
        }
        return instance;
    }

    public Song getSongBySongId(int songId) throws Exception {
        return songManager.getSongBySongId(songId);
    }
    public ObservableList<Song> getObservableSongs()
    {
        return allSongs;
    }

    public List<Song> getSongsByPlaylistSong(List<PlaylistSong> playlistSongs) throws Exception
    {
        List<Song> songs = new ArrayList<>();

        for(PlaylistSong playlistSong : playlistSongs)
        {
            Song s = songManager.getSongBySongId(playlistSong.getSongId());
            songs.add(s);
        }
        return songs;
    }

    public ObservableList<PlaylistSong> getObservablePlaylistSongs()
    {
        return allPlaylistSongs;
    }

    public void clearPlaylistSongs()
    {
        allPlaylistSongs.clear();
    }

    public List<PlaylistSong> getPlaylistSongById(int playListId) throws Exception {
        return songManager.getPlayListSongsById(playListId);
    }

    public void addToPlaylistSongs(List<PlaylistSong> playlistSongList)
    {
        allPlaylistSongs.addAll(playlistSongList);
    }

    public List<Song> getSongsByPlaylistId(int playlistId) throws Exception
    {
        return songManager.getSongsByPlaylistId(playlistId);
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

    public Song createNewSong(Song newSong) throws Exception
    {
        Song song = songManager.createNewSong(newSong);
        allSongs.add(song);
        return song;
    }

    public Playlist createNewPlaylist(Playlist playlist) throws Exception
    {
        Playlist p = songManager.createNewPlaylist(playlist);
        allPlaylists.add(p);
        return p;
    }

    public PlaylistSong createNewPlaylistSong(PlaylistSong playlistSong) throws Exception
    {
        PlaylistSong p = songManager.createNewPlaylistSong(playlistSong);
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

    public Song getSong(){
        return song;
    }

    public Playlist getPlaylist(){
        return playlist;
    }

    public void setSong(Song song){
        if(song != null){
            this.song = song;
        }
    }

    public void setPlaylist(Playlist playlist){
        if(playlist != null){
            this.playlist = playlist;
        }
    }
}
