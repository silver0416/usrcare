var gameListing = document.getElementById("diff-list");

// 使用 URLSearchParams 來解析 URL
var urlParams = new URLSearchParams(window.location.search);
// 使用 get 方法來獲得參數的值
var paramValue = urlParams.get('param');
// 使用 param1Value 進行相應的操作
console.log(paramValue);

games().then(function(gamePaths) {
    gamePaths.forEach(function(gamePath) {
        const imgElement = document.createElement('img');
        imgElement.src = "level_img/" + gamePath + ".png";

        var gameLink = document.createElement("a");
        gameLink.classList.add("game");
        gameLink.href = "./" + gamePath + "/index.html?param=" + encodeURIComponent(paramValue);
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
        "初級模式",
        "中級模式",
        "高級模式"
    ];
}
