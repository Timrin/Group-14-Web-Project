import apigateway.weatherAPI;
import connectSpotify.SearchTrack;

import static spark.Spark.port;

public class main {

	public static void main(String[] args) {
		System.out.println("Starting server");

		port(5000);

		weatherAPI.initAPI();
		SearchTrack searchTrack = new SearchTrack();
		//searchTrack.connectToSpotify();

		System.out.println("Server Running");

	}

}
