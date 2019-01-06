
package com.example.krvell.weatherappv3;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private ImageView iconView;
    private TextView cityName;
    private TextView temp;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;

    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconView = (ImageView)findViewById(R.id.thumbailIcon);
        cityName = (TextView)findViewById(R.id.cityText);
        temp = (TextView)findViewById(R.id.tempText);
        description = (TextView)findViewById(R.id.cloudText);
        humidity = (TextView)findViewById(R.id.humiText);
        pressure = (TextView)findViewById(R.id.pressText);
        wind = (TextView)findViewById(R.id.windText);
        sunrise = (TextView)findViewById(R.id.riseText);
        sunset = (TextView)findViewById(R.id.setText);
        updated = (TextView)findViewById(R.id.updateText);

        renderWeatherData("Veseli nad Moravou,CZ");
    }

    public void renderWeatherData(String city) {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric&appid=e167978f0bd487bf5bc7529f47cfe277"});
    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));
            weather = JSONWeatherParser.getWeather(data);
            Log.v("Data: ",weather.place.getCity());

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.change_cityId) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

