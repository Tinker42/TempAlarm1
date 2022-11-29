package com.ata.tempalarm1.Data;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherWorker extends Worker {

    static {
        System.loadLibrary("api-keys");
    }

    public native String getAPIKey();

    public static MutableLiveData<WeatherInfo> outputObservable = new MutableLiveData();

    public WeatherWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        GetDataService service = APIClient.getRetrofit().create(GetDataService.class);

        //getting the Retrofit instance and telling it to create a service that specifies what the call will be
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final Call<WeatherInfo>[] call = new Call[]{null};
        /*try {
            Thread.sleep(2);//UGPS=t, first break
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        if (!sharedPref.getBoolean("UseGPS", false)) {
            /*try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            call[0] = service.getWeatherAtZipcode(getAPIKey(), Integer.toString(sharedPref.getInt("Zipcode", 95928)));
            /*try {
                Thread.sleep(2);
                //UseGPS==false, first break, call[0] set
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        } else {
            /*try {
                Thread.sleep(2);
                //UGPS=t, second break
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                /*try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

            }else{
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {//entering even onFailure()??
                        /*try {
                            Thread.sleep(2);
                            //should be UGPS=t, third break
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        Log.e("LocationGot", Double.toString(location.getLatitude()) );
                        call[0] = service.getWeatherAtZipcode(getAPIKey(), Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude()));
                        //getting stuck here^
                        /*try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }
                });
            }



        }
        try{
            /*
            currTime= getTime()HH:MM:SS:MS;
            if( currTime > AwakeTime && currTime < AsleepTime){
                //runStuff
            }else{
                Thread.sleep((AwakeTime+24h)-currTime);
            }
            */
            try {
                Thread.sleep(2048);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Response<WeatherInfo> response = call[0].execute();
            //UseGPS==false, second break, after:API success
            //UseGPS==true, third break, after:API success
            /*try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            Log.e("APICall", "APICall Success");
            if (response.isSuccessful() && response.body() != null && response.body().getCurrent().getTempF() != null){
                Data output = new Data.Builder().putDouble("temperature",response.body().getCurrent().getTempF()).build();

                outputObservable.postValue(response.body());

                return Result.success();//output);
            }else{
                /*try {
                    Thread.sleep(2);
                } catch (InterruptedException x) {
                    x.printStackTrace();
                }*/
                return Result.retry();
            }
        }catch(Throwable e){
            /*try {
                Thread.sleep(2);
            } catch (InterruptedException y) {
                y.printStackTrace();
            }*/
            return Result.failure();
            //return Result.retry();
            //when UseGPS == true ^??
        }
        /*call.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                Log.e("APICall", "APICall Success");
                WeatherInfo weatherinfo= response.body();
                Log.e("APISent",Integer.toString(sharedPref.getInt("Zipcode", 95928)));
                Log.e("APICall", String.valueOf(weatherinfo.getCurrent().getTempF()));
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                Log.e("APICall", "APICall Fail");
            }
        });*/


    }
}
