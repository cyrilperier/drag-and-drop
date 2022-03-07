package com.edencoding.controllers;

import com.edencoding.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
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

    @FXML
    public Pane dropZone;
    @FXML
    public Pane dropInstructions;
    @FXML
    public ImageView imageView;
    @FXML
    public ComboBox<DistanceMethod> distanceMethod;
    public int k = 3;

    //TODO Faire choisir k, la méthode de calcul de distance et kmeans ou dbscan
    @FXML
    private void changeImage(ActionEvent event){
        event.consume();
        DistanceMethod distanceMethod = this.distanceMethod.valueProperty().getValue();
        BufferedImage bufferedImage = Utils.transformImageToBufferedImage(imageView.getImage());

        List<RGBRepresentation> listPixel = Utils.getRGBFromImg(bufferedImage);
        List<Cluster> clusterList = Utils.createClusters(k);

        Kmeans kmeans = new Kmeans(listPixel,clusterList);

        BufferedImage newImage = kmeans.doKmeans(bufferedImage,distanceMethod);
        Utils.createImage("",newImage,Algorithm.KMEANS,distanceMethod,k);
        System.out.println(newImage);

    }

    public void initialize() {
        makeTextAreaDragTarget(imageView);
    }

    public void makeTextAreaDragTarget(Node node) {
        node.setOnDragOver(event -> event.acceptTransferModes(TransferMode.ANY));

        this.distanceMethod.getItems().setAll(DistanceMethod.values());

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
