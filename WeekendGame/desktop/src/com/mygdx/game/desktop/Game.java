package com.mygdx.game.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BooleanArray;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Filip on 2016-01-31.
 */
public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture grass;
    private Texture rock;
    private Texture rockObject;
    private Player player1;
    private Vector2 camera;
    private HashMap<Integer,HashMap<Integer,Terrain>> tileMap;
    private  HashMap<Integer,HashMap<Integer,TerrainObjects>> objectMap;
    private  Random random;

    private int screenWidth;
    private int screenHeight;
    private int tileSize;


    private int stepsForATile;
    private boolean notMoving;
    private Vector2 destination;

    private int totalTiles;

    public Game(Integer screenHeight,Integer screenWidth) {
        this.screenHeight=screenHeight;
        this.screenWidth= screenWidth;


    }

    @Override
    public void create () {
        batch = new SpriteBatch();
        grass  = new Texture("TERRAINGRASS.png");
        rock = new Texture("TERRAINROCK.png");
        rockObject = new Texture("TerrainObjectRock.png");
        player1 = new Player(new Vector2(screenWidth/2,screenHeight/2),new Texture("ScientistAnimation.png"),Facing.SOUTH);
        tileMap = new HashMap<Integer,HashMap<Integer,Terrain>>();
        objectMap = new HashMap<Integer,HashMap<Integer,TerrainObjects>>();
        camera = new Vector2(0,0);
        random = new Random();
        tileSize = 16;

        notMoving=true;
        stepsForATile = 2;
        destination = camera;



        totalTiles = 0;

    }

    @Override
    public void render () {

        int renderDistanceY = (screenHeight / tileSize) + 2;
        int renderDistanceX = (screenWidth / tileSize) + 2;

        //dealing with playeranimation and moving of camera
        player1.setMoving(false);


        if(notMoving) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                destination = destination.add(0, tileSize);
                player1.setMoving(true);
                player1.setFacing(Facing.NORTH);
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                destination = destination.add(0, -tileSize);
                player1.setMoving(true);
                player1.setFacing(Facing.SOUTH);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                destination = destination.add(tileSize, 0);
                player1.setMoving(true);
                player1.setFacing(Facing.EAST);
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                destination = destination.add(-tileSize, 0);
                player1.setMoving(true);
                player1.setFacing(Facing.WEST);
            }
        }
        movingTowards();


        generateTerrainTileMap(renderDistanceY, renderDistanceX); //tileMap
        generateObjectMap(renderDistanceY,renderDistanceX); //objectMap

        Gdx.gl.glClearColor(0, 204, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draws
        //y-cordinate
        for (int i = 0; i < renderDistanceY; i++) {
            //x-cordinate
            for (int j = 0; j < renderDistanceX; j++) {
                    switch (tileMap.get(i + ((int) (Math.floor(camera.y / tileSize)))).get(j + ((int) (Math.floor(camera.x / tileSize))))) {

                        case GRASS:
                            batch.begin();
                            batch.draw(grass, (j * tileSize) - Math.abs((camera.x % tileSize)), (i * tileSize) - Math.abs((camera.y) % tileSize));
                            batch.end();
                            break;
                        case ROCK:
                            batch.begin();
                            batch.draw(rock, (j * tileSize) - Math.abs((camera.x % tileSize)), (i * tileSize) - Math.abs((camera.y) % tileSize));
                            batch.end();
                            break;
                        case WATER:
                            break;
                    }

                switch (objectMap.get(i + ((int) (Math.floor(camera.y / tileSize)))).get(j + ((int) (Math.floor(camera.x / tileSize))))) {

                    case ROCK:
                        batch.begin();
                        batch.draw(rockObject, (j * tileSize) - Math.abs((camera.x % tileSize)), (i * tileSize) - Math.abs((camera.y) % tileSize));
                        batch.end();
                        break;
                    case NONE:
                        break;
                    case TREE:
                        break;
                }
            }

            player1.draw(batch);

            System.out.println(camera);
            System.out.print(totalTiles);

        }
    }


    public void generateTerrainTileMap(Integer renderDistanceY,Integer renderDistanceX){

        for (int i =(((int) (Math.floor(camera.y / tileSize))));i<renderDistanceY+(int) (Math.floor(camera.y / tileSize));i++) {
            if (!(tileMap.containsKey(i))) {
                tileMap.put( i, new HashMap<Integer, Terrain>());
            }

            for (int j=(((int) (Math.floor(camera.x / tileSize))));j<renderDistanceX+(int) (Math.floor(camera.x / tileSize));j++) {
                if (!(tileMap.get(i).containsKey(j))) {

                    int randomizer = random.nextInt(6);

                    if (randomizer < 5) {
                        tileMap.get(i).put(j, Terrain.GRASS);
                        totalTiles++;
                    }
                    if (randomizer >= 5) {
                        tileMap.get(i).put(j, Terrain.ROCK);
                        totalTiles++;
                    }
                }
            }
        }
    }

    public void generateObjectMap(Integer renderDistanceY,Integer renderDistanceX) {

        for (int i = ((int) (Math.floor(camera.y / 16))); i < renderDistanceY + (int) (Math.floor(camera.y / 16)); i++) {
            if (!(objectMap.containsKey(i))) {
                objectMap.put(i, new HashMap<Integer, TerrainObjects>());
            }
            for (int j = ((int) (Math.floor(camera.x / 16))); j < renderDistanceX + (int) (Math.floor(camera.x / 16)); j++) {
                if (!(objectMap.get(i).containsKey(j))) {
                    if (tileMap.get(i).get(j) == Terrain.ROCK) {
                        int randomizer = random.nextInt(8);

                        if (randomizer == 7) {
                            objectMap.get(i).put(j, TerrainObjects.ROCK);

                        }
                        if (!(randomizer == 7)) {
                            objectMap.get(i).put(j, TerrainObjects.NONE);

                        }
                    }
                    else
                        objectMap.get(i).put(j, TerrainObjects.NONE);
                }
            }
        }
    }



    public void movingTowards(){

        if(camera.x!=destination.x){
            camera.x =+(tileSize/stepsForATile);
            notMoving=false;
        }
        if(camera.y!=destination.y){
            camera.y =+(tileSize/stepsForATile);
            notMoving=false;
        }
        if(camera==destination) {
            notMoving = true;
        }
    }
    public void dispose(){
        batch.dispose();
        grass.dispose();
        rock.dispose();

    }
}

