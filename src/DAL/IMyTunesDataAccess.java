package DAL;

import BE.Playlist;
import BE.PlaylistSong;
import BE.Song;

import java.util.List;

public interface IMyTunesDataAccess {

    public List<Song> getAllSongs() throws Exception;

    public Song createSong(Song song) throws Exception;

    public void updateSong(Song song) throws Exception;

    public void deleteSong(Song song) throws Exception;

    public List<Playlist> getAllPlaylists() throws Exception;

    public Playlist createPlaylist(Playlist playlist) throws Exception;

    public void updatePlaylist(Playlist playlist) throws Exception;

    public void deletePlaylist(Playlist playlist) throws Exception;

    public PlaylistSong createPlaylistSong(PlaylistSong playlistSong) throws Exception;

    public void deletePlaylistSong(PlaylistSong playlistSong) throws Exception;

    public List<PlaylistSong> getPlaylistSongsByPlaylistId(int playlistId) throws Exception;

    public List<Song> getSongsByPlaylistId(int playlistId) throws Exception;

    public Song getSongsBySongId(int songId) throws Exception;
}
