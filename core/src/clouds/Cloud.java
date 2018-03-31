package clouds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

/**
 * Created by iam47623980 on 7/03/18.
 */

public class Cloud extends Sprite {

    private World world;
    private Body body;
    private String cloudName;

    private boolean drawLeft;




    //Constructor
    public Cloud(World world, String cloudName) {

        super(new Texture("Clouds/" + cloudName + ".png"));     //crida al constructor del super, és a dir, de Sprite

        this.world = world;
        this.cloudName = cloudName;
    }



    void createBody(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(
                (getX() - 45) / GameInfo.PPM,
                getY() / GameInfo.PPM
/*
                (getX() + getWidth()/2) / GameInfo.PPM,
                (getY() + getHeight()/2) / GameInfo.PPM
*/
        );

        body = world.createBody(bodyDef);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                (getWidth()/2 - 25) / GameInfo.PPM,
                (getHeight()/2 - 10) / GameInfo.PPM
        );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();    //La shape creada 4 línies més amunt, ja l'hem utilitzat en la línia superior, i com q ja tenim la fixture, ja no ens cal més la shape
    }



    public void setSpritePosition(float x, float y){

        setPosition(x, y);

        createBody();
    }



    public String getCloudName() {
        return this.cloudName;
    }


    public boolean isDrawLeft() {
        return drawLeft;
    }

    public void setDrawLeft(boolean drawLeft) {
        this.drawLeft = drawLeft;
    }
}
