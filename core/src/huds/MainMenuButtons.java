package huds;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.escoladeltreball.m08.rranguera.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.Gameplay;
import scenes.Highscores;
import scenes.MainMenu;
import scenes.Optioins;



/**
 * Created by RR on 31/03/2018.
 */

public class MainMenuButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private ImageButton playBtn;
    private ImageButton highscoresBtn;
    private ImageButton optionsBtn;
    private ImageButton quitBtn;
    private ImageButton musicBtn;

    private Sound buttonSound;





    //constructor
    public MainMenuButtons(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);
        createAndPositionButtons();
        addAllListeners();

        stage.addActor(playBtn);
        stage.addActor(highscoresBtn);
        stage.addActor(optionsBtn);
        stage.addActor(quitBtn);
        stage.addActor(musicBtn);

        checkMusic();

        buttonSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Click Sound.wav"));
    }



    private void createAndPositionButtons() {

        playBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu/Start Game.png"))));

        highscoresBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu/Highscore.png"))));

        optionsBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu/Options.png"))));

        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu/Quit.png"))));

        musicBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu/Music On.png"))));


        playBtn.setPosition(    GameInfo.WIDTH/2, GameInfo.HEIGHT/2 + 120, Align.center);
        highscoresBtn.setPosition(GameInfo.WIDTH/2, GameInfo.HEIGHT/2 - 20, Align.center);
        optionsBtn.setPosition( GameInfo.WIDTH/2, GameInfo.HEIGHT/2 - 90, Align.center);
        quitBtn.setPosition(    GameInfo.WIDTH/2, GameInfo.HEIGHT/2 - 230, Align.center);
        musicBtn.setPosition(   GameInfo.WIDTH - 13 , 40, Align.right);

    }


    void addAllListeners(){

        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonSound.play();
//                System.out.println("S'ha apretat el botó PLAY");
                GameManager.getInstance().gameStartedFromMAinMenu = true;


                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new Gameplay(game));     //inicia la pantalla de partida (Gameplay.java)
                    }
                });

                SequenceAction seqActn = new SequenceAction();
                seqActn.addAction(Actions.fadeOut(0.3f));
                seqActn.addAction(run);

                stage.addAction(seqActn);
            }
        });


        highscoresBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonSound.play();
                game.setScreen(new Highscores(game));
            }
        });

        optionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonSound.play();
                game.setScreen(new Optioins(game));
            }
        });

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonSound.play();
                //TODO pdt Sortir
                System.out.println("-pendent-");
            }
        });


        musicBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                buttonSound.play();

                if (GameManager.getInstance().gameData.isMusicOn()){
                    GameManager.getInstance().gameData.setMusicOn(false);
                    GameManager.getInstance().stopMusic();
                }
                else {
                    GameManager.getInstance().gameData.setMusicOn(true);
                    GameManager.getInstance().playMusic();
                }
                GameManager.getInstance().saveData();
            }
        });

    }



    void checkMusic(){
        if (GameManager.getInstance().gameData.isMusicOn()){
            GameManager.getInstance().playMusic();
        }
    }



    public Stage getStage() {
        return stage;
    }


    public void dispose() { //he afegit jo aquest mètode, però crec que no s'executa mai
        System.out.println("executat el dispose() de MainMenuButtons.java");
        buttonSound.dispose();
    }
}
