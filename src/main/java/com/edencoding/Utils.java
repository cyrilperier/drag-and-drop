package com.edencoding;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
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
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        List<RGBRepresentation> rgbRepresentationList = new ArrayList<>();
        for(int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++)
            {
                int pixel = bufferedImage.getRGB(h,w);
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
    public static double distanceEuclidean(Cluster cluster,RGBRepresentation pixel){
        double res = Math.pow(pixel.getBlue() - cluster.getBlue(),2);
        res += Math.pow(pixel.getRed() - cluster.getRed(),2);
        res += Math.pow(pixel.getGreen() - cluster.getGreen(),2);
        //TODO A diviser par 3 ou pas ?
        return Math.sqrt(res);
    }

    public static double distanceManhattan(Cluster cluster,RGBRepresentation pixel){
        return 0;
    }

    /**
     * Method for find the belonging of a pixel to a cluster
     * @param clusters
     * @param pixel
     * @return
     */
    public static Cluster findNearestCluster(List<Cluster> clusters, RGBRepresentation pixel, DistanceMethod method){
        //Initialisation negatif
        double distance = -1;
        Cluster nearestCluster = null;
        for (Cluster cluster: clusters
             ) {
            //TODO Passer apr un consummer(fonction en parametre) pour eviter de faire une condition a chaque fois
            double tmp = method == DistanceMethod.EUCLIDEAN ? distanceEuclidean(cluster,pixel) : distanceManhattan(cluster,pixel);
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
     * @param clusterXPixel
     * @param listOfPixel
     * @return
     */


    /**
     * Create random cluster
     * @param image
     * @param k
     * @return
     */
    public static List<RGBRepresentation> createClusters(BufferedImage image, int k){
        List<RGBRepresentation> listClusters = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            listClusters.add(new RGBRepresentation());
        }
        return listClusters;
    }
}
