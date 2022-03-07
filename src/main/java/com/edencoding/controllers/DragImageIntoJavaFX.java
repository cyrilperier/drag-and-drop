package com.edencoding.controllers;

import com.edencoding.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

/**
 * @author Launois Remy / Perier Cyril
 * Project: drag-and-drop
 * Package: com.edencoding
 */
public class DragImageIntoJavaFX {

    public Pane dropZone;
    public Pane dropInstructions;
    public ImageView imageView;
    public BufferedImage bufferedImg;
    public Image newimg;
    public int k = 3;

    //TODO Faire choisir k, la méthode de calcul de distance et kmeans ou dbscan
    @FXML
    private void changeImage(ActionEvent event){
        event.consume();

        BufferedImage bufferedImage = Utils.transformImageToBufferedImage(imageView.getImage());
        List<RGBRepresentation> listPixel = Utils.getRGBFromImg(bufferedImage);
        List<Cluster> clusterList = Utils.createClusters(k);
        Kmeans kmeans = new Kmeans(listPixel,clusterList);
        BufferedImage bufferedImage1 = kmeans.doKmeans(bufferedImage,DistanceMethod.EUCLIDEAN);
        Utils.createImage("test.png",bufferedImage1);
        System.out.println(bufferedImage1);

    }

    public void initialize() {
        makeTextAreaDragTarget(imageView);
    }

    public void makeTextAreaDragTarget(Node node) {
        node.setOnDragOver(event -> event.acceptTransferModes(TransferMode.ANY));



        node.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();

            if(event.getDragboard().hasFiles()) {

                Image img = null;
                System.out.println(db.getFiles().get(0).getAbsolutePath());
                java.awt.Image image = null;
                try {
                    img = new Image(new FileInputStream(db.getFiles().get(0).getAbsolutePath()), 300, 300, false, true);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageView = new ImageView();
                imageView.setImage(img);
                imageView.setX(350);
                imageView.setY(40);
                dropInstructions.getChildren().add(imageView);


            }

            dropInstructions.setVisible(true);
        });
    }



}
