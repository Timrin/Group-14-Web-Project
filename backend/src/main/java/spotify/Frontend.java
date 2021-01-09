package spotify;

public class Frontend {


    private String track_name;
    private String artist;
    private String uri;
    private String weather;


    public Frontend(String track_name, String artist, String uri) {
        this.track_name = track_name;
        this.artist = artist;
        this.uri = uri;
        this.weather=weather;
    }



    public String getTrack_name() {
        return track_name;
    }

    public void setTrack_name(String track_name) {
        this.track_name = track_name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
