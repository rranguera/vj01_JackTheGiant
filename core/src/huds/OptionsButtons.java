package huds;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.escoladeltreball.m08.rranguera.GameMain;

import helpers.GameInfo;
import scenes.Gameplay;
import scenes.Highscores;
import scenes.MainMenu;



/**
 * Created by RR on 31/03/2018.
 */

public class OptionsButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private ImageButton easyBtn, mediumBtn, hardBtn;
    private Image checkSign;

    private ImageButton backBtn;



    //constructor
    public OptionsButtons(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        createAndPositionButtons();
        addAllListeners();

        stage.addActor(easyBtn);
        stage.addActor(mediumBtn);
        stage.addActor(hardBtn);
        stage.addActor(checkSign);
        stage.addActor(backBtn);

    }



    private void createAndPositionButtons() {

        easyBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Options Menu/Easy.png"))));

        mediumBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Options Menu/Medium.png"))));

        hardBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Options Menu/Hard.png"))));

        backBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Options Menu/Back.png"))));


        checkSign = new Image(
                new Texture("Buttons/Options Menu/Check Sign.png"));


        easyBtn.setPosition(  GameInfo.WIDTH/2, GameInfo.HEIGHT/2 + 40, Align.center);
        mediumBtn.setPosition(GameInfo.WIDTH/2, GameInfo.HEIGHT/2 - 40, Align.center);
        hardBtn.setPosition(  GameInfo.WIDTH/2, GameInfo.HEIGHT/2 - 120, Align.center);

        backBtn.setPosition(17, 17, Align.bottomLeft);

        //TODO: REM treure aquest (prova):      -vid 24-
        checkSign.setPosition(GameInfo.WIDTH/2 + 76 , mediumBtn.getY()+13, Align.bottomLeft);

    }


    void addAllListeners(){


        easyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO: guardar nivell de dificultat
                checkSign.setY(easyBtn.getY() + 13);
            }
        });

        mediumBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                checkSign.setY(mediumBtn.getY() + 13);
            }
        });

        hardBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                checkSign.setY(hardBtn.getY() + 13);
            }
        });


        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });

    }



    public Stage getStage() {
        return stage;
    }
}
