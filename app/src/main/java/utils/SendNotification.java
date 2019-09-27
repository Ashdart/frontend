package utils;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {

    public SendNotification(String message, String heading, String notificationKey) {

        notificationKey = "INSERTAR LA notificationKey QUE SE GENERA EN LA BDD"

        try {
            JSONObject notificationContent = new JSONObject("{'contents':{'en':'" + message + "'}," +
                    "'includes_player_ids':['" + notificationKey + "']," +
                    "'headings':{'en': '" + heading + "'}}");
            OneSignal.postNotification(notificationContent,null);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
