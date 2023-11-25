package BE;


public class Song {
    private int id;
    private String name;
    private String artist;
    private String category;
    private String filePath;
    private int playTime;


    /*
        Har fjernet de fleste parametre fra konstruktøren,
        tænker at sådan noget som playtime ikke behøves at angives her
        men noget vi henter fra mp3 filen, det samme med id som vi bare kan
        incriment på database niveau.
     */
    public Song (int id, String name, String artist, String category, String filePath){
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.category = category;
        this.filePath = filePath;
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

    public String getCategory()
    {
        return category;
    }

    public String getFilePath()
    {
    return filePath;
    }

    public int getPlayTime()
    {
        return playTime;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }
}
