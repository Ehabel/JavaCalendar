package holiday.view;

import holiday.model.input.Holiday;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewHolidayScreen {
    private Stage holidayInfo;

    /**
     * Displays information on the selected holiday. Shows a popout window.
     */
    public ViewHolidayScreen(){
        holidayInfo = new Stage();
        holidayInfo.initModality(Modality.WINDOW_MODAL);
    }

    /**
     * Displays information on the selected holiday.
     * @param h The holiday we want to display information about.
     */
    public void holidayInfo(Holiday h){
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        TextArea textArea = new TextArea();
        textArea.setText(h.calendarString());
        textArea.setEditable(false);
        vbox.getChildren().add(textArea);
        holidayInfo.setScene(new Scene(vbox));
        holidayInfo.show();
    }
}