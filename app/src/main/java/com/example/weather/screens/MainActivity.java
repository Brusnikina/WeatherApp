package com.example.weather.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.App;
import com.example.weather.R;
import com.example.weather.api.model.Daily;
import com.example.weather.api.model.Hourly;
import com.example.weather.api.model.WeatherDTO;
import com.example.weather.model.CurrentForecast;
import com.example.weather.model.DailyForecast;
import com.example.weather.model.HourlyForecast;
import com.example.weather.screens.recyclerAdapters.DailyAdapter;
import com.example.weather.screens.recyclerAdapters.HourlyAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    CompositeDisposable disposable = new CompositeDisposable();

    private Toolbar toolbar;

    // coefficient for converting pressure to millimeters of mercury column
    private static final double COEF = 0.75006;
    // Moscow coordinates, if geo permission was denied
    private static final double moscow_lat = 55.75;
    private static final double moscow_lon = 37.62;

    private static final int REQUEST_CODE = 1;

    private RecyclerView hourlyRecycler;
    private RecyclerView dailyRecycler;

    private TextView currentTemp;
    private TextView currentFeelsLike;
    private TextView currentWindSpeed;
    private TextView currentDescription;
    private TextView currentPressure;
    private TextView currentHumidity;
    private ImageView currentWeather;

    private WeatherViewModel viewModel;

    private LocationManager locationManager;

    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.default_title));

        currentDescription = findViewById(R.id.tv_current_description);
        currentFeelsLike = findViewById(R.id.tv_current_feels_like);
        currentTemp = findViewById(R.id.tv_current_temp);
        currentHumidity = findViewById(R.id.yv_current_humidity);
        currentPressure = findViewById(R.id.tv_current_pressure);
        currentWindSpeed = findViewById(R.id.tv_current_wind_speed);
        currentWeather = findViewById(R.id.iv_current_weather);

        hourlyRecycler = findViewById(R.id.recycler_hourly);
        HourlyAdapter hourlyAdapter = new HourlyAdapter();
        LinearLayoutManager hourlyLayoutManager =
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        hourlyRecycler.setLayoutManager(hourlyLayoutManager);
        hourlyRecycler.setAdapter(hourlyAdapter);

        dailyRecycler = findViewById(R.id.recycler_daily);
        DailyAdapter dailyAdapter = new DailyAdapter();
        LinearLayoutManager dailyLayoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        dailyRecycler.setLayoutManager(dailyLayoutManager);
        dailyRecycler.setAdapter(dailyAdapter);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        viewModel.getCurrentForecasts().observe(this, new Observer<List<CurrentForecast>>() {
            @Override
            public void onChanged(List<CurrentForecast> currentForecasts) {
                setCurrentWeather(currentForecasts);
            }
        });
        viewModel.getHourlyForecasts().observe(this, new Observer<List<HourlyForecast>>() {
            @Override
            public void onChanged(List<HourlyForecast> hourlyForecasts) {
                hourlyAdapter.setHourlyForecast(hourlyForecasts);
            }
        });
        viewModel.getDailyForecasts().observe(this, new Observer<List<DailyForecast>>() {
            @Override
            public void onChanged(List<DailyForecast> dailyForecasts) {
                dailyAdapter.setDailyForecast(dailyForecasts);
            }
        });

        requestLocation();
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callWithCityName(query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                1000, locationListener);
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                            1000, locationListener);
                } else {
                    Toast.makeText(this, getString(R.string.forecast_for_Moscow), Toast.LENGTH_SHORT).show();
                    makeCall(moscow_lat, moscow_lon);
                }
                return;
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            makeCall(location.getLatitude(), location.getLongitude());
        }
    };

    private void setCurrentWeather(List<CurrentForecast> currentForecasts) {

        if (currentForecasts.size() == 0) {
            return;
        }

        CurrentForecast current = currentForecasts.get(0);
        String pressure = String.format(getString(R.string.pressure_description), current.getPressure() * COEF);
        String humidity = String.format(getString(R.string.humidity_description), current.getHumidity());
        String speed = String.format(getString(R.string.wind_description), current.getWindSpeed());
        String temp = String.format(getString(R.string.tempriture_description), current.getTemp());
        String feelsLike = String.format(getString(R.string.feels_like_description), current.getFeelsLike());

        currentDescription.setText(current.getDescription());
        currentPressure.setText(pressure);
        currentHumidity.setText(humidity);
        currentTemp.setText(temp);
        currentFeelsLike.setText(feelsLike);
        currentWindSpeed.setText(speed);

        String name = getString(R.string.icon_frefix) + current.getIcon();
        int id = getResources().getIdentifier(name, getString(R.string.drawable_type), getPackageName());
        currentWeather.setImageResource(id);
    }

    private void insertHourly(HourlyForecast hourlyForecast) {
        executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                App.getInstance().getHourlyDao().insert(hourlyForecast);
            }
        });
        executor.shutdown();
    }

    private void insertDaily(DailyForecast dailyForecast) {
        executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                App.getInstance().getDailyDao().insert(dailyForecast);
            }
        });
        executor.shutdown();
    }

    private void insertCurrent(CurrentForecast currentForecast) {
        executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                App.getInstance().getCurrentDao().insert(currentForecast);
            }
        });
        executor.shutdown();
    }

    private void databaseClear() {
        executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                App.getInstance().clearAll();
            }
        });
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getCityByCoordinates(double lat, double lon) {
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        String city = null;
        try {
            List<Address> addresses = coder.getFromLocation(lat, lon, 1);
            city = addresses.get(0).getLocality();
            if (city == null) city = addresses.get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (city == null) city = getString(R.string.default_location);
        return city;
    }

    private void callWithCityName(String city) {
        Geocoder coder = new Geocoder(this);
        double longitude = 0;
        double latitude = 0;
        try {
            ArrayList<Address> addresses = (ArrayList<Address>) coder.getFromLocationName(city, 1);

            if (addresses.size() == 0) {
                Toast.makeText(this, getString(R.string.city_not_found), Toast.LENGTH_LONG).show();
                return;
            }

            longitude = addresses.get(0).getLongitude();
            latitude = addresses.get(0).getLatitude();
            makeCall(latitude, longitude);

        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.no_internet_error), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void makeCall(double lat, double lon) {

        setTitle(getCityByCoordinates(lat, lon));

        App app = (App) getApplication();
        disposable.add(app.getWeatherService().getApi().getWeatherByCoordinate(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<WeatherDTO, Throwable>() {
                    @Override
                    public void accept(WeatherDTO weather, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Toast.makeText(MainActivity.this, getString(R.string.data_loading_error), Toast.LENGTH_SHORT).show();
                            throwable.printStackTrace();
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.successful_data_update), Toast.LENGTH_SHORT).show();

                            databaseClear();

                            CurrentForecast currentForecast = new CurrentForecast();
                            currentForecast.setDescription(weather.getCurrent().getWeather().get(0).getDescription());
                            currentForecast.setFeelsLike(weather.getCurrent().getFeelsLike());
                            currentForecast.setHumidity(weather.getCurrent().getHumidity());
                            currentForecast.setIcon(weather.getCurrent().getWeather().get(0).getIcon());
                            currentForecast.setPressure(weather.getCurrent().getPressure());
                            currentForecast.setTemp(weather.getCurrent().getTemp());
                            currentForecast.setWindSpeed(weather.getCurrent().getWindSpeed());
                            insertCurrent(currentForecast);

                            HourlyForecast hourlyForecast;
                            for (Hourly h : weather.getHourly()) {
                                hourlyForecast = new HourlyForecast();
                                hourlyForecast.setDt(h.getDt());
                                hourlyForecast.setIcon(h.getWeather().get(0).getIcon());
                                hourlyForecast.setTemp(h.getTemp());
                                insertHourly(hourlyForecast);
                            }

                            DailyForecast dailyForecast;
                            for (Daily d : weather.getDaily()) {
                                dailyForecast = new DailyForecast();
                                dailyForecast.setDayTemp(d.getTemp().getDay());
                                dailyForecast.setNightTemp(d.getTemp().getNight());
                                dailyForecast.setDt(d.getDt());
                                dailyForecast.setIcon(d.getWeather().get(0).getIcon());
                                insertDaily(dailyForecast);
                            }
                        }
                    }
                }));
    }
}