package com.ata.tempalarm1.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherWorker extends Worker {

    static {System.loadLibrary("api-keys");}
    public native String getAPIKey();

    public static MutableLiveData <WeatherInfo> outputObservable = new MutableLiveData();

    public WeatherWorker(@NonNull Context context, @NonNull WorkerParameters workerParams){
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        GetDataService service= APIClient.getRetrofit().create(GetDataService.class);

        //getting the Retrofit instance and telling it to create a service that specifies what the call will be

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        Call<WeatherInfo> call=service.getWeatherAtZipcode( getAPIKey() ,Integer.toString(sharedPref.getInt("Zipcode",95928)));
        try{
            Response<WeatherInfo> response = call.execute();
            Log.e("APICall", "APICall Success");
            if (response.isSuccessful() && response.body() != null && response.body().getCurrent().getTempF() != null){
                Data output = new Data.Builder().putDouble("temperature",response.body().getCurrent().getTempF()).build();

                outputObservable.postValue(response.body());

                return Result.success();//output);
            }else{
                return Result.retry();
            }
        }catch(Throwable e){
            return Result.failure();
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
