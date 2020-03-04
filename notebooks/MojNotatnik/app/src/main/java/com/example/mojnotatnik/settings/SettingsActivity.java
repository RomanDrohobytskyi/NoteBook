package com.example.mojnotatnik.settings;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mojnotatnik.MainActivity;
import com.example.mojnotatnik.R;
import com.example.mojnotatnik.notatki.NoteActivity;

import java.util.Random;

public class SettingsActivity extends AppCompatActivity {

    private MyVibration vibration;

    private TextView vibrationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        vibrationTextView = findViewById(R.id.textViewVibration);
        vibration = new MyVibration();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewVibration:
                if (vibration.isActive()){
                    vibration.setActive(false);
                    Toast.makeText(SettingsActivity.this, "Wibracja wyłączona =)"
                            , Toast.LENGTH_SHORT).show();
                    vibrationTextView.setTextColor(Color.DKGRAY);
                } if (!vibration.isActive()) {
                    vibration.setActive(true);
                    Toast.makeText(SettingsActivity.this, "Wibracja włączona =)"
                            , Toast.LENGTH_SHORT).show();
                    vibrationTextView.setTextColor(Color.GREEN);
                }
                break;
        }
    }

    public void onClickSaveBTN(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickBackGround(View v) {
        int random = new Random().nextInt(2 + 1);
        switch (random){
            case 0:
                v.setBackgroundResource(R.drawable.background4);
                break;
            case 1:
                v.setBackgroundResource(R.drawable.background2);
                break;
            case 2:
                v.setBackgroundResource(R.drawable.background3);
                break;
            default:
                v.setBackgroundResource(R.drawable.background);
                break;
        }

    }


}
