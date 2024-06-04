#Tetris Clone

This is a pixel art tetris clone made using JavaFX.

###Installation Instructions:

1. Make sure Java 17+ is installed on your system.
2.  (OPTIONAL) Add Java to PATH so you can run java commands without having to specify path to your Java installation.
3. Clone the Tetris_Clone repo to a location of your choosing and then in terminal/command prompt, navigate to Tetris_Clone directory.
4. Build the project using maven wrapper (included in repo).
    - If on unix-based system, run the following command in terminal:
        - ./mvnw clean package
    - If on windows, run the following command in command prompt:
        - mvnw clean package
5. Once project has been built, a 'target' folder will be generated inside the project directory (Tetris_Clone).
6. Inside the target folder, there will be an executable jar file.
7. To run the executable jar, make sure you are in 'Tetris_Clone' directory and then run the following command in terminal/command prompt:
    - java -jar target/Tetris_Clone-1.0-SNAPSHOT-jar-with-dependencies.jar
    
###Instructions:
1. ######Menu Controls:
    - Navigate menu options:
        - UP and DOWN arrows
    - Select menu option:
        - SPACE or ENTER
2. ######Game Controls:
    - Move tetromino
        - LEFT and RIGHT arrows
    - Rotate tetromino
        - Z and X
    - Soft drop tetromino
        - DOWN arrow
    - Hard drop tetromino
        - UP arrow
    - Play/pause game
        - SPACE or ENTER
3. ######Rules:
    - Clear rows of placed tetromino tiles to increase score.
    - If 4 rows are cleared at once, a TETRIS is rewarded, and one of the letters of TETRIS will appear in top left display window.
    - Get all 6 TETRIS letters to receive a substantial score bonus!
    - Top 3 scores will be saved in TOP SCORES in the menu.
