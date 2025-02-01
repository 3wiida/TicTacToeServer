# TicTacToe Server
<img src="github_images/logo.png" align="left" width="180" hspace="10" vspace="10" />

A multiplayer Tic Tac Toe game server handling matchmaking, game logic, player authentication, and real-time communication between client and server.

This comes as the final project of Java course at ITI.<br><br><br><br>

## Screenshots

<div>
    <img src="github_images/server_home.png" width="350" height="250" alt="Screenshot 1">
    <img src="github_images/server_state.png" width="350" height="250" alt="Screenshot 2">
</div>

### 🧑🏻‍💻 Tech Stack

- Application view is entirely written in [JavaFX](https://openjfx.io/) and [CSS](https://developer.mozilla.org/en-US/docs/Web/CSS).
- [Java Sockets](https://docs.oracle.com/javase/tutorial/networking/sockets/index.html) for online communication.
- Asynchronous processing using [Java Threads](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html)
- [Json](https://www.json.org/json-en.html) for passing messages between the client and server.
- Architectural pattern using [MVC](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)
- [Derby database](https://db.apache.org/derby/) to store players data.

### Features ✨

- Start or stop The server.
- Monitor the statistics about online and offline players in real time.
- Manage communication between players.
- Has a database that contains all players data.

### How to install and run❔

1- Clone the project.<br>
2- Open it from [Netbeans.](https://netbeans-ide.en.softonic.com/download)<br>
3- Download [org.json](https://jar-download.com/artifacts/org.json/json/20230227/source-code)  jar file.<br>
4- Add this jar file for the project libraries in netbeans.<br>
5- Create a derby database of name "TicTacToeDatabse".<br>
6- Create a table "PLAYERS" with the following columns:
  - ID, VARCHAR(255) - PK
  - NAME, VARCHAR(255) - U
  - PASSWORD, VARCHAR(255)
  - STATUS, INTEGER
  - SCORE, INTEGER

7- Run the project.<br>

### Acknowledgments 🙌  

A huge **thank you** to my amazing teammates for their dedication, hard work, and collaboration throughout this project. This game wouldn’t have been possible without your efforts! 🎉  

**Team Members:**  
- [Mahmoud Ewida](https://github.com/3wiida)  
- [Khaled Mustafa](https://github.com/KhaledMustafaAhmed)  
- [Omar Abdulaziz](https://github.com/omarabdulaziz259)  
- [Reham Ibrahem](https://github.com/reham2002ibrahim)  

It was an incredible journey working together—debugging, brainstorming, and bringing this game to life. Thank you all for making this project a success! 🚀  


## Find this repository useful? :heart:
Support it by putting a star for this repository. :star:
