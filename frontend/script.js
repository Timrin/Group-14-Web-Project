let map; //google maps js api

const serverIP = 'http://localhost:5000/api/v1';

var player; //spotify web sdk player object
var play; //spotify play function

window.onload = function () {

    authSpotifyUser();

}

function authSpotifyUser() {
    if (getCookie("access_token") != "") {
        //There is an active access_token in the cookie
        //Nothing needs to be done

    } else if (window.location.href.search("access_token") != -1) {
        //User just logged in, get the access_token from the URL

        var tokenStart = window.location.href.search("access_token=") + 13; //Add 13 to not read 'access_token='
        var tokenEnd = window.location.href.search("&");
        var urlToken = window.location.href.substr(tokenStart, (tokenEnd - tokenStart));

        //Cookie should be valid for 1 hour
        setCookie("access_token", urlToken, 1);

        window.location.replace("http://localhost:8000") //Redirect the user back to root

    } else {
        //No active access_token in the cookie, user needs to login
        window.location.replace("https://accounts.spotify.com/authorize?client_id=9a46ce3d76b642eb8d94bfa7863aafbe&redirect_uri=http:%2F%2Flocalhost:8000&scope=user-modify-playback-state%20streaming&response_type=token&state=123")
        //callback url: http://localhost:8000
        //token scopes: user-modify-playback-state, streaming
    }
}

/**
 * Method required by the google maps js api
 */
function initMap() {
    var lat = 55.49129065713373;
    var lng = 8.43496739449165;

    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: lat, lng: lng },
        zoom: 3,
    });
}

/**
 * This function will read the coordinates at the center of 
 * the map and start the weather playlist with that location.
 */
function selectMapLocation() {
    var lat = map.getCenter().lat();
    var lng = map.getCenter().lng();

    startWeatherPlaylistGeo(lat, lng);
}

/**
 * This function will attempt to load the users geolocation
 * and start the weather playlist with that location. The
 * map will also center on the location.
 */
function loadUserGeolocation() {
    navigator.geolocation.getCurrentPosition(position => {
        //Success
        var lat = position.coords.latitude;
        var lng = position.coords.longitude;

        startWeatherPlaylistGeo(lat, lng);

        map.setCenter({ lat: lat, lng: lng });
        map.setZoom(10)

    }, error => {
        //Error
        console.log(error);
    });
}

/**
 * Method for querying our API for a location based weather playlist
 * 
 * @param {String} lat Latitude parameter value that will be sent with the query
 * @param {String} lng Longitude parameter value that will be sent with the query
 * @returns {String} Response body data from the api query
 */
async function fetchWeatherPlaylistGeo(lat, lng) {
    var fetchUrl = serverIP + "/weather/bylocation?lat=" + lat + "&lng=" + lng;
    var res = "";

    res = fetch(fetchUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((response) => response.text()
    ).then((data) => {
        console.log(data);
        return JSON.parse(data);
    });

    return res;
}

/**
 * Method for querying our API for a specific weather playlist
 * 
 * @param {String} weather Weather parameter value that will be sent with the query. The weather the playlist will be based upon
 * @returns {String} Response body data from the api query
 */
function fetchWeatherPlaylistWeather(weather) {
    var fetchUrl = serverIP + "/weather/" + weather;
    var res = "";

    res = fetch(fetchUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((response) => response.text()
    ).then((data) => {
        console.log(data)
        return JSON.parse(data);
    });

    return res;
}

function startWeatherPlaylistGeo(lat, lng) {
    //Fetch playlist from the server
    fetchWeatherPlaylistGeo(lat, lng).then(res => {

        var trackUriArray = [];
        res.tracks.forEach(track => {
            trackUriArray.push(track.uri);
        });

        play({
            playerInstance: player,
            spotify_uri: trackUriArray
        });

        setPlayingFromHeader("Weather Playlist");

    }).catch(error => {
        console.log("Error in startWeatherPlaylistGeo: " + error);
    });

}

function startWeatherPlaylistWeather(weather) {
    //Fetch playlist from the server
    fetchWeatherPlaylistWeather(weather).then(res => {

        var trackUriArray = [];
        res.tracks.forEach(track => {
            trackUriArray.push(track.uri);
        });

        play({
            playerInstance: player,
            spotify_uri: trackUriArray
        });

        setPlayingFromHeader("Weather Playlist");

    }).catch(error => {
        console.log("Error in startWeatherPlaylistWeather: " + error);
    });

}

function setSongTitleArtistHeader(title, artist) {
    console.log("setting title artist")
    document.getElementById('currentplaying').innerHTML = `${title} - ${artist}`;
}

function setPlayingFromHeader(playlist) {
    document.getElementById('currentplaylist').innerHTML = `${playlist}`;
}

function setLocationWeatherHeader(city, country, weather) {
    document.getElementById('locationWeatherHeader').innerHTML = `${city}, ${country}<br>Weather: ${weather}`;
}

function setTogglePlayIcon(isPaused) {
    if(isPaused) {
        //Use the pause icon
        document.getElementById('TogglePlay').getElementsByClassName("mediaControlIcon")[0].src = "play_arrow-white-18dp.svg"
    } else {
        //Use the play arrow icon
        document.getElementById('TogglePlay').getElementsByClassName("mediaControlIcon")[0].src = "pause-white-18dp.svg"
    }
}

/*
HELPER FUNCTIONS
*/

/**
 * Method for reading a cookie value from the cookie jar
 * Function from https://www.w3schools.com/js/js_cookies.asp
 * 
 * @param {String} cname The name of the cookie
 */
function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

/**
 * Method for storing a cookie in the cookie jar
 * Function from https://www.w3schools.com/js/js_cookies.asp
 * 
 * @param {String} cname The name of the cookie
 * @param {any} cvalue The value to be stored
 * @param {number} exHours Time to expiration, in hours
 */
function setCookie(cname, cvalue, exHours) {
    var d = new Date();
    d.setTime(d.getTime() + (exHours * 60 * 60 * 1000)); //exHours * 60 * 60 * 1000
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

/*
SPOTIFY
*/
window.onSpotifyWebPlaybackSDKReady = () => {
    token = getCookie("access_token");
    player = new Spotify.Player({
        name: 'Weather Playlist Web App',
        getOAuthToken: cb => { cb(token); }
        //volume?
    });

    // Error handling
    player.addListener('authentication_error', ({ message }) => { console.error(message); }); 
    player.addListener('initialization_error', ({ message }) => { console.error(message); });
    player.addListener('account_error', ({ message }) => { console.error(message); });
    player.addListener('playback_error', ({ message }) => { console.error(message); });

    // Playback status updates
    player.addListener('player_state_changed', state => {
        console.log(state);
        setSongTitleArtistHeader(state.track_window.current_track.name, state.track_window.current_track.artists[0].name);
        setTogglePlayIcon(state.paused);
    });

    // Ready
    player.addListener('ready', ({ device_id }) => {
        console.log('Ready with Device ID', device_id);
    });

    // Not Ready
    player.addListener('not_ready', ({ device_id }) => {
        console.log('Device ID has gone offline', device_id);
    });

    // Connect to the player!
    player.connect();

    //Set the play function
    play = ({
        spotify_uri,
        playerInstance: {
            _options: {
                getOAuthToken,
                id
            }
        }
    }) => {
        getOAuthToken(access_token => {
            fetch(`https://api.spotify.com/v1/me/player/play?device_id=${id}`, {
                method: 'PUT',
                body: JSON.stringify({ uris: spotify_uri }),
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${access_token}`
                },
            }).catch(e => {
                console.log(e);
            })
        });
    };

    /*
    Assign functions to the media buttons/playback controls
    */

    //Play/pause button
    document.getElementById('TogglePlay').onclick = function () {
        player.togglePlay().then(() => {
            console.log('Toggled playback!');
        });
    }

    //Skip/next button
    document.getElementById('SkipSong').onclick = function () {
        player.nextTrack().then(() => {
            console.log('Skipped to next track!');
        });
    }

};