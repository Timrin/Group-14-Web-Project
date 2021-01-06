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
    private  String CLIENT_ID = "";
    private String CLIENT_SECRET ="";
    private  String REDIRECT_URI = "http://localhost:8888/callback";
    private  String url = "https://accounts.spotify.com/authorize?" +
            "client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=" +
            REDIRECT_URI + "&scope=user-read-private%20user-email";

    private String ID_SECRET = CLIENT_ID+":"+CLIENT_SECRET;


    private HttpResponse response = null;
    private StatusLine status = null;
    private HttpEntity entity = null;
    private InputStream data = null;
    private Reader reader = null;
    private HttpClient httpclient = null;
    private Gson gson = null;
    private WeatherCondition weatherCondition;
    private SearchTrack searchTrack;
private String weather;
    public ClientCredidentials(String weather) {
        this.weatherCondition = weatherCondition;
        searchTrack = new SearchTrack();
        this.weather = weather;

    }

/*
* This mehtod connects to Spotify via the Client Credidentials authorization flow.
*/
    public void connect(String weather) {
        String encoded = Base64.getEncoder().encodeToString(ID_SECRET.getBytes());
        String ny = "Basic " + encoded;
        try {
            httpclient = HttpClients.custom().build();
          //  HttpHeaders headers;

            HttpUriRequest req = RequestBuilder.post()
                    .setUri("https://accounts.spotify.com/api/token")
                    .setHeader(HttpHeaders.AUTHORIZATION, ny)
                    .setEntity(new StringEntity("grant_type=client_credentials", ContentType.APPLICATION_FORM_URLENCODED))
                    .build();
            response = httpclient.execute(req);
            status = response.getStatusLine();


            entity = response.getEntity();
            data = entity.getContent();

            reader = new InputStreamReader(data);
            setJsonAccess(reader,  weather );

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
* This method parses the accesstoken from the response JSON obj. And sends it to the searchTrack method
* todo dette skal ikke gjøres herifra. Burde være et sted hvor vi kan samle det med været.
* */
    public void setJsonAccess (Reader reader, String weather){

        gson = new Gson();
        AccessToken token = gson.fromJson(reader, AccessToken.class);
        String access_token = "Bearer "+token.getAccess_token();
        searchTrack.connectToSpotify(access_token, weather );


    }

    /*public static void main(String[] args) {
        OpenWeatherApiConnect.getWeatherCondition(55.6059,13.0007);
        WeatherCondition weatherCondition = new WeatherCondition();
        ClientCredidentials cc = new ClientCredidentials(weatherCondition);
        cc.connect();
       // System.out.println(weatherCondition.getMain());
    }*/


}