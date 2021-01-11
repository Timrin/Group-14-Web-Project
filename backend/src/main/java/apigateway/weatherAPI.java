package apigateway;

import apimediator.OpenWeatherApiConnect;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import connectSpotify.ClientCredidentials;
import connectSpotify.SearchTrack;
import java.util.Arrays;

import static spark.Spark.*;


public class weatherAPI {

	//This array contains all of the available weather resources
	private static String[] weatherEndpoints = {
			"sunny",
			"fog",
			"ash",
			"tornado",
			"hail",
			"thunder",
			"rain",
			"snow",
			"clouds"
	};

	/**
	 * Method for initializing all the weather endpoints
	 */
	public static void initAPI() {

		get("/api/v1/weather/bylocation", ((req, res) -> {
			String longitude = req.queryParams("lng");
			String latitude = req.queryParams("lat");

			double longi = Double.parseDouble(longitude);
			double lati = Double.parseDouble(latitude);

			String weather = OpenWeatherApiConnect.getWeatherCondition(lati,longi);

			String access_token = ClientCredidentials.connectToSpotify();
			JsonArray spotifyarray = SearchTrack.getTrackFromSpotify(access_token, weather);

			Gson gson = new Gson();
			JsonElement weatherJson = gson.toJsonTree(weather);
			JsonObject obj = new JsonObject();

			obj.add("weather", weatherJson);
			obj.add("tracks",spotifyarray);

			res.header("Content-Type", "application/json");

			return obj ;
		}));


		get("/api/v1/weather/:weather", (req, res) -> {

			String weather = req.params("weather");

			if(!Arrays.asList(weatherEndpoints).contains(weather)) {
				//Not a valid weather endpoint
				return null;
			}

			String access_token = ClientCredidentials.connectToSpotify();
			JsonArray spotifyarray = SearchTrack.getTrackFromSpotify(access_token, req.params("weather"));


			JsonObject obj = new JsonObject();
			obj.add("tracks",spotifyarray);

			res.header("Content-Type", "application/json");

			return obj;
		});

		//CORS boilerplate
		//This enables CORS by including the correct headers,
		//also handles the preflight sent by some browsers
		options("/*",
				(request, response) -> {

					String accessControlRequestHeaders = request
							.headers("Access-Control-Request-Headers");
					if (accessControlRequestHeaders != null) {
						response.header("Access-Control-Allow-Headers",
								accessControlRequestHeaders);
					}

					String accessControlRequestMethod = request
							.headers("Access-Control-Request-Method");
					if (accessControlRequestMethod != null) {
						response.header("Access-Control-Allow-Methods",
								accessControlRequestMethod);
					}

					return "OK";
				});

		before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
	}

}
