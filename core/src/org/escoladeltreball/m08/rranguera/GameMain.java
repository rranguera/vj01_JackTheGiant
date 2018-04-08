package org.escoladeltreball.m08.rranguera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import helpers.GameManager;
import scenes.Gameplay;
import scenes.MainMenu;

public class GameMain extends Game {

	SpriteBatch batch;
	Texture img;



	@Override
	public void create () {
		batch = new SpriteBatch();
//		img = new Texture("Backgrounds/Game BG.png");

        // creem (o carreguem) el fitxer de configuració
        GameManager.getInstance().initializeGameData();

//		setScreen(new Gameplay(this));		//Esta línia comença directament en el joc, sense menú previ
        setScreen(new MainMenu(this));
	}



	@Override
	public void render () {
/*
//passades a java/scenes/Gameplay
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
*/

/*
//no calen
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
*/

        super.render();
	}
	
/*
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
*/


	public SpriteBatch getBatch(){
		return this.batch;
	}

}
