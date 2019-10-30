package utils;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {

    public SendNotification(String message, String heading, String notificationKey) {

        notificationKey = "c6ec71d0-5167-4968-b718-e7ef94e22bdd";

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
