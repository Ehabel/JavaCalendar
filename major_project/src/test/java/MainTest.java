import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import holiday.model.Database;
import holiday.model.UserFacade;
import holiday.model.input.Holiday;
import holiday.model.input.HolidayApi;
import holiday.model.input.HolidayOffline;
import holiday.model.input.HolidayOnline;
import holiday.model.output.Pastebin;
import holiday.model.output.PastebinOffline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static holiday.model.Database.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainTest {
    private UserFacade userFacade;
    private HolidayApi holidayApi;
    private Pastebin pastebin;
    private PastebinOffline pastebinOffline;
    private HolidayOffline holidayOffline;
    private HolidayOnline holidayOnline;
    private Database database;

    private List<Holiday> testHoliday(){
        String h = "[{\"name\":\"Test Day\",\"name_local\":\"\",\"language\":\"\",\"description\":\"\",\"country\":\"AU\",\"location\":\"Australia - Victoria\",\"type\":\"Local holiday\",\"date\":\"1/1/2022\",\"date_year\":\"2022\",\"date_month\":\"1\",\"date_day\":\"1\",\"week_day\":\"Sunday\"}]";
        JsonArray holidaysList = (JsonArray) JsonParser.parseString(h);
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Holiday>>() {}.getType();
        List<Holiday> allHolidays = gson.fromJson(holidaysList, collectionType);
        return allHolidays;
    }

    private List<Holiday> multipleTestHoliday(){
        String h = "[{\"name\":\"Another holiday\",\"name_local\":\"\",\"language\":\"\",\"description\":\"First holiday multi\",\"country\":\"AU\",\"location\":\"Australia - NSW\",\"type\":\"Local holiday\",\"date\":\"3/5/2022\",\"date_year\":\"2022\",\"date_month\":\"5\",\"date_day\":\"3\",\"week_day\":\"Sunday\"}," +
                "{\"name\":\"Holiday 2\",\"name_local\":\"\",\"language\":\"\",\"description\":\"Second holiday multi\",\"country\":\"AU\",\"location\":\"Australia - Victoria\",\"type\":\"Local holiday 2\",\"date\":\"3/5/2022\",\"date_year\":\"2022\",\"date_month\":\"5\",\"date_day\":\"3\",\"week_day\":\"Sunday\"}]";
        JsonArray holidaysList = (JsonArray) JsonParser.parseString(h);
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Holiday>>() {}.getType();
        List<Holiday> allHolidays = gson.fromJson(holidaysList, collectionType);
        return allHolidays;
    }

    private List<String> singleCacheTestHoliday(){
        String s = "{\"name\":\"Test Day\",\"name_local\":\"\",\"language\":\"\",\"description\":\"\",\"country\":\"AU\",\"location\":\"Australia - Victoria\",\"type\":\"Local holiday\",\"date\":\"1/1/2022\",\"date_year\":\"2022\",\"date_month\":\"1\",\"date_day\":\"1\",\"week_day\":\"Sunday\"}";
        List<String> h = new ArrayList<String>();
        h.add(s);
        return h;
    }

    private List<String> multipleCacheTestHoliday(){
        String first = "{\"name\":\"Another holiday\",\"name_local\":\"\",\"language\":\"\",\"description\":\"First holiday multi\",\"country\":\"AU\",\"location\":\"Australia - NSW\",\"type\":\"Local holiday\",\"date\":\"3/5/2022\",\"date_year\":\"2022\",\"date_month\":\"5\",\"date_day\":\"3\",\"week_day\":\"Sunday\"}";
        String second = "{\"name\":\"Holiday 2\",\"name_local\":\"\",\"language\":\"\",\"description\":\"Second holiday multi\",\"country\":\"AU\",\"location\":\"Australia - Victoria\",\"type\":\"Local holiday 2\",\"date\":\"3/5/2022\",\"date_year\":\"2022\",\"date_month\":\"5\",\"date_day\":\"3\",\"week_day\":\"Sunday\"}";
        List<String> h = new ArrayList<String>();
        h.add(first);
        h.add(second);
        return h;
    }

    private List<Holiday> noHolidays(){
        List<Holiday> allHolidays = new ArrayList<>();
        return allHolidays;
    }

    private List<String> customList(){
        List<String> s = new ArrayList<>();
        s.add("A name");
        s.add("N/A");
        s.add("N/A");
        s.add("A description");
        s.add("Location");
        s.add("Type");
        return s;
    }

    private List<String> customListTwo(){
        List<String> sTwo = new ArrayList<>();
        sTwo.add("A new name");
        sTwo.add("Not empty");
        sTwo.add("N/A");
        sTwo.add("A new description");
        sTwo.add("No Location");
        sTwo.add("N/A");
        return sTwo;
    }

    @BeforeEach
    public void setup(){
        holidayApi = mock(HolidayApi.class);
        pastebin = mock(Pastebin.class);
        database = mock(Database.class);
        pastebinOffline = new PastebinOffline();
        holidayOffline = new HolidayOffline();
        holidayOnline = new HolidayOnline();
        userFacade = new UserFacade(holidayApi, pastebin);
    }

    @Test
    public void inputApiCallAndExists(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        assertNotNull(userFacade.getHoliday());
        verify(holidayApi, times(1)).getHoliday("AU", d);
    }

    @Test
    public void controllerReturnsCorrectHolidays(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        assertNotNull(userFacade.getKnownHolidays());
    }

    @Test
    public void controllerReturnsNoCountry(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry(null);
        userFacade.setLocalDate(d);
        assertNull(userFacade.getHoliday());
    }

    @Test
    public void controllerReturnsNoDate(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(null);
        assertNull(userFacade.getHoliday());
    }

    @Test
    public void controllerReturnsNoDateorCountry(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry(null);
        userFacade.setLocalDate(null);
        assertNull(userFacade.getHoliday());
    }

    @Test
    public void controllerReturnsCorrectNumHolidays(){
        LocalDate d = LocalDate.parse("2022-01-01");
        String country = "AU";
        when(holidayApi.getHoliday(country, d)).thenReturn(testHoliday());
        userFacade.setCountry(country);
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        assertTrue(new ReflectionEquals(testHoliday().get(0)).matches(userFacade.getKnownHolidays().get(0)));
    }

    @Test
    public void outPutApiExistsAndExecuteOnce(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        when(pastebin.sendPaste(any(String.class))).thenReturn("Success!");
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        assertNotNull(userFacade.pastePaste());
        verify(pastebin, times(1)).sendPaste(any(String.class));
    }

    @Test
    public void outPutApiFails(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        when(pastebin.sendPaste(any(String.class))).thenReturn("Success!");
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        assertEquals(userFacade.pastePaste(), "No holidays");
    }

    @Test
    public void noHolidayTest(){
        userFacade.getHoliday();
        assertNull(userFacade.getHoliday());
    }

    @Test
    public void facadeStoreDate(){
        LocalDate d = LocalDate.parse("2022-01-01");
        userFacade.setLocalDate(d);
        assertEquals(d, userFacade.getLocalDate());
    }

    @Test
    public void facadeNullDate(){
        userFacade.setLocalDate(null);
        assertNull(userFacade.getLocalDate());
    }

    @Test
    public void facadeStoreCountry(){
        userFacade.setCountry("AU");
        assertEquals("AU", userFacade.getCountry());
    }

    @Test
    public void facadeNullCountry(){
        userFacade.setCountry(null);
        assertNull(userFacade.getCountry());
    }

    @Test
    public void sendPasteTest(){
        userFacade = new UserFacade(holidayApi, pastebinOffline);
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        assertNotNull(userFacade.pastePaste());
        assertEquals(userFacade.pastePaste(), "Report sent");
    }

    @Test
    public void sendPasteNoHolidaysTest(){
        userFacade = new UserFacade(holidayApi, pastebinOffline);
        LocalDate d = LocalDate.parse("2022-01-01");
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        assertNotNull(userFacade.pastePaste());
        assertEquals(userFacade.pastePaste(), "No holidays");
    }

    @Test
    public void sizeHolidays(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        assertEquals(userFacade.getKnownHolidays().size(), 1);
    }

    @Test
    public void clearHolidays(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        assertEquals(userFacade.getKnownHolidays().size(), 1);
        userFacade.clearKnownHolidays();
        assertEquals(userFacade.getKnownHolidays().size(), 0);
    }

    @Test
    public void addHolidaysMultiple(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        userFacade.getHoliday();
        userFacade.getHoliday();
        assertEquals(userFacade.getKnownHolidays().size(), 3);
    }

    @Test
    public void clearMultipleHolidays(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        userFacade.getHoliday();
        userFacade.getHoliday();
        assertEquals(userFacade.getKnownHolidays().size(), 3);
        userFacade.clearKnownHolidays();
        assertEquals(userFacade.getKnownHolidays().size(), 0);
    }

    @Test
    public void clearMultipleHolidaysReAdd(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        userFacade.getHoliday();
        userFacade.getHoliday();
        assertEquals(userFacade.getKnownHolidays().size(), 3);
        userFacade.clearKnownHolidays();
        assertEquals(userFacade.getKnownHolidays().size(), 0);
        userFacade.getHoliday();
        userFacade.getHoliday();
        assertEquals(userFacade.getKnownHolidays().size(), 2);
    }

    @Test
    public void holidaysOfflineTest(){
        userFacade = new UserFacade(holidayOffline, pastebin);
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        assertNotNull(userFacade.getHoliday());
        assertEquals(userFacade.getKnownHolidays().size(), 1);
    }

    @Test
    public void holidaysOfflineCacheTest(){
        userFacade = new UserFacade(holidayOffline, pastebin);
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        assertFalse(userFacade.checkUseCache(d));
    }

    @Test
    public void holidaysOfflineTestEmpty(){
        userFacade = new UserFacade(holidayOffline, pastebin);
        LocalDate d = LocalDate.parse("2022-01-27");
        when(holidayApi.getHoliday("AU", d)).thenReturn(noHolidays());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        assertNull(userFacade.getHoliday());
        assertEquals(userFacade.getKnownHolidays().size(), 0);
    }

    @Test
    public void holidaysOfflineTestMultiple(){
        userFacade = new UserFacade(holidayOffline, pastebin);
        LocalDate d = LocalDate.parse("2022-01-02");
        when(holidayApi.getHoliday("AU", d)).thenReturn(multipleTestHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        assertNotNull(userFacade.getHoliday());
        assertEquals(userFacade.getKnownHolidays().size(), 2);
    }

    @Test
    public void isHolidayAPIOffline(){
        assertFalse(holidayOffline.isOnline());
    }

    @Test
    public void isHolidayAPIOnline(){
        assertTrue(holidayOnline.isOnline());
    }

    @Test
    public void databaseTest(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(Database::createDB).thenReturn("Success");
            assertEquals(createDB(), "Success");
        }
    }

    @Test
    public void databaseRemoveTest(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(Database::removeDB).thenReturn("Removed existing DB file.");
            assertEquals(removeDB(), "Removed existing DB file.");
        }
    }

    @Test
    public void databaseSetupTableTest(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(Database::setupDB).thenReturn("Successfully created table");
            assertEquals(setupDB(), "Successfully created table");
        }
    }

    @Test
    public void databaseAddDataTest(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.addHoliday(anyString(), anyString())).thenReturn("Added holiday info");
            assertEquals(addHoliday(anyString(), anyString()), "Added holiday info");
        }
    }

    @Test
    public void databaseGetSingleDataTest(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(singleCacheTestHoliday());
            LocalDate d = LocalDate.parse("2022-01-01");
            userFacade.setLocalDate(d);
            userFacade.setCountry("AU");
            assertNotNull(userFacade.getCachedHoliday());
            assertTrue(new ReflectionEquals(testHoliday()).matches(userFacade.getKnownHolidays()));
        }
    }

    @Test
    public void databasechechUseCache(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(singleCacheTestHoliday());
            when(holidayApi.isOnline()).thenReturn(true);
            LocalDate d = LocalDate.parse("2022-01-01");
            userFacade.setLocalDate(d);
            userFacade.setCountry("AU");
            assertNotNull(userFacade.getCachedHoliday());
            assertTrue(userFacade.checkUseCache(d));
        }
    }

    @Test
    public void databasechechUseCacheFalse(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(singleCacheTestHoliday());
            when(holidayApi.isOnline()).thenReturn(false);
            LocalDate d = LocalDate.parse("2022-01-01");
            userFacade.setLocalDate(d);
            userFacade.setCountry("AU");
            assertNotNull(userFacade.getCachedHoliday());
            assertFalse(userFacade.checkUseCache(d));
        }
    }

    @Test
    public void databaseCheckMultipleDataTest(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(multipleCacheTestHoliday());
            LocalDate d = LocalDate.parse("2022-01-02");
            userFacade.setLocalDate(d);
            userFacade.setCountry("AU");
            assertNotNull(userFacade.getCachedHoliday());
            assertTrue(new ReflectionEquals(multipleTestHoliday()).matches(userFacade.getKnownHolidays()));
        }
    }

    @Test
    public void databaseCheckDataTestNullDate(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(multipleCacheTestHoliday());
            userFacade.setLocalDate(null);
            userFacade.setCountry("AU");
            assertNull(userFacade.getCachedHoliday());
        }
    }

    @Test
    public void databaseCheckDataTestNullCountry(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(multipleCacheTestHoliday());
            LocalDate d = LocalDate.parse("2022-01-02");
            userFacade.setLocalDate(d);
            userFacade.setCountry(null);
            assertNull(userFacade.getCachedHoliday());
        }
    }

    @Test
    public void databaseCheckDataTestNoHolidays(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(null);
            LocalDate d = LocalDate.parse("2022-01-02");
            userFacade.setLocalDate(d);
            userFacade.setCountry("AU");
            assertNotNull(userFacade.getCachedHoliday());
            assertEquals(userFacade.getKnownHolidays().size(), 0);
        }
    }

    @Test
    public void databaseCheckClearTestNoHolidays(){
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(multipleCacheTestHoliday());
            LocalDate d = LocalDate.parse("2022-01-02");
            userFacade.setLocalDate(d);
            userFacade.setCountry("AU");
            assertNotNull(userFacade.getCachedHoliday());
            userFacade.clearKnownHolidays();
            assertEquals(userFacade.getKnownHolidays().size(), 0);
        }
    }

    @Test
    public void databaseCheckCacheAndAddTestHolidays(){
        LocalDate d = LocalDate.parse("2022-01-02");
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(multipleCacheTestHoliday());
            when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
            userFacade.setLocalDate(d);
            userFacade.setCountry("AU");
            assertNotNull(userFacade.getCachedHoliday());
            userFacade.getHoliday();
            assertEquals(userFacade.getKnownHolidays().size(), 3);
        }
    }

    @Test
    public void databaseCheckCacheAndAddTestHolidaysMatch(){
        LocalDate d = LocalDate.parse("2022-01-02");
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(multipleCacheTestHoliday());
            when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
            userFacade.setLocalDate(d);
            userFacade.setCountry("AU");
            userFacade.getCachedHoliday();
            userFacade.getHoliday();
            assertTrue(new ReflectionEquals(testHoliday().get(0)).matches(userFacade.getKnownHolidays().get(2)));
            assertTrue(new ReflectionEquals(multipleTestHoliday().get(0)).matches(userFacade.getKnownHolidays().get(0)));
            assertTrue(new ReflectionEquals(multipleTestHoliday().get(1)).matches(userFacade.getKnownHolidays().get(1)));
        }
    }

    @Test
    public void databaseCheckCacheAndAddTestHolidaysClear(){
        LocalDate d = LocalDate.parse("2022-01-02");
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(multipleCacheTestHoliday());
            when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
            userFacade.setLocalDate(d);
            userFacade.setCountry("AU");
            assertNotNull(userFacade.getCachedHoliday());
            userFacade.getHoliday();
            userFacade.clearKnownHolidays();
            assertEquals(userFacade.getKnownHolidays().size(), 0);
        }
    }

    @Test
    public void databaseExecutesTwice(){
        LocalDate d = LocalDate.parse("2022-01-02");
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(multipleCacheTestHoliday());
            userFacade.setLocalDate(d);
            userFacade.setCountry("AU");
            assertNotNull(userFacade.getCachedHoliday());
            mock.verify(() -> Database.checkHolidayExists(anyString()), times(2));
        }
    }

    @Test
    public void databaseNoExecutesNull(){
        LocalDate d = LocalDate.parse("2022-01-02");
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(multipleCacheTestHoliday());
            userFacade.setLocalDate(null);
            userFacade.setCountry("AU");
            assertNull(userFacade.getCachedHoliday());
            mock.verify(() -> Database.checkHolidayExists(anyString()), times(0));
        }
    }

    @Test
    public void databaseNoExecutesNullCountry(){
        LocalDate d = LocalDate.parse("2022-01-02");
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(multipleCacheTestHoliday());
            userFacade.setLocalDate(d);
            userFacade.setCountry(null);
            assertNull(userFacade.getCachedHoliday());
            mock.verify(() -> Database.checkHolidayExists(anyString()), times(0));
        }
    }

    @Test
    public void databaseNoExecutesNullBoth(){
        LocalDate d = LocalDate.parse("2022-01-02");
        try (MockedStatic<Database> mock = Mockito.mockStatic(Database.class)) {
            mock.when(() -> Database.checkHolidayExists(anyString())).thenReturn(multipleCacheTestHoliday());
            userFacade.setLocalDate(null);
            userFacade.setCountry(null);
            assertNull(userFacade.getCachedHoliday());
            mock.verify(() -> Database.checkHolidayExists(anyString()), times(0));
        }
    }

    @Test
    public void customDateNull(){
        userFacade.setCustomDate(null);
        assertNull(userFacade.getCustomDate());
    }

    @Test
    public void customDateGet(){
        LocalDate d = LocalDate.parse("2022-01-02");
        userFacade.setCustomDate(d.toString());
        assertEquals(userFacade.getCustomDate().toString(), d.toString());
    }

    @Test
    public void customHolidayExists(){
        String country = "AU";
        LocalDate d = LocalDate.parse("2022-01-02");
        userFacade.setLocalDate(d);
        userFacade.setCountry(country);
        userFacade.setCustomDate(d.toString());
        userFacade.setCustomHoliday(customList());
        assertNotNull(userFacade.getUserHoliday());
    }

    @Test
    public void noCustomHoliday(){
        String country = "AU";
        LocalDate d = LocalDate.parse("2022-01-02");
        userFacade.setLocalDate(d);
        userFacade.setCountry(country);
        assertNull(userFacade.getUserHoliday().getName());
    }

    @Test
    public void noCustomHolidayNoDate(){
        String country = "AU";
        LocalDate d = LocalDate.parse("2022-01-02");
        userFacade.setLocalDate(d);
        userFacade.setCountry(country);
        assertNull(userFacade.getUserHoliday().getDate());
    }

    @Test
    public void noCustomHolidayBoth(){
        String country = "AU";
        LocalDate d = LocalDate.parse("2022-01-02");
        userFacade.setLocalDate(d);
        userFacade.setCountry(country);
        assertNull(userFacade.getUserHoliday().getName());
        assertNull(userFacade.getUserHoliday().getDate());
    }

    @Test
    public void customHolidayExistsString(){
        String country = "AU";
        LocalDate d = LocalDate.parse("2022-01-02");
        userFacade.setLocalDate(d);
        userFacade.setCountry(country);
        userFacade.setCustomDate(d.toString());
        userFacade.setCustomHoliday(customList());
        assertNotNull(userFacade.getCustomHoliday());
    }

    @Test
    public void customHolidayEmptyString(){
        String country = "AU";
        LocalDate d = LocalDate.parse("2022-01-02");
        userFacade.setLocalDate(d);
        userFacade.setCountry(country);
        userFacade.setCustomDate(d.toString());
        assertEquals(userFacade.getCustomHoliday().size(), 0);
    }

    @Test
    public void customHolidayNull(){
        String country = "AU";
        LocalDate d = LocalDate.parse("2022-01-02");
        userFacade.setLocalDate(d);
        userFacade.setCountry(country);
        userFacade.setUserHoliday(null);
        assertNull(userFacade.getUserHoliday());
    }

    @Test
    public void customHolidaysOneItem(){
        String country = "AU";
        LocalDate d = LocalDate.parse("2022-01-02");
        userFacade.setLocalDate(d);
        userFacade.setCountry(country);
        userFacade.setCustomDate(d.toString());
        userFacade.setCustomHoliday(customList());
        assertEquals(userFacade.getNewCustomHolidays().size(), 1);
    }

    @Test
    public void customHolidaysMultipleItems(){
        String country = "AU";
        LocalDate d = LocalDate.parse("2022-01-02");
        LocalDate dTwo = LocalDate.parse("2022-02-02");
        userFacade.setLocalDate(d);
        userFacade.setCountry(country);
        userFacade.setCustomDate(d.toString());
        userFacade.setCustomHoliday(customList());
        userFacade.setCustomDate(dTwo.toString());
        userFacade.setCustomHoliday(customListTwo());
        assertEquals(userFacade.getNewCustomHolidays().size(), 2);
    }

    @Test
    public void customHolidaysRemoveItems(){
        String country = "AU";
        LocalDate d = LocalDate.parse("2022-01-02");
        LocalDate dTwo = LocalDate.parse("2022-02-02");
        userFacade.setLocalDate(d);
        userFacade.setCountry(country);
        userFacade.setCustomDate(d.toString());
        userFacade.setCustomHoliday(customList());
        userFacade.setCustomDate(dTwo.toString());
        userFacade.setCustomHoliday(customListTwo());
        userFacade.removeUpdatedElement(d);
        assertEquals(userFacade.getNewCustomHolidays().size(), 1);
    }

    @Test
    public void customHolidaysRemoveAllItems(){
        String country = "AU";
        LocalDate d = LocalDate.parse("2022-01-02");
        LocalDate dTwo = LocalDate.parse("2022-02-02");
        userFacade.setLocalDate(d);
        userFacade.setCountry(country);
        userFacade.setCustomDate(d.toString());
        userFacade.setCustomHoliday(customList());
        userFacade.setCustomDate(dTwo.toString());
        userFacade.setCustomHoliday(customListTwo());
        userFacade.removeUpdatedElement(d);
        userFacade.removeUpdatedElement(dTwo);
        assertEquals(userFacade.getNewCustomHolidays().size(), 0);
    }

    @Test
    public void checkCustomDate() {
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        userFacade.setCustomDate("2022-01-01");
        userFacade.getKnownHolidays().get(0).setDate("01/01/2022");
        assertEquals(d, userFacade.getCustomDate());
    }

    @Test
    public void checkDateIntercept(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        userFacade.setCustomDate("2022-01-01");
        userFacade.getKnownHolidays().get(0).setDate("01/01/2022");
        assertEquals(d, userFacade.getCustomDate());
        assertTrue(userFacade.check());
    }

    @Test
    public void checkDateInterceptFalse(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        userFacade.setCustomDate("2022-01-02");
        userFacade.getKnownHolidays().get(0).setDate("01/01/2022");
        assertFalse(userFacade.check());
    }

    @Test
    public void checkDateInterceptFalseNoCustom(){
        LocalDate d = LocalDate.parse("2022-01-01");
        when(holidayApi.getHoliday("AU", d)).thenReturn(testHoliday());
        userFacade.setCountry("AU");
        userFacade.setLocalDate(d);
        userFacade.getHoliday();
        assertFalse(userFacade.check());
    }

    @Test
    public void holidayPOJOTest(){
        Holiday h = new Holiday();
        h.setName("Name");
        h.setName_local("local");
        h.setLanguage("language");
        h.setDescription("d");
        h.setCountry("AU");
        h.setLocation("location");
        h.setType("type");
        h.setDate("1");
        h.setDate_day(1);
        h.setDate_year(2000);
        h.setDate_month(1);
        h.setWeek_day("Sunday");
        assertEquals(h.getName(), "Name");
        assertEquals(h.getDate(), "1");
        assertEquals(h.getDate_day(), 1);
        assertEquals(h.getDate_year(), 2000);
        assertEquals(h.getDate_month(), 1);
        assertEquals(h.getCountry(), "AU");
        assertEquals(h.getLanguage(), "language");
        assertEquals(h.getName_local(), "local");
        assertEquals(h.getDescription(), "d");
        assertEquals(h.getLocation(), "location");
        assertEquals(h.getType(), "type");
        assertEquals(h.getWeek_day(), "Sunday");
        assertEquals(h.toString(), "Name, AU");
    }

}