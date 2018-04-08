package clouds;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

import collectables.Collectable;
import helpers.GameInfo;
import helpers.GameManager;
import player.Player;

/**
 * Created by RR on 12/03/18.
 */

public class CloudsController {


    private World world;

    private Array<Cloud> clouds = new Array<Cloud>();
    private Array<Collectable> collectables = new Array<Collectable>();

    private final float DISTANCE_BETWEEN_CLOUDS = 250f;
    private float minX, maxX;

    private Random random = new Random();
    private float cameraYPos;
    private float lastCloudPositionY;


    //Constructor
    public CloudsController(World world) {
        this.world = world;

        minX = GameInfo.WIDTH/2 - 150;
        maxX = GameInfo.WIDTH/2 + 150;

        createClouds();
        positionClouds(true);
    }



    private void createClouds() {

        //Volem 2 núvols dolents, i 6 bons. Els afegim a l'array "clouds":

        for (int i=0; i<2; i++){
            clouds.add(new Cloud(world, "Dark Cloud"));
        }


        int index = 1;

        for (int i=0; i<6; i++){
            clouds.add(new Cloud(world, "Cloud " + index));

            index++;
            if (index>3) index = 1;     //Tenim clouds de 1 a 3 (els .png)
        }


        //I ara, anem a mesclar els núvols que hem afegit a l'array clouds:

        clouds.shuffle();


        //Detall final: no volem que el primer núvol siqui el que mata al player:
            //REM: al vid 10 (10:50), ell posa el while a positionClouds(), però crec que aquí té més sentit

//        while (clouds.get(0).getCloudName() == "Dark Cloud"){     //ERROR: fa un == per comparar strings; de vegades pot donar problemes
        while (clouds.get(0).getCloudName().equals("Dark Cloud")){
            clouds.shuffle();
        }
    }



    public void positionClouds(boolean firstTimeArranging){

        float positionY;
        if (firstTimeArranging){
            positionY = GameInfo.HEIGHT/2f;
        }
        else {
            positionY = lastCloudPositionY;
        }


        int controlX = 0;

        for (Cloud c: clouds) {

            if (c.getX()==0 && c.getY()==0){    // Si la nuve es nueva (no es de las que ya teníamos antes, que ya había sido posicionada), la posicionamos:

                float tempX = 0;
                int grauDeDescentrat = 60;  //Canviant aquest valor, els núvols se n'aniran més cap als marges de la pantalla, o estaran més propers al centre

                if (controlX == 0){
                    tempX = randomBetweenNumbers(maxX-grauDeDescentrat, maxX);
                    controlX = 1;
                    c.setDrawLeft(false);
                }
                else if (controlX == 1){
                    tempX = randomBetweenNumbers(minX+grauDeDescentrat, minX);
                    controlX = 0;
                    c.setDrawLeft(true);
                }

                c.setSpritePosition(tempX, positionY);

                positionY -= DISTANCE_BETWEEN_CLOUDS;
                lastCloudPositionY = positionY;


                // Collectables:
                if (!firstTimeArranging && c.getCloudName()!="Dark Cloud"){

                    int rand = random.nextInt(10);  //0-9

                    if (rand > 4) {     //Quan més baix sigui el número, més ítems collectables apareixeran

                        int randomCollectable = random.nextInt(3);  //0-2

                        if (randomCollectable == 0){
                            //spawn a life... si el contador de vidas es <2

                            if (GameManager.getInstance().lifeScore < 2){
                                Collectable collectable = new Collectable(world, "Life");
                                collectable.setCollectablePosition(
                                        c.getX(),
                                        c.getY() + 40
                                );
                                collectables.add(collectable);
                            }
                            else {
                                //spawn a coin instead a life
                                Collectable collectable = new Collectable(world, "Coin");
                                collectable.setCollectablePosition(
                                        c.getX(),
                                        c.getY() + 40
                                );
                                collectables.add(collectable);
                            }

                        }
                        else {
                            //spawn a coin
                            Collectable collectable = new Collectable(world, "Coin");
                            collectable.setCollectablePosition(
                                    c.getX(),
                                    c.getY() + 40
                            );
                            collectables.add(collectable);
                        }
                    }
                }


            }


        }


/*
        // Bloc per a testejar (situa un ítem al 2n núvol):

        Collectable collectable = new Collectable(world, "Life");
        collectable.setCollectablePosition(
                clouds.get(1).getX(),
                clouds.get(1).getY() + 40
        );
        collectables.add(collectable);
*/

    }



    public void drawClouds(SpriteBatch batch){

        for (Cloud c: clouds) {
            if (c.isDrawLeft()){
                batch.draw(c,
                        c.getX() - c.getWidth()/2 - 20,
                        c.getY() - c.getHeight()/2);
            }
            else {
                batch.draw(c,
                        c.getX() - c.getWidth()/2 + 10,
                        c.getY() - c.getHeight()/2);
            }
        }
    }



    public void drawCollectables(SpriteBatch batch){

        for (Collectable c : collectables){
            c.updateCollectable();
            batch.draw(c, c.getX(), c.getY());
        }
    }



    public void removeCollectables(){

        for (int i=0; i<collectables.size; i++){
            if (collectables.get(i).getFixture().getUserData() == "Remove"){
                collectables.get(i).changeFilter();                             // **??
                collectables.get(i).getTexture().dispose();
                collectables.removeIndex(i);
            }
        }
    }



    public void createAndArrangeNewClouds(){

        for (int i=0; i<clouds.size; i++){
            if ( (clouds.get(i).getY() - GameInfo.HEIGHT/2 - 15) > cameraYPos ){
                //cloud is out of bounds --> remove it
                clouds.get(i).getTexture().dispose();
                clouds.removeIndex(i);
            }
        }


        if (clouds.size == 4){
            createClouds();



            positionClouds(false);
        }
    }



    public void removeOffscreenCollectables(){

        for (int i=0; i<collectables.size; i++){

            if ((collectables.get(i).getY() - GameInfo.HEIGHT/2f - 15) > cameraYPos){
                collectables.get(i).getTexture().dispose();
                clouds.removeIndex(i);
            }
        }
    }



    public void setCameraYPos (float cameraYPos){
        this.cameraYPos = cameraYPos;
    }



    public Player positionThePlayerAtStart(Player player){
        player = new Player(world,
                clouds.get(0).getX(),
                clouds.get(0).getY() + 78
        );

        return player;
    }


    private float randomBetweenNumbers(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }


}
