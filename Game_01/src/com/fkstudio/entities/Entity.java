package com.fkstudio.entities;

import com.fkstudio.main.Game;
import com.fkstudio.world.Camera;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
    
    public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6 * 16, 0, 16, 16); // medic quip
    public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7 * 16, 0, 16, 16); // weapon
    public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6 * 16, 16, 16, 16); // bullet
    public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7 * 16, 16, 16, 16); // enemy
    public static BufferedImage ENEMY2_EN = Game.spritesheet.getSprite(7 * 16, 32, 16, 16); // enemy2
    public static BufferedImage BOSS_EN = Game.spritesheet.getSprite(6 * 16, 3 * 16, 32 , 32); // boss
    public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(9*16, 16, 16, 16); // enemy feedback
    public static BufferedImage ENEMY2_FEEDBACK = Game.spritesheet.getSprite(9*16, 32, 16, 16); // enemy feedback
    public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128, 0, 16, 16);  // gun 
    public static BufferedImage GUN_lEFT = Game.spritesheet.getSprite(128+16, 0, 16, 16);
    public static BufferedImage GUN_DOWN = Game.spritesheet.getSprite(1*16, 16, 16, 16);
    public static BufferedImage GUN_RIGHT_FEEDBACK = Game.spritesheet.getSprite(0, 2*16, 16, 16);
    public static BufferedImage GUN_LEFT_FEEDBACK = Game.spritesheet.getSprite(1*16, 2*16, 16, 16);
    
    protected double x;
    protected double y;
    protected int width;
    protected int height;

    private BufferedImage sprite;
    private int maskx, masky, mwidth, mheight;
    
    public Entity(int x, int y, int width, int height, BufferedImage sprite) { // construtor 
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite; 
        
        this.maskx = 0;
        this.masky = 0;
        this.mwidth = width;
        this.mheight = height;
    }
    
    public void setMask(int maskx, int masky, int mwidht, int mheigth){
        this.maskx = maskx;
        this.masky = masky;
        this.mwidth = mwidth;
        this.mheight = mheight;
    }
    
    public void tick(){
        
    }
    
    public double calculateDistance(int x1, int y1, int x2, int y2){
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
    
    public static boolean isColidding(Entity e1,Entity e2){
	Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx,e1.getY()+e1.masky,e1.mwidth,e1.mheight);
	Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx,e2.getY()+e2.masky,e2.mwidth,e2.mheight);
	
	return e1Mask.intersects(e2Mask);
    }
    
    public void render(Graphics g){
        g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
        //g.setColor(Color.red);
        //g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, mheight);
        
    }
    
    // getters
    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // setters
    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    
    
}
