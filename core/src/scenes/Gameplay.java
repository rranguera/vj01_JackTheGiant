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
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.escoladeltreball.m08.rranguera.GameMain;

import clouds.CloudsController;
import helpers.GameInfo;
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


    //TODO DELETE this later, it's just a tester (vid 7)
//    Cloud c;


    private UIHud hud;  // vid 25

    private CloudsController cloudsController;

    private Player player;








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



    void update(float dt){
        handleInput(dt);
        moveCamera();
        checkBackgroundsOutOfBounds();
        cloudsController.setCameraYPos(mainCamera.position.y);
        cloudsController.createAndArrangeNewClouds();
    }



    void moveCamera() {
        //velocitat de desplaçament vertical de la càmera/joc:
        mainCamera.position.y -= 3;
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





    @Override
    public void show() {

    }



    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(1, 0, 1, 1);
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
        debugRenderer.render(world, box2DCamera.combined);

        game.getBatch().setProjectionMatrix(mainCamera.combined);   //configuració intrínseca de la càmera
        mainCamera.update();

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);   // UI hud -vid 25, min 24:10-
        hud.getStage().draw();                                                      // TODO-REM: en afegir aquestes dues línies, el joc ja no fa scroll avall

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
