package holiday.model.output;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class PastebinOnline implements Pastebin {
    private final String apiKey;

    public PastebinOnline(){
        this.apiKey = System.getenv("PASTEBIN_API_KEY");
    }

    /**
     * Notifies the user if a paste was successful and returns a link to the paste.
     * @param text What we want to upload to the PasteBin server.
     * @return A link to the paste.
     */
    @Override
    public String sendPaste(String text){
        try{
            URL url = new URL("https://pastebin.com/api/api_post.php");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");
            String myString = "api_dev_key=" + apiKey + "&api_paste_code=" + text + "&api_option=paste" + "&api_paste_expire_date=10M";
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            byte[] bytes = myString.getBytes(StandardCharsets.UTF_8);
            String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);
            httpConn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(utf8EncodedString);
            writer.flush();
            writer.close();
            httpConn.getOutputStream().close();

            InputStream responseStream;
            int responseCode = httpConn.getResponseCode();
            if(responseCode / 100 == 2){
                responseStream = httpConn.getInputStream();
            }
            else{
                responseStream = httpConn.getErrorStream();
            }

            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";
            System.out.println(response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Failed";
    }



}