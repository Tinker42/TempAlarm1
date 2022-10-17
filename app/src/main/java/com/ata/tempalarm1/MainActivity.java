package com.ata.tempalarm1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Database;
import androidx.room.Room;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ata.tempalarm1.Data.APIClient;
import com.ata.tempalarm1.Data.Alarm;
import com.ata.tempalarm1.Data.GetDataService;
import com.ata.tempalarm1.Data.ListDAO;
import com.ata.tempalarm1.Data.MainDB;
import com.ata.tempalarm1.Data.WeatherInfo;
import com.ata.tempalarm1.Data.WeatherWorker;
import com.ata.tempalarm1.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        /*//________________________________________________

        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);//getting default alarm service
        Intent alarmRecieverIntent = new Intent(MainActivity.this,AlarmReceiver.class);
        alarmRecieverIntent.putExtra("alarmText","Here I Am");

        PendingIntent alarmIntent= PendingIntent.getBroadcast(this,(int)System.currentTimeMillis(),alarmRecieverIntent,PendingIntent.FLAG_IMMUTABLE);//breaking here

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),alarmIntent);



        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.setContentTitle("Notification");
        mBuilder.setContentText("The current Temperature of 82℉ has exceeded your monitored Temp of 80℉");
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        //mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText("The current Temperature of 82℉ has exceeded your monitored Temp of 80℉"));
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel("default","default channel", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            //mBuilder.setNotificationChannel
            mBuilder.setChannelId("default");
        }

        Notification notification = mBuilder.build();
        notification.flags|=Notification.FLAG_INSISTENT;
        mNotificationManager.notify(1,notification);


        //AlarmManager alarmManager2= (AlarmManager) getSystemService(ALARM_SERVICE);
        //if(alarmManager.canScheduleExactAlarms()){}
        //PendingIntent alarmIntent2= PendingIntent.getBroadcast(getApplicationContext(),0,new Intent(getApplicationContext(),AlarmReciever.class),0);
        //alarmManager2.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),alarmIntent2);


        */ //________________________________________________

        //mainViewModel.updateListOfAlarms();
        mainViewModel.getListOfAlarms().observe(this,alarms -> {//this or the onResume work
            //update UI
            //onChange(ListOfAlarms)
            mainAdapter = new MainAdapter(alarms,getApplicationContext());
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
                //startActivityForResult(intent,RESULT_OK);
                startService(intent);
            }
        });
        Constraints constraint = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();//set a constraint to be sure the device is connected to a network
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(WeatherWorker.class,
                15/* technically tied to specific phone's power management service 10/15 min, variable later*/, TimeUnit.MINUTES).addTag("MainActivity")
                .setConstraints(constraint).setBackoffCriteria(BackoffPolicy.LINEAR,//if something goes wrong in the api call, it automatically takes the difference in time and handles it by itself
                        PeriodicWorkRequest.MIN_BACKOFF_MILLIS,TimeUnit.MILLISECONDS).build();
        WorkManager mWorkManager = WorkManager.getInstance(getApplicationContext());//the instance of the WorkManager directly in the MainActivity instead of the ViewModel

        //WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("getTemperature",
        mWorkManager.enqueueUniquePeriodicWork("getTemperature",
                ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest);//@24:
        mWorkManager.getWorkInfosByTagLiveData("MainActivity").observe(this,listOfWorkInfo->{
            if(listOfWorkInfo == null || listOfWorkInfo.isEmpty()){
                return;
            }
            WorkInfo workInfo = listOfWorkInfo.get(0);//get first item of array list
            Log.e("work manager", workInfo.getState().toString());
            switch(workInfo.getState()){
                case ENQUEUED:
                    break;
                case RUNNING:
                    WeatherWorker.outputObservable.observe(MainActivity.this, weatherInfo -> {
                        mWorkManager.getWorkInfosByTagLiveData("MainActivity").removeObservers(MainActivity.this);
                        /*NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(this);*/

                        String alarmText = mainViewModel.compare(weatherInfo.getCurrent().getTempF());//runs compare of list vs current and returns notification string

                        if(!alarmText.isEmpty()){
                            AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);//getting default alarm service
                            //if(alarmManager.canScheduleExactAlarms()){}
                            Intent alarmRecieverIntent = new Intent(MainActivity.this,AlarmReceiver.class);
                            alarmRecieverIntent.putExtra("alarmText",alarmText);
                            //___________________________________________

                            //___________________________________________
                            Log.e("pre1", workInfo.getState().toString());
                            PendingIntent alarmIntent= PendingIntent.getBroadcast(this,(int)System.currentTimeMillis(),alarmRecieverIntent,PendingIntent.FLAG_IMMUTABLE);//breaking here//fixed
                            Log.e("post1", workInfo.getState().toString());

                            Log.e("pre2", workInfo.getState().toString());
                            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),alarmIntent);
                            Log.e("post2", workInfo.getState().toString());
                            //

                        }

                    });
                    break;
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


        mainViewModel.getListOfAlarms().observe(this, alarms ->{
            binding.recyclerView.setAdapter(new MainAdapter(alarms,getApplicationContext()));
        });

        //if(binding.recyclerView.getAdapter() != null){
        //    binding.recyclerView.getAdapter().notifyDataSetChanged();
        //}

    }
}