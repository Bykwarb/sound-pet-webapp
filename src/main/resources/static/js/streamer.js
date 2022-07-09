const audioDiv = document.querySelector('#audio-player');
const audioScreen = document.querySelector('#audio-screen');
const params = window.location.href.replace('http://localhost:8080/room/content/', '')
const queryParams = Object.fromEntries(new URLSearchParams(window.location.search));
send()

fetch('http://localhost:8080/api/audio/getRoomAudio/'+params)
    .then(result => result.json())
    .then(result => {
        const myAudio = document.querySelector('#your-audio');
        if(result.length > 0){
            for(let aud of result){
                const li = document.createElement('LI');
                const link = document.createElement('A');
                let button = document.createElement("BUTTON");
                link.innerText = aud;
                link.href = window.location.origin + window.location.pathname + '?audio=' + aud;
                li.appendChild(link);
                button.name = "remove_button " + 1;
                button.type = "submit";
                button.innerText = "remove";
                button.addEventListener('click', async _ => {
                    const response = await fetch('http://localhost:8080/api/audio/delete?track_name=' + aud + '&room_url=' + params, {
                        method: 'post'
                    });
                    document.location.reload();
                });
                myAudio.appendChild(li);
                myAudio.appendChild(button);
            }

        }else{
            myAudio.innerHTML = 'No audio found';
        }
    });

if(queryParams.audio){
    audioScreen.src = `http://localhost:8080/api/audio/getResource/${queryParams.audio}`;
    audioDiv.style.display = 'block';
    document.querySelector('#now-playing')
        .innerText = 'Now playing ' + queryParams.audio;
}

const audio = document.getElementById("audio-screen");
const track = queryParams.audio;
const time = audio.currentTime;
function send(){
    const audio = document.getElementById("audio-screen");
    let track = queryParams.audio;
    let time = audio.currentTime;
    let condition = 'true';
    if(audio.paused){
        condition = 'false';
    }
    let roomUrl = params;
    fetch('http://localhost:8080/api/audio/postStreamingData', {
        method: "post",
        body: JSON.stringify({
            'room': roomUrl,
            'track': track,
            'time': time,
            'condition': condition
        }),
        headers:{
            'Content-type' : 'application/json'
        }
    });
}
setInterval(send, 5000);