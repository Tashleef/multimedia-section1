package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import PageSwitcher.PageSwitcher;
import interfaces.Page;

public class ColorSelector implements Page{

    PageSwitcher pageSwitcher;
    public ColorSelector(PageSwitcher pageSwitcher) {
        this.pageSwitcher = pageSwitcher;
    }

    private ListView<Color> colorListView;
    private ObservableList<Color> selectedColors;
    private VBox layout;
    private Button selectFolderButton;
    public Parent getView() {
        selectedColors = FXCollections.observableArrayList();
        colorListView = new ListView<>();
        colorListView.setPrefSize(200, 150);
        colorListView.setItems(selectedColors);

        Button selectColorButton = new Button("Select Color");
        selectColorButton.setOnAction(e -> showColorPicker());
        
          selectFolderButton = new Button("Select Folder");
        selectFolderButton.setOnAction(e -> selectFolder());
        
        layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(colorListView, selectColorButton, selectFolderButton);

        return layout;
    }

    private void selectFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(layout.getScene().getWindow());
        if (selectedDirectory != null && getSelectedColors().size() != 0) {
            DisplayColorImageFinder displayColorImageFinder = new DisplayColorImageFinder(selectedColors, selectedDirectory.getAbsolutePath().toString());
            pageSwitcher.switchToPage(displayColorImageFinder);
            
        }
    }
    private void showColorPicker() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(e -> {
            Color selectedColor = colorPicker.getValue();
            if (!selectedColors.contains(selectedColor)) {
                selectedColors.add(selectedColor);
            }
        });

        Stage colorPickerStage = new Stage();
        colorPickerStage.setTitle("Select Color");
        colorPickerStage.setScene(new Scene(colorPicker));
        colorPickerStage.show();
    }


    public List<Color> getSelectedColors() {
        return new ArrayList<>(selectedColors);
    }
}
