package holiday.view;

import holiday.model.UserFacade;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.WorldMapView;

import static holiday.model.Database.clearCache;

public class Mainwindow {
    private HolidayWindow hw;
    private CustomCalendar customCalendar;
    private CustomHolidayWindow customHolidayWindow;
    private BorderPane pane;
    private final Scene scene;
    private MenuBar menuBar;
    private VBox sideButtonBar;
    private UserFacade controller;
    private ViewHolidayScreen viewHolidayScreen;
    private ViewAboutScreen viewAboutScreen;
    private SplashScreen splashScreen;
    private ProgressBar progress;
    private int loadTime = 15;

    /**
     * Initialise the main window for the app.
     * @param controller The controller that mutates the model.
     */
    public Mainwindow(UserFacade controller){
        this.controller = controller;
        this.hw = new HolidayWindow();
        this.viewHolidayScreen = new ViewHolidayScreen();
        this.viewAboutScreen = new ViewAboutScreen();
        this.splashScreen = new SplashScreen();
        this.progress = new ProgressBar();
        this.progress.setMinWidth(1280);
        this.customCalendar = new CustomCalendar(this.controller, viewHolidayScreen);
        this.customHolidayWindow = new CustomHolidayWindow(this.controller, this.customCalendar);
        this.hw.setUpNodes();
        this.pane = new BorderPane();
        this.scene = new Scene(pane, 1280, 720);
        buildMenuBar();
        buildSideButtons();

        loadingBar(progress);
    }

    /**
     * Replaces the splashscreen with the main app items.
     */
    private void removeSplashScreen(){
        this.pane.getChildren().clear();
        this.pane.setTop(menuBar);
        this.pane.setCenter(hw.getWmv());
        this.pane.setRight(sideButtonBar);
    }

    /**
     * Runs the loading bar animation for 15 seconds before changing the app back to its normal functionality.
     * @param progress The progress bar we are displaying.
     */
    private void loadingBar(ProgressBar progress){
        Group group = new Group();
        group.getChildren().addAll(this.splashScreen.imageView(), progress);
        progress.setLayoutY(700);
        this.pane.setCenter(group);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(loadTime), e-> {removeSplashScreen(); System.out.println("15 seconds over");},
                        new KeyValue(progress.progressProperty(), 1)));
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Get the current scene.
     * @return The scene.
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Build the side buttons that are located on the right side of the screen.
     * If we are using the online version of the API we add the ability to clear the cache.
     */
    private void buildSideButtons() {
        Button countryBtn = new Button("Select country");
        countryBtn.setOnAction((event) -> getCountries());
        Button cacheBtn = new Button("Clear cache");
        cacheBtn.setOnAction((event) -> clearCache());
        this.sideButtonBar = new VBox(countryBtn);
        if(controller.isApiOnline()){
            this.sideButtonBar.getChildren().add(cacheBtn);
        }
    }

    /**
     * Add a menu bar to the top of the screen with an About popup resulting from clicking the about menu item.
     */
    private void buildMenuBar() {
        Menu menu = new Menu("Menu");
        MenuItem menuItem1 = new MenuItem("About");
        menu.getItems().add(menuItem1);
        menuItem1.setOnAction((event) -> viewAboutScreen.aboutInfo());
        this.menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
    }

    /**
     * Get the country and add option to send a paste if there are holidays known for the month.
     */
    public void getCountries(){
        Button allHolidays = new Button("Send paste");
        allHolidays.setOnAction((event) -> sendPaste());
        Button customHoliday = new Button("Create Custom Holiday");
        customHoliday.setOnAction((event) -> customHoliday());
        ObservableList<WorldMapView.Country> s = hw.getWmv().getSelectedCountries();
        if(s.size() > 0){
            this.setCountry(s.get(0).toString());
            this.pane.setCenter(customCalendar.getView());
            this.sideButtonBar.getChildren().remove(0);
            this.sideButtonBar.getChildren().add(0, allHolidays);
            this.sideButtonBar.getChildren().addAll(customHoliday);
        }
    }

    /**
     * Set the country in the controller.
     * @param country The country we want to set in the controller.
     */
    public void setCountry(String country) {
        controller.setCountry(country);
    }

    /**
     * Returns a link to the paste we have created or error message.
     */
    private void sendPaste(){
        Stage pasteLink = new Stage();
        pasteLink.initModality(Modality.WINDOW_MODAL);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        TextArea textArea = new TextArea();
        textArea.setText(controller.pastePaste());
        textArea.setEditable(false);
        vbox.getChildren().add(textArea);
        pasteLink.setScene(new Scene(vbox));
        pasteLink.show();
    }

    /**
     * Create a new customholiday view.
     */
    private void customHoliday(){
        customHolidayWindow.customHoliday();
    }

}