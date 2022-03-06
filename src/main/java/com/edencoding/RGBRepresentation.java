package com.edencoding;
import java.util.Random;
/**
 * @author Launois Remy / Perier Cyril
 * Project: drag-and-drop
 * Package: com.edencoding
 */
public class RGBRepresentation {
    private int red;
    private int blue;
    private int green;

    public RGBRepresentation(int red, int green,int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public RGBRepresentation() {
        this.generateRGB();
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void generateRGB(){
        Random rand = new Random();
        this.red = rand.nextInt(256);
        this.green = rand.nextInt(256);
        this.blue = rand.nextInt(256);
    }
}
