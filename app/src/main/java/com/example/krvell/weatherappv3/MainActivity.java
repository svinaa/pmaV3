
package com.example.krvell.weatherappv3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import data.CityToSearch;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;
import util.utils;

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
    private TextView cloudiness;
    private TextView minTemp;
    private TextView maxTemp;
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
        cloudiness = (TextView)findViewById(R.id.cloudinessText);
        minTemp = (TextView)findViewById(R.id.minTempText);
        maxTemp = (TextView)findViewById(R.id.maxTempText);
        //updated = (TextView)findViewById(R.id.updateText);

        CityToSearch cityToSearch = new CityToSearch(MainActivity.this);

        renderWeatherData(cityToSearch.getCity());
    }

    public void renderWeatherData(String city) {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric&appid=e167978f0bd487bf5bc7529f47cfe277&lang=cz"});
    }

    private class DownImgAsync extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImg();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            iconView.setImageBitmap(bitmap);

          //  super.onPostExecute(bitmap);
        }

        // ziskani obrazku
        private Bitmap downloadImg() {
            try {
                URL url = new URL(utils.ICON_URL + weather.currentCondition.getIcon() + ".png");
                Log.v("DataImg : ", url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap currentBitmap = BitmapFactory.decodeStream(input);
                return currentBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            weather.iconData = weather.currentCondition.getIcon();

            weather = JSONWeatherParser.getWeather(data);

            Log.v("Data: ",weather.currentCondition.getIcon());

            new DownImgAsync().execute(weather.iconData);

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

          /*  DateFormat dateFormat = DateFormat.getTimeInstance();

            String sunriseDate = dateFormat.format(new Date(weather.place.getSunrise()));
            String sunsetDate = dateFormat.format(new Date(weather.place.getSunset()));
            String updateDate = dateFormat.format(new Date(weather.place.getLastUpdate()));*/

            DecimalFormat decimalFormat = new DecimalFormat("#.#");

            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());
            String minTempFormat = decimalFormat.format(weather.currentCondition.getMinTemp());
            String maxTempFormat = decimalFormat.format(weather.currentCondition.getMaxTemp());


            long dvsunrise = Long.valueOf(weather.place.getSunrise()) * 1000;
            Date dfsunrise = new java.util.Date(dvsunrise);
            String sunriseValue = new SimpleDateFormat("HH:mm").format(dfsunrise);
            long dvsunset = Long.valueOf(weather.place.getSunset()) * 1000;
            Date dfsunset = new java.util.Date(dvsunset);
            String sunsetValue = new SimpleDateFormat("HH:mm").format(dfsunset);
            sunrise.setText("Sunrise : " + sunriseValue);
            sunset.setText("Sunset: " + sunsetValue);

            cityName.setText(weather.place.getCity() + ", " + weather.place.getCountry());
            temp.setText("" + tempFormat + " °C");
            description.setText("" + weather.currentCondition.getDescription());
            humidity.setText("Vlhkost: " + weather.currentCondition.getHumidity() + " %");
            pressure.setText("Tlak vzduchu: " + weather.currentCondition.getPressure() + " hPa");
            wind.setText("Rychlost větru: " + weather.wind.getSpeed() + " m/s");
            minTemp.setText("Min. teplota: " + minTempFormat + " °C");
            maxTemp.setText("Max. teplota: " + maxTempFormat + " °C");
            cloudiness.setText("Oblačnost: " + weather.clouds.getPrecipitation() + " %");
          /*  sunrise.setText("Sunrise:" + sunriseDate);
            sunset.setText("Sunset:" + sunsetDate);
            updated.setText("Last update: " + updateDate);*/


        }
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change city");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(cityInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CityToSearch cityToSearch = new CityToSearch(MainActivity.this);
                cityToSearch.setCity(cityInput.getText().toString());

                String newCity = cityToSearch.getCity();

                renderWeatherData(newCity);
            }
        });
        builder.show();
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
            showInputDialog();
        }

        return super.onOptionsItemSelected(item);
    }
}

