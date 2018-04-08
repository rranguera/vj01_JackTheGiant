package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.escoladeltreball.m08.rranguera.GameMain;

import clouds.CloudsController;
import helpers.GameInfo;
import helpers.GameManager;
import huds.UIHud;
import player.Player;

/**
 * Created by RR on 5/03/18.
 */

public class Gameplay implements Screen, ContactListener {

    private GameMain game;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;


    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;

    private World world;


    private Sprite bg1, bg2, bg3;
    private Sprite[] bgs;
    private float lastYPosition;

    private float cameraSpeed = 10;     // vid 43
    private float maxSpeed = 10;        // vid 43
    private float acceleration = 10;    // vid 43

    private boolean touchedForTheFirstTime = false;


    //TODO DELETE this later, it's just a tester (vid 7)
//    Cloud c;


    private UIHud hud;  // vid 25

    private CloudsController cloudsController;

    private Player player;

    private float lastPlayerY;      //vid 37, emprat per a la pauntuació








    // Constructor
    public Gameplay (GameMain game){
        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f, 0);

        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);


        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, GameInfo.WIDTH/GameInfo.PPM, GameInfo.HEIGHT/GameInfo.PPM);
        box2DCamera.position.set(GameInfo.WIDTH/2, GameInfo.HEIGHT/2, 0);

        debugRenderer = new Box2DDebugRenderer();

        hud = new UIHud(game);

        world = new World(
                    new Vector2(0, -9.8f),  //gravetat
                    true
        );
        //inform the world that the contact listener is the Gameplay class:
        world.setContactListener(this);


//TODO DELETE (test vid 7)
/*
        //test:
        c = new Cloud(world, "Cloud 1");
//        c = new Cloud(world, "Dark Cloud");
        c.setPosition(GameInfo.WIDTH/2, GameInfo.HEIGHT/2);
*/

        cloudsController = new CloudsController(world);

        player = cloudsController.positionThePlayerAtStart(player);



        createBackgrounds();

        setCameraSpeed();
    }



    void handleInput(float dt){
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.setWalking(true);
            player.movePlayer(-2);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.setWalking(true);
            player.movePlayer(2);
        }
        else {
            player.setWalking(false);
        }
    }



    private void checkForFirstTouch() {
        if (!touchedForTheFirstTime){
            if (Gdx.input.justTouched()){
                touchedForTheFirstTime = true;
                GameManager.getInstance().isPaused = false;
                lastPlayerY = player.getY();
            }
        }
    }



    void update(float dt){

        checkForFirstTouch();

        if (GameManager.getInstance().isPaused){
            // No fem res --> no es mou res = joc pausat
        }
        else {
            handleInput(dt);
            moveCamera(dt);
            checkBackgroundsOutOfBounds();
            cloudsController.setCameraYPos(mainCamera.position.y);
            cloudsController.createAndArrangeNewClouds();
            cloudsController.removeOffscreenCollectables();
            checkPlayersBounds();
            countScore();
        }
    }



    void moveCamera(float delta) {
        //velocitat de desplaçament vertical de la càmera/joc:
        mainCamera.position.y -= cameraSpeed * delta;

        cameraSpeed += acceleration * delta;

        if (cameraSpeed > maxSpeed){
            cameraSpeed = maxSpeed;
        }
    }


    void setCameraSpeed(){      // velocitats del joc, en funció de lo elegit al menú d'opcions

        if (GameManager.getInstance().gameData.isEasyDifficulty()){
            cameraSpeed = 60;
            maxSpeed = 90;
//            System.out.println("adjustada velocitat a mode EASY");
        }
        else if (GameManager.getInstance().gameData.isMediumDifficulty()){
            cameraSpeed = 100;
            maxSpeed = 120;
        }
        else if (GameManager.getInstance().gameData.isHardDifficulty()){
            cameraSpeed = 140;
            maxSpeed = 160;
//            System.out.println("adjustada velocitat a mode HARD");
        }
    }



    void createBackgrounds(){

        bgs = new  Sprite[3];

        for (int i=0; i<bgs.length; i++){
            bgs[i] = new Sprite(new Texture("Backgrounds/Game BG.png"));
            bgs[i].setPosition(0, -(bgs[i].getHeight() * i) );
//variant per a testeig:
//            bgs[i].setPosition(5*i, -(bgs[i].getHeight() * i) );
        }

        lastYPosition = Math.abs( bgs[bgs.length-1].getY() );   // OPTIMITZAT: en el vid 06, min 3, posa això dintre el for. Ho he tret fora.

    }


    void drawBackgrounds() {
        for (int i=0; i<bgs.length; i++){
            game.getBatch().draw(bgs[i], bgs[i].getX(), bgs[i].getY());
        }
    }



    void checkBackgroundsOutOfBounds(){

        // Mirem si cal reposicionar algun dels fons:

        for (int i=0; i<bgs.length; i++){

//System.out.printf(".");

            if (mainCamera.position.y < (bgs[i].getY() - bgs[i].getHeight()/2f -5) ){

                float newPosition = bgs[i].getHeight() + lastYPosition;

//System.out.println("\nreposicionant el fons " + i + " a Y=-" + newPosition);
                bgs[i].setPosition(0, -newPosition);
//variant per a testeig:
//                bgs[i].setPosition(20*i, -newPosition);
                lastYPosition = Math.abs(newPosition);
            }
        }
    }


    void checkPlayersBounds(){

        //REM: en les coordenades en pantalla, el (0,0) és a la part inferior esquerra.

        final boolean DEBUG_VERVOSE_MODE = false;


        int margeExteriorVerticalExtra = 300;

        // límit superior
        if ((player.getY() - GameInfo.HEIGHT/2 - player.getHeight()/2)
                > mainCamera.position.y + margeExteriorVerticalExtra){

//            GameManager.getInstance().isPaused = true;        // passat a un altre lloc (vid 38, minut 1)

            if (!player.isDead()){
                playerDied();
            }


            if (DEBUG_VERVOSE_MODE){
                System.out.println("Player OUT of bounds -top-");
                System.out.println("cam position y:" + mainCamera.position.y);
                System.out.println("player pos" + player.getY() + GameInfo.HEIGHT/2 + player.getHeight()/2);
            }

        }


        // límit inferior
        if ((player.getY() + GameInfo.HEIGHT/2 + player.getHeight()/2)
                < mainCamera.position.y  - margeExteriorVerticalExtra){

//            GameManager.getInstance().isPaused = true;        // passat a un altre lloc (vid 38, minut 1)

            if (!player.isDead()){
                playerDied();
            }


            if (DEBUG_VERVOSE_MODE){
                System.out.println("Player OUT of bounds -per baix-");
                System.out.println("cam position y:" + mainCamera.position.y);
                System.out.println("player pos" + player.getY() + GameInfo.HEIGHT/2 + player.getHeight()/2);
            }
        }


        int margeExteriorHoritzontalExtra = 150;

        // límit dret
        if (player.getX() > GameInfo.WIDTH + margeExteriorHoritzontalExtra ){

//            GameManager.getInstance().isPaused = true;        // passat a un altre lloc (vid 38, minut 1)

            if (!player.isDead()){
                playerDied();
            }


            if (DEBUG_VERVOSE_MODE){
                System.out.println("Player OUT of bounds -right-");
                System.out.println("cam position y:" + mainCamera.position.y);
                System.out.println("player pos" + player.getY() + GameInfo.HEIGHT/2 + player.getHeight()/2);
            }

        }
        // límit esquerre
        else if (player.getX() < (0 - player.getWidth() - margeExteriorHoritzontalExtra) ){

//            GameManager.getInstance().isPaused = true;        // passat a un altre lloc (vid 38, minut 1)

            if (!player.isDead()){
                playerDied();
            }


            if (DEBUG_VERVOSE_MODE){
                System.out.println("Player OUT of bounds -left-");
                System.out.println("cam position y:" + mainCamera.position.y);
                System.out.println("player pos" + player.getY() + GameInfo.HEIGHT/2 + player.getHeight()/2);
            }

        }


        // **Tots aquests if es podrien agrupar en un sol, amb ORs (com s'explica al vid 36 al minut 11), però prefereixo tenir-ho separat per a poder mostrar en modo debugging els valors del player i la cam


    }



    void countScore(){
        if (lastPlayerY > player.getY()){
            hud.incrementScore(1);
            lastPlayerY = player.getY();
        }
    }



    void playerDied(){

        GameManager.getInstance().isPaused = true;

        //decrement life display
        hud.decrementLife();

        player.setDead(true);

        player.setPosition(-1000, -1000);       // "eliminar" el player de la pantalla



        if (GameManager.getInstance().lifeScore < 0){
            //no queden vides


            //mirar si s'ha aconseguit un highscore
            GameManager.getInstance().checkForNewHighscores();


            //show the end score
            hud.createGameOverPanel();


            // tornar al main menu
            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new MainMenu(game));
                }
            });

            SequenceAction seqActn = new SequenceAction();
            seqActn.addAction(Actions.delay(3f));
            seqActn.addAction(Actions.fadeOut(1f));
            seqActn.addAction(run);

            hud.getStage().addAction(seqActn);


        }
        else {
            // reload game (reiniciar partida)

            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new Gameplay(game));
                }
            });

            SequenceAction seqActn = new SequenceAction();
            seqActn.addAction(Actions.delay(3f));
            seqActn.addAction(Actions.fadeOut(1f));
            seqActn.addAction(run);

            hud.getStage().addAction(seqActn);
        }
    }




    @Override
    public void show() {

    }



    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);    // BGcolor blanc
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        game.getBatch().begin();        //----------------ppi de les ordres de dibuixat

        drawBackgrounds();

        //TODO DELETE (test vid 7)
        /*
        game.getBatch().draw(c, c.getX(), c.getY());
        */

        cloudsController.drawClouds(game.getBatch());
        cloudsController.drawCollectables(game.getBatch());     //+vid 29

        player.drawPlayerIdle(game.getBatch());

        player.drawPlayerAnimation(game.getBatch());

        game.getBatch().end();          //----------------fi de les ordres de dibuixat



        //és el requadre groc al voltant dels núvols/etc (segons com es posiciona malament/no es veu pq queda fora):
//        debugRenderer.render(world, box2DCamera.combined);        // *per a DEBUGGER MODE

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);   // UI hud -vid 25, min 24:10-
        hud.getStage().draw();                                                      // TODO-REM: en afegir aquestes dues línies després de la següent setProjectionMatrix, el joc ja no fa scroll avall. Han d'estar abans de la mainCamera
        hud.getStage().act();

        game.getBatch().setProjectionMatrix(mainCamera.combined);   //configuració intrínseca de la càmera
        mainCamera.update();

        player.updatePlayer();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }



    @Override
    public void resize(int width, int height) {

        gameViewport.update(width, height);
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
    public void dispose() {     //**Apareix al vid 18, min 3:35, com si s'hagués fet abans, no sé quan

        world.dispose();

        for (int i=0; i<bgs.length; i++){
            bgs[i].getTexture().dispose();
        }

        player.getTexture().dispose();

        debugRenderer.dispose();

    }   //implements Screen de com.badlogic.gdx




    // 4 métodos de la interfaz ContactListener:

    @Override
    public void beginContact(Contact contact) {

        Fixture body1, body2;   // el body1 serà sempre el player:

        if (contact.getFixtureA().getUserData() == "Player"){
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        }
        else {
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }


        if (body1.getUserData() == "Player" && body2.getUserData() == "Coin"){
            //collided with the coin
//            System.out.println("collided with COIN");
            body2.setUserData("Remove");
            cloudsController.removeCollectables();
            hud.incrementCoins();
        }
        else if (body1.getUserData() == "Player" && body2.getUserData() == "Life"){
            //collided with the life
//            System.out.println("collided with LIFE");
            body2.setUserData("Remove");
            cloudsController.removeCollectables();
            hud.incrementLifes();
        }


        if (body1.getUserData() == "Player" && body2.getUserData() == "Dark Cloud") {
            //collided with Dark Cloud
//            System.out.println("collided with Dark Cloud");

            if (!player.isDead()){
                playerDied();
            }
        }

    }



    @Override
    public void endContact(Contact contact) {

    }



    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }



    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
