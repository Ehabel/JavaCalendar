package holiday.model.input;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import static holiday.model.Database.*;


public class HolidayOnline implements HolidayApi {
    private final CloseableHttpClient httpClient;
    private final String apiKey;

    public HolidayOnline(){
        this.httpClient = HttpClients.createDefault();
        this.apiKey = System.getenv("INPUT_API_KEY");
        createDB();
        setupDB();
    }


    /**
     * Checks if there are any holidays on a specified date for a specified country and returns a list of holidays.
     * @param country The country we want to check holidays for.
     * @param d The date we want to check if there are any holidays for.
     * @return A list of holidays for a specified country and date
     */
    @Override
    public List<Holiday> getHoliday(String country, LocalDate d) {
        try {
            String base = "https://holidays.abstractapi.com/v1/";
            String url = base + "?api_key=" + this.apiKey + "&country=" + country + "&year=" + d.getYear() +
                    "&month=" + d.getMonth().getValue() + "&day=" + d.getDayOfMonth();
            HttpGet getter = new HttpGet(url);
            InputStream stream = httpClient.execute(getter).getEntity().getContent();
            String result = readInput(stream);
            JsonArray holidaysList = (JsonArray) JsonParser.parseString(result);
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Holiday>>() {}.getType();
            List<Holiday> allHolidays = gson.fromJson(holidaysList, collectionType);

            if(allHolidays.size() == 0){
                String date = d.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                addHoliday("No Holidays!", date);
            }
            else{
                for(Holiday h: allHolidays){
                    addHoliday(h.toJsonString(), h.getDate());
                }
            }

            return allHolidays;
        } catch (IOException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * A boolean dictating whether we are using the online API or offline API.
     * @return Boolean value telling us the API is the online version.
     */
    @Override
    public boolean isOnline() {
        return true;
    }

    /**
     * Read the response from the API and convert it to a string
     * @param stream The stream of bytes we want to convert to a string.
     * @return The string representation of the input
     */
    private String readInput(InputStream stream) {
        Scanner sc = new Scanner(stream);
        StringBuilder out = new StringBuilder();
        while (sc.hasNextLine()) {
            out.append(sc.nextLine());
        }
        System.out.println(out);
        return out.toString();
    }

}