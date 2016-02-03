package com.mygdx.game.desktop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Filip on 2016-01-31.
 */
public class Player implements MovingCharacter{

    private Vector2 Position;
    private Texture image;
    private Facing facing;
    private int animationSpeed;

    private boolean moving;
    private boolean lastLeftFoot;

    private int animationX;
    private int animationY;




    public Player(Vector2 position, Texture texture,Facing facing) {
        Position = position;
        image = texture;
        this.facing = facing;
        animationSpeed = 100;
        animationX = 1;
        animationY = 1;
        moving = false;
        lastLeftFoot = false;

    }

    public void setPosition(Vector2 position) {
        Position = position;
    }

    public Vector2 getPosition(){
        return Position;
    }
    public void setMoving(boolean moving){
        this.moving=moving;
    }

    public Texture getTexture(){
        return image;
    }

    public void setFacing(Facing facing){
        this.facing = facing;
    }

    public void walkingAnimation(){


    }

    @Override
    public void draw(SpriteBatch batch) {
    walkingAnimation();

        switch (this.facing) {
            case NORTH:
                animationX=1;
                break;
            case SOUTH:
                animationX=0;
                break;
            case WEST:
                animationX=2;
                break;
            case EAST:
                animationX=3;
                break;
        }
        if(moving&&System.currentTimeMillis()%animationSpeed==0){
            switch (animationY){
                case 0:
                    if(lastLeftFoot) {
                        animationY = 1;
                        break;
                    }
                    else{
                        animationY=2;
                        break;
                    }

                case 1:
                    animationY=0;
                    lastLeftFoot=false;
                    break;
                case 2:
                    animationY=0;
                    lastLeftFoot=true;
                    break;
            }
        }
        if(!moving)
            animationY=0;

        batch.begin();
        batch.draw(image, this.getPosition().x, this.getPosition().y, (image.getWidth()/4)*animationX, (image.getHeight()/3)*animationY, image.getWidth()/4, image.getHeight()/3);
        batch.end();

    }

    @Override
    public void update(){

    }

    @Override
    public void dispose() {
        image.dispose();

    }
}


