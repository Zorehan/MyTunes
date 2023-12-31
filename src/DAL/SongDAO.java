package DAL;

import BE.Playlist;
import BE.PlaylistSong;
import BE.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;

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

    public List<PlaylistSong> getPlaylistSongsByPlaylistId(int playlistId) throws SQLException {
        List<PlaylistSong> playlistSongs = new ArrayList<>();

        try (Connection connection = dataBaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PlaylistSongs WHERE PlaylistId = ?")
        ) {
            preparedStatement.setInt(1, playlistId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int songId = resultSet.getInt("SongId");
                    PlaylistSong playlistSong = new PlaylistSong(playlistId, songId);
                    playlistSongs.add(playlistSong);
                }
            }
        }

        return playlistSongs;
    }



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
                String filePath = rs.getString("FilePath");;

                Song song = new Song(id, title, artist, category, filePath);
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


    public Song createSong(Song song) throws Exception {
        String sql = "INSERT INTO dbo.Song (Title, Artist, Category, FilePath) VALUES (?, ?, ?, ?);";

        try (Connection conn = dataBaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);)
        {
            stmt.setString(1,song.getName());
            stmt.setString(2,song.getArtist());
            stmt.setString(3,song.getCategory());
            stmt.setString(4,song.getFilePath());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if(rs.next())
            {
                id = rs.getInt(1);
            }

            Song createdSong = new Song(id,song.getName(), song.getArtist(),
                                        song.getCategory(),song.getFilePath());

            return createdSong;
        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create song", ex);
        }
    }
    public Song getSongsBySongId(int songId) throws Exception
    {
        String sql = "SELECT * FROM dbo.Song WHERE id = ?;";
        try (Connection conn = dataBaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, songId);
            ResultSet rs = stmt.executeQuery();

            Song song = null;
            while (rs.next()) {
                int id = rs.getInt("Id");
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
                String category = rs.getString("Category");
                String filePath = rs.getString("FilePath");

                song = new Song(id, title, artist, category, filePath);
            }
            return song;
        }
    }

    public List<Song> getSongsByPlaylistId(int playlistId) throws Exception
    {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT Song.* FROM Song " + "JOIN PlaylistSongs ON Song.Id = PlaylistSongs.SongId " + "WHERE PlaylistSongs.playlistId = ?;";
        try(Connection conn = dataBaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, playlistId);

            try (ResultSet rs = stmt.executeQuery())
            {
                while(rs.next())
                {
                    int id = rs.getInt("Id");
                    String title = rs.getString("Title");
                    String artist = rs.getString("Artist");
                    String category = rs.getString("Category");
                    String filePath = rs.getString("FilePath");

                    Song song = new Song(id, title, artist, category, filePath);
                    songs.add(song);
                }
            }
        }
        return songs;
    }

    public void updateSong(Song song) throws Exception {
    String sql = "UPDATE dbo.Song SET Title = ?, Artist = ?, Category = ?, FilePath = ? WHERE id = ?;";
    try (Connection conn = dataBaseConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);)
    {
        stmt.setString(1,song.getName());
        stmt.setString(2,song.getArtist());
        stmt.setString(3,song.getCategory());
        stmt.setString(4,song.getFilePath());
        stmt.setInt(5,song.getId());

        stmt.executeUpdate();

    }
    catch(SQLException ex)
    {
        ex.printStackTrace();
        throw new Exception("Could not update song", ex);
    }
    }


    public void deleteSong(Song song) throws Exception {
    String sql = "DELETE FROM dbo.Song WHERE id = ?;";
    try(Connection conn = dataBaseConnector.getConnection())
    {
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1,song.getId());
        stmt.executeUpdate();
    }
    catch(SQLException ex)
    {
        ex.printStackTrace();
        throw new Exception("Could not delete song", ex);
    }
    }

    @Override
    public List<Playlist> getAllPlaylists() throws Exception {
        ArrayList<Playlist> allPlaylists = new ArrayList<>();

        try(Connection conn = dataBaseConnector.getConnection();
            Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM dbo.Playlist;";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next())
            {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");

                Playlist playlist = new Playlist(id, name);
                allPlaylists.add(playlist);
            }
            return allPlaylists;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database", ex);
        }
    }

    @Override
    public Playlist createPlaylist(Playlist playlist) throws Exception {
        String sql = "INSERT INTO dbo.Playlist (Name) VALUES (?);";

        try (Connection conn = dataBaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);)
        {
            stmt.setString(1, playlist.getName());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if(rs.next())
            {
                id = rs.getInt(1);
            }

            Playlist createdPlaylist = new Playlist(id, playlist.getName());

            return createdPlaylist;
        }
    }

    @Override
    public void updatePlaylist(Playlist playlist) throws Exception {
        String sql = "UPDATE dbo.Playlist SET Name = ? WHERE id = ?;";
        try (Connection conn = dataBaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);)
        {
            stmt.setString(1, playlist.getName());
            stmt.setInt(2, playlist.getId());

            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update playlist", ex);
        }
    }

    public void deletePlaylist(Playlist playlist) throws Exception {
        String sql = "DELETE FROM dbo.Playlist WHERE id = ?;";
        try(Connection conn = dataBaseConnector.getConnection())
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1,playlist.getId());
            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not delete playlist", ex);
        }
    }

    public PlaylistSong createPlaylistSong(PlaylistSong playlistSong) throws SQLServerException {
        String sql = "INSERT INTO dbo.PlaylistSongs (PlaylistId, SongId) VALUES (?,?);";

        try(Connection conn = dataBaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);)
        {
            stmt.setInt(1, playlistSong.getPlaylistId());
            stmt.setInt(2, playlistSong.getSongId());

            stmt.executeUpdate();

            PlaylistSong newPlaylistSong = new PlaylistSong(playlistSong.getPlaylistId(), playlistSong.getSongId());

            return newPlaylistSong;

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePlaylistSong(PlaylistSong playlistSong) throws Exception
    {
        String sql = "DELETE FROM dbo.PlaylistSongs WHERE PlaylistId = ? AND SongId = ?;";

        try(Connection conn = dataBaseConnector.getConnection())
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, playlistSong.getPlaylistId());
            stmt.setInt(2, playlistSong.getSongId());

            stmt.executeUpdate();

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not delete song", ex);
        }
    }
}
