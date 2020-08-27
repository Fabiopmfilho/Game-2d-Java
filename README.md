# Game-2d-Java
## Jogo simples treinando mecanicas em java.

gif

O jogo funciona basicamente dentro de duas funções:
`tick()` e `render()`

### tick() 
é responsavel por atualizar a todo instante o jogo.

### render()
é responsavel por renderizar todo o gráfico do jogo.

Bibliotecas mais importantes:
1. java.awt.Color
2. java.awt.Graphics
3. java.awt.Graphics2D
4. java.awt.event.KeyListener
5. java.awt.event.MouseListener
6. java.awt.image.DataBuffer
7. java.awt.image.BufferedImage

  imagem-spritesheet

O mapa do jogo é carregado por uma imagem com alguns pixels, e uma classe verifica as cores e corresponde
por uma imagem do spritesheet.

![img mapa](\Game_01\res)

```
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
```
