package threads;

import java.util.List;
import java.util.concurrent.Callable;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import search.ColorImageSearcher;

public class Threads implements Callable<List<Image>> {

    String path;
    List <Color> color;
    public Threads(String path,List <Color> color) {
        this.path = path;
        this.color = color;
    }
    @Override
    public List<Image> call() throws Exception {
        return ColorImageSearcher.searchImagesByColors(color, path);
    }
    
}
