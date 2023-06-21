package ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import interfaces.Page;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import search.ColorImageSearcher;

class DisplayColorImageFinder implements Page{

    List<Image> images = new ArrayList<>();
     Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    double fitWidth = screenBounds.getWidth();
    double fitHeight = screenBounds.getHeight();
    public DisplayColorImageFinder(List<Color> targetColors, String folderPath) {
        this.images = ColorImageSearcher.searchImagesByColors(targetColors, folderPath);
        fitWidth /= Math.max(1, images.size());
        fitHeight /= Math.max(1, images.size());
    }

    public Parent getView() {
       BorderPane root = new BorderPane();

       ArrayList <ImageView> imagesView = new ArrayList<>();

       for(int i = 0; i < images.size(); i++) {
        
        ImageView imageView = new ImageView(images.get(i));
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imagesView.add(imageView);
       }
       

       // Create a VBox to stack the image views vertically
       VBox imageContainer = new VBox();
       imageContainer.getChildren().addAll(imagesView);


       // Set the image container in the center of the root pane
       root.setLeft(imageContainer);

        return root;
    }
}