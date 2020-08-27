package com.fkstudio.main;

import com.fkstudio.world.World;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Menu {

    private BufferedImage menu;
    private Image image;
    
    
    public String[] options = {"Novo Jogo", "Carregar Jogo", "Sair"};
    
    public int currentOptions = 0;
    public int maxOptions = options.length - 1;
    public static boolean up, down, enter;
    public static boolean pause = false;
    public static boolean saveExist = false;
    public static boolean saveGame = false;
    
    public void tick(){
        File file = new File("save.txt");
        
        if(file.exists()){
            saveExist = true;
        } else{
            saveExist = false;
        }
        if(up){
            up = false;
            currentOptions--;
            if(currentOptions < 0){
                currentOptions = maxOptions;
            }
        }
        if(down){
            down = false;
            currentOptions++;
            if(currentOptions > maxOptions){
                currentOptions = 0; 
            }
        }
        if(enter){
            Sound.menu.stop();
            Sound.music.loop();
            enter = false;
            if(options[currentOptions] == "Novo Jogo" || options[currentOptions] == "Continuar"){
                Game.gameState = "NORMAL";
                pause = false;
            } else if(options[currentOptions] == "Carregar Jogo"){
                file = new File("save.txt");
                if(file.exists()){
                    String saver = loadGame(10);
                    applySave(saver);
                }
            } else if(options[currentOptions] == "Sair"){
                System.exit(1);
            }
        } 
    }
    
    public static void applySave(String str){
        String[] spl = str.split("/");
        for (int i = 0; i < spl.length; i++) {
            String[] spl2 = spl[i].split(":");
            switch(spl2[0]){
                case "level":
                    World.restartGame("level" + spl2[1] + ".png");
                    Game.gameState = "NORMAL";
                    pause = false;
                    break;
                case "vida":
                    Game.player.life = Integer.parseInt(spl2[1]);
                    break;
            }
        }
    }
    
    public static String loadGame(int encode){
        String line = "";
        File file = new File("save.txt");
        if(file.exists()){
            try {
                String singleLine = null;
                BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
                try {
                    while((singleLine = reader.readLine()) != null){
                        String[] transition = singleLine.split(":");
//                        if (transition[0] == "level") {
                            // carrega o jogo com base do valor de transição 1
                            char[] val = transition[1].toCharArray();
                            transition[1] = "";
                            for (int i = 0; i < val.length; i++) {
                                val[i] -= encode; // descriptografando
                                transition[1] += val[i];
                            }
                            line += transition[0];
                            line += ":";
                            line += transition[1];
                            line += "/";
                        //}
                    }
                } catch (IOException e) {}
            } catch (FileNotFoundException e) {}
        }
        return line;
    }
    
    public static void saveGame(String[] val1, int[] val2, int encode){
        BufferedWriter write = null;
        try {
            write = new BufferedWriter(new FileWriter("save.txt"));
        } catch (IOException e) {}
    
        for (int i = 0; i < val1.length; i++) {
            String current = val1[i];
            //level:[i]
            current += ":";
            char[] value = Integer.toString(val2[i]).toCharArray();
            for (int n = 0; n < value.length; n++) {
                value[n]+=encode; // encriptografando
                current += value[n];
            }
            try {
                write.write(current);
                if(i < val1.length - 1)
                    write.newLine();
            } catch (IOException e) {}
        }
    
        try {
            write.flush();
            write.close();
        } catch (IOException e) {}
        
    }
    
    public void render(Graphics g){
        //g.setColor(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0,0,0,100));
        
        try{
            menu = ImageIO.read(getClass().getResourceAsStream("/menu.gif"));
        }catch(IOException e){
                e.printStackTrace();
        }
//        URL url = getClass().getResource("menu.gif");
//        Image image = new ImageIcon(url).getImage();
        g.drawImage(menu, 0,0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE, null);
//       
        g.fillRect(0, 0, Game.WIDTH* Game.SCALE, Game.HEIGHT*Game.SCALE);
        g.setColor(Color.RED);
        g.setFont(new Font("arial", Font.BOLD, 36));
        g.drawString(">Survive: Monsters<", Game.WIDTH * Game.SCALE / 2 - 160, Game.HEIGHT * Game.SCALE / 2 - 160);
        
        // menu
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 24));
        if(pause == false){
            g.drawString("Novo Jogo", Game.WIDTH * Game.SCALE / 2 - 50, 160);
            g.drawString("Carregar Jogo", Game.WIDTH * Game.SCALE / 2 - 70, 200);
            g.drawString("Sair", Game.WIDTH * Game.SCALE / 2 - 10, 240);
        }else{    
            g.drawString("Resumir", Game.WIDTH * Game.SCALE / 2 - 40, 160);
            g.drawString("Carregar Jogo", Game.WIDTH * Game.SCALE / 2 - 70, 200);
            g.drawString("Sair", Game.WIDTH * Game.SCALE / 2 - 10, 240);
        }
        
        if(options[currentOptions] == "Novo Jogo"){
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 90, 160);
        } else if(options[currentOptions] == "Carregar Jogo") {
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 90, 200);
        } else if(options[currentOptions] == "Sair"){
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 40, 240);
        }
    }
    
}
