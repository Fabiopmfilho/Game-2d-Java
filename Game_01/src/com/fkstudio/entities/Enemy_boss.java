package com.fkstudio.entities;

import com.fkstudio.main.Game;
import com.fkstudio.main.Sound;
import com.fkstudio.world.Camera;
import com.fkstudio.world.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;


public class Enemy_boss extends Entity{
   
    private double speed = 0.5;
    private int frames = 0, max_frames = 20, index = 0, max_index = 1;
    private int maskx=8, masky=3, maskw=15, maskh=30;  
    private BufferedImage[] sprites;
    
    private int life = 100;
    
    public boolean isDamage = false;
    private int damageFrames = 10, damageCurrent = 0;
    
    public Enemy_boss(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        sprites = new BufferedImage[2];
        sprites[0] = Game.spritesheet.getSprite(6 * 16,3 * 16,32,32);
        sprites[1] = Game.spritesheet.getSprite(8 * 16,3 * 16,32,32);
    }
    
    public void tick(){
         Random rand = new Random();
         
         for (int i = 0; i < 3; i++) {
            x += rand.nextInt(5);
        }
        
//        if(isColiddingWithPlayer() == false){
//             if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())
//                     && !isColidding((int)(x+speed), this.getY())){
//                x+=speed;
//            } else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
//                    && !isColidding((int)(x-speed), this.getY())){
//                x-=speed;
//            }
//        
//            if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))
//                    && !isColidding(this.getX(), (int)(y+speed))){
//                y+=speed;
//            } else if((int)x > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))
//                    && !isColidding(this.getX(), (int)(y-speed))){
//                y-=speed;
//            }
//        } else{
//            // proximo ao player
//            if(Game.rand.nextInt(100) < 10){
//                Sound.hurtEffect.play();
//                Game.player.life-=Game.rand.nextInt(6);
//                Game.player.isDamage = true;    
//            }
//        }
        
        frames++;
            if(frames == max_frames){
                frames = 0;
                index++;
                if(index > max_index){
                    index = 0;
                }
            }
            
        isColiddingBullet();
        if(life <= 0){
            destroySelf();
            return;
        }
        
        if(isDamage){
            this.damageCurrent++;
            if(this.damageCurrent == this.damageFrames){
                this.damageCurrent = 0;
                this.isDamage = false;
                
            }
        }
    }
    
    public void destroySelf(){
        Game.enemies.remove(this);
        Game.entities.remove(this);
    }
    
    public void isColiddingBullet(){
        for (int i = 0; i < Game.bullets.size(); i++) {
            Entity e = Game.bullets.get(i);
            if(e instanceof BulletShoot){
                if(Entity.isColidding(this, e)){
                    Sound.hurtEnemy.play();
                    isDamage = true;
                    life--;
                    Game.bullets.remove(i);
                    return;
                }
            }   
        }
    }
    
    public boolean isColiddingWithPlayer(){
        Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
        Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(),16,16);
        
        return enemyCurrent.intersects(player);
    }
    
    public boolean isColidding(int xnext, int ynext){
        Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
        for (int i = 0; i < Game.boss.size(); i++) {
            Enemy_boss e = Game.boss.get(i);
            if(e == this){
                continue;
            }
            
            Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
            if(enemyCurrent.intersects(targetEnemy)){
                return true;
            }
        }
        
        return false;
    }
    
    public void render(Graphics g){
        if(!isDamage){
            g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        } else{
            g.drawImage(Entity.ENEMY2_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
        
        }
        
         /*** HIT BOX ***/
//        g.setColor(Color.blue);
//        g.fillRect(this.getX() +maskx - Camera.x, this.getY() +masky - Camera.y, maskw, maskh);
    }
    
}
