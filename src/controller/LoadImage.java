package controller;

import java.io.File;
import java.io.IOException;

import PageSwitcher.PageSwitcher;
import interfaces.Page;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ui.ColorSelector;
import ui.DisplayPage;
import ui.DisplaySimilarImages;

public class LoadImage {

    private PageSwitcher pageSwitcher;
    public LoadImage(PageSwitcher pageSwitcher) {
        this.pageSwitcher = pageSwitcher;
    }
    @FXML
    Button chooseImageButton;

    @FXML 
    Button searchImage;

    @FXML
    private void upload() {
        File selectedFile = pageSwitcher.showFileChooser();
        if (selectedFile != null) {
            DisplayPage displayPage = new DisplayPage(selectedFile.toURI().toString());
        
          pageSwitcher.switchToPage(displayPage);
        }
        return ;
    }

    @FXML
    private void search() throws IOException {
            DisplaySimilarImages displaySimilarImages = new DisplaySimilarImages();
          pageSwitcher.switchToPage(displaySimilarImages);
    }

    @FXML 
    private void useColors() {
        ColorSelector colorSelector = new ColorSelector(pageSwitcher);
        pageSwitcher.switchToPage(colorSelector);
    }
}
