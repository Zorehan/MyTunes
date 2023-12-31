package BLL.util;

import BE.Song;

import java.util.ArrayList;
import java.util.List;

public class SongSearcher {

    public List<Song> search(List<Song> searchBase, String query)
    {
        List<Song> searchResult = new ArrayList<>();

        for(Song song : searchBase)
        {
            if(compareToSongTitle(query, song) || compareToSongArtist(query, song) || compareToSongCategory(query, song))
            {
                searchResult.add(song);
            }
        }

        return searchResult;
    }

    private boolean compareToSongTitle(String query, Song song)
    {
        return song.getName().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToSongCategory(String query, Song song)
    {
        return song.getCategory().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToSongArtist(String query, Song song)
    {
        return song.getArtist().toLowerCase().contains(query.toLowerCase());
    }
}
