package com.edencoding;

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

    public void doKmeans(BufferedImage image, int k,DistanceMethod method){

        int[] idClusterForEachPixel = new int[this.listPixels.size()];
        Arrays.fill(idClusterForEachPixel,-1);
        boolean pixelChangedCluster = false;
        do{
            pixelChangedCluster = false;
            int i = 0;

            for (RGBRepresentation pixel: this.listPixels
            ) {
                Cluster cluster = Utils.findNearestCluster(this.listClusters,pixel,method);

                if(idClusterForEachPixel[i] != cluster.getIdCluster()){
                    //TODO Supprimer le pixel la où il était avant
                    this.removePixelIfExist(pixel);
                    cluster.addPixel(pixel);
                    pixelChangedCluster = true;
                }
                i++;
            }

            List<Cluster> listClusterNew = new ArrayList<>();
            for (Cluster cluster: this.listClusters
                 ) {
                cluster.mean();
            }

        }while(pixelChangedCluster);


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

    public boolean clusterStable(List<Cluster> clusterList){
        return false;

    }
}
