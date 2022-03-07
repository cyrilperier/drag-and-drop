package com.edencoding;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Launois Remy
 * Project: drag-and-drop
 * Package: com.edencoding
 */
public class Kmeans {
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

    public BufferedImage doKmeans(BufferedImage image, int k, DistanceMethod method){

        int[] idClusterForEachPixel = new int[this.listPixels.size()];
        Arrays.fill(idClusterForEachPixel,-1);
        boolean pixelChangedCluster = false;
        do{
            int i = 0;
            //TODO J'ai pas associé tout=s les pxiels ???
            pixelChangedCluster = associatePixelToCluster(method, idClusterForEachPixel, i);
            meanCLusters();

        }while(pixelChangedCluster);

        return createImageFromCluster(image,idClusterForEachPixel);


    }

    /**
     * Create new image with the color of cluster;
     * @param image
     * @param idClusterForEachPixel
     * @return
     */
    private BufferedImage createImageFromCluster(BufferedImage image, int[] idClusterForEachPixel) {
        int height = image.getHeight();
        int width = image.getWidth();
        List<Color> test = new ArrayList<>();
        BufferedImage result = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int idCluster = idClusterForEachPixel[width * h + w];
                Cluster cluster = this.listClusters.get(idCluster);
                int red = cluster.getRed();
                int green = cluster.getGreen();
                int blue = cluster.getBlue();
                Color pixel = new Color(red,green,blue);
                test.add(pixel);
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

    public Cluster findPixelInCLuster(RGBRepresentation pixel){
        for (Cluster cluster: this.listClusters
             ) {
            if(cluster.containsPixel(pixel)){
                return cluster;
            }
        }
        return null;
    }

    public void removePixelIfExist(RGBRepresentation pixel){
        Cluster cluster = findPixelInCLuster(pixel);
        if(cluster != null) {
            cluster.removePixel(pixel);
        }

    }

}
