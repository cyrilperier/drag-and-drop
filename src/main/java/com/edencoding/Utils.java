package com.edencoding;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Launois Remy
 * Project: drag-and-drop
 * Package: com.edencoding
 */
public class Utils {

    /**
     *
     * @param img
     * @return
     */
    public static List<RGBRepresentation> getRGBFromImg(Image img){
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(img, null);
        List<RGBRepresentation> rgbRepresentationList = new ArrayList<>();
        for(int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++)
            {
                int pixel = bufferedImage.getRGB(i,j);
                //Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                //Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                rgbRepresentationList.add(new RGBRepresentation(red,green,blue));
            }
        }

        return rgbRepresentationList;
    }

    /**
     * Method  to return the distance between one pxiel and one cluster
     * @param cluster
     * @param pixel pixel
     * @return double distance
     */
    public static double distanceEuclidean(RGBRepresentation cluster,RGBRepresentation pixel){
        double res = Math.pow(pixel.getBlue() - cluster.getBlue(),2);
        res += Math.pow(pixel.getRed() - cluster.getRed(),2);
        res += Math.pow(pixel.getGreen() - cluster.getGreen(),2);

        return Math.sqrt(res);
    }

    public static double distanceManhattan(RGBRepresentation cluster,RGBRepresentation pixel){
        return 0;
    }

    /**
     * Method for find the belonging of a pixel to a cluster
     * @param clusters
     * @param pixel
     * @return
     */
    public static RGBRepresentation nearestCluster(List<RGBRepresentation> clusters, RGBRepresentation pixel, String method){
        //Initialisation negatif
        double distance = -1;
        RGBRepresentation nearestCluster = null;
        for (RGBRepresentation cluster: clusters
             ) {
            //TODO Passer apr un consummer(fonction en parametre) pour eviter de faire une condition a chaque fois
            double tmp = method.equals("euclidean") ? distanceEuclidean(cluster,pixel) : distanceManhattan(cluster,pixel);
            if(distance < 0 || tmp < distance ){
                distance = tmp;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }

    //TODO passer directement la map
    /**
     *
     * @param listOfCluster
     * @param listOfPixel
     * @return
     */
    public  static Map<RGBRepresentation,List<RGBRepresentation>> getPixelSort(List<RGBRepresentation> listOfCluster, List<RGBRepresentation> listOfPixel){

        return null;

    }
}
