package com.edencoding;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Launois Remy / Perier Cyril
 * Project: drag-and-drop
 * Package: com.edencoding
 */
public class Cluster {
    private int idCluster;
    private RGBRepresentation rgbRepresentation;
    private List<RGBRepresentation> pixelAssociate;

    public Cluster(int idCluster, RGBRepresentation rgbRepresentation) {
        this.idCluster = idCluster;
        this.rgbRepresentation = rgbRepresentation;
        this.pixelAssociate = new ArrayList<>();
    }
    public Cluster(RGBRepresentation rgbRepresentation) {
        this.idCluster = Integer.MIN_VALUE;
        this.rgbRepresentation = rgbRepresentation;
        this.pixelAssociate = new ArrayList<>();
    }
    public Cluster(int idCluster, RGBRepresentation rgbRepresentation, List<RGBRepresentation> pixelAssociate) {
        this.idCluster = idCluster;
        this.rgbRepresentation = rgbRepresentation;
        this.pixelAssociate = pixelAssociate;
    }

    public int getIdCluster() {
        return idCluster;
    }

    public void setIdCluster(int idCluster) {
        this.idCluster = idCluster;
    }

    public RGBRepresentation getRgbRepresentation() {
        return rgbRepresentation;
    }

    public void setRgbRepresentation(RGBRepresentation rgbRepresentation) {
        this.rgbRepresentation = rgbRepresentation;
    }

    public List<RGBRepresentation> getPixelAssociate() {
        return pixelAssociate;
    }
    public void setPixelAssociate(List<RGBRepresentation> pixelAssociate) {
        this.pixelAssociate = pixelAssociate;
    }

    public void addPixel(RGBRepresentation pixel){
        this.pixelAssociate.add(pixel);
    }
    public void addAllPixels(List<RGBRepresentation> pixels){
        this.pixelAssociate.addAll(pixels);
    }
    public int getRed() {
        return this.rgbRepresentation.getRed();
    }

    public void setRed(int red) {
        this.rgbRepresentation.setRed(red);
    }

    public int getBlue() {
        return this.rgbRepresentation.getBlue();
    }

    public void setBlue(int blue) {
        this.rgbRepresentation.setBlue(blue);
    }

    public int getGreen() {
        return this.rgbRepresentation.getGreen();
    }

    public void setGreen(int green) {
        this.rgbRepresentation.setGreen(green);
    }
    /**
     * Do the mean of cluster and apply it
     */
    public void mean(){
        int red = 0;
        int green = 0;
        int blue = 0;

        for (RGBRepresentation pixel: pixelAssociate
             ) {
            red += pixel.getRed();
            green += pixel.getGreen();
            blue += pixel.getBlue();
        }
        if (pixelAssociate.size() != 0){
            red /= pixelAssociate.size();
            green /= pixelAssociate.size();
            blue /= pixelAssociate.size();
        }
        this.rgbRepresentation = new RGBRepresentation(red,green,blue);
    }


    public boolean containsPixel(RGBRepresentation pixel){
        return pixelAssociate.contains(pixel);
    }


    public void removePixel(RGBRepresentation pixel) {
        this.pixelAssociate.remove(pixel);
    }

    public Color getColorCluster() {
        int red = this.getRed();
        int green = this.getGreen();
        int blue = this.getBlue();
        return new Color(red,green,blue);
    }

    @Override
    public String toString() {
        return "idCluster = " + idCluster +
                ", rgbRepresentation = " + rgbRepresentation +
                ", pixelAssociate=" + pixelAssociate ;
    }
}


