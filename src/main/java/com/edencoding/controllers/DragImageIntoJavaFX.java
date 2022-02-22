package com.edencoding.controllers;

import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

import static javafx.embed.swing.SwingFXUtils.toFXImage;


public class DragImageIntoJavaFX {

    public Pane dropZone;
    public Pane dropInstructions;
    public ImageView imageView;
    public BufferedImage bufferedImg;
    public Image newimg;

    public void initialize() {
        makeTextAreaDragTarget(imageView);
    }

    public void makeTextAreaDragTarget(Node node) {
        node.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
        });



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
