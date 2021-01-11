package connectSpotify;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Calendar;


public class SeasonTrack {

    /**
     * Get current month as int
     *
     * @return month
     */
    public static int getDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Get month to set current season
     *
     * @return season as String
     */
    public static String seasonSet() {
        int month = getDate();
        String season = "";
        if (month >= 3 && month < 6) {
            season = "Spring";
        } else if (month >= 6 && month < 9) {
            season = "Summer";
        } else if (month >= 9 && month < 11) {
            season = "Autumn";
        } else if (month == 12) {
            season = "Christmas";
        } else
            season = "Winter";

        return season;

    }

    /**
     * based on current season, method select correct uri
     *
     * @return uri as a String
     */
    public static String selectUri() {
        String season = seasonSet();
        String uri = "";
        switch (season) {
            case "Winter":
                uri = "spotify:track:1jWQ4n73jdT7y9gR2VHu0g";
                break;
            case "Autumn":
                uri = "spotify:track:65WvqwvSRc7z1627c5f5nC";
                break;
            case "Summer":
                uri = "spotify:track:6j8B9usR1XCCghJw2gspr8";
                break;
            case "Spring":
                uri = "spotify:track:5xbuJuQsTVheVZvX2AJVIv";
                break;
            default:
                uri = "computer says noo";
                break;
        }
        return uri;

    }

    /**
     * get current season and uri and create JsonObject
     *
     * @return JsonObject
     */
    public static JsonObject spotifySeason() {
        String season = seasonSet();
        String uri = selectUri();
        String artist = "Vivaldi, Recomposed by Max Richter";

        Gson gson = new Gson();
        JsonElement jsonTrack = gson.toJsonTree(season);
        JsonElement jsonArtist = gson.toJsonTree(artist);
        JsonElement jsonUri = gson.toJsonTree(uri);

        JsonObject obj = new JsonObject();
        obj.add("track", jsonTrack);
        obj.add("artist", jsonArtist);
        obj.add("uri", jsonUri);

        JsonArray trackArray = new JsonArray();

        trackArray.add(obj);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("season",jsonTrack);
        jsonObject.add("tracks",trackArray);
        return jsonObject;


    }


}
