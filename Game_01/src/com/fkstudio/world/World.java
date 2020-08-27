package com.fkstudio.world;

import com.fkstudio.entities.Bullet;
import com.fkstudio.entities.Enemy;
import com.fkstudio.entities.Enemy2;
import com.fkstudio.entities.Enemy_boss;
import com.fkstudio.entities.Entity;
import com.fkstudio.entities.Lifepack;
import com.fkstudio.entities.Player;
import com.fkstudio.entities.Weapon;
import com.fkstudio.graficos.Spritesheet;
import com.fkstudio.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class World {

    private static Tile[] tiles;
    public static int WIDTH,HEIGHT;
    public static final int TILE_SIZE = 16;
    
    public World(String path) {
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            tiles = new Tile[map.getWidth() * map.getHeight()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
            for (int xx = 0; xx < map.getWidth(); xx++) {
                for (int yy = 0; yy < map.getHeight(); yy++) {
                    int pixel_atual = pixels[xx + (yy * map.getWidth())]; 
                    tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    if(pixel_atual == 0xFF000000){
                        // Floor (chao)
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    } else if(pixel_atual == 0xFFFFFFFF){
                        // Wall (parede)
                        tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
                    } else if(pixel_atual == 0xFF0026FF){
                        // player
                        Game.player.setX(xx * 16);
                        Game.player.setY(yy * 16);
                    } else if(pixel_atual == 0xFFFF0000){
                        // enemy
                        Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
                        Game.entities.add(en);
                        Game.enemies.add(en);
                        
                    } else if(pixel_atual == 0xFFFF6A00){
                        // weapon
                        Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
                    } else if(pixel_atual == 0xFFFF7F7F){
                        // life pack
//                        Game.entities.add(new Lifepack(xx*16, yy*16, 16, 16, Entity.LIFEPACK_EN));
                        Lifepack pack = new Lifepack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN);
                        pack.setMask(8, 8, 8, 8);
                        Game.entities.add(pack);
                    }else if(pixel_atual == 0xFFFFD800){
                        // bullet
                        Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
                    }else if(pixel_atual == 0xFF4800FF){
                        // enemy
                        Enemy2 ene = new Enemy2(xx * 16, yy * 16, 16, 16, Entity.ENEMY2_EN);
                        Game.entities.add(ene);
                        Game.enemies2.add(ene);
                    } else if(pixel_atual == 0xFFFF72FA){
                        // boss
                        Enemy_boss boss = new Enemy_boss(xx * 16, yy * 16, 32, 32, Entity.BOSS_EN);
                        Game.entities.add(boss);
                        Game.boss.add(boss);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean isFree(int xnext, int ynext){
        int x1 = xnext / TILE_SIZE;
        int y1 = ynext / TILE_SIZE;
        
        int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
        int y2 = ynext / TILE_SIZE;
        
        int x3 = xnext / TILE_SIZE;
        int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
        
        int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
        int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
        
        return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
                (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
                (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
                (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
   }
    
    public static void restartGame(String level){
        Game.entities = new ArrayList<Entity>();
        Game.enemies = new ArrayList<Enemy>();
        Game.enemies2 = new ArrayList<Enemy2>();
        Game.spritesheet = new Spritesheet("/spritesheet.png");
        Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
        Game.entities.add(Game.player);
        Game.world = new World("/" + level);
        return;
    }
    
    public void render(Graphics g){
        int xstart = Camera.x >> 4;
        int ystart = Camera.y >> 4;
        
        int xfinal = xstart + (Game.WIDTH / 16);
        int yfinal = ystart + (Game.HEIGHT / 16);
        
        for (int xx = xstart; xx <= xfinal; xx++) {
            for (int yy = ystart; yy <= yfinal; yy++) {
                if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT){
                    continue; 
                }
                Tile tile = tiles[xx + (yy * WIDTH)];
                tile.render(g);
            }
        }
    }
    
}
