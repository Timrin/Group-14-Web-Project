package connectSpotify;

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

/**
 * This method connects to Spotify with the Client Credentials Authorization flow.
 * @return parsed accesstoken from the method setJsonAccess
 * */
    public static String connectToSpotify() {

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

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return setJsonAccess(reader);
    }

    /**
     * This method parses the accesstoken from the response JSON obj. And sends it to the searchTrack method
     * @param reader holds the accestoken
     * @return access_token as java String
     * */
    public static String setJsonAccess(Reader reader) {
        Gson gson = new Gson();
        AccessToken token = gson.fromJson(reader, AccessToken.class);
        String access_token = "Bearer " + token.getAccess_token();
        return access_token;
    }
}