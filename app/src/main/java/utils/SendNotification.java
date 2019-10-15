package utils;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {

    public SendNotification(String message, String heading, String notificationKey) {

        notificationKey = "7ec55ea4-da59-46ab-8cde-5a549a6d992b";

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
