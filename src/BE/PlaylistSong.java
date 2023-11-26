package BE;

public class PlaylistSong {
    private int playlistId;
    private int songId;

    public PlaylistSong(int playlistId, int songId)
    {
        this.playlistId = playlistId;
        this.songId = songId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public int getSongId() {
        return songId;
    }
}
