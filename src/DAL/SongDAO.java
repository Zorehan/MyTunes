package DAL;

import BE.Playlist;
import BE.Song;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SongDAO implements IMyTunesDataAccess {

    private DataBaseConnector dataBaseConnector;

    public SongDAO() throws Exception
    {
        dataBaseConnector = new DataBaseConnector();
    }

    @Override
    public List<Song> getAllSongs() throws Exception {
        ArrayList<Song> allSongs = new ArrayList<>();

        try(Connection conn = dataBaseConnector.getConnection();
            Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM dbo.Song;";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                int id = rs.getInt("Id");
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
                String category = rs.getString("Category");
                String filePath = rs.getString("FilePath");
                int playTime = rs.getInt("playTime");

                Song song = new Song(id, title, artist, category, filePath, playTime);
                allSongs.add(song);
            }
            return allSongs;
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get movies from database", ex);
        }
    }

    @Override
    public Song createSong(Song song) throws Exception {
        return null;
    }

    @Override
    public void updateSong(Song song) throws Exception {

    }

    @Override
    public void deleteSong(Song song) throws Exception {

    }

    @Override
    public List<Playlist> getAllPlaylists() throws Exception {
        return null;
    }

    @Override
    public Playlist createPlaylist(Playlist playlist) throws Exception {
        return null;
    }

    @Override
    public void updatePlaylist(Playlist playlist) throws Exception {

    }

    @Override
    public void deletePlaylist(Playlist playlist) throws Exception {

    }
}
