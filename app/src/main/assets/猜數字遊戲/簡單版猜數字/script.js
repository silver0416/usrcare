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

var guessNumber = Math.floor(Math.random() * (maxRange - minRange + 1)) + minRange;
userData.guessTimes = 0;

sendDataToAndroid(userData);

guessBu.onclick = function () {
    userData.guessTimes++;
    count.innerHTML = userData.guessTimes;
    var userGuess = parseInt(text.value);

        if (isNaN(userGuess) || userGuess < 1 || userGuess >100)  {
            result.innerHTML = "請輸入1-100的數字";
            result.style.color = "red";
            resultDisplay.innerHTML = "輸入錯誤！"; 
        }else if (userGuess > guessNumber) {
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
            result.className = "c2";
            result.innerHTML = "恭喜猜對了！";
            result.style.color = "green";
            resultDisplay.innerHTML = "恭喜猜對了！";
            playCorrectGuessSound();
            sendDataToAndroid(userData);
        }
    

}
function playCorrectGuessSound() {
    correctAudio.play();
}

reBu.onclick = function () {
    minRange = 1;
    maxRange = 100;
    guessNumber = Math.floor(Math.random() * (maxRange - minRange + 1)) + minRange;
    userData.guessTimes = 0;
    count.innerHTML = userData.guessTimes;
    count.innerHTML = userData.guessTimes;
    result.innerHTML = "";
    resultDisplay.innerHTML = "";
    text.value = "";
    sendDataToAndroid(userData);
}

function sendDataToAndroid(data) {
    console.log(data);
    try{
        AndroidInterface.processWebData(JSON.stringify(data));
    }catch (e){
       console.log(e);
    }
}
