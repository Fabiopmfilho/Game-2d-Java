# Game-2d-Java
## Jogo simples 2d em 16x16 pixels, treinando mecanicas em java.

![Menu inicial](https://github.com/Fabipmfilho/Game-2d-Java/blob/master/Game_01/menu_inicial.png)

![gif do jogo](https://github.com/Fabipmfilho/Game-2d-Java/blob/master/Game_01/Meu%20V%C3%ADdeo1.gif)

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

![Spritesheet](https://github.com/Fabipmfilho/Game-2d-Java/blob/master/Game_01/res/spritesheet.png)

O mapa do jogo é carregado por uma imagem com alguns pixels, e uma classe verifica as cores e correspondentes
por uma imagem do spritesheet.

![Level2](https://github.com/Fabipmfilho/Game-2d-Java/blob/master/Game_01/res/level2.png)

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
O jogo ainda não foi terminado, ainda estou aprendendo e testando novas features!

![gif do jogo](https://github.com/Fabipmfilho/Game-2d-Java/blob/master/Game_01/Meu%20V%C3%ADdeo.gif)

# Obrigado!

----------------------------------------------------------------
## 🦸 Autor
<a href="https://github.com/Fabiopmfilho">
 <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/37713768?s=400&u=5ae234755a07a41c40e5c9ab0b91c3eb26fd42e9&v=4" width="100px;" alt=""/>
 <br />
 <sub><b>Fábio Filho</b></sub></a> <a href="https://github.com/Fabiopmfilho" title="Rocketseat">🚀</a>
 <br />

[![Twitter Badge](https://img.shields.io/badge/-@Kathur_-1ca0f1?style=flat-square&labelColor=1ca0f1&logo=twitter&logoColor=white&link=https://twitter.com/@Kathur_)](https://twitter.com/@Kathur_) [![Linkedin Badge](https://img.shields.io/badge/-Fábio-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/fabiopmfilho/)](https://www.linkedin.com/in/fabiopmfilho/) [![Gmail Badge](https://img.shields.io/badge/-fabiopmfilho@gmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:fabiopmfilho@gmail.com)](mailto:fabiopmfilho@gmail.com) [![Whatsapp Badge](https://img.shields.io/badge/-Whatsapp-4CA143?style=flat-square&labelColor=4CA143&logo=whatsapp&logoColor=white&link=https://api.whatsapp.com/send?phone=5522988498559!)](https://api.whatsapp.com/send?phone=5511999569942!)

---

### 📝 Licença

Este projeto esta sobe a licença [MIT](./LICENSE).

Feito com ❤️ por Fábio Filho 👋🏽 [Entre em contato!](https://www.linkedin.com/in/fabiopmfilho/)
