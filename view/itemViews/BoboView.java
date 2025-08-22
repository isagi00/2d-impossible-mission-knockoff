package view.itemViews;

import model.inventoryrelated.BoBo;
import model.inventoryrelated.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BoboView extends ItemView{

    private BufferedImage boboimage;


    public BoboView(BoBo bobo) {
        super(bobo);
        loadSprites();
    }


    @Override
    public void loadSprites() {
        try {
            boboimage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/bobo.png"));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        if (boboimage != null){
            g2d.drawImage(boboimage, x, y, width, height, null);
        }
        else{
            g2d.setColor(Color.WHITE);
            g2d.fillRect(x, y, width, height);
        }


    }
}
