package com.edencoding;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Launois Remy / Perier Cyril
 * Project: drag-and-drop
 * Package: com.edencoding
 */
public class Utils {

    /**
     * Method to get the list of pixel, {@link RGBRepresentation}  from the image
     * @param bufferedImage Image we want to apply alorithm
     * @return list of pixel, form RGB
     */
    public static List<RGBRepresentation> getRGBFromImg(BufferedImage bufferedImage){
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        List<RGBRepresentation> rgbRepresentationList = new ArrayList<>();
        for(int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++)
            {
                int pixel = bufferedImage.getRGB(w,h);
                //Creating a Color object from pixel value
                Color color = new Color(pixel, true);

                //Retrieving the RGB values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                rgbRepresentationList.add(new RGBRepresentation(red,green,blue,width * h + w));
            }
        }
        return rgbRepresentationList;
    }

    /**
     * Create Buffered image from Image JavaFX
     * @param image Image JavaFX
     * @return BufferedImage Java awt
     */
    public static BufferedImage transformImageToBufferedImage(Image image){
        return SwingFXUtils.fromFXImage(image, null);
    }

    /**
     * Create new image with the color of cluster;
     * @param bufferedImage Old image, to have width and height
     * @param idClusterForEachPixel Array with cluster associate for all pixel
     * @return new Image, after clustering
     */
    public static BufferedImage createImageFromCluster(BufferedImage bufferedImage, int[] idClusterForEachPixel, List<Cluster> listClusters) {
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        BufferedImage result = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                //Get Cluster associate to the pixel
                int idCluster = idClusterForEachPixel[width * h + w];
                //Create color from cluster
                Color pixel = new Color(0,0,0);
                if(idCluster != -1){
                    pixel = listClusters.get(idCluster).getColorCluster();
                }
                //Apply color to the new image
                result.setRGB(w,h,pixel.getRGB());
            }
        }

        return result;
    }

    /**
     * Method  to return the euclidean distance between one pixel and one cluster
     * @param cluster CLuster from which we want to calculate distance
     * @param pixel pixel from which we want to calculate distance
     * @return double distance
     */
    public static double distanceEuclidean(Cluster cluster,RGBRepresentation pixel){
        double res = Math.pow(pixel.getBlue() - cluster.getBlue(),2);
        res += Math.pow(pixel.getRed() - cluster.getRed(),2);
        res += Math.pow(pixel.getGreen() - cluster.getGreen(),2);
        return Math.sqrt(res);
    }

    /**
     * Method  to return the manhattan distance between one pixel and one cluster
     * @param cluster CLuster from which we want to calculate distance
     * @param pixel pixel from which we want to calculate distance
     * @return double distance
     */
    public static double distanceManhattan(Cluster cluster,RGBRepresentation pixel){
        double res = Math.abs(pixel.getBlue() - cluster.getBlue());
        res += Math.abs(pixel.getRed() - cluster.getRed());
        res += Math.abs(pixel.getGreen() - cluster.getGreen());
        return res;
    }

    /**
     * Method for find the belonging of a pixel to a cluster
     * @param clusters List of cluster
     * @param pixel The RGBRepresentation we want to find the cluster
     * @param method Method to calculate distance, if null or something else than euclidean it will be manhattan //TODO Gestion des erreus
     * @return the nearest cluster of pixel
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

    /**
     * Create random cluster
     * @param k number of cluster we want
     * @return List of Random {@link  Cluster}
     */
    public static List<Cluster> createClusters(int k){
        List<Cluster> listClusters = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            RGBRepresentation rgbRepresentationAlea = new RGBRepresentation();
            listClusters.add(new Cluster(i,rgbRepresentationAlea));
        }
        return listClusters;
    }

    /**
     * Create file image from a bufferedImage with a name of file
     * @param filename Image name
     * @param bufferedImage Image we want to save
     */
    public static void createImage(String filename, BufferedImage bufferedImage, Algorithm algorithm, DistanceMethod distanceMethod,String k) {
        File file = new File("src/output/"+filename+"_"+algorithm+"_"+k+"_"+distanceMethod+".png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
