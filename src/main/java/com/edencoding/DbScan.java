package com.edencoding;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
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
    private List<RGBRepresentation> listPixelInitial;
    /**
     * Minimum de point requis pour etre un cluster
     */
    private int minClosePoints;
    private double distanceInterClasses;
    private List<Cluster> clusterList;
    private int[] idClusterForEachPixel ;

    public DbScan(List<RGBRepresentation> listPixels, int minCLosePoints, double distanceInterClasses, List<Cluster> clusterList) {
        this.listPixelsUnvisited = listPixels;
        this.listPixelInitial = new ArrayList<>(listPixels);
        this.minClosePoints = minCLosePoints;
        this.distanceInterClasses = distanceInterClasses;
        this.clusterList = clusterList;
        this.idClusterForEachPixel = new int[this.listPixelsUnvisited.size()];
    }
    public DbScan(List<RGBRepresentation> listPixels, int minCLosePoints, double distanceInterClasses) {
        this.listPixelsUnvisited = listPixels;
        this.listPixelInitial = new ArrayList<>(listPixels);
        this.minClosePoints = minCLosePoints;
        this.distanceInterClasses = distanceInterClasses;
        this.clusterList = new ArrayList<>();
        this.idClusterForEachPixel = new int[this.listPixelsUnvisited.size()];
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

        Arrays.fill(idClusterForEachPixel,-1);

        int idCLuster = 0;
        while (this.listPixelsUnvisited.size() > 0 ){
            RGBRepresentation pixel = getAleaPixel();
            this.listPixelsUnvisited.remove(pixel);
            Cluster cluster = searchAllNeighbours(pixel, method);
            if(cluster.getPixelAssociate().size() > this.minClosePoints){
                cluster.setIdCluster(idCLuster);
                idCLuster++;
                this.clusterList.add(cluster);
            }else{
                //Mettre en noir le pixel qui corresspond pas a un cluster
                this.listPixelsUnvisited.addAll(cluster.getPixelAssociate());
            }


            //Il faut garder la lsite de tous les pixels
            /*
            Pou chaque pixel cherchais quel est son cluster, et mettre le couleur de son cluster a la palce
             */

        }

        getIdClusterForEachPixel();
        return Utils.createImageFromCluster(bufferedImage,idClusterForEachPixel,this.clusterList);
    }

    private void getIdClusterForEachPixel() {
        int i = 0;
        for (RGBRepresentation pixel:
             this.listPixelInitial) {

            int indiceCluster = 0;
            boolean goodCLuster = false;
            while (!goodCLuster && indiceCluster < this.clusterList.size()){
                goodCLuster = this.clusterList.get(indiceCluster).containsPixel(pixel);
                indiceCluster++;
            }

            this.idClusterForEachPixel[i] = --indiceCluster;
            i++;
        }
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
        List<RGBRepresentation> pixelVisited = new ArrayList<>();
        for (RGBRepresentation pixel: this.listPixelsUnvisited
             ) {
            double distance = method == DistanceMethod.EUCLIDEAN ? Utils.distanceEuclidean(cluster,pixel) : Utils.distanceManhattan(cluster,pixel);
            if(distance <= this.distanceInterClasses ){
                cluster.addPixel(pixel);
                pixelVisited.add(pixel);
            }
        }
        listPixelsUnvisited.removeAll(pixelVisited);

        return cluster.getPixelAssociate();
    }

    /**
     * Method which search all neighbours of neighbours
     * @param pixel
     * @param method
     */
    private Cluster searchAllNeighbours(RGBRepresentation pixel, DistanceMethod method){
        //Ajout de tous les premiers voisins
        Cluster cluster = new Cluster(pixel);

        //TODO je ne parcours pas les voisins des voisins des voisins
        //Ajout des voisins pour chaque voisins
        List<RGBRepresentation> listNeighbours =  searchNeighboursOnePixel(pixel,method);;
        for (RGBRepresentation pixelAssociate : listNeighbours) {
            cluster.addPixel(pixelAssociate);
            List<RGBRepresentation> newNeighbours = searchNeighboursOnePixel(pixel,method);
            cluster.addAllPixels(newNeighbours);
        }
        return cluster;
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
