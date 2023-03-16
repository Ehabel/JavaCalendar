package holiday.model.input;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

public class HolidayOffline implements HolidayApi{

    /**
     * Checks if there are any holidays on a specified date for a specified country and returns a list of holidays.
     * @param country The country we want to check holidays for.
     * @param d The date we want to check if there are any holidays for.
     * @return A list of holidays for a specified country and date
     */
    @Override
    public List<Holiday> getHoliday(String country, LocalDate d) {
        String holiday = "";
        if(d.getDayOfMonth() == 1 || d.getDayOfMonth() == 15 || d.getDayOfMonth() == 22){
            holiday = "[{\"name\":\"Test Day\",\"name_local\":\"\",\"language\":\"\",\"description\":\"\",\"country\":\"AU\",\"location\":\"Australia - Victoria\",\"type\":\"Local holiday\",\"date\":\"12/25/2022\",\"date_year\":\"2022\",\"date_month\":\"1\",\"date_day\":\"1\",\"week_day\":\"Sunday\"}]";
        }
        else if(d.getDayOfMonth() == 2){
            holiday = "[{\"name\":\"Another holiday\",\"name_local\":\"\",\"language\":\"\",\"description\":\"First holiday multi\",\"country\":\"AU\",\"location\":\"Australia - NSW\",\"type\":\"Local holiday\",\"date\":\"3/5/2022\",\"date_year\":\"2022\",\"date_month\":\"5\",\"date_day\":\"3\",\"week_day\":\"Sunday\"}," +
                    "{\"name\":\"Holiday 2\",\"name_local\":\"\",\"language\":\"\",\"description\":\"Second holiday multi\",\"country\":\"AU\",\"location\":\"Australia - Victoria\",\"type\":\"Local holiday 2\",\"date\":\"3/5/2022\",\"date_year\":\"2022\",\"date_month\":\"5\",\"date_day\":\"3\",\"week_day\":\"Sunday\"}]";
        }else{
            holiday = "[]";
        }
        JsonArray holidaysList = (JsonArray) JsonParser.parseString(holiday);
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Holiday>>() {}.getType();
        List<Holiday> allHolidays = gson.fromJson(holidaysList, collectionType);
        return allHolidays;
    }

    /**
     * A boolean dictating whether we are using the online API or offline API.
     * @return Boolean value telling us the API is the offline version.
     */
    @Override
    public boolean isOnline() {
        return false;
    }
}