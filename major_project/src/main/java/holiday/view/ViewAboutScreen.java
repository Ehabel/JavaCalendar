package holiday.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewAboutScreen {
    private Stage aboutInfo;

    /**
     * Displays "About me" information.
     */
    public ViewAboutScreen(){
        aboutInfo = new Stage();
        aboutInfo.initModality(Modality.WINDOW_MODAL);
    }

    /**
     * Displays information about me in a popout window.
     */
    public void aboutInfo(){
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        TextArea textArea = new TextArea();
        textArea.setText(aboutText());
        textArea.setEditable(false);
        vbox.getChildren().add(textArea);
        aboutInfo.setScene(new Scene(vbox));
        aboutInfo.show();
    }

    /**
     * Has the text we want to display in the About window.
     * @return The text we want to display in the popout window
     */
    private String aboutText(){
        return "Welcome to the World Holiday Calendar!\n My name is Ehab El Cheikh." +
                "\n References:\n Calendar was inspired by https://github.com/SirGoose3432/javafx-calendar \n" +
                "Splash screen: https://unsplash.com/photos/Q1p7bh3SHj8";
    }
}