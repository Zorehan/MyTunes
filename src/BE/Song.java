package BE;

public class Song {
    private int id;
    private String name;
    private String artist;

    public Song (int id, String name, String artist, String category, String filePath, int playTime){
        this.id = id;
        this.name = name;
        this.artist = artist;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
