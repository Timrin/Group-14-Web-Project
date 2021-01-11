package apigateway;

import com.google.gson.JsonObject;
import connectSpotify.SeasonTrack;

import static spark.Spark.*;


public class seasonAPI {

	/**
	 * Method for initializing all of the season endpoints
	 */
	public static void initAPI() {
		get("/api/v1/season", ((req, res) -> {
			res.header("Content-Type", "application/json");

			JsonObject seasonTrackInfo = new JsonObject();
			seasonTrackInfo = SeasonTrack.spotifySeason();

			return seasonTrackInfo;
		}));
	}

}
