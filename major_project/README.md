**Input API:** Holidays-API

**Output API:** Pastebin

## Running app
 To run the app simply type gradle run --args "{} {}" and replace {} with online or offline depending on what version of the API you want to test.
 To run online API keys are required from Pastebin and https://www.abstractapi.com/api/holidays-api
 Otherwise Offline will mock the online APIs.
 
## Worldmap
After running the app you will see a worldmap. Clicking on a country and then clicking on the select country button will lock that choice for the remainder of the session.
To change countries close and reopen the app. After selecting a country you will be greeted by the calendar.

## Calendar
Simply click on a date to view if there are any holidays available for that day. If there are non the cell will change to red and display "No Holidays!" in the cell. If there are holidays you will then see a list of holidays for that day.
To select a holiday double click on any day in the list to view more information which will open as a pop-up. The offline version will have holidays for the 1st and 2nd of every month but none for the rest of the month.

## Pastes
To send a paste after having the calendar screen open, all known holidays (those displayed) will be recorded and to send the paste simply click the send paste button on the top right of the screen.

## Caching
Running the app will create a new cache. The cache will only work when the inputAPI is online. To use the cache simply click on a day to retrieve the data. Upon re-opening the app or changing months
and coming back when clicking on the same day the user will be prompted to either choose to use the cached data (click "OK") or retrieve fresh data from the API
(clicking "Cancel"). This works when there are no holidays for a day as well. Sending a paste of cached holidays works by saving all known holidays (those displayed on the GUI) which will be recorded. To send the paste simply click the send paste button on the top right of the screen.

## Added features
### About section
The about section can be accessed at anytime by clicking on the menu and clicking the About menu item.

### SplashScreen
The splashscreen will remain visible until 15 seconds have passed before disappearing and the program can run as normal. To change the display time simply change the value of loadTime variable in the MainWindow.java file.

# Exam
To access the new feature (custom holiday) first select a country. The way the app runs normally is the same. Then click the "Create Custom Holiday" button.
First enter a date for the holiday and then click "Set date" button. Then fill in the fields and click the button to allow the app to display the custom holiday.
The custom holiday will appear with blue background and will always be rendered when on the correct month with no further action needed. To check if a holiday exists on a selected day click on the different shaded part at the bottom of the cell.
For custom holidays if there is no holiday no change will occur. If there is it will inform the user and update the view to the new holiday from the API. If a holiday or none is already displayed and the user tries to add a custom holiday to that day it will fail and inform the user.
API holidays are displayed with green backgrounds as per previous versions and No holiday dates will now have a red background.

### References
Inspiration from calendar was gathered from https://github.com/SirGoose3432/javafx-calendar. This model was changed to suit the needs of the project. This relates to the CustomCalendar class.
Splash image retrieved from https://unsplash.com/photos/Q1p7bh3SHj8.

