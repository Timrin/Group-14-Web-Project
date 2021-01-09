package connectSpotify;

import apimediator.OpenWeatherApiConnect;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchTrack {

    private HttpResponse httpResponse = null;
    private HttpClient httpclient = null;
    private StatusLine status = null;
    private HttpEntity httpEntity = null;
    private InputStream dataStream = null;
    private Reader reader = null;
    private Gson gson = null;
    private List<Item> items = null;
    private Track tracks = null;
    private String weatherType;

    private static JsonArray jsonArray;
    private static Map<String, JsonArray> jsonMap;
    private static JsonObject obj;

   /* public SearchTrack(String weatherType) {
        this.weatherType=weatherType;
    }*/

    //Create Get request using accesscode, get data and use Reader object to later parse into Track object
    public static JsonArray connectToSpotify(String ACCESS_CODE, String weather) { // fjernet weather

        String url = "https://api.spotify.com/v1/search";
        try {
            HttpClient httpclient = HttpClients.custom().build();
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(url)
                    .addHeader(HttpHeaders.AUTHORIZATION, ACCESS_CODE)
                    .addParameter("q", "track:" + weather)      //FIXME: value ska vara weatherType
                    .addParameter("type", "track")
                    .addParameter("limit", "10")
                    .build();
            HttpResponse httpResponse = httpclient.execute(request);
            StatusLine status = httpResponse.getStatusLine();
//
            //statusCode(status, httpResponse);
            if (status.getStatusCode() == 200) {
                System.out.println(status);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream dataStream = httpEntity.getContent();

                Reader reader = new InputStreamReader(dataStream);
                return jsonToJava(reader);
            }else if (status.getStatusCode()!= 200){

            }
//
        } catch (IOException | JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // checks if status code is OK
 /*   public static void statusCode(StatusLine status, HttpResponse httpResponse) throws IOException {
        if (status.getStatusCode() == 200) {
            System.out.println(status);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream dataStream = httpEntity.getContent();

            Reader reader = new InputStreamReader(dataStream);
            jsonToJava(reader);
        }

    }*/

    //Parse reader to Track object using Gson
    public static JsonArray jsonToJava(Reader reader) {

        Gson gson = new Gson();
        Envelope envelope = gson.fromJson(reader, Envelope.class);
        Track tracks = envelope.getTracks();
        List<Item> items = tracks.getItems();

        return pickRelevantInfo(items);

    }

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

        obj = new JsonObject();
        obj.add("track",arr);

        return arr;
    }


    public static JsonArray getJsonArray() {
        return jsonArray;
    }

    public static void setJsonArray(JsonArray jsonArray) {
        SearchTrack.jsonArray = jsonArray;
        //System.out.println("Set JsonArray\n"+jsonArray);
    }

    public static JsonObject javaToJson(Frontend item) {
        Gson gson = new Gson();

        JsonElement track = gson.toJsonTree(item.getTrack_name());
        JsonElement artist = gson.toJsonTree(item.getArtist());
        JsonElement uri = gson.toJsonTree(item.getUri());

        JsonArray array = new JsonArray();

        JsonObject mus = new JsonObject();
        mus.add("track_name", track);
        mus.add("artist", artist);
        mus.add("uri", uri);

       // array.add(mus);

        //JsonElement tree3 = gson.toJsonTree(weather); // fixme be gone. et annet sted.

//todo skal gjøres i get i apiclassen
      /*  obj = new JsonObject();
        //obj.add("weather", tree3); //fixme flytt
        obj.add("track", array);
        setObj(obj);
        //System.out.println(obj)*/
        return mus;
    }

    public static JsonObject getObj() {
        return obj;
    }

    public static void setObj(JsonObject obj) {
        SearchTrack.obj = obj;
        System.out.println(obj);
    }
}
