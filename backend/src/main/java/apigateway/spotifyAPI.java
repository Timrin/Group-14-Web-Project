package apigateway;

import connectSpotify.ClientCredidentials;
import connectSpotify.SearchTrack;

import static spark.Spark.*;

public class spotifyAPI {
    private static ClientCredidentials clientCredidentials; //todo Skal ikke en utvikler sende sitt eget id + secret?
    private static SearchTrack searchTrack;

    public spotifyAPI() {
        clientCredidentials = new ClientCredidentials();
        searchTrack = new SearchTrack();

    }

    public static void initAPI() {

        get("/spotify/track",((request, response) ->{

            return "uri";
        } ));
    }

}
