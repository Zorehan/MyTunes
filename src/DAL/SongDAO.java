package DAL;

import BE.Playlist;
import BE.Song;

import java.io.IOException;
import java.sql.*;
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
        String sql = "INSERT INTO dbo.Song (Title,Artist,Category,FilePath,playTime) VALUES (?,?);";

        try (Connection conn = dataBaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);)
        {
            stmt.setString(1,song.getName());
            stmt.setString(2,song.getArtist());
            stmt.setString(3,song.getCategory());
            stmt.setString(4,song.getFilePath());
            stmt.setInt(5,song.getPlayTime());
            
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if(rs.next())
            {
                id = rs.getInt(1);
            }

            Song createdSong = new Song(id,song.getName(), song.getArtist(),
                                        song.getCategory(),song.getFilePath(),
                                        song.getPlayTime());

            return createdSong;
        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create song", ex);
        }
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
