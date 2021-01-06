package apimediator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import connectSpotify.ClientCredidentials;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * Class which connects to the Openweather API to receive a precise weather update by using Latitude and Longitude
 * <p>
 * @author
 */

public class OpenWeatherApiConnect {
    private static String API_KEY = ""; //Ta bort innan du pushar upp!!!!!!!!!!!!

    /**
     * Method which fetches an Json object from openWeather api
     * <p>
     *
     * @return Weather Condition and alot of other information
     */

    public static String getWeatherCondition(double lat, double lon) {
        String ret = " ";
        CloseableHttpClient client = HttpClientBuilder.create().build();
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&cnt=1&appid=" + API_KEY;
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse httpResponse = client.execute(get);
            String result = EntityUtils.toString(httpResponse.getEntity());
            System.out.println(result);
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(result).getAsJsonObject();
            System.out.println(obj.get("cod").getAsString());

            String weatherId = (((JsonObject) obj.getAsJsonArray("list")
                    .get(0)).getAsJsonArray("weather")
                    .get(0).getAsJsonObject()
                    .get("id")).toString();
            ret = getWeatherFromId(weatherId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String getWeatherFromId(String id) {
        return getWeatherFromId(Integer.parseInt(id));
    }

    private static String getWeatherFromId(int id) {
        int modId = id / 100;
        String ret = "Clear";

        switch (id) {
            case 800:
                ret = "Clear";
                break;
            case 701:
            case 721:
            case 741:
                ret = "Fog";
                break;
            case 762:
                ret = "Ash";
                break;
            case 781:
                ret = "Tornado";
            default:
                switch (modId) {
                    case 2:
                        ret = "Thunderstorm";
                        break;
                    case 3:
                        ret = "Drizzle";
                        break;
                    case 5:
                        ret = "Rain";
                        break;
                    case 6:
                        ret = "Snow";
                        break;
                    case 8:
                        ret = "Clouds";
                        break;
                }
                break;
        }
        return ret;
    }

    public static void main(String[] args) {
        System.out.println("Hello World");
        String res = OpenWeatherApiConnect.getWeatherCondition(55.6059,13.0007); // Coordinates for Malmo
        System.out.println(res);

        // test av å hente weathertype til spotify. funker nice.
        ClientCredidentials clientCredidentials = new ClientCredidentials(res);
        clientCredidentials.connect(res);



    }

}
