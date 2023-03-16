package holiday.model.input;

import java.time.LocalDate;
import java.util.List;

public interface HolidayApi {

    /**
     * Checks if there are any holidays on a specified date for a specified country and returns a list of holidays.
     * @param country The country we want to check holidays for.
     * @param d The date we want to check if there are any holidays for.
     * @return A list of holidays for a specified country and date
     */
    List<Holiday> getHoliday(String country, LocalDate d);

    /**
     * A boolean dictating whether we are using the online API or offline API.
     * @return Boolean value telling us whether it is Online API or not.
     */
    boolean isOnline();
}