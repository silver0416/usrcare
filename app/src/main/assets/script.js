var gameListing = document.getElementById("game-list");

games().then(function(gamePaths) {
    gamePaths.forEach(function(gamePath) {
        const imgElement = document.createElement('img');
        imgElement.src = "imgs/" + gamePath + ".png";

        var gameLink = document.createElement("a");
        gameLink.classList.add("game");
        gameLink.href = "./" + gamePath + "/index.html";
        gameLink.textContent = gamePath;

        gameLink.insertBefore(imgElement, gameLink.firstChild);

        var gameElement = document.createElement("div");
        gameElement.classList.add("game-dox");
        gameElement.appendChild(gameLink);

        gameListing.appendChild(gameElement);
    });
});

async function games() {
    return [
        "翻牌記憶小遊戲",
        "打地鼠遊戲",
        "幾A幾B"
    ];
}
