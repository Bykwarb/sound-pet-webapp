const audioScreen = document.querySelector('#audio-screen');
const params = window.location.href.replace('http://localhost:8080/room/content/', '');
const audio = document.getElementById("audio-screen");

let trackAPI;
let timeAPI;
let conditionAPI;

let track;
let time = 0;
let condition;
fetch('http://localhost:8080/api/audio/getStreamingData/'+params)
    .then(result => result.json())
    .then(result => {
        trackAPI = result.track;
        timeAPI = result.time;
        conditionAPI = result.condition;
        condition = conditionAPI;
        if (track !== trackAPI){
            track = trackAPI;
            audioScreen.src = `http://localhost:8080/api/audio/getResource/` + track;
            audio.currentTime = parseInt(timeAPI);
        }
        if((parseFloat(time) + 10 > parseFloat(timeAPI)) || (parseFloat(time) + 10 < parseFloat(timeAPI))){
            time = timeAPI;
            audio.currentTime = parseInt(time);
        }
        if(condition === 'false'){
            audio.pause();
        }
        if (condition === 'true'){
            audio.play();
        }
    });

function getData(){
    fetch('http://localhost:8080/api/audio/getStreamingData/'+params)
        .then(result => result.json())
        .then(result => {
            trackAPI = result.track;
            timeAPI = result.time;
            conditionAPI = result.condition;
        });
    condition = conditionAPI;
    if (track !== trackAPI){
        track = trackAPI;
        audioScreen.src = `http://localhost:8080/api/audio/getResource/` + track;
        audio.currentTime = parseInt(timeAPI);
    }
    if((parseFloat(time) + 10 > parseFloat(timeAPI)) || (parseFloat(time) + 10 < parseFloat(timeAPI))){
        time = timeAPI;
        audio.currentTime = parseInt(time);
    }
    if(condition === 'false'){
        audio.pause();
    }
    if (condition === 'true'){
        audio.play();
    }
}
setInterval(getData, 5000);



