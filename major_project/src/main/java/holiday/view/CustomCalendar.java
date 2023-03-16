// Inspired by https://github.com/SirGoose3432/javafx-calendar

package holiday.view;

import holiday.model.UserFacade;
import holiday.model.input.Holiday;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomCalendar {

    private ArrayList<StackPaneNode> daysInMonth = new ArrayList<>(42);
    private final List<Text> daysOfWeek = new ArrayList<>();
    private VBox view;
    private LocalDate currentDate;
    private Text calendarYearTitle;
    private Text calendarMonthTitle;
    private UserFacade controller;
    private ViewHolidayScreen viewHolidayScreen;
    private Background defaultColor = new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY));
    private Background noHolidays = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
    private Background foundHolidays = new Background(new BackgroundFill(Color.rgb(85, 250, 65), CornerRadii.EMPTY, Insets.EMPTY));
    private Background customColor = new Background(new BackgroundFill(Color.rgb(35, 100, 215), CornerRadii.EMPTY, Insets.EMPTY));
    private Background customEmptyColor = new Background(new BackgroundFill(Color.rgb(85, 60, 235), CornerRadii.EMPTY, Insets.EMPTY));
    private Background listHolidays = new Background(new BackgroundFill(Color.rgb(35, 210, 65), CornerRadii.EMPTY, Insets.EMPTY));

    /**
     * Creates the Custom calendar.
     * @param controller The controller which alters the state of the model.
     * @param viewHolidayScreen The screen that displays information on the holidays.
     */
    public CustomCalendar(UserFacade controller, ViewHolidayScreen viewHolidayScreen){
        this.viewHolidayScreen = viewHolidayScreen;
        this.controller = controller;
        this.currentDate = LocalDate.now();
        initialiseDayNames();
        GridPane cal = new GridPane();
        GridPane header = new GridPane();

        setUpCalendarDays(cal);
        setUpDaysOfWeekLayout(header);
        setUpHeader(header, cal);
    }

    /**
     * Creates and sets up the grid structure that displays the day of the month.
     * @param cal A gridpane that we want to view as a calendar.
     */
    private void setUpCalendarDays(GridPane cal){
        cal.setGridLinesVisible(true);
        cal.setPrefSize(1280, 720);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPaneNode sp = new StackPaneNode();
                sp.setPrefSize(640,480);
                cal.add(sp,j,i);
                daysInMonth.add(sp);
            }
        }
    }

    /**
     * Adds the string representation of the days of the week as a header ontop of the calendar.
     * @param header The grid which will be filled with the days of the week.
     */
    private void setUpDaysOfWeekLayout(GridPane header){
        header.setPrefSize(1280, 20);
        int column = 0;
        int row = 0;
        for(Text day: this.daysOfWeek){
            StackPane stackPane = new StackPane();
            stackPane.setPrefSize(400, 200);
            stackPane.getChildren().add(day);
            header.add(stackPane, column, row);
            column++;
        }

    }

    /**
     * Uses a date and updates the view of the calendar to display the correct view for that month.
     * @param localDate The date which we want to display on the calendar.
     */
    private void fillCalendar(LocalDate localDate){
        LocalDate monthBeginning = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
        String currMonth = monthBeginning.getMonth().toString();
        this.calendarMonthTitle.setText(monthBeginning.getMonth().toString());
        this.calendarYearTitle.setText(String.valueOf(monthBeginning.getYear()));

        while(!monthBeginning.getDayOfWeek().toString().equals("MONDAY")){
            monthBeginning = monthBeginning.minusDays(1);
        }
        for(StackPaneNode stackPaneNode: daysInMonth) {
            stackPaneNode.setBackground(defaultColor);
            if(stackPaneNode.getChildren().size() > 0){
                stackPaneNode.getChildren().clear();
                stackPaneNode.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            }
            stackPaneNode.setDate(monthBeginning);
            if(!stackPaneNode.getDate().getMonth().toString().equals(currMonth)){
                stackPaneNode.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                stackPaneNode.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
            stackPaneNode.getChildren().add(new Text(String.valueOf(monthBeginning.getDayOfMonth())));
            stackPaneNode.setOnMouseClicked(e -> setBehaviour(stackPaneNode));
            monthBeginning = monthBeginning.plusDays(1);
        }
        getHolidate();
    }

    /**
     * Adjusts the month displayed on the calendar by changing the view to show us the new month.
     * @param direction A string specifying if we are going back or forwards.
     */
    private void setMonth(String direction){
        if(direction.equals("PREV")){
            currentDate = currentDate.minusMonths(1);
            fillCalendar(currentDate);
            controller.clearKnownHolidays();
        }
        else if(direction.equals("NEXT")){
            currentDate = currentDate.plusMonths(1);
            fillCalendar(currentDate);
            controller.clearKnownHolidays();
        }
    }

    /**
     * Adjusts the year displayed on the calendar by changing the view to show us the new month of that year.
     * @param direction A string specifying if we are going back or forwards.
     */
    private void setYear(String direction){
        if(direction.equals("PREV")){
            currentDate = currentDate.minusYears(1);
            fillCalendar(currentDate);
            controller.clearKnownHolidays();
        }
        else if(direction.equals("NEXT")){
            currentDate = currentDate.plusYears(1);
            fillCalendar(currentDate);
            controller.clearKnownHolidays();
        }
    }

    /**
     * Get the string representation of the days of the week in all uppercase.
     */
    private void initialiseDayNames(){
        for (DayOfWeek c : DayOfWeek.values())
            this.daysOfWeek.add(new Text(c.toString().toUpperCase()));
    }

    /**
     * Creates the view by setting the buttons that change month and year at the top of a HBox. This is then added to a VBox with
     * the header and calendar view panes underneath in an ordered manner.
     * @param header The header gridpane containing the days of the week.
     * @param cal The calendar gridpane containing 42 days to display the days of that month. (Extra days for prev and next months are grey.)
     */
    private void setUpHeader(GridPane header, GridPane cal){
        this.calendarMonthTitle = new Text();
        this.calendarYearTitle = new Text();
        Button prevMonth = new Button("Prev");
        prevMonth.setOnAction(e -> setMonth("PREV"));
        Button nextMonth = new Button("Next");
        nextMonth.setOnAction(e -> setMonth("NEXT"));
        Button prevYear = new Button("Prev");
        prevYear.setOnAction(e -> setYear("PREV"));
        Button nextYear = new Button("Next");
        nextYear.setOnAction(e -> setYear("NEXT"));

        HBox titleBar = new HBox(prevMonth, calendarMonthTitle, nextMonth);
        titleBar.setAlignment(Pos.BASELINE_CENTER);
        titleBar.setSpacing(15);

        HBox yearBar = new HBox(prevYear, calendarYearTitle, nextYear);
        yearBar.setAlignment(Pos.BASELINE_RIGHT);
        yearBar.setSpacing(15);
        fillCalendar(currentDate);

        HBox headerBar = new HBox(titleBar, yearBar);
        headerBar.setAlignment(Pos.BASELINE_CENTER);
        view = new VBox(headerBar, header, cal);
    }

    /**
     * Returns the view for the calendar we have created.
     * @return The view for the calendar.
     */
    public VBox getView() {
        return view;
    }

    /**
     * Inform the controller to manipulate the model and store the current day.
     * @param currentDay Takes in a date which we want to store in the model.
     */
    public void setCurrentDay(LocalDate currentDay) {
        controller.setLocalDate(currentDay);
    }

    /**
     * Updates the stackpane node to show us whether there are any holidays or not for the specified day.
     * @param stackPaneNode A singular node in the calendar gridpane.
     */
    private void setBehaviour(StackPaneNode stackPaneNode){
        this.setCurrentDay(stackPaneNode.getDate());
        List<Holiday> holiday;
        if(controller.checkUseCache(stackPaneNode.getDate()) && !stackPaneNode.getBackground().equals(customEmptyColor)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cached data exists!");
            alert.setHeaderText("Use cached data?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                holiday = controller.getCachedHoliday();
            }else{
                holiday = controller.getHoliday();
            }
        }
        else{
            holiday = controller.getHoliday();
        }
        if(holiday != null && holiday.size() != 0){
            if(controller.check()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Found Holidays Online");
                alert.setContentText("You added a holiday to a day that already has one!");
                alert.show();
                controller.removeUpdatedElement(stackPaneNode.getDate());
            }
            ListView<Holiday> listView = new ListView<>();
            listView.setMinSize(171, 78);
            listView.setMaxSize(171, 78);
            listViewFactory(listView);
            for(Holiday holiday1: holiday){
                listView.getItems().add(holiday1);
                stackPaneNode.getChildren().clear();
                stackPaneNode.getChildren().add(listView);
                stackPaneNode.setAlignment(listView, Pos.TOP_CENTER);
                stackPaneNode.setBackground(foundHolidays);
                stackPaneNode.setMaxSize(171, 108);
            }
            showHolidayPopUp(listView);

        }
        else{
            if(!stackPaneNode.getBackground().equals(customEmptyColor)){
                Text txt = new Text("No Holidays found!");
                stackPaneNode.getChildren().clear();
                stackPaneNode.setBackground(noHolidays);
                stackPaneNode.getChildren().add(txt);
            }
        }
    }

    /**
     * A factory that edits how it displays the cells with holidays.
     * @param listView The listView we are editing
     */
    private void listViewFactory(ListView<Holiday> listView) {
        listView.setCellFactory(param -> new ListCell<Holiday>() {
            @Override
            public void updateItem(Holiday item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null){
                    setBackground(listHolidays);
                    setText(item.toString());
                }
                else{
                    setBackground(listHolidays);
                }
            }
        });
    }

    /**
     * A factory that edits how it displays the cells with custom holidays.
     * @param listView The listView we are editing
     */
    private void listViewFactoryCustom(ListView<Holiday> listView) {
        listView.setCellFactory(param -> new ListCell<Holiday>() {
            @Override
            public void updateItem(Holiday item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null){
                    setBackground(customColor);
                    setText(item.toString());
                }
                else{
                    setBackground(customColor);
                }
            }
        });
    }

    /**
     * Display the custom holiday.
     */
    public void getHolidate(){
        if(controller.getCustomDate() != null){
            for(StackPaneNode s: daysInMonth){
                if(controller.getNewCustomHolidays().get(s.getDate()) != null){
                        if(!s.getBackground().equals(noHolidays)){
                            if(!controller.check()){
                                s.getChildren().clear();
                                ListView<Holiday> listView = new ListView<>();
                                listView.getItems().add(controller.getNewCustomHolidays().get(s.getDate()));
                                listView.setMinSize(171, 78);
                                listView.setMaxSize(171, 78);
                                listViewFactoryCustom(listView);
                                s.setAlignment(listView, Pos.TOP_CENTER);
                                s.setBackground(customEmptyColor);
                                s.getChildren().add(listView);
                                showHolidayPopUp(listView);
                            }
                            else{
                                for (LocalDate key : controller.getNewCustomHolidays().keySet() ) {
                                    if(key.equals(controller.getCustomDate())){
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Date checked");
                                        alert.setContentText("Already found holidays for this checked date");
                                        alert.show();
                                        controller.removeUpdatedElement(s.getDate());
                                    }
                                }
                            }
                        }else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Date checked");
                            alert.setContentText("Already found no holidays for this checked date");
                            alert.show();
                            controller.removeUpdatedElement(s.getDate());
                        }
                    }
            }
        }
    }

    /**
     * Shows the popUp with information on the selected holiday.
     * @param listView The listView are selecting the holiday to see more information about.
     */
    private void showHolidayPopUp(ListView<Holiday> listView) {
        listView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                ObservableList<Holiday> selectedIndices = listView.getSelectionModel().getSelectedItems();
                if(selectedIndices.size() > 0){
                    Holiday sl = selectedIndices.get(0);
                    viewHolidayScreen.holidayInfo(sl);
                }
            }
        });
    }

}