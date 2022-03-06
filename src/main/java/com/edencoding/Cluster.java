package com.edencoding;

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
        this.rgbRepresentation = rgbRepresentation;
        this.pixelAssociate = new ArrayList<>();
    }

    public Cluster(int idCluster, RGBRepresentation rgbRepresentation, List<RGBRepresentation> pixelAssociate) {
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
     * Do the mean of cluster
     * @return
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
        red /= pixelAssociate.size();
        green /= pixelAssociate.size();
        blue /= pixelAssociate.size();
        this.rgbRepresentation = new RGBRepresentation(red,green,blue);
    }


//TODO faire la methode equals et hasCode dans rgbRepresentation
    public boolean containsPixel(RGBRepresentation pixel){
        return pixelAssociate.contains(pixel);
    }


    public void removePixel(RGBRepresentation pixel) {
        this.pixelAssociate.remove(pixel);
    }
}
