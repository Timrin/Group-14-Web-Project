package apigateway;

import apimediator.OpenWeatherApiConnect;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import connectSpotify.ClientCredidentials;
import connectSpotify.SearchTrack;

import static spark.Spark.*;


public class weatherAPI {

	/**
	 * Method for initializing all the weather endpoints
	 */
	public static void initAPI() {

		get("/api/v1/weather/bylocation", ((req, res) -> {
			res.header("Content-Type", "application/json");
			String longitude = req.queryParams("lng");
			String latitude = req.queryParams("lat");

			Double longi = Double.parseDouble(longitude);
			Double lati = Double.parseDouble(latitude);

			String weather = OpenWeatherApiConnect.getWeatherCondition(longi,lati);
			String access_token = ClientCredidentials.connectToSpotify();
			JsonArray spotifyarray = SearchTrack.getTrackFromSpotify(access_token, weather);
			//todo skal det være her?
			Gson gson = new Gson();
			JsonElement weatherJson = gson.toJsonTree(weather);
			JsonObject obj = new JsonObject();

			obj.add("weather", weatherJson);
			obj.add("tracks",spotifyarray);

			System.out.println(spotifyarray);
			return obj ;
		}));

		get("/api/v1/weather/thunderstorm", (req, res) -> {
			res.header("Content-Type", "application/json");

			String access_token = ClientCredidentials.connectToSpotify();
			JsonArray spotifyarray = SearchTrack.getTrackFromSpotify(access_token, "thunderstorm");


			JsonObject obj = new JsonObject();
			obj.add("tracks",spotifyarray);

			return obj;
		});

		get("/api/v1/weather/drizzle", (req, res) -> {
			res.header("Content-Type", "application/json");

			String access_token = ClientCredidentials.connectToSpotify();
			JsonArray spotifyarray = SearchTrack.getTrackFromSpotify(access_token, "drizzle");

			JsonObject obj = new JsonObject();
			obj.add("tracks",spotifyarray);

			return obj;
		});

		get("/api/v1/weather/rain", (req, res) -> {
			res.header("Content-Type", "application/json");

			String access_token = ClientCredidentials.connectToSpotify();
			JsonArray spotifyarray = SearchTrack.getTrackFromSpotify(access_token, "rain");

			JsonObject obj = new JsonObject();
			obj.add("tracks",spotifyarray);

			return obj;
		});

		get("/api/v1/weather/snow", (req, res) -> {
			res.header("Content-Type", "application/json");

			String access_token = ClientCredidentials.connectToSpotify();
			JsonArray spotifyarray = SearchTrack.getTrackFromSpotify(access_token, "snow");

			JsonObject obj = new JsonObject();
			obj.add("tracks",spotifyarray);

			return obj;
		});

		get("/api/v1/weather/fog", (req, res) -> {
			res.header("Content-Type", "application/json");

			String access_token = ClientCredidentials.connectToSpotify();
			JsonArray spotifyarray = SearchTrack.getTrackFromSpotify(access_token, "fog");

			JsonObject obj = new JsonObject();
			obj.add("tracks",spotifyarray);

			return obj;
		});

		get("/api/v1/weather/clear", (req, res) -> {
			res.header("Content-Type", "application/json");
			String access_token = ClientCredidentials.connectToSpotify();
			JsonArray spotifyarray = SearchTrack.getTrackFromSpotify(access_token, "clear");

			JsonObject obj = new JsonObject();
			obj.add("tracks",spotifyarray);

			return obj;
		});

		get("/api/v1/weather/clouds", (req, res) -> {
			res.header("Content-Type", "application/json");

			String access_token = ClientCredidentials.connectToSpotify();
			JsonArray spotifyarray = SearchTrack.getTrackFromSpotify(access_token, "clouds");

			JsonObject obj = new JsonObject();
			obj.add("tracks",spotifyarray);

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
