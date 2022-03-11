package com.edencoding;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Launois Remy
 * Project: drag-and-drop
 * Package: com.edencoding
 */
public class DbScan implements ExecutableAlgorithm{
//TODO Core point pour améliorer
    private List<RGBRepresentation> listPixels;
    private int minCLosePoints;
    private double distanceInterClasses;
    private List<Cluster> clusterList;


    public DbScan(List<RGBRepresentation> listPixels, int minCLosePoints, double distanceInterClasses, List<Cluster> clusterList) {
        this.listPixels = listPixels;
        this.minCLosePoints = minCLosePoints;
        this.distanceInterClasses = distanceInterClasses;
        this.clusterList = clusterList;
    }

    public DbScan() {
    }

    public List<RGBRepresentation> getListPixels() {
        return listPixels;
    }

    public void setListPixels(List<RGBRepresentation> listPixels) {
        this.listPixels = listPixels;
    }

    public int getMinCLosePoints() {
        return minCLosePoints;
    }

    public void setMinCLosePoints(int minCLosePoints) {
        this.minCLosePoints = minCLosePoints;
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
        Random rand = new Random();
        int randomH = rand.nextInt(height);
        int randomW = rand.nextInt(width);

        int indicePixel = width * randomH + randomW;
        Cluster clusterTest = new Cluster(this.listPixels.get(indicePixel));
        Cluster pixelTested = addPrecedentSuivantCluster(clusterTest,indicePixel,method,new ArrayList<>());
        return null;
    }

    private Cluster addPrecedentSuivantCluster(Cluster clusterTest, int indice, DistanceMethod method, List<RGBRepresentation> listPixelVisted ) {

        int numberOfClosePoint;
        int indicePrecedent = indice - 1;
        int indiceSuivant = indice + 1;
        int sizeAssociate = clusterTest.getPixelAssociate().size();
        int  numberPixelSee = listPixelVisted.size();
        if( numberPixelSee > sizeAssociate && numberPixelSee  >  this.minCLosePoints){
            return clusterTest;
        }else{
            if(indicePrecedent > 0 && this.listPixels.get(indicePrecedent) != null && listPixelVisted.contains(this.listPixels.get(indicePrecedent))){
                searchPixel(method, listPixelVisted, clusterTest, indicePrecedent);
            }else if(indiceSuivant < this.listPixels.size() && this.listPixels.get(indiceSuivant) != null && listPixelVisted.contains(this.listPixels.get(indiceSuivant))){
                searchPixel(method, listPixelVisted, clusterTest, indiceSuivant);

            }
        }

        return clusterTest;
    }

    private void searchPixel(DistanceMethod method, List<RGBRepresentation> listPixelVisted, Cluster clusterTest, int indicePrecedent) {
        RGBRepresentation pixelPrecedent = this.listPixels.get(indicePrecedent);
        listPixelVisted.add(pixelPrecedent);
        double distance = method == DistanceMethod.EUCLIDEAN ? Utils.distanceEuclidean(clusterTest,pixelPrecedent) : Utils.distanceManhattan(clusterTest,pixelPrecedent);
        if(distance < this.distanceInterClasses ){
            clusterTest.addPixel(pixelPrecedent);
            addPrecedentSuivantCluster(clusterTest,indicePrecedent,method,listPixelVisted);

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
