package search;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimilarImagesFinder {
    private static final double SIMILARITY_THRESHOLD = 0.5; // Adjust this threshold as needed

    public static List<Image> findSimilarImages() throws IOException {
        List<Image> similarImages = new ArrayList<>();

        File selectedImageFile = chooseImageFile();
        if (selectedImageFile != null) {
            BufferedImage selectedImageBI = ImageIO.read(selectedImageFile);

            File directory = selectedImageFile.getParentFile(); // Assuming the images are in the same directory
            File[] files = directory.listFiles();

            for (File file : files) {
                if (file.isFile() && !file.equals(selectedImageFile) && hasImageExtension(file)) {
                    System.out.println(file.getName());
                    BufferedImage image = ImageIO.read(file);
                    double similarity = calculateSimilarity(selectedImageBI, image);
                    if (similarity >= SIMILARITY_THRESHOLD) {
                        Image similarImage = new Image(file.toURI().toString());
                        similarImages.add(similarImage);
                    }
                }
            }
        }
        System.out.println(similarImages.size());
        return similarImages;
    }

     public static boolean hasImageExtension(File file) {
        String filename = file.getName();
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

        // List of recognized image file extensions
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp"};

        for (String imageExtension : imageExtensions) {
            if (extension.equals(imageExtension)) {
                return true;
            }
        }

        return false;
    }

    private static File chooseImageFile() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(null);
    }

    private static double calculateSimilarity(BufferedImage image1, BufferedImage image2) {
        // Implement your own similarity calculation logic here
        // This example uses a simple pixel-by-pixel comparison

        int width = image1.getWidth();
        int height = image1.getHeight();
        int numPixels = width * height;

        int numSimilarPixels = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel1 = image1.getRGB(x, y);
                int pixel2 = image2.getRGB(x, y);

                if (pixel1 == pixel2) {
                    numSimilarPixels++;
                }
            }
        }

        return (double) numSimilarPixels / numPixels;
    }
}
