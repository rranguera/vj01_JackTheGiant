package helpers;

/**
 * Created by RR on 08/04/2018.
 */

public class GameManager {

    private static final GameManager ourInstance = new GameManager();

    public boolean gameStartedFromMAinMenu;
    public boolean isPaused = true;
    public int lifeScore, coinScore, score;




    // Constructor (*privat, ningú més podrà crear instàncies)
    private GameManager() {
    }



    public static GameManager getInstance() {
        return ourInstance;
    }
}
