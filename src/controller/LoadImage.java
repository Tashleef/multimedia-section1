package controller;

import java.io.File;

import PageSwitcher.PageSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ui.DisplayPage;

public class LoadImage {

    private PageSwitcher pageSwitcher;
    public LoadImage(PageSwitcher pageSwitcher) {
        this.pageSwitcher = pageSwitcher;
    }
    @FXML
    Button chooseImageButton;

    @FXML
    private void upload() {
        File selectedFile = pageSwitcher.showFileChooser();
        if (selectedFile != null) {
            DisplayPage displayPage = new DisplayPage(selectedFile.toURI().toString());
            System.out.println(selectedFile.getPath());
             pageSwitcher.switchToPage(displayPage);;
        }
        return ;
    }
}
