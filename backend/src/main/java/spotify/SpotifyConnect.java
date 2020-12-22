package spotify;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SpotifyConnect {
private static final String CLIENT_ID = "client_id";
private static final String REDIRECT_URI = "http://localhost:8888/callback";

    public static void main(String[] args) {
       try {
           String url_authorize ="https://accounts.spotify.com/authorize?"+
                   "client_id="+CLIENT_ID+"&response_type=code&redirect_uri="+
                   REDIRECT_URI+"&scope=user-read-private%20user-email";
           URL url = new URL(url_authorize);
           URLConnection urlCon = url.openConnection();
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           if(connection.getResponseCode() != 200){
               throw new RuntimeException("computer says noo"+ connection.getResponseCode());
           }
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
           String output;
           System.out.println("output from server......\n");
           while ((output = bufferedReader.readLine()) !=null){
               System.out.println(output);
           }
           connection.disconnect();

       }catch (IOException exception){
           exception.printStackTrace();
       }



    }
}
