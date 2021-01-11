package connectSpotify;

import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import spotify.*;
import java.io.*;
import java.util.List;
import java.util.Random;

public class SearchTrack {

    /**
     * This method searches for a track form spotify, using the weather parameter.
     * The method uses a Get-request to the spotify api.
     * @param ACCESS_CODE from connectToSpotify
     * @param weather from OpenWeatherApiConnect
     * @return jsonarray
     * */
    public static JsonArray getTrackFromSpotify(String ACCESS_CODE, String weather) { // fjernet weather

        int offset = new Random().nextInt(201);
        String url = "https://api.spotify.com/v1/search";
        try {
            HttpClient httpclient = HttpClients.custom().build();
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(url)
                    .addHeader(HttpHeaders.AUTHORIZATION, ACCESS_CODE)
                    .addParameter("q", "track:" + weather)      //FIXME: value ska vara weatherType
                    .addParameter("type", "track")
                    .addParameter("limit", "10")
                    .addParameter("offset", String.valueOf(offset))
                    .build();
            HttpResponse httpResponse = httpclient.execute(request);
            StatusLine status = httpResponse.getStatusLine();


            if (status.getStatusCode() == 200) {
                System.out.println(status);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream dataStream = httpEntity.getContent();

                Reader reader = new InputStreamReader(dataStream);
                return jsonToJava(reader);

            } else if (status.getStatusCode() != 200) {
                System.out.println("not 200");
            }
        } catch (IOException | JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * This method uses marshalling to parse json objects to java objects.
     * @param reader getTrackFromSpotify gives this method a json Object
     * @return JsonArray pickRelevantInfo
     * */
    public static JsonArray jsonToJava(Reader reader) {

        Gson gson = new Gson();
        Envelope envelope = gson.fromJson(reader, Envelope.class);
        Track tracks = envelope.getTracks();
        List<Item> items = tracks.getItems();

        return pickRelevantInfo(items);

    }


    /**
     * This method picks out relevant information
     * @param items List of java objects
     * @return a jsonArray, that will be sent back to frontend
     * */
    public static JsonArray pickRelevantInfo(List<Item> items) {
        JsonArray arr = new JsonArray();
        String singer = null;

        for (Item item : items) {
            String uri = item.getUri();
            Artist[] artist = item.getArtists();
            for (Artist artists : artist) {
                singer = artists.getName();
            }
            String track_name = item.getName();
            Frontend frontend = new Frontend(track_name, singer, uri);

            arr.add(javaToJson(frontend));
        }
        JsonObject obj;
        obj = new JsonObject();
        obj.add("track", arr);

        return arr;
    }

/**
 * This method parses java objects of track info to Json objects.
 *
 * @param item given by pickRelevantInfo(). Objects to parse.
 * @return JsonObject will be sent to frontend via other methods.
 * */
    public static JsonObject javaToJson(Frontend item) {
        Gson gson = new Gson();

        JsonElement track = gson.toJsonTree(item.getTrack_name());
        JsonElement artist = gson.toJsonTree(item.getArtist());
        JsonElement uri = gson.toJsonTree(item.getUri());

        JsonObject mus = new JsonObject();
        mus.add("track_name", track);
        mus.add("artist", artist);
        mus.add("uri", uri);

        return mus;
    }
}
