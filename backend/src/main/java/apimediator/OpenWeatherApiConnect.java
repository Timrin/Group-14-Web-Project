package apimediator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * Class which connects to the Openweather API to receive a precise weather update by using Latitude and Longitude
 * <p>
 *
 * @author
 */
public class OpenWeatherApiConnect {
    private static String API_KEY = ""; //Ta bort innan du pushar upp!!!!!!!!!!!!

    /**
     * Method for getting the weather of a specific location
     *
     * @param lat Latitude position of the location
     * @param lon Longitude position of the location
     * @return Weather from the location as plain text, ex. Rain or Clouds
     */
    public static String getWeatherCondition(double lat, double lon) {
        String ret = "";

        String response = fetchWeatherFromOpenWeather(lat, lon);

        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(response).getAsJsonObject();

        String weatherId = (((JsonObject) obj.getAsJsonArray("list")
                .get(0)).getAsJsonArray("weather")
                .get(0).getAsJsonObject()
                .get("id")).toString();

        ret = getWeatherFromId(weatherId);

        return ret;
    }

    /**
     * Method for querying the OpenWeather API for the weather of a specific location
     *
     * @param lat Latitude parameter value that will be sent with the query
     * @param lon Longitude parameter value that will be sent with the query
     * @return Response from the server, as a JSON formatted String
     */
    private static String fetchWeatherFromOpenWeather(double lat, double lon) {
        String ret = " ";

        HttpClient client = HttpClientBuilder.create().build();
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&cnt=1&appid=" + API_KEY;
        HttpGet get = new HttpGet(url);

        try {
            HttpResponse httpResponse = client.execute(get);

            ret = EntityUtils.toString(httpResponse.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Overloaded getWeatherFromId
     * This method takes the id as a String instead of an int
     *
     * @param id OpenWeatherAPI weather id as an String, see @link{https://openweathermap.org/weather-conditions}
     * @return Weather as plain text, ex. Rain or Clouds
     */
    private static String getWeatherFromId(String id) {
        return getWeatherFromId(Integer.parseInt(id));
    }

    /**
     * Helper method for converting an OpenWeatherAPI weather condition id to plain text weather
     *
     * @param id OpenWeatherAPI weather id as an int, see @link{https://openweathermap.org/weather-conditions}
     * @return Weather as plain text, ex. Rain or Clouds
     */
    private static String getWeatherFromId(int id) {
        int modId = id / 100;
        String ret = "Sunny"; //Clear weather is default

        switch (id) {
            case 800:
                ret = "Sunny";
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
                break;
            case 611:
            case 612:
            case 613:
                ret = "Hail";
                break;
            default:
                switch (modId) {
                    case 2:
                        ret = "Thunder";
                        break;
                    case 3:
                        //ret = "Drizzle"; //Drizzle is rain, so let the case fall through
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


}
