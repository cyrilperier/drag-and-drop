package com.edencoding;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Launois Remy / Perier Cyril
 * Project: drag-and-drop
 * Package: com.edencoding
 */
public class Kmeans implements ExecutableAlgorithm {
    private List<RGBRepresentation> listPixels;
    private  List<Cluster> listClusters;

    public Kmeans(List<RGBRepresentation> listPixels, List<Cluster> listClusters) {
        this.listPixels = listPixels;
        this.listClusters = listClusters;
    }

    public List<RGBRepresentation> getListPixels() {
        return listPixels;
    }

    public void setListPixels(List<RGBRepresentation> listPixels) {
        this.listPixels = listPixels;
    }

    public List<Cluster> getListClusters() {
        return listClusters;
    }

    public void setListClusters(List<Cluster> listClusters) {
        this.listClusters = listClusters;
    }

    /**
     * Main method, do execute k-means algorithm
     * @param image Image we want to execute kmeans
     * @param method Method to calculate distance
     * @return new bufferedImage according to clustering
     */
    public BufferedImage executeAlgorithm(BufferedImage image,DistanceMethod method){

        int[] idClusterForEachPixel = new int[this.listPixels.size()];
        Arrays.fill(idClusterForEachPixel,-1);
        boolean pixelChangedCluster;

        do{
            int i = 0;
            //TODO J'ai pas associé tous les pxiels ???
            pixelChangedCluster = associatePixelToCluster(method, idClusterForEachPixel, i);
            meanCLusters();

        }while(pixelChangedCluster); //Stop when pixel stop changing of cluster

        return createImageFromCluster(image,idClusterForEachPixel);


    }

    /**
     * Create new image with the color of cluster;
     * @param bufferedImage Old image, to have width and height
     * @param idClusterForEachPixel Array with cluster associate for all pixel
     * @return new Image, after clustering
     */
    private BufferedImage createImageFromCluster(BufferedImage bufferedImage, int[] idClusterForEachPixel) {
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        BufferedImage result = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                //Get Cluster associate to the pixel
                int idCluster = idClusterForEachPixel[width * h + w];
                //Create color from cluster
                Color pixel = this.listClusters.get(idCluster).getColorCluster();
                //Apply color to the new image
                result.setRGB(w,h,pixel.getRGB());
            }
        }

        return result;
    }



    /**
     * Do the mean on each cluster
     */
    private void meanCLusters() {
        for (Cluster cluster: this.listClusters
             ) {
            cluster.mean();
        }
    }

    /**
     * Associate a pixel to his nearest Cluster
     * @param method Method to calculate distance bewteen cluster and pixel
     * @param idClusterForEachPixel Array of cluster associate to pixel
     * @param i indice of pixel
     * @return if the pixel have change of cluster
     */
    private boolean associatePixelToCluster(DistanceMethod method, int[] idClusterForEachPixel, int i) {
        boolean pixelChangedCluster = false;

        for (RGBRepresentation pixel: this.listPixels
        ) {
            Cluster cluster = Utils.findNearestCluster(this.listClusters,pixel, method);

            if(idClusterForEachPixel[i] != cluster.getIdCluster()){
                this.removePixelIfExist(pixel);
                cluster.addPixel(pixel);
                idClusterForEachPixel[i] = cluster.getIdCluster();
                pixelChangedCluster = true;
            }
            i++;
        }
        return pixelChangedCluster;
    }

    /**
     * Method to find if the pixel is in one of the cluster
     * @param pixel we search in list of clsuter
     * @return Cluster if the pixel is in it, else null
     */
    public Cluster findPixelInCLuster(RGBRepresentation pixel){
        for (Cluster cluster: this.listClusters
             ) {
            if(cluster.containsPixel(pixel)){
                return cluster;
            }
        }
        return null;
    }

    /**
     * Delete pixel in the cluster if he exists
     * @param pixel to remove
     */
    public void removePixelIfExist(RGBRepresentation pixel){
        Cluster cluster = findPixelInCLuster(pixel);
        if(cluster != null) {
            cluster.removePixel(pixel);
        }

    }

}
