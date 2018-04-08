package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Created by RR on 08/04/2018.
 */

public class GameManager {

    private static final GameManager ourInstance = new GameManager();

    //--tmp:  estic fent el vid43, havent-me saltat els 40, 41, 42, i sembla q han crat algo així:
    public GameData gameData;  //vid 41

    private Json json = new Json();     //vid 41
//    private FileHandle fileHandle = Gdx.files.internal("bin/GameData.json");    //*no es pot escriure a internal
    private FileHandle fileHandle = Gdx.files.local("my_bin/GameData.json");    //vid 41


    public boolean gameStartedFromMAinMenu;
    public boolean isPaused = true;

    public int lifeScore, coinScore, score;





    // Constructor (*privat, ningú més podrà crear instàncies; només existirà una, la que hem creat en la 1ª línia)
    private GameManager() {
    }



    public void initializeGameData(){
        if (!fileHandle.exists()){      //first time running the game
            gameData = new GameData();

            gameData.setHighscore(0);
            gameData.setCoinHighscore(0);

            gameData.setEasyDifficulty(false);
            gameData.setMediumDifficulty(true);
            gameData.setHardDifficulty(false);

            gameData.setMusicOn(false);

            saveData();
            System.out.println("config desada");
        }
        else {
            loadData();
            System.out.println("config llegida");
        }
    }



    public void saveData(){
        if (gameData != null){
            fileHandle.writeString(json.prettyPrint(gameData), false);  //append: false means overwrite file (delete previous content)
        }
    }



    public void loadData(){
        gameData = json.fromJson(GameData.class, fileHandle.readString());
    }



    public void checkForNewHighscores(){

        int oldHighscore = gameData.getHighscore();
        int oldCoinScore = gameData.getCoinHighscore();

        if (oldHighscore < score){
            gameData.setHighscore(score);
        }

        if (oldCoinScore < coinScore){
            gameData.setCoinHighscore(coinScore);
        }

        saveData();


    }


    public static GameManager getInstance() {
        return ourInstance;
    }
}
