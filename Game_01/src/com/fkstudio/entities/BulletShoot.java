package com.fkstudio.entities;

import com.fkstudio.main.Game;
import com.fkstudio.world.Camera;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BulletShoot extends Entity{

    private double dx, dy;
    private double speed = 4;
    
    private int life = 25, curLife = 0;
    
    public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
        super(x, y, width, height, sprite);
        this.dx = dx;
        this.dy = dy;
    }

    public void tick(){
        x+=dx*speed;
        y+=dy*speed;
        curLife++;
        if(curLife == life){
            Game.bullets.remove(this);
            return;
        }
    }
    
    public void render(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
    }
    
}
