package holiday.model;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String dbName = "holidays.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;

    /**
     * Creates a new SQL database.
     * @return A message indicating if we succeeded.
     */
    public static String createDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            System.out.println("Database already created");
            return "Database already created";
        }
        try (Connection ignored = DriverManager.getConnection(dbURL)) {
            System.out.println("A new database has been created.");
            return "A new database has been created.";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Removes the database file by deleting it.
     * @return A message indicating if we succeeded.
     */
    public static String removeDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            boolean result = dbFile.delete();
            if (!result) {
                System.out.println("Couldn't delete existing db file");
                return "Couldn't delete existing db file";
            } else {
                System.out.println("Removed existing DB file.");
                return "Removed existing DB file.";
            }
        } else {
            System.out.println("No existing DB file.");
            return "No existing DB file.";
        }
    }

    /**
     * Clears the contents of the cache.
     */
    public static void clearCache() {
        String clearCache =
                """
                DELETE FROM holidayInfo
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(clearCache);

            System.out.println("Created tables");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates new tables for the database where we will store our holidays.
     * @return A message indicating if we succeeded.
     */
    public static String setupDB() {
        String createHolidaySQL =
                """
                        CREATE TABLE IF NOT EXISTS holidayInfo (
                            serialString text NOT NULL,
                            fulldate text NOT NULL,
                            PRIMARY KEY(serialString, fulldate)
                        );
                        """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(createHolidaySQL);

            System.out.println("Created tables");
            return "Successfully created table";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Adds a new holiday to the database.
     * @param serializedString A string indicating
     * @param dateOfHoliday
     * @return
     */
    public static String addHoliday(String serializedString, String dateOfHoliday) {
        String addHoliday =
                """
                INSERT INTO holidayInfo(serialString, fullDate) VALUES
                    (?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement statement = conn.prepareStatement(addHoliday)) {
            statement.setString(1, serializedString);
            statement.setString(2, dateOfHoliday);
            statement.executeUpdate();
            System.out.println("Added holiday info");
            return "Added holiday info";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }


    /**
     * Checks if a holiday exists in the database.
     * @param dateToCheck The date we want to check if any holidays occur on are stored.
     * @return A list of holidays stored in the cache for the specified date.
     */
    public static List<String> checkHolidayExists(String dateToCheck){
        List<String> lst = new ArrayList<String>();
        String checkHoliday =
                """
                SELECT *
                FROM holidayInfo WHERE fullDate IS ?
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement statement = conn.prepareStatement(checkHoliday)) {
            statement.setString(1, dateToCheck);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                lst.add(results.getString("serialString"));
            }
            return lst;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}