package holiday.model.output;

public interface Pastebin {
    /**
     * Notifies the user if a paste was successful and returns a link to the paste.
     * @param text What we want to upload to the PasteBin server.
     * @return A link to the paste.
     */
    String sendPaste(String text);
}