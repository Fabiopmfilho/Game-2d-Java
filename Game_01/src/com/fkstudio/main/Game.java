package com.fkstudio.main;

import com.fkstudio.entities.BulletShoot;
import com.fkstudio.entities.Enemy;
import com.fkstudio.entities.Enemy2;
import com.fkstudio.entities.Enemy_boss;
import com.fkstudio.entities.Player;
import com.fkstudio.graficos.Spritesheet;
import com.fkstudio.entities.Entity;
import com.fkstudio.graficos.UI;
import com.fkstudio.world.Camera;
import com.fkstudio.world.World;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener{
    
    private static final long serialVersionUID = 1L;
    public static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    public static final int WIDTH = 240;  // 720
    public static final int HEIGHT = 160; // 480
    public static final int SCALE = 3;
    
    private int CUR_LEVEL = 1, MAX_LEVEL = 3;
    private BufferedImage image;
    
    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<Enemy2> enemies2;
    public static List<Enemy_boss> boss;
    public static List<BulletShoot> bullets;
    
    public static Spritesheet spritesheet;
    public static World world;  
    public static Player player;
    public static Random rand;
    public UI ui;
    public Menu menu;
    
    public static InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
    public static Font newFont;
    
    public static String gameState = "MENU";
    
    private boolean showMessageGameOver = false;
    
    private int framesGameOver = 0;
    private boolean restartGame = false;
    
    public static int[] pixels;
    
    public boolean saveGame = false;
    
    public Game(){
        //Sound.musicBackground.loop();
        rand = new Random();
        addKeyListener(this);
        addMouseListener(this);
        //setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize())); // full screem
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
        
        // incializando objetos
        ui = new UI();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        enemies2 = new ArrayList<Enemy2>();
        boss = new ArrayList<Enemy_boss>();
        bullets = new ArrayList<BulletShoot>();
        
        spritesheet = new Spritesheet("/spritesheet.png");
        player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
        entities.add(player);
        world = new World("/level1.png");
        
        try {
            newFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(45f);
        } catch (FontFormatException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        menu = new Menu();
        
    }
    
    public void initFrame(){
        frame = new JFrame("Survive: Monsters");
        frame.add(this);
        //frame.setUndecorated(true); // remover barra
        frame.setResizable(false);
        frame.pack();
        //icone da janela
        Image imagem = null;
        try {
            imagem = ImageIO.read(getClass().getResource("/icon_window2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource("/icon.png"));
        Cursor c = toolkit.createCustomCursor(image, new Point(0, 0), "img");
        
        frame.setCursor(c);
        frame.setIconImage(imagem);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    public synchronized void start(){
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }
    
    public synchronized void stop(){
        isRunning=false;
        try {
            thread.join();
        } catch (InterruptedException ex) {
           ex.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        Game game = new Game();
        game.start();
    }

    public void tick(){
        if(gameState == "NORMAL"){
            if(this.saveGame){
                this.saveGame = false;
                String[] opt1 = {"level", "vida"};
                int[] opt2 = {this.CUR_LEVEL, (int)player.life};
                Menu.saveGame(opt1, opt2, 10);
                System.out.println("jogo salvo!");
            }
            this.restartGame = false;
            for(int i = 0; i < entities.size(); i++){
                Entity e = entities.get(i);
                e.tick();
            }

            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).tick();
            }

            // proximo lvl
            if(enemies.size() == 0){
                CUR_LEVEL++;
                if(CUR_LEVEL > MAX_LEVEL){
                    CUR_LEVEL = 1;
                }
                String newWorld = "level" + CUR_LEVEL + ".png";
                World.restartGame(newWorld);
            }
        } else if(gameState == "GAME_OVER"){
            this.framesGameOver++;
            if(this.framesGameOver == 35){
                this.framesGameOver = 0;
                if(this.showMessageGameOver){
                    this.showMessageGameOver = false;
                } else {
                    this.showMessageGameOver = true;
                }
            }
            if(restartGame){
                restartGame = false;
                gameState = "NORMAL";
                CUR_LEVEL = 1;
                String newWorld = "level" + CUR_LEVEL + ".png";
                World.restartGame(newWorld);
            }
        } else if(gameState == "MENU"){
            // MENU
            Sound.menu.loop();
            menu.tick();
        }
        
    }
    
    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(0,0,0));  // background
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Renderização
        //Graphics2D g2 = (Graphics2D) g;
        //World firts
        world.render(g);
        for(int i = 0; i < entities.size(); i++){
            Entity e = entities.get(i);
            e.render(g);
        }
        
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).render(g);
        }
        ui.render(g);
        /***/
        g.dispose();
        g = bs.getDrawGraphics();
        // renderizar full screem
        //g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null);
        // renderizar tela tormal
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        //g.drawString("Munição: " + player.ammo, 600,25);
        g.setFont(newFont);
        g.drawString("Munição: " + player.ammo, 550, 35);
        if(gameState == "GAME_OVER"){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0,0,0,150));
            g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER!", (WIDTH*SCALE)/2 - 167, (HEIGHT*SCALE)/2 -50);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            if(showMessageGameOver){
                g.drawString(">Pressione Enter para reiniciar<", (WIDTH*SCALE)/2 - 230, (HEIGHT*SCALE)/2 + 10);
            }
        } else if(gameState == "MENU") {
            menu.render(g);
        }
        bs.show();
    }
    
    @Override
    public void run() {
        requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        
        while(isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            
            if (delta >= 1){
                tick();
                render();
                frames++;
                delta--;
            }
            if(System.currentTimeMillis() - timer >= 1000){
                //System.out.println("FPS: " + frames);
                frames =0;
                timer += 1000;
            }
        }
        stop();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
            player.right = true;
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
            player.left = true;
        } 
        
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
            player.up = true;
            // menu pra cima
            if(gameState == "MENU"){
                Menu.up = true;
            }
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
            player.down = true;
            //menu pra baixo
            if(gameState == "MENU"){
                Menu.down = true;
            }
        }
        
        if(e.getKeyCode() == KeyEvent.VK_X){
            player.shoot = true;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            this.restartGame = true;
            if(gameState == "MENU") {
                menu.enter = true;
            }
	}
        
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            gameState = "MENU";
            menu.pause = true;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            if (gameState == "NORMAL"){
                this.saveGame = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
            player.right = false;
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
            player.left = false;
        } 
        
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
            player.up = false;
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
            player.down = false;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_X){
            player.shoot = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    
    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.mouseShoot = true;
        player.mx = (e.getX() / 3);
        player.my = (e.getY() / 3);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
    
    }
}
