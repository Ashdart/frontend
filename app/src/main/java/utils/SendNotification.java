package utils;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {

    public SendNotification(String message, String heading, String notificationKey) {

        notificationKey = "2c1d8817-a37b-40cd-bbbf-b6e093b11e40";

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
