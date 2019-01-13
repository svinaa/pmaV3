package data;

import android.app.Activity;
import android.content.SharedPreferences;

public class CityToSearch {
    SharedPreferences preferences;

    public CityToSearch(Activity activity) {
        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return preferences.getString("city","Prague,CZ");
    }

    public void setCity(String city) {
        preferences.edit().putString("city",city).commit();
    }
}
