package com.dglproject.tour.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

import com.dglproject.tour.R;

import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageButton en = (ImageButton)findViewById(R.id.langEn);
        ImageButton mn = (ImageButton)findViewById(R.id.langMn);
        ImageButton cn = (ImageButton)findViewById(R.id.langCn);
        ImageButton ru = (ImageButton)findViewById(R.id.langRu);

        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage("en");
            }
        });

        mn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage("mn");
            }
        });

        cn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage("zh");
            }
        });

        ru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage("ru");
            }
        });
    }
    private void changeLanguage(String lang) {
        Resources res = getApplicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang);
        res.updateConfiguration(conf, dm);

        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
