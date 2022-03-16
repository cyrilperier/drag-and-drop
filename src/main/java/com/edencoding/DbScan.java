package com.edencoding;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * @author Launois Remy
 * Project: drag-and-drop
 * Package: com.edencoding
 */
public class DbScan implements ExecutableAlgorithm{
//TODO Core point pour améliorer
    private List<RGBRepresentation> listPixelsUnvisited;
    /**
     * Minimum de point requis pour etre un cluster
     */
    private int minClosePoints;
    private double distanceInterClasses;
    private List<Cluster> clusterList;


    public DbScan(List<RGBRepresentation> listPixels, int minCLosePoints, double distanceInterClasses, List<Cluster> clusterList) {
        this.listPixelsUnvisited = listPixels;
        this.minClosePoints = minCLosePoints;
        this.distanceInterClasses = distanceInterClasses;
        this.clusterList = clusterList;
    }

    public DbScan() {
    }

    public List<RGBRepresentation> getListPixelsUnvisited() {
        return listPixelsUnvisited;
    }

    public void setListPixelsUnvisited(List<RGBRepresentation> listPixelsUnvisited) {
        this.listPixelsUnvisited = listPixelsUnvisited;
    }

    public int getMinClosePoints() {
        return minClosePoints;
    }

    public void setMinClosePoints(int minClosePoints) {
        this.minClosePoints = minClosePoints;
    }

    public double getDistanceInterClasses() {
        return distanceInterClasses;
    }

    public void setDistanceInterClasses(double distanceInterClasses) {
        this.distanceInterClasses = distanceInterClasses;
    }

    public List<Cluster> getClusterList() {
        return clusterList;
    }

    public void setClusterList(List<Cluster> clusterList) {
        this.clusterList = clusterList;
    }

    @Override
    public BufferedImage executeAlgorithm(BufferedImage bufferedImage, DistanceMethod method) {
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();

        while (this.listPixelsUnvisited.size() > 0 ){
            RGBRepresentation pixel = getAleaPixel();
            this.listPixelsUnvisited.remove(pixel);
            Cluster newCluster = new Cluster(pixel);
            searchAllNeighbours(newCluster, method);
            if(newCluster.getPixelAssociate().size() > this.minClosePoints){
                System.out.println(newCluster);
            }else{
                //Mettre en noir le pixel qui corresspond pas a un cluster
                this.listPixelsUnvisited.addAll(newCluster.getPixelAssociate());
            }



        }



        return null;
    }


    public RGBRepresentation getAleaPixel(){
        Random rand = new Random();
        int indicePixel = rand.nextInt(listPixelsUnvisited.size());
        return this.listPixelsUnvisited.get(indicePixel);

    }

    /**
     * Method which search all neighbours for one pixel, with the distance between him and all other points unvisited
     * @param CenterPixel
     * @param method
     * @return
     */
    private List<RGBRepresentation> searchNeighboursOnePixel(RGBRepresentation CenterPixel, DistanceMethod method){
        Cluster cluster = new Cluster(CenterPixel);
        for (RGBRepresentation pixel: this.listPixelsUnvisited
             ) {
            double distance = method == DistanceMethod.EUCLIDEAN ? Utils.distanceEuclidean(cluster,pixel) : Utils.distanceManhattan(cluster,pixel);
            if(distance <= this.distanceInterClasses ){
                cluster.addPixel(pixel);
                this.listPixelsUnvisited.remove(pixel);
            }
        }

        return cluster.getPixelAssociate();
    }

    /**
     * Method which search all neighbours of neighbours
     * @param cluster
     * @param method
     */
    private void searchAllNeighbours(Cluster cluster, DistanceMethod method){
        //Ajout de tous les premiers voisins
        cluster.addAllPixels(searchNeighboursOnePixel(cluster.getRgbRepresentation(),method));
        //Ajout des voisins pour chaque voisins
        for (RGBRepresentation pixel : cluster.getPixelAssociate()) {
            cluster.addAllPixels(searchNeighboursOnePixel(pixel,method));
        }
    }



    /**
     * Que
     */

    /**
     * Prendre un point alea
     * Si il a au moins minClosePoints pixel proche peut devenir un cluster ils deviennt tous dans le cluster
     * Pour chaque pixel dedans regardant les pixels proches et les rajouter au cluster courant
     * Si il n'y en a plus proche nouveau cluster, avec point aléatoire
     */
}
