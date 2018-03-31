package huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.escoladeltreball.m08.rranguera.GameMain;

import helpers.GameInfo;



/**
 * Created by RR on 31/03/2018.
 */


public class UIHud {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Image coinImg, lifeImg, scoreImg;

    private Label coinLbl, lifeLbl, scoreLbl;

    private ImageButton pauseBtn;


    //Constructor
    public UIHud(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);


        createUIImgs();
        createUILabels();
        createBtnAndListener();


        Table coinsAndLifeTable = new Table();
        coinsAndLifeTable.top().left();
        coinsAndLifeTable.setFillParent(true);

        // table row 1
        coinsAndLifeTable.add(coinImg).padLeft(10).padTop(10);
        coinsAndLifeTable.add(coinLbl).padLeft(5);

        // table row 2
        coinsAndLifeTable.row();
        coinsAndLifeTable.add(lifeImg).padLeft(10).padTop(10);
        coinsAndLifeTable.add(lifeLbl).padLeft(5);


        Table scoreTable = new Table();
        scoreTable.top().right();
        scoreTable.setFillParent(true);

        scoreTable.add(scoreImg).padRight(10).padTop(10);
        scoreTable.row();
        scoreTable.add(scoreLbl).padRight(10).padTop(15);



/*
        stage.addActor(coinImg);
        stage.addActor(lifeImg);
        stage.addActor(scoreImg);

        stage.addActor(coinLbl);
        stage.addActor(lifeLbl);
        stage.addActor(scoreLbl);
*/
        stage.addActor(coinsAndLifeTable);
        stage.addActor(scoreTable);

        stage.addActor(pauseBtn);
    }




    private void createUIImgs() {

        coinImg = new Image(
                new Texture("Collectables/Coin.png"));

        lifeImg = new Image(
                new Texture("Collectables/Life.png"));

        scoreImg = new Image(
                new Texture("Buttons/Gameplay/Score.png"));


/* Posicionament d'elements ubstituit per taules, en el constructor
        coinImg.setPosition(  GameInfo.WIDTH/2, GameInfo.HEIGHT/2 + 40, Align.center);
        lifeImg.setPosition(GameInfo.WIDTH/2, GameInfo.HEIGHT/2 - 40, Align.center);
        scoreImg.setPosition(  GameInfo.WIDTH/2, GameInfo.HEIGHT/2 - 120, Align.center);
*/

    }



    private void createUILabels() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = 40;

        BitmapFont ingameFont = generator.generateFont(parameters);

        coinLbl  = new Label("x0", new Label.LabelStyle(ingameFont, Color.WHITE));
        lifeLbl  = new Label("x2", new Label.LabelStyle(ingameFont, Color.WHITE));
        scoreLbl = new Label("100", new Label.LabelStyle(ingameFont, Color.WHITE));

/* Posicionament d'elements ubstituit per taules, en el constructor
        coinLbl.setPosition(GameInfo.WIDTH/2, GameInfo.HEIGHT/2 - 120);
        lifeLbl.setPosition(GameInfo.WIDTH/2, GameInfo.HEIGHT/2 - 220);
        scoreLbl.setPosition(GameInfo.WIDTH/2, GameInfo.HEIGHT/2 - 220);
*/
    }



    void createBtnAndListener(){

        pauseBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Gameplay/Pause.png"))));


        pauseBtn.setPosition(  460, 17, Align.bottomRight);



        pauseBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO...
            }
        });

    }



    public Stage getStage() {
        return stage;
    }

}
