# sound-pet-webapp

A small web application, with the ability to login / register. 
The application itself is a room in which there are 2 types of user - a streamer (the creator of the room) and a listener (anyone who entered the room using its ID and password). 
When creating a room, streamer initially initializes it with music content using either a link to a spotify album / playlist, or to any video / playlist from YouTube (it will be converted to audio format). 
The algorithm, in the first case, first sends a Get request to the spotify api, and receives a response with the names of the songs and their authors in JSON format and converts them to LinkedHashMap. 
Then, in both cases, using the console utility, the video is loaded and converted to the server file system. 
In the room, with the help of the REST api, streaming of music from the resource to the player is implemented. 
Every 5 seconds, the streamer client sends a post request with data about the player to the server using Rest API. 
Every 5 seconds, the listener client sends a get request to the server with player data. 
Thus, the idea of ​​a pear to pear connection (web sockets for the weak) is extremely stupidly implemented.
