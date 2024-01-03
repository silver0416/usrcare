let userData = {
    score: 0
};

const holes = document.querySelectorAll('.hole');
const startButton = document.getElementById('startButton');
const scoreValue = document.getElementById('score');
const timeValue = document.getElementById('time');

userData.score = 0;
let time = 60;
let gameInterval;
let gameStarted = false;

function initializeGame() {
    userData.score = 0;
    time = 60;
    scoreValue.textContent = `得分：${userData.score}分`;
    timeValue.textContent = `時間：${time}秒`;
    startButton.disabled = false;

    holes.forEach(hole => {
        hole.style.backgroundImage = 'url(yellow.jpg)';
        hole.classList.remove('mole', 'rat');
    });

    const popup = document.getElementById('popup');
    popup.style.display = 'none';
    sendDataToAndroid(userData);
}

function startGame() {
    if (gameStarted) return;
    
    gameStarted = true;
    userData.score = 0;
    time = 60;
    scoreValue.textContent = `得分：${userData.score}分`;
    timeValue.textContent = `時間：${time}秒`;

    startButton.disabled = true;
    
    gameInterval = setInterval(() => {
        time--;
        timeValue.textContent = `時間：${time}秒`;
        if (time <= 0) {
            endGame();
        }
        const randomHole = getRandomHole();
        popMole(randomHole);
    }, 1000);
}

function endGame() {
    clearInterval(gameInterval);
    startButton.disabled = false;
    gameStarted = false;

    holes.forEach(hole => {
        hole.style.backgroundImage = 'url(yellow.jpg)';
        hole.classList.remove('mole', 'rat');
    });

    const popup = document.getElementById('popup');
    const finalScore = document.getElementById('finalScore');
    popup.style.display = 'block';
    //finalScore.textContent = userData.score;
    sendDataToAndroid(userData);
}


const playAgainButton = document.getElementById('playAgainButton');
playAgainButton.addEventListener('click', () => {
    initializeGame();
});

function getRandomHole() {
    const index = Math.floor(Math.random() * holes.length);
    return holes[index];
}

function popMole(hole) {
    if (Math.random() < 0.7) {
        hole.style.backgroundImage = 'url(mouse.png)';
        hole.classList.add('mole');
    } else {
        hole.style.backgroundImage = 'url(rat.png)';
        hole.classList.add('rat');
    }

    setTimeout(() => {
        if (hole.classList.contains('mole') || hole.classList.contains('rat')) {
            hole.style.backgroundImage = 'url(yellow.jpg)';
            hole.classList.remove('mole', 'rat');
        }
    }, 1000);
}

function whackMole(e) {
    if (!e.isTrusted) return;
    if (this.classList.contains('mole')) {
        this.style.backgroundImage = 'url(yellow.jpg)';
        this.classList.remove('mole');
        userData.score += 2;
        scoreValue.textContent = `得分：${userData.score}分`;
        const goodMoleSound = document.getElementById('goodMoleSound');
        goodMoleSound.play();
    } else if (this.classList.contains('rat')) {
        this.style.backgroundImage = 'url(yellow.jpg)';
        this.classList.remove('rat');
        time -= 5;
        if (time < 0) time = 0;
        timeValue.textContent = `時間：${time}秒`;
    }
}

holes.forEach(hole => hole.addEventListener('click', whackMole));
startButton.addEventListener('click', startGame);
const showInstructionsButton = document.getElementById("showInstructionsButton");
const gameInstructions = document.getElementById("game-instructions");

showInstructionsButton.addEventListener("click", function() {
    if (gameInstructions.style.display === "none" || gameInstructions.style.display === "") {
        gameInstructions.style.display = "block";
    } else {
        gameInstructions.style.display = "none";
    }
});

function sendDataToAndroid(data) {
    console.log(data);
    try{
        AndroidInterface.processWebData(JSON.stringify(data));
    }catch (e){
       console.log(e);
    }
}

