package com.fkstudio.entities;

import com.fkstudio.graficos.Spritesheet;
import com.fkstudio.main.Game;
import com.fkstudio.main.Sound;
import com.fkstudio.world.Camera;
import com.fkstudio.world.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity{
    
    public boolean right, left, up, down;
    public int right_dir=0, left_dir=1, up_dir=2, down_dir=3;
    public int dir = down_dir;
    public double speed = 1.3;
    private int maskx=4, masky=3, maskw=8, maskh=10;
    
    private int frames = 0, max_frames = 5, index = 0, max_index = 3;
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage[] upPlayer;
    private BufferedImage[] downPlayer;
    
    private BufferedImage playerDamage;
    
    private boolean hasGun = false;
    
    public int ammo;
    
    public boolean isDamage = false;
    private int damageFrames = 0;
    
    public boolean shoot = false, mouseShoot = false;
    
    public double life = 100, maxLife = 100;
    public int mx, my;
    
    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        
        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        upPlayer = new BufferedImage[4];
        downPlayer = new BufferedImage[4];
        playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
        for(int i = 0; i < 4; i++){
            rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
        }
        for(int i = 0; i < 4; i++){
            leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
        }
        for(int i = 0; i < 4; i++){
            downPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 32, 16, 16);
        }
        for(int i = 0; i < 4; i++){
            upPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 48, 16, 16);
        }
        
    }
    
    public void tick(){
        moved = false;
        if(right && World.isFree(this.getX()+(int)(speed),this.getY())) {
            moved = true;
            dir = right_dir;
            x += speed;
        } else if(left && World.isFree(this.getX()-(int)(speed),this.getY())) {
            moved = true;
            dir = left_dir;
            x -= speed;
        }
        if(up && World.isFree(this.getX(),this.getY()-(int)(speed))) {
            moved = true;
            dir = up_dir;
            y -= speed;
        } else if(down && World.isFree(this.getX(),this.getY()+(int)(speed))) {
            moved = true;
            dir = down_dir;
            y += speed;
        }
        
        if(moved){
            frames++;
            if(frames == max_frames){
                frames = 0;
                index++;
                if(index > max_index){
                    index = 0;
                }
            }
        } 
        
        checkColisionLifePack();
        checkColisionAmmo();
        checkColisionGun();
        
        if(isDamage ){
            this.damageFrames++;
            if(this.damageFrames == 8){
                this.damageFrames = 0;
                isDamage = false;
            }
        }
        
        if(shoot){
            shoot = false;
            if(hasGun && ammo > 0){
                // criar bala e atirar
                ammo--;
                Sound.shoot.play();
                int dx = 0;
                int px = 0;
                int py = 6;
                if (dir == right_dir) {
                    px = 22;
                    dx = 1;
                } else{
                    px = -6;
                    dx = -1;
                }
            
            BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
            Game.bullets.add(bullet);
            }
        }
        
        if(mouseShoot){
            mouseShoot = false;
            if(hasGun && ammo > 0){
                ammo--;
                Sound.shoot.play();
                // criar bala e atirar
                int px = 0, py = 8;
                double angle = 0;
                if (dir == right_dir) {
                    px = 22;
                    angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX()+8 - Camera.x));
                } else if(dir == left_dir){
                    px = -6;
                    angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX()+8 - Camera.x));
                } else if(dir == up_dir){
                    px = +10;
                    angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX()+8 - Camera.x));
                } else{
                    px = +10;
                    angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX()+8 - Camera.x));
                }
                
                
                double dx = Math.cos(angle);
                double dy = Math.sin(angle);
            
            BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
            Game.bullets.add(bullet);
            }
        }
        
        if(Game.player.life <= 0){
            // game over
            life = 0;
            Game.gameState = "GAME_OVER";
        }
        
        updateCamera();
    }
    
    public void updateCamera(){
        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
    }
    
    public void checkColisionLifePack(){
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);
            if (atual instanceof Lifepack) {
                if (Entity.isColidding(this, atual) && life < 100) {
                    life+=50;
                    if (life >= 100) {
                        life = 100;
                    }
                    Game.entities.remove(i);
                    return;
                }
            }
        }
    }
    
    public void checkColisionAmmo(){
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);
            if (atual instanceof Bullet) {
                if (Entity.isColidding(this, atual)) {
                    ammo+=25;
                    Game.entities.remove(atual);
                    return;
                }
            }
        }
    }
    
    public void checkColisionGun(){
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);
            if (atual instanceof Weapon) {
                if (Entity.isColidding(this, atual)) {
                    hasGun = true;
                    Game.entities.remove(atual);
                    return;
                }
            }
        }
    }
    
    public void render(Graphics g){
        if(!isDamage){
            if (dir == right_dir) {
                g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if(hasGun){
                    // arma para direita
                    g.drawImage(Entity.GUN_RIGHT, this.getX() - Camera.x+10, this.getY() - Camera.y+3, null);
                }
            } else if(dir == left_dir){
                g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if(hasGun){
                    // arma para esquerda
                    g.drawImage(Entity.GUN_lEFT, this.getX() - Camera.x-10, this.getY() - Camera.y+3, null);
                }
            } else if(dir == up_dir){
                g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);                
            } else if(dir == down_dir){
                g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if(hasGun){
                    //arma para baixo
                    g.drawImage(Entity.GUN_DOWN, this.getX() - Camera.x, this.getY() - Camera.y+3, null);
                }
            } else{

            }
        } else{
            g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
            if(hasGun) {
		if(dir == left_dir) {
                    g.drawImage(Entity.GUN_LEFT_FEEDBACK, this.getX()-8 - Camera.x,this.getY()+4 - Camera.y, null);
                }else {
                    g.drawImage(Entity.GUN_RIGHT_FEEDBACK, this.getX()+8 - Camera.x,this.getY()+4 - Camera.y, null);
		}
            }
        }
        
//        g.setColor(Color.blue);
//        g.fillRect(this.getX() +maskx - Camera.x, this.getY() +masky - Camera.y, maskw, maskh);
    }
    
}
