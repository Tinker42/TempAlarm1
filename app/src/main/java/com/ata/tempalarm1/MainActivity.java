package com.ata.tempalarm1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Database;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ata.tempalarm1.Data.APIClient;
import com.ata.tempalarm1.Data.Alarm;
import com.ata.tempalarm1.Data.GetDataService;
import com.ata.tempalarm1.Data.ListDAO;
import com.ata.tempalarm1.Data.MainDB;
import com.ata.tempalarm1.Data.WeatherInfo;
import com.ata.tempalarm1.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private MainAdapter mainAdapter;


    @Override //Overriding the AppCompactActivity which is the android class that has a function onCreate that represents the view lifecycle
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creating a new view model, based on the MainViewModel class, and storing it in the variable mainViewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);//passing the class
        mainViewModel.initializeDataBase(getApplicationContext());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        DividerItemDecoration drawBox = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        drawBox.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.box));
        binding.recyclerView.addItemDecoration(drawBox);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));//this 4.011230
        //List<Alarm> emptyList = new ArrayList<Alarm>();
        //binding.recyclerView.setAdapter(new MainAdapter( emptyList));

        setContentView(binding.getRoot());

        //mainViewModel.updateListOfAlarms();
        mainViewModel.getListOfAlarms().observe(this,alarms -> {//this or the onResume work
            //update UI
            //onChange(ListOfAlarms)
            mainAdapter = new MainAdapter(alarms);
            binding.recyclerView.setAdapter(mainAdapter);
        });

        //setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        binding.zipEditText.setText(Integer.toString(sharedPref.getInt("Zipcode",95928)));
        binding.replaceZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref.edit().putInt("Zipcode", Integer.parseInt(binding.zipEditText.getText().toString())).apply();
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), AlarmInputActivity.class);
                startActivityForResult(intent,RESULT_OK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Alarm rAlarm= (Alarm) data.getExtras().get("alarm");
            mainAdapter.addItem(rAlarm);
            //binding.recyclerView.getAdapter().addItem();
        }
    }

    @Override
    protected void onResume(){//an option
        super.onResume();
        GetDataService service= APIClient.getRetrofit().create(GetDataService.class);
        //getting the Retrofit instance and telling it to create a service that specifies what the call will be

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        Call<WeatherInfo> call=service.getWeatherAtZipcode("76c322c4b7mshc53f33b4e4b7f6fp1e1a4ejsn10e701dd2344",Integer.toString(sharedPref.getInt("Zipcode",95928)));
        call.enqueue(new Callback<WeatherInfo>() {
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
        });

        mainViewModel.getListOfAlarms().observe(this, alarms ->{
            binding.recyclerView.setAdapter(new MainAdapter(alarms));
        });

        //if(binding.recyclerView.getAdapter() != null){
        //    binding.recyclerView.getAdapter().notifyDataSetChanged();
        //}

    }
}