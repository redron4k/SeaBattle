package com.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.play:
                startActivity(new Intent(MainActivity.this, CreateActivity.class));
                break;
            case R.id.rules:
                startActivity(new Intent(MainActivity.this, RulesActivity.class));
                break;
            case R.id.autors:
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
                break;
            case R.id.closeAll:
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
                break;
        }
    }
}