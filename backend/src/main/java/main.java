import apigateway.weatherAPI;

import static spark.Spark.port;

public class main {

	public static void main(String[] args) {
		System.out.println("Starting server");

		port(5000);

		weatherAPI.initAPI();

		System.out.println("Server Running");

	}

}
