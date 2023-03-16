import holiday.model.UserFacade;
import holiday.model.input.HolidayApi;
import holiday.model.input.HolidayOffline;
import holiday.model.input.HolidayOnline;
import holiday.model.output.Pastebin;
import holiday.model.output.PastebinOffline;
import holiday.model.output.PastebinOnline;
import holiday.view.Mainwindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private String inputParameters;
    private String outputParameters;
    private UserFacade controller;
    private Mainwindow mw;
    private HolidayApi inputAPI;
    private Pastebin outputAPI;

    @Override
    public void start(Stage stage) {
        if(getParameters().getRaw().size() != 2){
            System.out.println("Incorrect run commands!");
            System.exit(0);
        }
        inputParameters = getParameters().getRaw().get(0);
        outputParameters = getParameters().getRaw().get(1);

        if(inputParameters.equals("online")){
            this.inputAPI = new HolidayOnline();
        }else if(inputParameters.equals("offline")){
            this.inputAPI = new HolidayOffline();
        }else{
            System.out.println("Incorrect run commands!");
            System.exit(0);
        }
        if(outputParameters.equals("online")){
            this.outputAPI = new PastebinOnline();
        }else if(outputParameters.equals("offline")){
            this.outputAPI = new PastebinOffline();
        }
        this.controller = new UserFacade(inputAPI, outputAPI);
        this.mw = new Mainwindow(controller);
        stage.setScene(mw.getScene());
        stage.show();

    }

    public static void main(String[] args){
        launch(args);
    }
}