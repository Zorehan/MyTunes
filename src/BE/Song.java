package BE;


import java.io.File;
import java.math.BigDecimal;

import ws.schild.jave.MultimediaObject;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;


public class Song {
    private int id;
    private String name;
    private String artist;
    private String category;
    private String filePath;
    private double playTime;


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
        this.playTime = getPlayTime();
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

    public double getPlayTime()
    {
        try {
            // Tjekker om det er en mp3 fil
            if (filePath.toLowerCase().endsWith(".mp3")) {
                AudioFile audioFile = AudioFileIO.read(new File(filePath));
                int seconds = audioFile.getAudioHeader().getTrackLength() % 60;
                int minutes = audioFile.getAudioHeader().getTrackLength() / 60;

                // Vi bruger "BigDecimal" til af afrunde til 2 cifre.
                BigDecimal roundedValue = new BigDecimal(minutes + (seconds / 100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
                return roundedValue.doubleValue();
            }
            // Tjekker om det er en .WAV fil
            else if (filePath.toLowerCase().endsWith(".wav")) {
                // WAV filer skal bruge en anden type library end jaudiotagger
                File file = new File(filePath);
                MultimediaObject multimediaObject = new MultimediaObject(file);
                long durationMillis = multimediaObject.getInfo().getDuration();
                double durationMinutes = durationMillis / 1000.0 / 60.0;
                int minutes = (int) durationMinutes;
                int seconds = (int) ((durationMinutes - minutes) * 60);

                return minutes + (seconds / 100.0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return 0.0; //Retunerer 0.0 hvis playtime ikke kan bestemmes.
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

}
