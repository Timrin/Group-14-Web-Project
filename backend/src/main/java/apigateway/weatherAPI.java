package apigateway;

import apimediator.OpenWeatherApiConnect;
import com.google.gson.JsonObject;
import connectSpotify.ClientCredidentials;
import connectSpotify.SearchTrack;

import static spark.Spark.*;


public class weatherAPI {

	/**
	 * Method for initializing all the weather endpoints
	 */
	public static void initAPI() {

		get("/weather/bylocation", ((request, response) -> {
			String longitude = request.queryParams("lng");
			String latitude = request.queryParams("lat");

			Double longi = Double.parseDouble(longitude);
			Double lati = Double.parseDouble(latitude);

			String weather = OpenWeatherApiConnect.getWeatherCondition(longi,lati);
			System.out.println(weather);
			String access_token = ClientCredidentials.connect();
			System.out.println(access_token);
			String spotifyarray = SearchTrack.connectToSpotify(access_token, weather).toString();
			System.out.println(spotifyarray);

			return spotifyarray ;
		}));

		get("/weather/thunderstorm", (req, res) -> {
			return "Thunderstorm";
		});

		get("/weather/drizzle", (req, res) -> {


			ClientCredidentials.connect();
			return "Drizzle";
		});

		get("/weather/rain", (req, res) -> {
			return "Rain";
		});

		get("/weather/snow", (req, res) -> {
			return "Snow";
		});

		get("/weather/fog", (req, res) -> {
			return "Fog";
		});

		get("/weather/clear", (req, res) -> {
			return "Clear";
		});

		get("/weather/clouds", (req, res) -> {
			return "Clouds";
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
