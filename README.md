## **Description**


## **Install and Setup**
Clone or download the project from github

### **Setup backend**
**Add the api keys**  
For OpenWeatherMap to work an api key has to be added in the `OpenWeatherAPIConnect` class, found here: `backend>src>main>java>apimediator>OpenWeatherAPIConnect.java`. The API key should be pasted into the static String variable `API_KEY`.
For spotify a client id and client secret has to be added in the `ClientCredidentials`class, found here: `backend>src>main>java>connectSpotify>ClientCredidentials.java`. The client id should be pasted into the static String variable `CLIENT_ID`, and the client secret pasted into the static String variable `CLIENT_SECRET`.  
**Build the backend server**  
The backend folder contains a maven project. After the api key and client credentials have been added build the maven project. 

### **Setup frontend**
No setup neccesary.   


## **Run the project**

### **Run backend**
The pom.xml for the backend project has not been setup to generate an executable JAR. However the maven project can be run with the help of an IDE or through the commandline.


### **Run frontend**
The frontend site has to be hosted at localhost:8000. This can be done by running a simple local HTTP server. Follow [this guide.](https://developer.mozilla.org/en-US/docs/Learn/Common_questions/set_up_a_local_testing_server#running_a_simple_local_http_server "Mozilla simple HTTP server guide")

