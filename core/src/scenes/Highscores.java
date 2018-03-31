package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.escoladeltreball.m08.rranguera.GameMain;

import helpers.GameInfo;



/**
 * Created by RR on 31/03/2018.
 */

public class Highscores implements Screen {

    private GameMain game;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private Texture bg;



    //constructor
    public Highscores(GameMain game) {
        this.game = game;

        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH/2, GameInfo.HEIGHT, 0);

        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        bg = new Texture("Backgrounds/Highscore BG.png");
    }



    @Override
    public void show() {

    }



    @Override
    public void render(float delta) {

        // limpiem la pantalla abans de fer res
        Gdx.gl.glClearColor(1, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        game.getBatch().begin();    //----------------ppi de les ordres de dibuixat

        game.getBatch().draw(bg, 0, 0);

        game.getBatch().end();      //----------------fi de les ordres de dibuixat
    }



    @Override
    public void resize(int width, int height) {

    }



    @Override
    public void pause() {

    }



    @Override
    public void resume() {

    }



    @Override
    public void hide() {

    }



    @Override
    public void dispose() {
        bg.dispose();
    }
}
