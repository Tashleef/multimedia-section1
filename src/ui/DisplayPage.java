package ui;

import java.io.File;
import java.util.ArrayList;

import EditedClasses.Image;
import algorithms.KMeanAlgorithm;
import algorithms.MedianCut;
import colors.ColorPlateGenerator;
import indexedImage.ImageToIndexedConverter;
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
    int algorithmsNumber;
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    double fitWidth = screenBounds.getWidth();
    double fitHeight = screenBounds.getHeight();
    public DisplayPage(String imageUrl) {

        File directory = new File("/Users/faek-ayoubi/Desktop/multimediaProject/project/src/algorithms");
        File[] files = directory.listFiles();
        algorithmsNumber = files.length;

        fitWidth /= Math.max(1, algorithmsNumber);
        fitHeight /= Math.max(1, algorithmsNumber);

        Algorithm[] algorithm = new Algorithm[algorithmsNumber];
        algorithm[0] = new KMeanAlgorithm();
        algorithm[1] = new MedianCut();
        int[] k = new int[algorithmsNumber];
        k[0] = 10;
        k[1] = 256;
        for(int i = 0; i < algorithmsNumber; i++) {
            Image image = new Image(imageUrl);
            image.setImage(algorithm[i].startAlgorithm(image.getImage(), k[i]));
            images.add(image);
            ImageToIndexedConverter.convertToIndexed(image.getImage(), "/Users/faek-ayoubi/Desktop/multimediaProject/project/savedImage/" + (i+1) + "Image" );
        }

    }
    

    public Parent getView() {
       BorderPane root = new BorderPane();

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
