package apigateway;

import static spark.Spark.*;


public class weatherAPI {

	/**
	 * Method for initializing all the weather endpoints
	 */
	public static void initAPI() {

		get("/weather/bylocation", ((request, response) -> {
			String longitude = request.queryParams("lng");
			String latitude = request.queryParams("lat");

			return "location: " + longitude + ", " + latitude;
		}));

		get("/weather/thunderstorm", (req, res) -> {
			return "Thunderstorm";
		});

		get("/weather/drizzle", (req, res) -> {
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
