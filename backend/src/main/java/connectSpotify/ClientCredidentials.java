package connectSpotify;

import apimediator.OpenWeatherApiConnect;
import apimediator.WeatherCondition;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import spotify.AccessToken;

import java.io.*;
import java.util.Base64;


public class ClientCredidentials {
    private String CLIENT_ID = "69905c2752464578b578f03aa8149dd2";
    private String CLIENT_SECRET = "691d850b36574cbeb79b099c3eebd27c";
    /* private  String REDIRECT_URI = "http://localhost:8888/callback";
     private  String url = "https://accounts.spotify.com/authorize?" +
             "client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=" +
             REDIRECT_URI + "&scope=user-read-private%20user-email";
     */
    private String ID_SECRET = CLIENT_ID + ":" + CLIENT_SECRET;


    private HttpResponse response = null;
    private StatusLine status = null;
    private HttpEntity entity = null;
    private InputStream data = null;
    private Reader reader = null;
    private HttpClient httpclient = null;
    private Gson gson = null;
    private SearchTrack searchTrack;
    private static String weather;

    /*public ClientCredidentials(String weather) {
        this.weather = weather;
        searchTrack = new SearchTrack(weather);


    }*/

    /*
     * This mehtod connects to Spotify via the Client Credidentials authorization flow.
     */

    public static String getWeather() {
        return weather;
    }

    public static void setWeather(String weather) {
        ClientCredidentials.weather = weather;
    }

    public static String connect() {
        //setWeather(weather);
        String CLIENT_ID = "";
        String CLIENT_SECRET = "";
        String ID_SECRET = CLIENT_ID + ":" + CLIENT_SECRET;
        Reader reader = null;
        String encoded = Base64.getEncoder().encodeToString(ID_SECRET.getBytes());
        String ny = "Basic " + encoded;
        try {
            HttpClient httpclient = HttpClients.custom().build();

            HttpUriRequest req = RequestBuilder.post()
                    .setUri("https://accounts.spotify.com/api/token")
                    .setHeader(HttpHeaders.AUTHORIZATION, ny)
                    .setEntity(new StringEntity("grant_type=client_credentials", ContentType.APPLICATION_FORM_URLENCODED))
                    .build();
            HttpResponse response = httpclient.execute(req);
            StatusLine status = response.getStatusLine();

            HttpEntity entity = response.getEntity();
            InputStream data = entity.getContent();

            reader = new InputStreamReader(data);
            System.out.println(status + " ConnectSpotify");
            //setJsonAccess(reader);


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return setJsonAccess(reader);
    }

    /*
     * This method parses the accesstoken from the response JSON obj. And sends it to the searchTrack method
     * todo dette skal ikke gjøres herifra. Burde være et sted hvor vi kan samle det med været.
     * */
    public static String setJsonAccess(Reader reader) {
        // reader = new InputStreamReader(reader);
        Gson gson = new Gson();
        AccessToken token = gson.fromJson(reader, AccessToken.class);
        String access_token = "Bearer " + token.getAccess_token();
        //SearchTrack.connectToSpotify(access_token);
        // searchTrack.connectToSpotify(access_token);// fjernet weather
        return access_token;
    }

}