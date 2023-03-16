package holiday.model.output;

public class PastebinOffline implements Pastebin {
    /**
     * Notifies the user if a paste was successful and returns a string (offline only).
     * @param text What we want to upload to the PasteBin server.
     * @return A string denoting success.
     */
    @Override
    public String sendPaste(String text) {
        return "Report sent";
    }
}