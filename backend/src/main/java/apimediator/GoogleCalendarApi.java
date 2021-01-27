package apimediator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoogleCalendarApi {
	private static String API_KEY = "";
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");


	/**
	 * Method for fetching the holiday calendar from the Google Calendar API.
	 *
	 * API reference for the endpoint used here.
	 * https://developers.google.com/calendar/v3/reference/events/list
	 *
	 * @param timeMin Lower bound (exclusive) for an event's start time to filter by.
	 * @param timeMax Upper bound (exclusive) for an event's start time to filter by.
	 * @return Response from the server, formatted as JSON
	 */
	private static String fetchHolidaysFromGoogle(Date timeMin, Date timeMax) {
		String ret = "";

		HttpClient client = HttpClientBuilder.create().build();
		String url = "https://www.googleapis.com/calendar/v3/calendars/"
				+ "sv.swedish%23holiday%40group.v.calendar.google.com/events"
				+ "?key=" + API_KEY
				+ "&timeMin=" + dateFormatter.format(timeMin.toInstant().atOffset(ZoneOffset.UTC))
				+ "&timeMax=" + dateFormatter.format(timeMax.toInstant().atOffset(ZoneOffset.UTC));

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
	 * Method for getting all the active holidays.
	 *
	 * @return A List containing any active holidays. If there are no active holidays the list is empty
	 */
	public static List<String> getCurrentHolidays() {
		List<String> ret = new ArrayList<>();

		long timeSpanMilli = 86400000L * 2; //Current span set to 2 days
		Date timeMinParam = new Date(System.currentTimeMillis() - (timeSpanMilli / 2) );
		Date timeMaxParam = new Date(System.currentTimeMillis() + (timeSpanMilli / 2) );

		//String response = fetchHolidaysFromGoogle(new Date(System.currentTimeMillis()-4000000000L), new Date(System.currentTimeMillis()+1000L));
		String response = fetchHolidaysFromGoogle(timeMinParam, timeMaxParam);

		JsonParser responseParser = new JsonParser();
		JsonObject jsonResponse = responseParser.parse(response).getAsJsonObject();

		for (JsonElement item: jsonResponse.getAsJsonArray("items")) {
			ret.add(item.getAsJsonObject().get("summary").getAsString());
		}

		return ret;
	}

}
