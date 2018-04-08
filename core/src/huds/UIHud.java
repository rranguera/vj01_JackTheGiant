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
import helpers.GameManager;
import scenes.MainMenu;



/**
 * Created by RR on 31/03/2018.
 */


public class UIHud {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Image coinImg, lifeImg, scoreImg, pausePanel;

    private Label coinLbl, lifeLbl, scoreLbl;

    private ImageButton pauseBtn, resumeBtn, quitBtn;


    //Constructor
    public UIHud(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);


        if (GameManager.getInstance().gameStartedFromMAinMenu){
            // El joc s'ha iniciat des del menú (no és un reinici de quan ha mort), és l'inici de la partida, per tant definim els valors inicials:
            GameManager.getInstance().gameStartedFromMAinMenu = false;

            GameManager.getInstance().coinScore = 0;
            GameManager.getInstance().score = 0;
            GameManager.getInstance().lifeScore = 2;
        }

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

        coinLbl  = new Label("x"+GameManager.getInstance().coinScore, new Label.LabelStyle(ingameFont, Color.WHITE));
        lifeLbl  = new Label("x"+GameManager.getInstance().lifeScore, new Label.LabelStyle(ingameFont, Color.WHITE));
        //scoreLbl = new Label(String.valueOf(GameManager.getInstance().score), new Label.LabelStyle(ingameFont, Color.WHITE));
        //alternativa:
        scoreLbl = new Label(""+GameManager.getInstance().score, new Label.LabelStyle(ingameFont, Color.WHITE));

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

                if (!GameManager.getInstance().isPaused){   //si no està pausat ja, fem la pausa:

                    //Pausem el joc:
                    GameManager.getInstance().isPaused = true;

                    //Mostrem el panell de pausa:
                    createPausePanel();
                }
            }
        });

    }


    void createPausePanel(){

        pausePanel = new Image(new Texture("Buttons/Paused/Pause Panel.png"));

        resumeBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Paused/Resume.png"))));

        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Paused/Quit 2.png"))));


        pausePanel.setPosition(GameInfo.WIDTH/2, GameInfo.HEIGHT/2, Align.center);
        resumeBtn.setPosition (GameInfo.WIDTH/2, GameInfo.HEIGHT/2 + 50, Align.center);
        quitBtn.setPosition   (GameInfo.WIDTH/2, GameInfo.HEIGHT/2 - 80, Align.center);


        resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                removePausePanel();
                GameManager.getInstance().isPaused = false;
            }
        });

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });


        stage.addActor(pausePanel);
        stage.addActor(resumeBtn);
        stage.addActor(quitBtn);

    }



    void removePausePanel(){
        quitBtn.remove();
        resumeBtn.remove();
        pausePanel.remove();
    }



    public void createGameOverPanel(){

        Image gameOverPanel = new Image(new Texture("Buttons/Paused/Show Score.png"));


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = 70;

        BitmapFont bigFont = generator.generateFont(parameters);

        Label endScore = new Label(String.valueOf(GameManager.getInstance().score),
                new Label.LabelStyle(bigFont, Color.WHITE));

        Label endCoinScore = new Label(String.valueOf(GameManager.getInstance().coinScore),
                new Label.LabelStyle(bigFont, Color.WHITE));


        gameOverPanel.setPosition(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f, Align.center);

        endScore.setPosition(
                GameInfo.WIDTH/2f - 30,
                GameInfo.HEIGHT/2f + 20,
                Align.center);

        endCoinScore.setPosition(
                GameInfo.WIDTH/2f - 30,
                GameInfo.HEIGHT/2f - 90,
                Align.center);


        stage.addActor(gameOverPanel);
        stage.addActor(endScore);
        stage.addActor(endCoinScore);

    }



    public void incrementScore (int inc){
        GameManager.getInstance().score += inc;
        scoreLbl.setText(String.valueOf(GameManager.getInstance().score));
    }

    public void incrementCoins(){
        GameManager.getInstance().coinScore++;
        coinLbl.setText("x"+GameManager.getInstance().coinScore);
        incrementScore(200);
    }

    public void incrementLifes(){
        GameManager.getInstance().lifeScore++;
        lifeLbl.setText("x"+GameManager.getInstance().lifeScore);
        incrementScore(300);
    }



    public void decrementLife(){

        GameManager.getInstance().lifeScore--;

        if (GameManager.getInstance().lifeScore >= 0){
            lifeLbl.setText("x"+GameManager.getInstance().lifeScore);
        }
    }



    public Stage getStage() {
        return stage;
    }

}
