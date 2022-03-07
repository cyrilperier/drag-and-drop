package com.edencoding;
import java.util.Objects;
import java.util.Random;

/**
 * @author Launois Remy / Perier Cyril
 * Project: drag-and-drop
 * Package: com.edencoding
 * Class representing RGB attributes
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
        this.blue =  rand.nextInt(256);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RGBRepresentation that = (RGBRepresentation) o;
        return red == that.red && blue == that.blue && green == that.green;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, blue, green);
    }

    @Override
    public String toString() {
        return "{ red = " + red +
                ", blue = " + blue +
                ", green = " + green + "}";
    }
}
