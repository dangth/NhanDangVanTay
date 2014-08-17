/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author j
 */
public class ImagePanel extends JPanel{
    BufferedImage buffImg;
    
    public ImagePanel() {
        super();
       // setSize(600, 700);
    }
    
    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, 1000, 1000);
        g.drawImage(buffImg, 3, 3,this);
    }
    
    public void updateImage(BufferedImage buffImg) {
        this.buffImg = buffImg;
        repaint();
    }
    
    public BufferedImage getImage() {
        return buffImg;
    }
}
