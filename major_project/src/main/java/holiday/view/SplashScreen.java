package holiday.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SplashScreen {
    private Image image;
    private ImageView imageView;

    public SplashScreen(){
        InputStream stream = null;
        try {
            stream = new FileInputStream("src/main/resources/world.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image = new Image(stream);
        imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(1280);
        imageView.setPreserveRatio(true);
    }

    /**
     * Return the imageview object of the image we want to display.
     * @return The imageview object.
     */
    public ImageView imageView(){
        return imageView;
    }
}