let userData = {
    guessTimes: 0
};

var text = document.querySelector("#text");
var count = document.querySelector("#count");
var result = document.querySelector(".result");
var guessBu = document.querySelector("#guess");
var reBu = document.querySelector("#reBu");
var resultDisplay = document.querySelector("#result");
var correctAudio = document.getElementById("correct-audio");

var minRange = 1;
var maxRange = 100;
var start_time;
var end_time;
var started = false;

var guessNumber;
userData.guessTimes = 0;

function generate_guess_number(){
    guessNumber = Math.floor(Math.random() * (maxRange - minRange + 1)) + minRange;
}

generate_guess_number();

guessBu.onclick = function() {
    if (text.value == '') {
        text.focus();
        return;
    }
    if(!started){
        started = true;
        start_time = new Date();
    }
    userData.guessTimes++;
    count.innerHTML = userData.guessTimes;
    var userGuess = parseInt(text.value);

    if (isNaN(userGuess) || userGuess < 1 || userGuess > 100) {
        result.innerHTML = "請輸入1-100的數字";
        result.style.color = "red";
        resultDisplay.innerHTML = "輸入錯誤！";
    } else if (userGuess > guessNumber) {
        result.innerHTML = "猜大了！";
        result.style.color = "red";
        if (userGuess <= maxRange) {
            maxRange = userGuess;
        }

        resultDisplay.innerHTML = `${minRange}-${maxRange}`;
    } else if (userGuess < guessNumber) {
        result.innerHTML = "猜小了！";
        result.style.color = "red";
        if (userGuess >= minRange) {
            minRange = userGuess;
        }
        resultDisplay.innerHTML = `${minRange}-${maxRange}`;
    } else {
        end_time = new Date();
        result.className = "c2";
        result.innerHTML = "恭喜猜對了！";
        result.style.color = "green";
        resultDisplay.innerHTML = "恭喜猜對了！";
        playCorrectGuessSound();
        start_time.setHours(start_time.getHours() + 8)
        end_time.setHours(end_time.getHours() + 8)
        gamedata = {
            "game": "Da Vinci Code",
            'guess_times': userData.guessTimes,
            "start_time": start_time.toISOString().split(".")[0],
            "end_time": end_time.toISOString().split(".")[0]
        }
        sendDataToAndroid(gamedata);
        return;
    }
    text.value = '';
    text.focus();


}

function playCorrectGuessSound() {
    correctAudio.play();
}

reBu.onclick = function() {
    started = false;
    generate_guess_number();
    minRange = 1;
    maxRange = 100;
    guessNumber = Math.floor(Math.random() * (maxRange - minRange + 1)) + minRange;
    userData.guessTimes = 0;
    count.innerHTML = userData.guessTimes;
    count.innerHTML = userData.guessTimes;
    result.innerHTML = "";
    resultDisplay.innerHTML = "";
    text.value = "";
}

function sendDataToAndroid(data) {
    console.log(data);
    try {
        AndroidInterface.processWebData(JSON.stringify(data));
    } catch (e) {
        console.log(e);
    }
}