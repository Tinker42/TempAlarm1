package com.ata.tempalarm1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ata.tempalarm1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override //Overriding the AppCompactActivity which is the android class that has a function onCreate that represents the view lifecycle
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());



        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);
    }
}