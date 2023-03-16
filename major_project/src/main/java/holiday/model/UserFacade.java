package holiday.model;

import com.google.gson.Gson;
import holiday.model.input.Holiday;
import holiday.model.input.HolidayApi;
import holiday.model.output.Pastebin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static holiday.model.Database.checkHolidayExists;

public class UserFacade {
    private HolidayApi holidayApi;
    private Pastebin pastebin;
    private LocalDate localDate;
    private String country;
    private List<Holiday> knownHolidays;
    private List<String> customHoliday;
    private Holiday userHoliday;
    private LocalDate customDate;
    private Map<LocalDate, Holiday> newCustomHolidays;

    /**
     * Creates a new Facade for manipulating the model.
     * @param holidayApi The holiday input API
     * @param pastebin The PasteBin output API
     */
    public UserFacade(HolidayApi holidayApi, Pastebin pastebin){
        this.holidayApi = holidayApi;
        this.pastebin = pastebin;
        this.knownHolidays = new ArrayList<>();
        this.customHoliday = new ArrayList<>();
        this.userHoliday = new Holiday();
        newCustomHolidays = new HashMap<>();
    }

    /**
     * Gets a list of holidays for a specified country and date using the model.
     * @return A list of holidays from the model.
     */
    public List<Holiday> getHoliday(){
        if(country == null || localDate == null){
            return null;
        }
        List<Holiday> holidayList = holidayApi.getHoliday(this.country, this.localDate);
        if(holidayList.size() == 0){
            return null;
        }
        for(Holiday h: holidayList){
            knownHolidays.add(h);
        }
        return holidayList;
    }


    /**
     * Gets a list of cached holidays for a specified country and date using the model.
     * @return A cached list of holidays from the model.
     */
    public List<Holiday> getCachedHoliday(){
        if(country == null || localDate == null){
            return null;
        }
        List<String> lst;
        List<Holiday> holidayList = new ArrayList<>();
        String date = this.localDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        if (checkHolidayExists(date) != null) {
            lst = checkHolidayExists(date);
            for(String s: lst){
                if (!s.equals("No Holidays!")) {
                    Gson gson = new Gson();
                    Holiday h = gson.fromJson(s, Holiday.class);
                    knownHolidays.add(h);
                    holidayList.add(h);
                }
            }
        }
        return holidayList;
    }

    /**
     * Creates a paste on the PasteBin server containing all known holidays for the current month.
     * @return A link to the paste.
     */
    public String pastePaste(){
        if(getKnownHolidays().size() == 0){
            return "No holidays";
        }else{
            String holidays = "";
            for(Holiday h: knownHolidays){
                if(h.getDate_month() == localDate.getMonth().getValue()){
                    holidays += h.calendarString() + "\n";
                }
            }
            System.out.println(holidays);
            return pastebin.sendPaste(holidays);
        }
    }

    /**
     * Checks whether a holiday exists on this day when dealing with custom holidays.
     * @return A boolean dictating if a holiday exists on this day already.
     */
    public boolean check(){
        for(Holiday h: knownHolidays){
            if(customDate != null){
                String date = customDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                System.out.println(date);
                System.out.println(h.getDate());
                if(date.equals(h.getDate())){
                    return true;
                }
                return false;
                }
            }
        return false;
    }


    /**
     * Sets the date for the custom holiday.
     * @param customDate The date we want for the custom holiday.
     */
    public void setCustomDate(String customDate){
        if(customDate!= null){
            this.customDate = LocalDate.parse(customDate);
        }
    }

    /**
     * Retrieve the date we have stored for the custom holiday.
     * @return The custom holidays date.
     */
    public LocalDate getCustomDate(){
        return this.customDate;
    }

    /**
     * Creates the new custom holiday in the format of the Holiday class and stores it in a HashMap.
     * @param customHoliday The string containing information on the custom holiday.
     */
    public void setCustomHoliday(List<String> customHoliday){
        this.customHoliday = customHoliday;
        userHoliday.setName(customHoliday.get(0));
        userHoliday.setName_local(customHoliday.get(1));
        userHoliday.setLanguage(customHoliday.get(2));
        userHoliday.setDescription(customHoliday.get(3));
        userHoliday.setCountry(this.country);
        userHoliday.setLocation(customHoliday.get(4));
        userHoliday.setType(customHoliday.get(5));
        userHoliday.setDate(this.customDate.toString());
        userHoliday.setDate_year(this.customDate.getYear());
        userHoliday.setDate_month(this.customDate.getMonthValue());
        userHoliday.setDate_day(this.customDate.getDayOfMonth());
        userHoliday.setWeek_day(this.customDate.getDayOfWeek().name());
        newCustomHolidays.put(customDate, userHoliday);
    }

    /**
     * Returns the string list of details on the custom holiday.
     * @return The string list of details on the custom holiday.
     */
    public List<String> getCustomHoliday(){
        return this.customHoliday;
    }

    /**
     * Sets the new custom holiday.
     * @param userHoliday The holiday we want to set as the custom holiday.
     */
    public void setUserHoliday(Holiday userHoliday){
        this.userHoliday = userHoliday;
    }

    /**
     * Get the custom holiday.
     * @return The custom holiday.
     */
    public Holiday getUserHoliday(){
        return this.userHoliday;
    }

    /**
     * Returns the HashMap with all the custom holidays in it.
     * @return The HashMap of custom holidays.
     */
    public Map<LocalDate, Holiday> getNewCustomHolidays() {
        return newCustomHolidays;
    }

    /**
     * Remove a holiday from the HashMap by date.
     * @param date The date of the holiday we want to remove.
     */
    public void removeUpdatedElement(LocalDate date){
        this.newCustomHolidays.remove(date);
    }

    /**
     * Get the current date shown by the calendar.
     * @return The current date shown by the calendar.
     */
    public LocalDate getLocalDate() {
        return localDate;
    }

    /**
     * Set the date of the controller.
     * @param localDate The date we want to change to.
     */
    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    /**
     * Get the country the user selected.
     * @return The current country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Changes the country we have stored.
     * @param country The new country we want to set to.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns a list of all known holidays for the current month.
     * @return A list of all known holidays for the current month.
     */
    public List<Holiday> getKnownHolidays() {
        return knownHolidays;
    }

    /**
     * Clears the list of holidays.
     */
    public void clearKnownHolidays(){
        knownHolidays.clear();
    }

    /**
     * Check whether the current data exists in the cache.
     * @param d The date we are checking the data against.
     * @return Whether we can use the cache or not.
     */
    public boolean checkUseCache(LocalDate d) {
        String date = d.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        return checkHolidayExists(date) != null && checkHolidayExists(date).size() > 0 && isApiOnline();
    }

    /**
     * Check whether we are using the online API.
     * @return Whether we are using the online API or not.
     */
    public boolean isApiOnline(){
        return holidayApi.isOnline();
    }
}