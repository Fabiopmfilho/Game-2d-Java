package com.fkstudio.graficos;

import com.fkstudio.entities.Player;
import com.fkstudio.main.Game;
import static com.fkstudio.main.Game.newFont;
import static com.fkstudio.main.Game.stream;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;

public class UI {

    public static InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelft.ttf");
    public static Font newFont2;
    
    public void render(Graphics g){
//        try {
//            newFont2 = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(15f);
//        } catch (FontFormatException ex) {
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        
        g.setColor(Color.red);
        g.fillRect(8, 4, 70, 8);
        g.setColor(Color.green);
        g.fillRect(8, 4,(int)((Game.player.life/Game.player.maxLife)*70), 8);
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 8 ) {
        });
        g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife, 30, 11);
//        g.setFont(newFont2);
//        g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife, 30, 11);
    }
    
}
