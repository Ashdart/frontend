package utils;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {

    public SendNotification(String message, String heading, String notificationKey) {

        notificationKey = "a2f92471-769f-4877-b51d-94e15d767d02";

        try {
            JSONObject notificationContent = new JSONObject("{" +
                    "'contents': {'en':'" + message + "'}, " +
                    "'include_player_ids': ['" + notificationKey + "','" + notificationKey + "'], " +
                    "'headings': {'en':'"+ heading +"'}}");

            OneSignal.postNotification(notificationContent,null);
            System.out.println(notificationContent);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
