package player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import helpers.GameInfo;

/**
 * Created by RR on 14/03/18.
 */

public class Player extends Sprite {

    private World world;
    private Body body;

    private TextureAtlas playerAtlas;
    private Animation animation;
    private float elapsedTime;

    private boolean isWalking;



    //Constructor
    public Player(World world, float x, float y) {
        super(new Texture("Player/Player 1.png"));
        this.world = world;
        setPosition(x, y);

        createBody();

        playerAtlas = new TextureAtlas("Player Animation/Player Animation.atlas");
    }


    void createBody(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
                getX()/GameInfo.PPM,
                getY()/GameInfo.PPM
        );

        body = world.createBody(bodyDef);
        body.setFixedRotation(true);    //Per a que el body del player (rectangle vertical), no pugui rotar i es tombi en desplaçar-se



        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                (getWidth()/2 - 20) / GameInfo.PPM,     //reduim una mica l'ample, per "ignorar" els braços, així sembla que l'ample del player son els peus, i no es quedarà flotant als bordes dels núvols
                (getHeight()/2) / GameInfo.PPM
        );

        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.density = 4f;     //mass of the body
        fixtureDef.density = 0f;     //**al vid 30 (min 2:05) apareix 0, quan en el seu moment haviem posat 4)
        fixtureDef.friction = 2f;    //per a que el player no rellisqui
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.PLAYER;
        fixtureDef.filter.maskBits = GameInfo.DEFAUL | GameInfo.COLLECTABLE;    //con qué elementos (los de estas categorías) colisionará el player

        Fixture fixture = body.createFixture(fixtureDef);       //**ATENCIÓ, que aquí no fa new Fixture, sino body.createFixture(...)
        fixture.setUserData("Player");

        shape.dispose();
    }



    public void movePlayer(float x){

        // Per fer un flip horitzontal de la textura (quan el player està parat)
        if (x < 0 && !this.isFlipX()){
            this.flip(true,false);
        }
        else if (x > 0 && this.isFlipX()){
            this.flip(true,false);
        }


        body.setLinearVelocity(x, body.getLinearVelocity().y);
    }



    public void drawPlayerIdle (SpriteBatch batch){

            if (!isWalking){
                batch.draw(this,
                    getX() + getWidth()/2,
                    getY() - getHeight()/2
                );
            }
    }



    public void drawPlayerAnimation (SpriteBatch batch){

        if (isWalking){
            elapsedTime += Gdx.graphics.getDeltaTime();

            Array<TextureAtlas.AtlasRegion> frames = playerAtlas.getRegions();

            for (TextureRegion frame: frames){
                // Per fer un flip horitzontal de la textura (quan el player està en moviment)
                if (body.getLinearVelocity().x < 0 && !frame.isFlipX()){
                    frame.flip(true, false);
                }
                else if (body.getLinearVelocity().x > 0 && frame.isFlipX()){
                    frame.flip(true, false);
                }
            }

            animation = new Animation(1/10f,  playerAtlas.getRegions());   // OJO: si no es posa la f de float, va malament

            batch.draw((TextureRegion) animation.getKeyFrame(elapsedTime, true),      //Al vid 17, apareix algo diferent
                    getX() + getWidth()/2,
                    getY() - getHeight()/2
            );
        }

    }




    public void updatePlayer(){

        if (body.getLinearVelocity().x > 0){    //going right

            setPosition(
                    body.getPosition().x * GameInfo.PPM,
                    body.getPosition().y * GameInfo.PPM
            );
        }
        else if (body.getLinearVelocity().x < 0){   //going left

            setPosition(
                    (body.getPosition().x - 0.3f) * GameInfo.PPM,
                    body.getPosition().y * GameInfo.PPM
            );

        }
    }


    public void setWalking(boolean walking) {
        isWalking = walking;
    }


}