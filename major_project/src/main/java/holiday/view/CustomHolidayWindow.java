package holiday.view;

import holiday.model.UserFacade;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CustomHolidayWindow {
    private UserFacade controller;
    private Stage customHoliday;
    private CustomCalendar customCalendar;

    public CustomHolidayWindow(UserFacade controller, CustomCalendar customCalendar){
        this.controller = controller;
        this.customCalendar = customCalendar;
    }

    /**
     * Create a new view for the custom holiday that asks for the date.
     */
    public void customHoliday(){
        customHoliday = new Stage();
        customHoliday.initModality(Modality.WINDOW_MODAL);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        HBox hBox = new HBox();
        Label info = new Label("Date (YYYY-MM-DD format): ");
        TextField date = new TextField();
        hBox.getChildren().addAll(info, date);
        Button setCustomHoliday = new Button("Set date");
        setCustomHoliday.setOnAction((event) -> getFields(date, vbox));
        vbox.getChildren().addAll(hBox, setCustomHoliday);
        customHoliday.setScene(new Scene(vbox, 640, 360));
        customHoliday.show();
    }

    /**
     * Create a view for retrieving more info on the holiday.
     * @param vbox The vbox we store the view elements in.
     */
    public void setFieldsOfHoliday(VBox vbox){
        vbox.getChildren().clear();
        vbox.setMinSize(640, 360);
        VBox getInput = new VBox();
        ArrayList<String> s = new ArrayList<>();
        Label info = new Label("Leaving fields blank default to 'N/A'");
        for(int i = 0; i < 6; i++){
            TextField tf = new TextField();
            tf.setPromptText(holidayFields()[i]);
            getInput.getChildren().add(tf);
        }
        vbox.getChildren().add(getInput);
        Button setCustomHoliday = new Button("Set holiday");
        setCustomHoliday.setOnAction((event) -> {
            for (Node child : getInput.getChildren()) {
                TextField tf = (TextField) child;
                if (tf.getText().isEmpty()) {
                    tf.setText("N/A");
                }
                s.add(tf.getText());
            }
            controller.setCustomHoliday(s);
            customCalendar.getHolidate();
            customHoliday.close();
        });
        vbox.getChildren().addAll(info, setCustomHoliday);
    }


    /**
     * The fields we are collecting as a string array.
     * @return The fields we are collecting information on.
     */
    private String[] holidayFields(){
        String[] fields = {"Name:        ", "Local name:  ", "Language:    ", "Description: "
                , "Location:    ", "Type:        "};
        return fields;
    }

    /**
     * Get the fields input.
     * @param date The date we are setting for the holiday.
     * @param vbox The VBox we are reusing for the view.
     */
    private void getFields(TextField date, VBox vbox){
        controller.setCustomDate(date.getText().replaceAll("\n", System.getProperty("line.separator")));
        setFieldsOfHoliday(vbox);
    }



}