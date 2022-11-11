package com.ata.tempalarm1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ata.tempalarm1.databinding.ActivityMainBinding;
import com.ata.tempalarm1.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
ActivitySettingsBinding binding;
    @Override //Overriding the AppCompactActivity which is the android class that has a function onCreate that represents the view lifecycle
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        binding.zipEditText.setText(Integer.toString(sharedPref.getInt("Zipcode",95928)));
        binding.Z2GS.setChecked(sharedPref.getBoolean("UseGPS",false));
        binding.replaceZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref.edit().putInt("Zipcode", Integer.parseInt(binding.zipEditText.getText().toString())).apply();
                sharedPref.edit().putBoolean("UseGPS",binding.Z2GS.isChecked()).apply();
                finish();
            }
        });
        binding.Z2GS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPref.edit().putBoolean("UseGPS",b).apply();
            }
        });
    }




}
