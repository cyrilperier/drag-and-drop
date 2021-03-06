package com.edencoding.controllers;

import com.edencoding.*;
import javafx.embed.swing.SwingFXUtils;
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
    @FXML
    public ComboBox<Integer> comboboxK;
    @FXML
    public ComboBox<Algorithm> comboboxAlgorithm;
    @FXML
    public ComboBox<Integer> comboboxVoisins;
    @FXML
    public ComboBox<Integer> comboboxDistance;

    public String name;
    @FXML
    public ComboBox<Integer> sizeImageH;
    @FXML
    public ComboBox<Integer> sizeImageW;

    //TODO Chercher pourquoi appliquer la premiere fois sur l'image c'est si lonng
    @FXML
    private void changeImage(ActionEvent event){
        event.consume();

        DistanceMethod distanceMethod = this.distanceMethod.valueProperty().getValue();
        int k = this.comboboxK.valueProperty().getValue();
        Algorithm algorithm = this.comboboxAlgorithm.valueProperty().getValue();

        int minClosePoint = this.comboboxVoisins.valueProperty().getValue();
        int distanceInterClasses = this.comboboxDistance.valueProperty().getValue();



        BufferedImage bufferedImage = Utils.transformImageToBufferedImage(imageView.getImage());

        List<RGBRepresentation> listPixel = Utils.getRGBFromImg(bufferedImage);
        List<Cluster> clusterList = Utils.createClusters(k);

        String suffixe;
        ExecutableAlgorithm executableAlgorithm;
        if (algorithm == Algorithm.DBSCAN) {
            executableAlgorithm = new DbScan(listPixel,minClosePoint,distanceInterClasses);
            suffixe = minClosePoint +"_"+distanceInterClasses;
        } else {
            executableAlgorithm = new Kmeans(listPixel, clusterList);
            suffixe = String.valueOf(k);
        }

        BufferedImage newImage = executableAlgorithm.executeAlgorithm(bufferedImage,distanceMethod);
        Utils.createImage(name,newImage,algorithm,distanceMethod,suffixe);

        Image image = SwingFXUtils.toFXImage(newImage, null);
        this.imageView.setImage(image);
    }

    public void initialize() {
        makeTextAreaDragTarget(imageView);
    }

    public void makeTextAreaDragTarget(Node node) {
        node.setOnDragOver(event -> event.acceptTransferModes(TransferMode.ANY));

        populateCombobox();

        node.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();

            if(event.getDragboard().hasFiles()) {
                dropInstructions.getChildren().remove(this.imageView);
                Image img = null;
                File file = db.getFiles().get(0);
                this.name  = file.getName().replaceFirst("[.][^.]+$", "");
                java.awt.Image image = null;
                try {

                    img = new Image(new FileInputStream(file.getAbsolutePath()), this.sizeImageH.getValue(), this.sizeImageW.getValue(), false, true);


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

    private void populateCombobox() {
        this.distanceMethod.getItems().setAll(DistanceMethod.values());
        this.distanceMethod.getSelectionModel().selectFirst();

        this.comboboxAlgorithm.getItems().setAll(Algorithm.values());
        this.comboboxAlgorithm.getSelectionModel().selectFirst();

        this.comboboxK.getItems().add(3);
        this.comboboxK.getItems().add(16);
        this.comboboxK.getItems().add(32);
        this.comboboxK.getItems().add(64);
        this.comboboxK.getSelectionModel().selectFirst();

        this.comboboxDistance.getItems().add(2);
        this.comboboxDistance.getItems().add(3);
        this.comboboxDistance.getItems().add(5);
        this.comboboxDistance.getItems().add(10);
        this.comboboxDistance.getItems().add(20);
        this.comboboxDistance.getSelectionModel().selectFirst();

        this.comboboxVoisins.getItems().add(2);
        this.comboboxVoisins.getItems().add(3);
        this.comboboxVoisins.getItems().add(5);
        this.comboboxVoisins.getItems().add(7);
        this.comboboxVoisins.getItems().add(10);
        this.comboboxVoisins.getSelectionModel().selectFirst();

        this.sizeImageH.getItems().add(100);
        this.sizeImageH.getItems().add(200);
        this.sizeImageH.getItems().add(300);
        this.sizeImageH.getItems().add(400);
        this.sizeImageH.getSelectionModel().select(2);

        this.sizeImageW.getItems().add(100);
        this.sizeImageW.getItems().add(200);
        this.sizeImageW.getItems().add(300);
        this.sizeImageW.getItems().add(400);
        this.sizeImageW.getSelectionModel().select(2);



    }


}
