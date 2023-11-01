document.getElementById('guess').addEventListener('keydown', function (event) {
    if (event.key === 'Enter') {
        submitGuess();
    }
});


function generateRandomNumber() {
    let numbers = [];
    while (numbers.length < 4) {
        let randomNumber = Math.floor(Math.random() * 10);
        if (!numbers.includes(randomNumber)) {
            numbers.push(randomNumber);
        }
    }
    return numbers.join('');
}

let secretNumber = generateRandomNumber();
let attempts = 0;
const guessHistory = [];

function restartGame() {
    secretNumber = generateRandomNumber();
    attempts = 0;
    guessHistory.length = 0;
    document.getElementById('submit').disabled = false;
    document.getElementById('result').innerHTML = '';
    document.getElementById('guess').value = '';
    updateGuessHistory();
}

document.getElementById('submit').addEventListener('click', function () {
    const guessInput = document.getElementById('guess');
    const guess = guessInput.value;
    if (guess.length !== 4 || isNaN(guess)) {
        alert('請輸入4位數字。');
        return;
    }

    let a = 0;
    let b = 0;

    for (let i = 0; i < 4; i++) {
        if (guess[i] === secretNumber[i]) {
            a++;
        } else if (secretNumber.includes(guess[i])) {
            b++;
        }
    }

    attempts++;
    guessHistory.push({ guess: guess, result: `${a}A${b}B` });

    if (a === 4) {
        document.getElementById('result').innerHTML = `恭喜你猜對了！答案是${secretNumber}，總共猜了${attempts}次。`;
        document.getElementById('submit').disabled = true;
        document.getElementById('restart-button').style.display = 'block';
    } else {
        document.getElementById('result').innerHTML = `第 ${attempts} 次猜測：${guess} => ${a}A${b}B`;
        updateGuessHistory();
    }

    guessInput.value = ''; 
});

document.getElementById('restart-button').addEventListener('click', function () {
    restartGame();
    document.getElementById('restart-button').style.display = 'none';
});

function updateGuessHistory() {
    const guessHistoryDiv = document.getElementById('guess-history');
    guessHistoryDiv.innerHTML = '';
    guessHistory.forEach((item, index) => {
        const guessRecord = document.createElement('p');
        guessRecord.textContent = `猜測 ${index + 1} : ${item.guess}  => ${item.result}`;
        guessHistoryDiv.appendChild(guessRecord);
    });
}

function submitGuess() {
    const guessInput = document.getElementById('guess');
    const guess = guessInput.value;
    if (guess.length !== 4 || isNaN(guess)) {
        alert('请输入4位数字。');
        return;
    }

    let a = 0;
    let b = 0;

    for (let i = 0; i < 4; i++) {
        if (guess[i] === secretNumber[i]) {
            a++;
        } else if (secretNumber.includes(guess[i])) {
            b++;
        }
    }

    attempts++;
    guessHistory.push({ guess: guess, result: `${a}A${b}B` });

    if (a === 4) {
        document.getElementById('result').innerHTML = `恭喜你猜對了！答案是 ${secretNumber}，總共猜了 ${attempts} 次。`;
        document.getElementById('submit').disabled = true;
        document.getElementById('restart-button').style.display = 'block';
        playCorrectSound();
    } else {
        document.getElementById('result').innerHTML = `第 ${attempts} 次猜测：${guess} => ${a}A${b}B`;
        updateGuessHistory();
    }

    guessInput.value = '';
}

var rulesDiv = document.querySelector('.rules');
var showRulesButton = document.getElementById('show-rules-button');
var closeRulesButton = document.getElementById('close-rules-button');

var rulesDiv = document.querySelector('.rules');
var showRulesButton = document.getElementById('show-rules-button');

showRulesButton.addEventListener('click', function () {
    if (rulesDiv.style.display === 'block') {
        rulesDiv.style.display = 'none';
    } else {
        rulesDiv.style.display = 'block';
    }
});

rulesDiv.addEventListener('click', function (event) {
    if (event.target === rulesDiv) {
        rulesDiv.style.display = 'none';
    }
});

document.getElementById('close-rules-button').addEventListener('click', function () {
    rulesDiv.style.display = 'none';
});




function playCorrectSound() {
    var audio = document.getElementById('correct-audio');
    audio.play();
}
