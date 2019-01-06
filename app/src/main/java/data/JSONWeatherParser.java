package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Place;
import model.Weather;
import util.utils;

public class JSONWeatherParser {
    public static Weather getWeather(String data)
    {
        Weather weather = new Weather();

        //json object
        try {
            JSONObject jsonObject = new JSONObject(data);

            Place place = new Place();

            JSONObject coordObj = utils.getObject("coord",jsonObject);
            place.setLat(utils.getFloat("lat",coordObj));
            place.setLon(utils.getFloat("lon",coordObj));

            JSONObject sysObj = utils.getObject("sys",jsonObject);
            place.setCountry(utils.getString("country",sysObj));
            place.setLastUpdate(utils.getInt("dt",jsonObject));
            place.setSunrise(utils.getInt("sunrise",sysObj));
            place.setSunset(utils.getInt("sunset",sysObj));
            place.setCity(utils.getString("name",jsonObject));
            weather.place = place;

            //get weather info
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(utils.getInt("id",jsonWeather));
            weather.currentCondition.setDescription(utils.getString("description",jsonWeather));
            weather.currentCondition.setIcon(utils.getString("icon",jsonWeather));
            weather.currentCondition.setCondition(utils.getString("main",jsonWeather));

            JSONObject windObj = utils.getObject("wind",jsonObject);
            weather.wind.setSpeed(utils.getFloat("speed",windObj));
            weather.wind.setDeg(utils.getFloat("deg",windObj));

            JSONObject cloudObj = utils.getObject("clouds",jsonObject);
            weather.clouds.setPrecipitation(utils.getInt("all",cloudObj));

            return weather;

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }
}
