package com.edencoding;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

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

    @Override
    public BufferedImage executeAlgorithm(BufferedImage bufferedImage, DistanceMethod method) {
        Arrays.fill(idClusterForEachPixel,-1);

        int idCLuster = 0;
        while (this.listPixelsUnvisited.size() > 0 ){
            RGBRepresentation pixel = getAleaPixel();
            this.listPixelsUnvisited.remove(pixel);
            Cluster cluster =new Cluster(pixel);
            cluster.setIdCluster(idCLuster);
            idCLuster++;
            searchAllNeighbours(cluster, method);
            if(cluster.getPixelAssociate().size() < this.minClosePoints){
                cluster.setBlack();
            }
            this.clusterList.add(cluster);

        }


        for (Cluster cluster:
                this.clusterList) {
            for (RGBRepresentation pixel :
                    cluster.getPixelAssociate()) {
                this.idClusterForEachPixel[pixel.getId()] = cluster.getIdCluster();
            }
        }

        return Utils.createImageFromCluster(bufferedImage,idClusterForEachPixel,this.clusterList);
    }

    public RGBRepresentation getAleaPixel(){
        Random rand = new Random();
        int indicePixel = rand.nextInt(listPixelsUnvisited.size());
        return this.listPixelsUnvisited.get(indicePixel);

    }

    /**
     * Method which search all neighbours for one pixel, with the distance between him and all other points unvisited
     * @param centerPixel
     * @param method
     * @return
     */
    private List<RGBRepresentation> searchNeighboursOnePixel(RGBRepresentation centerPixel, DistanceMethod method){
        Cluster cluster = new Cluster(centerPixel);
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
     * @param cluster
     * @param method
     */
    private void searchAllNeighbours(Cluster cluster, DistanceMethod method){
        cluster.addPixel(cluster.getRgbRepresentation());
        List<RGBRepresentation> listNeighbours =  searchNeighboursOnePixel(cluster.getRgbRepresentation(),method);
        //List a ajouter dans le cluster a la fin
        List<RGBRepresentation> listAssociatePixel =  new ArrayList<>(listNeighbours);

        //Tant qu'on trouve de voisins
        while(!listNeighbours.isEmpty()){
            RGBRepresentation pixelToSearchNeighbour = listNeighbours.get(0);
            listNeighbours.remove(pixelToSearchNeighbour);

            //Search tous les voisins
            List<RGBRepresentation> newNeighbours = searchNeighboursOnePixel(pixelToSearchNeighbour,method);
            //Ajout les nouveaux voisins aux voisins actuels
            listNeighbours.addAll(newNeighbours);
            //Ajout les nouveaux voisisn au total des voisins
            listAssociatePixel.addAll(newNeighbours);
            //Supprime le voisin qu'on vient de visiter
            if(!listAssociatePixel.contains(pixelToSearchNeighbour)){
                listAssociatePixel.add(pixelToSearchNeighbour);
            }
        }
        //Ajout de tous les voisins
        cluster.addAllPixels(listAssociatePixel);
    }

}
