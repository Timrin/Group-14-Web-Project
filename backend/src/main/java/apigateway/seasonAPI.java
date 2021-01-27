package apigateway;

import apimediator.GoogleCalendarApi;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import connectSpotify.ClientCredidentials;
import connectSpotify.SearchTrack;
import connectSpotify.SeasonTrack;
import model.Season;

import java.util.Date;
import java.util.List;

import static spark.Spark.*;


public class seasonAPI {

	//Not currently implemented
	//This array contains all of the supported holidays that we base playlists on
	/*private static String[] supportedHolidays = {
			"Alla hjärtans dag",
			"Påskdagen",
			"Mors dag",
			"Sveriges nationaldag",
			"Alla helgons dag",
			"Fars dag",
			"Nyårsdagen"
	};*/

	/**
	 * Method for initializing all of the season endpoints
	 */
	public static void initAPI() {
		get("/api/v1/season", ((req, res) -> {
			res.header("Content-Type", "application/json");

			JsonObject seasonObject = buildSeasonObject();

			String season = seasonObject.get("season_string").getAsString();
			String holiday = seasonObject.get("active_holiday") == null ? null : seasonObject.get("active_holiday").getAsString();
			JsonElement tracks = buildSeasonTrackArray(holiday, season);

			JsonObject responseObj = new JsonObject();

			responseObj.add("season", seasonObject);
			responseObj.add("tracks", tracks);

			return responseObj;
		}));
	}

	private static JsonElement buildSeasonTrackArray(String holiday, String season) {
		String access_token = ClientCredidentials.connectToSpotify();
		JsonElement tracks;

		if (holiday != null) {
			tracks = SearchTrack.getTrackFromSpotify(access_token, holiday, 10, 0);
		} else if (season.equals("Christmas")) {
			tracks = SearchTrack.getTrackFromSpotify(access_token, season);
		} else {
			tracks = SeasonTrack.spotifySeason();
		}

		return tracks;
	}

	private static JsonObject buildSeasonObject() {
		JsonObject ret = new JsonObject();
		Date currentDate = new Date();
		String season;
		String holiday;


		int currentMonth = Integer.parseInt(String.format("%tm", currentDate));

		//Season
		if (currentMonth == 12) {
			season = "Christmas";
		} else {
			//Calculate what season it is
			season = Season.values()[(currentMonth % 12) / 3].toString();

		}

		//Holiday
		//This is a very basic implementation of holiday support
		List<String> activeHolidays = GoogleCalendarApi.getCurrentHolidays();
		if (activeHolidays.size() > 0) {
			holiday = activeHolidays.get(0);

			//Since there is not always an active holiday, only add the JSON field when there is one
			ret.addProperty("active_holiday", holiday);
		}

		ret.addProperty("season_string", season);

		return ret;
	}


}
