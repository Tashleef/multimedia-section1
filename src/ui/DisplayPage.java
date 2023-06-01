package ui;

import java.io.File;
import java.util.ArrayList;

import EditedClasses.Image;
import algorithms.KMeanAlgorithm;
import algorithms.MedianCut;
import algorithms.OctreeQuantization;
import colorPlate.ColorPlateGenerator;
import interfaces.Algorithm;
import interfaces.Page;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class DisplayPage implements Page {

    private ArrayList<Image> images = new ArrayList<>();
    int algorithmsNumber = 0;
    public DisplayPage(String imageUrl) {

        File directory = new File("/Users/faek-ayoubi/Desktop/multimediaProject/project/src/algorithms");
        File[] files = directory.listFiles();
        algorithmsNumber = files.length;
        Algorithm[] algorithm = new Algorithm[algorithmsNumber];
        algorithm[0] = new KMeanAlgorithm();
        algorithm[1] = new MedianCut();
        algorithm[2] = new OctreeQuantization();
        int[] k = new int[3];
        k[0] = 10;
        k[1] = 256;
        k[2] = 256;

        for(int i = 0; i < algorithmsNumber; i++) {
            Image image = new Image(imageUrl);
            image.setImage(algorithm[i].startAlgorithm(image.getImage(), k[i]));
            images.add(image);
        }
    }
    

    public Parent getView() {
       BorderPane root = new BorderPane();

       // Set the fit width and height of the image views
       Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
       double fitWidth = screenBounds.getWidth() / Math.max(1, algorithmsNumber);
       double fitHeight = screenBounds.getHeight() / Math.max(1, algorithmsNumber);
       ArrayList <ImageView> imagesView = new ArrayList<>();
       Pane[] colorPlate = new Pane[algorithmsNumber];
       for(int i = 0; i < algorithmsNumber; i++) {
        ColorPlateGenerator colorPlateGenerator = new ColorPlateGenerator(images.get(i).getImage(), fitWidth*2, fitHeight);
        colorPlate[i] = colorPlateGenerator.generateColorPlate();
        
        ImageView imageView = new ImageView(images.get(i).getImage());
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imagesView.add(imageView);
       }
       

       // Create a VBox to stack the image views vertically
       VBox imageContainer = new VBox();

       imageContainer.getChildren().addAll(imagesView);
       VBox colorPlateContainer = new VBox();
       colorPlateContainer.setMinHeight(fitHeight);
       colorPlateContainer.getChildren().addAll(colorPlate);

       // Set the image container in the center of the root pane
       root.setLeft(imageContainer);
       root.setRight(colorPlateContainer);

        return root;
    }
}