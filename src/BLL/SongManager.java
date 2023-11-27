package BLL;

import BE.Playlist;
import BE.PlaylistSong;
import BE.Song;
import BLL.util.SongSearcher;
import DAL.IMyTunesDataAccess;
import DAL.SongDAO;

import java.io.IOException;
import java.util.List;

public class SongManager {

    private SongSearcher songSearcher = new SongSearcher();

    private IMyTunesDataAccess songDAO;

    public SongManager() throws Exception
    {
        songDAO = new SongDAO();
    }

    public List<PlaylistSong> getPlayListSongsById(int playlistId) throws Exception
    {
        return songDAO.getPlaylistSongsByPlaylistId(playlistId);
    }

    public List<Song> getAllSongs() throws Exception
    {
        return songDAO.getAllSongs();
    }
    public Song getSongBySongId(int songId) throws Exception
    {
        return songDAO.getSongsBySongId(songId);
    }
    public List<Song> getSongsByPlaylistId(int playlistId) throws Exception
    {
        return songDAO.getSongsByPlaylistId(playlistId);
    }

    public List<Playlist> getAllPlaylists() throws Exception
    {
        return songDAO.getAllPlaylists();
    }

    public List<Song> searchSongs(String query) throws Exception
    {
        List<Song> allSongs = getAllSongs();
        List<Song> searchResult = songSearcher.search(allSongs, query);
        return searchResult;
    }

    public Song createNewSong(Song newSong) throws Exception
    {
        return songDAO.createSong(newSong);
    }

    public Playlist createNewPlaylist(Playlist newPlaylist) throws Exception
    {
        return songDAO.createPlaylist(newPlaylist);
    }

    public PlaylistSong createNewPlaylistSong(PlaylistSong newPlaylistSong) throws Exception {
        return songDAO.createPlaylistSong(newPlaylistSong);
    }

    public void deletePlaylistSong(PlaylistSong playlistSong) throws Exception
    {
        songDAO.deletePlaylistSong(playlistSong);
    }

    public void deleteSong(Song song) throws Exception
    {
        songDAO.deleteSong(song);
    }

    public void deletePlaylist(Playlist playlist) throws Exception
    {
        songDAO.deletePlaylist(playlist);
    }

    public void updateSong(Song song) throws Exception
    {
        songDAO.updateSong(song);
    }

    public void updatePlaylist(Playlist playlist) throws Exception
    {
        songDAO.updatePlaylist(playlist);
    }

}
