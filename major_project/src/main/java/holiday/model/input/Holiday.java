package holiday.model.input;

public class Holiday {
    private String name;
    private String name_local;
    private String language;
    private String description;
    private String country;
    private String location;
    private String type;
    private String date;
    private int date_year;
    private int date_month;
    private int date_day;
    private String week_day;

    /**
     * Get the name of the holiday.
     * @return Name of the holiday.
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the holiday.
     * @param name The new name we want to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the local name of the holiday.
     * @return Local name of the holiday.
     */
    public String getName_local() {
        return name_local;
    }

    /**
     * Changes the local name of the holiday.
     * @param name_local The new local name we want to set.
     */
    public void setName_local(String name_local) {
        this.name_local = name_local;
    }

    /**
     * Get the language of the holiday.
     * @return Language of the holiday.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Changes the language of the holiday.
     * @param language The new language we want to set.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Get the description of the holiday.
     * @return Description of the holiday.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Changes the description of the holiday.
     * @param description The new description we want to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the country in which the holiday occurs.
     * @return Country where the holiday occurs.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the country in which the holiday occurs.
     * @param country The new country we want to set the holiday in
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get the location in which the holiday occurs.
     * @return Location where the holiday occurs.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the location in which the holiday occurs.
     * @param location The new location we want to set the holiday in.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get the type of holiday.
     * @return Type of holiday.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of holiday.
     * @param type The new type of holiday.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the date on which the holiday occurs.
     * @return Date when the holiday occurs.
     */
    public String getDate() {
        return date;
    }

    /**
     * Set the date of holiday.
     * @param date The new date of holiday.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get the year in which the holiday occurs.
     * @return Year when the holiday occurs.
     */
    public int getDate_year() {
        return date_year;
    }

    /**
     * Set the year of holiday.
     * @param date_year The new year in which holiday occurs.
     */
    public void setDate_year(int date_year) {
        this.date_year = date_year;
    }

    /**
     * Get the month in which the holiday occurs.
     * @return Month when the holiday occurs.
     */
    public int getDate_month() {
        return date_month;
    }

    /**
     * Set the month of holiday.
     * @param date_month The new month in which holiday occurs.
     */
    public void setDate_month(int date_month) {
        this.date_month = date_month;
    }

    /**
     * Get the day on which the holiday occurs.
     * @return Day when the holiday occurs.
     */
    public int getDate_day() {
        return date_day;
    }

    /**
     * Set the day of holiday.
     * @param date_day The new day on which holiday occurs.
     */
    public void setDate_day(int date_day) {
        this.date_day = date_day;
    }

    /**
     * Get the day of the week on which the holiday occurs.
     * @return Day of the week when the holiday occurs.
     */
    public String getWeek_day() {
        return week_day;
    }

    /**
     * Set the weekday of holiday.
     * @param week_day The new weekday on which holiday occurs.
     */
    public void setWeek_day(String week_day) {
        this.week_day = week_day;
    }

    @Override
    public String toString(){
        return name + ", " + country;
    }

    /**
     * Return a string representation containing all information about the holiday.
     * @return All information about the holiday.
     */
    public String calendarString(){
        if(this.name_local.isEmpty()){
            this.name_local = "N/A";
        }
        if(this.language.isEmpty()){
            this.language = "N/A";
        }
        if(this.description.isEmpty()){
            this.description = "N/A";
        }
        return "Name: " + name + ", Local name: " + name_local + ", Language: " + language + "\nDescription: "
                + description + ", Country: " + country + ", Location: " + location + ", Type: " + type
                +  "\nDate: " + date + ", Year: " + date_year + ", Month: " + date_month + ", Day: "
                + date_day + ", Weekday: " + week_day + "\n";
    }

    /**
     * Converts the string to a string Json format for storage in SQL table.
     * @return a String that has Json formatting.
     */
    public String toJsonString(){
        return "{\"name\":" + '"' + name + '"' +",\"name_local\":" + '"' + name_local+ '"' + ",\"language\":" + '"'
                + language + '"' + ",\"description\":" + '"' +  description + '"' +  ",\"country\":" + '"' +  country
                + '"' +  ",\"location\":" + '"' +  location + '"' +  ",\"type\":" + '"' +  type + '"' +  ",\"date\":"
                + '"' +  date + '"' + ",\"date_year\":" + '"' + date_year + '"' + ",\"date_month\":" + '"' + date_month
                + '"' + " ,\"date_day\":" + '"' + date_day + '"' + ",\"week_day\":" + '"' + week_day + '"' + "}";
    }


}